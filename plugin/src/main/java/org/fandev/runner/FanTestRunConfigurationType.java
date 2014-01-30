package org.fandev.runner;

import org.fandev.icons.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import com.intellij.execution.configurations.ConfigurationTypeUtil;

/**
 * Date: Sep 16, 2009
 * Time: 11:30:20 PM
 *
 * @author Dror Bereznitsky
 */
public class FanTestRunConfigurationType extends FanRunConfigurationType {
    public FanTestRunConfigurationType() {
        super();
        configurationFactory = new FanTestConfigurationFactory(this);
    }

    public String getDisplayName() {
        return "Fantom Test";
    }

    public String getConfigurationTypeDescription() {
        return "Fantom Test";
    }

    public Icon getIcon() {
        return Icons.FAN_16;
    }

    @NotNull
    public String getId() {
        return "FanTestRunConfiguration";
    }

    public static FanTestRunConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(FanTestRunConfigurationType.class);
    }
}

