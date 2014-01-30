package org.fandev.module.wizard.ui;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.projectRoots.Sdk;

import javax.swing.*;

import org.fandev.sdk.ui.FanSdkChooserPanel;
import org.fandev.sdk.FanSdkType;
import org.fandev.lang.fan.FanBundle;
import org.fandev.module.wizard.FanModuleBuilder;
import org.fandev.icons.Icons;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 19, 2009 2:55:40 PM
 */
public class FanSdkSelectStep extends ModuleWizardStep {
    protected final FanSdkChooserPanel myPanel;
    protected final FanModuleBuilder mySettingsHolder;

    public FanSdkSelectStep(@NotNull final FanModuleBuilder settingsHolder, @Nullable final Project project) {
        super();
        myPanel = new FanSdkChooserPanel(project);
        mySettingsHolder = settingsHolder;
    }

    public JComponent getComponent() {
        return myPanel;
    }

    public void updateDataModel() {
        final Sdk sdk = getSdk();
        mySettingsHolder.setSdk(sdk);
    }

    private Sdk getSdk() {
        return myPanel.getChosenJdk();
    }

    @Override
    public Icon getIcon() {
        return Icons.FAN_24;
    }
    
    @Override
    public JComponent getPreferredFocusedComponent() {
        return myPanel.getPreferredFocusedComponent();
    }

    @Override
    public boolean validate() {
        final Sdk jdk = myPanel.getChosenJdk();
        if (jdk==null){
            int result = Messages.showYesNoDialog(
                    FanBundle.message("sdk.error.no.sdk.prompt.messge.confirm.without.sdk"),
                    FanBundle.message("sdk.select.prompt.title"),
                    Messages.getWarningIcon()
            );
            return result == DialogWrapper.OK_EXIT_CODE;
        }
        if (!(jdk.getSdkType() instanceof FanSdkType)) {
            Messages.showErrorDialog(
                    FanBundle.message("sdk.error.prompt.message.sdk.not.valid"),
                    FanBundle.message("sdk.select.prompt.title")
            );
            return false;
        }

        return true;
    }
}
