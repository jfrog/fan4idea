package org.fandev.module;

import com.intellij.ProjectTopics;
import com.intellij.openapi.module.*;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.messages.MessageBusConnection;
import org.fandev.sdk.FanSdkType;
import org.jetbrains.annotations.NotNull;
import org.fandev.settings.FanModuleSettingsStorage;
import org.fandev.settings.SettingAttribute;
import org.fandev.module.ModuleSynchFileListener;
import org.fandev.module.pod.PodModel;
import org.fandev.module.pod.PodFileParser;
import org.fandev.utils.VirtualFileUtil;
import org.fandev.index.FanIndex;

import java.util.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 27, 2009 7:10:11 PM
 */
public class FanModuleSettings implements ModuleComponent {
    protected final Module myModule;
    private FanModuleSettingsStorage myModuleSettingsStorage;
    private ModuleSynchFileListener buildScriptListener;
    private FanIndex fanIndex;

    public static final String COMPONENT_NAME = "FanModuleSettings";

    private static final Logger logger = Logger.getInstance("Settings");

    public FanModuleSettings(final Module module) {
        myModule = module;
        myModuleSettingsStorage = FanModuleSettingsStorage.getInstance(module);
        buildScriptListener = new ModuleSynchFileListener(getBuildScript(), module);
        fanIndex = (FanIndex) module.getProject().getComponent(FanIndex.COMPONENT_NAME);

        final MessageBusConnection conn = myModule.getMessageBus().connect();
        conn.subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootListener() {
            private Sdk lastSdk;

            public void beforeRootsChange(ModuleRootEvent event) {
                lastSdk = ModuleRootManager.getInstance(myModule).getSdk();
            }

            public void rootsChanged(ModuleRootEvent event) {
                final Sdk moduleSdk = ModuleRootManager.getInstance(myModule).getSdk();
                if (moduleSdk != null && !moduleSdk.equals(lastSdk)) {
                    if (fanIndex != null) {
                        ApplicationManager.getApplication().runWriteAction(new Runnable() {
                            public void run() {
                                final ModifiableRootModel modifiableRootModel = ModuleRootManager.getInstance(myModule).getModifiableModel();
                                final LibraryTable libraryTable = modifiableRootModel.getModuleLibraryTable();
                                final Library[] libraries = libraryTable.getLibraries();
                                for (Library lib : libraries) {
                                    fanIndex.indexLibrary(lib);
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    public static FanModuleSettings getInstance(@NotNull final Module module) {
        return module.getComponent(FanModuleSettings.class);
    }

    public VirtualFile getBuildScript() {
        return getVirtualFileAttribute(SettingAttribute.BUILD_SCRIPT);
    }

    public String getBuildScriptPath() {
        return myModuleSettingsStorage.getAttributeValue(SettingAttribute.BUILD_SCRIPT);
    }

    public void setBuildScript(final VirtualFile buildScript) {
        setVirtualFileAttribute(SettingAttribute.BUILD_SCRIPT, buildScript);
        buildScriptListener = new ModuleSynchFileListener(buildScript, myModule);
    }
    public Module getModule() {
        return myModule;
    }
    
    private VirtualFile getVirtualFileAttribute(final SettingAttribute attribute) {
        final String url = myModuleSettingsStorage.getAttributeValue(attribute);
        return VirtualFileManager.getInstance().refreshAndFindFileByUrl(url);
    }

    private void setVirtualFileAttribute(final SettingAttribute attribute, final VirtualFile value) {
        final String url = value != null ? value.getUrl() : "";
        myModuleSettingsStorage.setAttributeValue(attribute, url);
    }

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    /**
     * Should run in the scope of a write action
     */
    public void synchModule() {
        final String s = VirtualFileUtil.buildUrl(myModule.getModuleFile().getParent().getPath(), PodModel.BUILD_FAN);
        final VirtualFile v = VirtualFileUtil.refreshAndFindFileByLocalPath(s);
        FanModuleSettings.getInstance(myModule).setBuildScript(v);
        setBuildScript(v);
        
        final PodModel podModel = PodFileParser.parse(getBuildScript(), myModule);

        if (podModel != null) {
            
            final ModifiableRootModel modifiableRootModel = ModuleRootManager.getInstance(myModule).getModifiableModel();
            final ContentEntry[] entries = modifiableRootModel.getContentEntries();
            for (final ContentEntry entry : entries) {
                if (entry.getFile().equals(myModule.getModuleFile().getParent())) {
                    final SourceFolder[] sourceFolders = entry.getSourceFolders();
                    for (final SourceFolder srcFolder : sourceFolders) {
                        entry.removeSourceFolder(srcFolder);
                    }
                    for (final Pair<String,String> srcDir : podModel.getSrcDirs()) {
                        final VirtualFile sourceFolder = VirtualFileManager.getInstance().refreshAndFindFileByUrl(VirtualFileUtil.buildUrl(entry.getUrl(), srcDir.getFirst()));
                        if (sourceFolder != null) {
                            entry.addSourceFolder(sourceFolder, false);
                        }
                    }
                }
            }
            synchLibraries(podModel, modifiableRootModel);
            modifiableRootModel.commit();

            if (!myModule.getName().equals(podModel.getName())) {
                final ModifiableModuleModel moduleModel = ModuleManager.getInstance(myModule.getProject()).getModifiableModel();
                try {
                    moduleModel.renameModule(myModule, podModel.getName());
                    moduleModel.commit();
                } catch (ModuleWithNameAlreadyExists moduleWithNameAlreadyExists) {
                    // Could not rename
                    logger.error("Failed to rename module " + myModule.getName() + " to " + podModel.getName());
                }
            }
        } else {
            WindowManager.getInstance().getStatusBar(myModule.getProject()).setInfo("Failed to parse build script");
        }
    }

    private void synchLibraries(final PodModel podModel, final ModifiableRootModel modifiableRootModel) {
        // Add pod dependencies as module libraries
        final LibraryTable libraryTable = modifiableRootModel.getModuleLibraryTable();
        final Library[] libraries = libraryTable.getLibraries();
        final List<String> dependencies = podModel.getDeclaredDependencies();
        final List<String> dependenciesToAdd = new LinkedList<String>(dependencies);

        for (final Library library : libraries) {
            final String libName = library.getName();
            if (dependencies.contains(libName)) {
                dependenciesToAdd.remove(libName); // only add new libraries
            } else {
                libraryTable.removeLibrary(library);
            }
        }

        final Sdk moduleSdk = ModuleRootManager.getInstance(myModule).getSdk();

        for (final String dependency : dependenciesToAdd) {
            String podName = dependency.indexOf(' ') > 0 ? dependency.substring(0, dependency.indexOf(' ')) : dependency;
            String moduleLibraryPath = VirtualFileUtil.constructLocalUrl(String.format("%s%s%s%s%s.pod", moduleSdk.getHomePath(),
                    VirtualFileUtil.VFS_PATH_SEPARATOR, FanSdkType.getFanLibDir(),
                    VirtualFileUtil.VFS_PATH_SEPARATOR, podName));
            String sourceLibraryPath = VirtualFileUtil.constructLocalUrl(String.format("%s%s%s%s%s%s", moduleSdk.getHomePath(),
                    VirtualFileUtil.VFS_PATH_SEPARATOR, FanSdkType.getSrcDir(),
                    VirtualFileUtil.VFS_PATH_SEPARATOR, podName, VirtualFileUtil.VFS_PATH_SEPARATOR));
            
            final Library library = libraryTable.createLibrary();
            final Library.ModifiableModel modifiableModel = library.getModifiableModel();
            modifiableModel.setName(dependency);
            modifiableModel.addRoot(moduleLibraryPath, OrderRootType.CLASSES);
            if (sourceLibraryPath != null) {
                modifiableModel.addRoot(sourceLibraryPath, OrderRootType.SOURCES);
            }
            modifiableModel.commit();

            if (fanIndex != null) {
                fanIndex.indexLibrary(library);
            }
        }
    }

    public void projectOpened() {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                synchModule();
            }
        });
    }

    public void projectClosed() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moduleAdded() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void initComponent() {
        VirtualFileManager.getInstance().addVirtualFileListener(buildScriptListener);
    }

    public void disposeComponent() {
        VirtualFileManager.getInstance().removeVirtualFileListener(buildScriptListener);
    }
}
