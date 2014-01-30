package org.fandev.module.wizard.ui;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

import org.fandev.module.wizard.FanModuleBuilder;
import org.fandev.module.pod.PodModel;
import org.fandev.icons.Icons;
import org.fandev.utils.TextUtil;
import org.fandev.utils.VirtualFileUtil;
import org.fandev.lang.fan.FanBundle;
import org.fandev.sdk.FanSdkType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 26, 2009 1:42:19 PM
 */
public class FanPodCreateStep extends ModuleWizardStep {
    protected final FanModuleBuilder mySettingsHolder;
    protected final FanPodCreatePanel podCreatePanel;
    protected PodModel pod;

    public FanPodCreateStep(@NotNull final FanModuleBuilder settingsHolder) {
        super();
        mySettingsHolder = settingsHolder;
        if (null == settingsHolder.getModulePod())
        {
            pod = new PodModel();
            settingsHolder.setModulePod(pod);
        }

        // hackery
        if (settingsHolder.getContentEntryPath() != null ) {
            pod.setBuildScriptFile(
                VirtualFileUtil.refreshAndFindFileByLocalPath(
                        VirtualFileUtil.buildUrl(settingsHolder.getContentEntryPath(), PodModel.BUILD_FAN)));
        }
        pod.setBuildScriptName(PodModel.BUILD_FAN);
        podCreatePanel = new FanPodCreatePanel(settingsHolder);
    }

    public JComponent getComponent() {
        return podCreatePanel;
    }

    public void updateDataModel() {
        mySettingsHolder.setModulePod(pod);    
    }

    @Override
    public boolean validate() throws ConfigurationException {
        super.validate();
        podCreatePanel.updateModel();
        String message = "";
        String fieldsName =  "";
        fieldsName += TextUtil.isEmpty(pod.getName()) ? FanBundle.message("pod.name") + ", " : "";
        fieldsName += TextUtil.isEmpty(pod.getVersion()) ? FanBundle.message("pod.version") + ", " : "";
        fieldsName += TextUtil.isEmpty(pod.getBuildScriptName()) ? FanBundle.message("build.script.name") : "";

        if (!TextUtil.isEmpty(fieldsName)) {
            message += FanBundle.message("validation.field.not.empty", fieldsName) + "\n";
        }
        if (!TextUtil.isEmpty(message)) {
            podCreatePanel.setMessage(message);
            return false;
        }
        return true;
    }

    @Override
    public void updateStep() {
        super.updateStep();
        pod.setName(mySettingsHolder.getName());
        podCreatePanel.setModel(pod);
    }

    @Override
    public Icon getIcon() {
        return Icons.FAN_16;
    }
}
