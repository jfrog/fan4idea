package org.fandev.runner;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.psi.FanFile;

/**
 * @author Dror Bereznitsky
 * @date Jan 20, 2009 8:45:09 PM
 */
public class FanScriptRunConfigurationType extends FanRunConfigurationType {
    public FanScriptRunConfigurationType() {
        super();
        configurationFactory = new FanScriptConfigurationFactory(this);
    }

    public boolean isConfigurationByLocation(final RunConfiguration configuration, final Location location) {
        return false;
    }

    public String getDisplayName() {
        return "Fantom Script";
    }

    public String getConfigurationTypeDescription() {
        return "Fantom Script";
    }

    public Icon getIcon() {
        return Icons.FAN_16;
    }

    @NotNull
    public String getId() {
        return "FanScriptRunConfiguration";
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{configurationFactory};
    }

    public static FanScriptRunConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(FanScriptRunConfigurationType.class);
    }
}
