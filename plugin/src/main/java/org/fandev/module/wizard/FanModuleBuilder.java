package org.fandev.module.wizard;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleWithNameAlreadyExists;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectJdksModel;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.fandev.module.FanModuleSettings;
import org.fandev.module.FanModuleType;
import org.fandev.module.pod.PodFileParser;
import org.fandev.module.pod.PodModel;
import org.fandev.sdk.FanSdkType;
import org.fandev.index.FanIndex;
import org.fandev.utils.FanUtil;
import org.fandev.utils.TextUtil;
import org.fandev.utils.VirtualFileUtil;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Dror Bereznitsky
 * @date Jan 19, 2009 9:58:28 AM
 */
public class FanModuleBuilder extends ModuleBuilder implements SourcePathsBuilder {
    private List<Pair<String, String>> mySourcePaths = new ArrayList<Pair<String, String>>();
    private String myContentRootPath;
    private Sdk mySdk;
    private PodModel modulePod;
    private final List<Pair<String, String>> myModuleLibraries = new ArrayList<Pair<String, String>>();

    @NotNull
    @Override
    public Module createModule(final ModifiableModuleModel moduleModel) throws InvalidDataException, IOException, ModuleWithNameAlreadyExists, JDOMException, ConfigurationException {
        final Module myModule = super.createModule(moduleModel);
        final VirtualFile v = VirtualFileUtil.refreshAndFindFileByLocalPath(VirtualFileUtil.buildUrl(myContentRootPath , PodModel.BUILD_FAN));
        FanModuleSettings.getInstance(myModule).setBuildScript(v);
        if (modulePod != null) {
            modulePod.setBuildScriptFile(v);
            modulePod.setBuildScriptName(PodModel.BUILD_FAN);
        }
        return myModule;
    }

    @Override
    public boolean isSuitableSdk(final Sdk sdk) {
        return (sdk.getSdkType().equals(FanSdkType.getInstance()));
    }

    @Override
    public Module commitModule(final Project project, final ModifiableModuleModel model) {
        final Module m = super.commitModule(project, model);
        final PodModel pod = getModulePod();
        // hackery
        if (TextUtil.isEmpty(pod.getOutDir())) {
            pod.setOutDir("out/");
        }
        final String s = VirtualFileUtil.buildUrl(getContentEntryPath(), pod.getOutDir());
        final File outFolder = new File(s);
        outFolder.mkdirs();
        pod.setOutDir(s);
        final VirtualFile v = FanUtil.generateBuildScript(this.getContentEntryPath(), project, pod);
        pod.setBuildScriptFile(v);
        FanModuleSettings.getInstance(m).setBuildScript(v);
        final List l = this.getSourcePaths();
        l.clear();
        l.addAll(pod.getSrcDirs());
        l.addAll(pod.getResDirs());
        return m;
    }
    
