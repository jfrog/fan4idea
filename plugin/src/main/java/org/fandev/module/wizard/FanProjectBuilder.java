package org.fandev.module.wizard;

import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.projectImport.ProjectImportBuilder;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.ui.Messages;
import org.fandev.module.FanProject;
import org.fandev.module.FanModuleType;
import org.fandev.module.pod.PodModel;
import org.fandev.module.pod.PodFileParser;
import org.fandev.icons.Icons;
import org.fandev.utils.FanUtil;
import org.fandev.utils.TextUtil;
import org.fandev.utils.VirtualFileUtil;
import org.fandev.lang.fan.FanBundle;

import javax.swing.*;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Date: Sep 20, 2009
 * Time: 11:13:14 PM
 *
 * @author Dror Bereznitsky
 */
public class FanProjectBuilder extends ProjectImportBuilder<FanProject> {
    private static final Logger logger = Logger.getInstance("Project import");
    private String buildScriptPath;
    private Sdk sdk;
    private List<FanProject> list = Arrays.asList(new FanProject());

    public String getName() {
        return "Fantom";
    }

    public Icon getIcon() {
        return Icons.FAN_16;
    }

    public List<FanProject> getList() {
        return list;
    }

    public boolean isMarked(FanProject fanProject) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setList(List<FanProject> fanProjects) throws ConfigurationException {
        list.clear();
        list.addAll(fanProjects);
    }

    public void setOpenProjectSettingsAfter(boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Module> commit(final Project project, final ModifiableModuleModel model, final ModulesProvider provider,
            final ModifiableArtifactModel model2) {
        final List<Module> modules = new ArrayList<Module>();
        if (!TextUtil.isEmpty(buildScriptPath)) {
            final VirtualFile buildScriptFile = VirtualFileUtil.findFileByLocalPath(buildScriptPath);
            if (buildScriptFile != null) {
                final FanModuleBuilder myBuilder = FanModuleType.getInstance().createModuleBuilder();
                final PodModel podModel = PodFileParser.parse(buildScriptFile, sdk);
                if (podModel != null) {
                    final String name = podModel.getName();
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        public void run() {
                            try {
                                myBuilder.setModulePod(podModel);
                                myBuilder.setSdk(sdk);
                                myBuilder.setName(name);
                                myBuilder.setContentEntryPath(project.getBaseDir().getPath());

                                final Module module = myBuilder.commitModule(project, model);
                                if (module != null) {
                                    modules.add(module);
                                }
                            } catch (Exception e) {
                                logger.error("Could not import module: " + name, e);
                                Messages.showErrorDialog(FanBundle.message("project.import.fan.error.adding.modules", new Object[]{name}), getTitle());
                            }
                        }
                    });
                } else {
                    logger.error("Could not import module");
                    Messages.showErrorDialog(FanBundle.message("project.import.fan.error.adding.modules", new Object[]{"unknown"}), getTitle());    
                }
            }
        }

        return modules;
    }

    public void setBuildScriptPath(final String buildScriptPath) {
        this.buildScriptPath = buildScriptPath;
    }

    public void setSdk(final Sdk sdk) {
        this.sdk = sdk;
    }
}