    @Override
    public void setupRootModel(final ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        final FanIndex index = (FanIndex) modifiableRootModel.getProject().getComponent(FanIndex.COMPONENT_NAME);

        final Module module = modifiableRootModel.getModule();
        if (mySdk != null) {
            modifiableRootModel.setSdk(mySdk);
            //HACK. Otherwise for new Project first roots change event have wrong SDK!
            //      Value must be same as key, not clone!
            ProjectJdksModel.getInstance(module.getProject()).getProjectJdks().put(mySdk, mySdk);

            if (mySdk.getSdkType().equals(FanSdkType.getInstance())) {
                final String s = VirtualFileUtil.buildUrl(getContentEntryPath(), PodModel.BUILD_FAN);
                final VirtualFile v = VirtualFileUtil.refreshAndFindFileByLocalPath(s);
                modulePod.setBuildScriptFile(v);
                FanModuleSettings.getInstance(module).setBuildScript(v);
                final PodModel tmpPodModel = PodFileParser.parse(modulePod.getBuildScriptFile(), mySdk);
                if (tmpPodModel != null) {
                    modulePod.setName(tmpPodModel.getName());
                    modulePod.setDescription(tmpPodModel.getDescription());
                    modulePod.setVersion(tmpPodModel.getVersion());
                    modulePod.setDependencies(tmpPodModel.getDependencies());
                    modulePod.setSrcDirs(tmpPodModel.getSrcDirs());
                    modulePod.setDocApi(tmpPodModel.getDocApi());
                    modulePod.setDocSrc(tmpPodModel.getDocSrc());
                    modulePod.setOutDir(tmpPodModel.getOutDir());
                    modulePod.setIndexes(tmpPodModel.getIndexes());
                    modulePod.setMetas(tmpPodModel.getMetas());
                    modulePod.setResDirs(tmpPodModel.getResDirs());

                    clearModuleLibrary();
                    final List<String> dependencies = modulePod.getDependencies();
                    for (final String dependency : dependencies) {
                        addModuleLibrary(dependency, dependency);
                    }
                }
            }
        } else {
            modifiableRootModel.inheritSdk();
        }

        setupContentRoot(modifiableRootModel);

        // Add pod dependencies as module libraries
        final LibraryTable libraryTable = modifiableRootModel.getModuleLibraryTable();
        for (final Pair<String, String> libInfo : myModuleLibraries) {
            final String moduleLibraryPath = libInfo.first;
            final String sourceLibraryPath = libInfo.second;
            final File libFile = new File(moduleLibraryPath);

            final Library library = libraryTable.createLibrary();
            final Library.ModifiableModel modifiableModel = library.getModifiableModel();
            modifiableModel.setName(libFile.getName());
            modifiableModel.addRoot(moduleLibraryPath, OrderRootType.CLASSES);
            if (sourceLibraryPath != null) {
                modifiableModel.addRoot(sourceLibraryPath, OrderRootType.SOURCES);
            }
            modifiableModel.commit();

            index.indexLibrary(library);
        }
    }

    @Override
    public ModuleType getModuleType() {
        return FanModuleType.getInstance();
    }

    public String getContentEntryPath() {
        return myContentRootPath;
    }

    public void setContentEntryPath(final String moduleRootPath) {
        myContentRootPath = moduleRootPath;
    }

    public List<Pair<String, String>> getSourcePaths() {
        return mySourcePaths;
    }

    public void addSourcePath(final Pair<String, String> sourcePathInfo) {
        if (mySourcePaths == null) {
            mySourcePaths = new ArrayList<Pair<String, String>>();
        }
        mySourcePaths.add(sourcePathInfo);
    }

    public void setSourcePaths(final List<Pair<String, String>> sourcePaths) {
        mySourcePaths.clear();
        mySourcePaths.addAll(sourcePaths);
    }

    @Nullable
    public Sdk getSdk() {
        return mySdk;
    }

    public void setSdk(final Sdk jdk) {
        mySdk = jdk;
    }

    public PodModel getModulePod() {
        return modulePod;
    }

    public void setModulePod(final PodModel modulePod) {
        this.modulePod = modulePod;
    }

    public void addModuleLibrary(final String moduleLibraryPath, final String sourcePath) {
        myModuleLibraries.add(Pair.create(moduleLibraryPath, sourcePath));
    }
    public void clearModuleLibrary() {
        myModuleLibraries.clear();
    }
    private void setupContentRoot(final ModifiableRootModel rootModel) {
        final String moduleRootPath = getContentEntryPath();
        if (moduleRootPath != null) {
            final LocalFileSystem lfs = LocalFileSystem.getInstance();
            final VirtualFile moduleContentRoot = lfs.refreshAndFindFileByPath(FileUtil.toSystemIndependentName(moduleRootPath));
            if (moduleContentRoot != null) {
                final ContentEntry contentEntry = rootModel.addContentEntry(moduleContentRoot);
                final List<Pair<String, String>> sourcePaths = getSourcePaths();
                if (sourcePaths != null) {
                    for (final Pair<String, String> sourcePath : sourcePaths) {
                        final VirtualFile sourceRoot = lfs.refreshAndFindFileByPath(FileUtil.toSystemIndependentName(sourcePath.first));
                        if (sourceRoot != null) {
                            contentEntry.addSourceFolder(sourceRoot, false, sourcePath.second);
                        }
                    }
                }
            }
        }
    }
}
