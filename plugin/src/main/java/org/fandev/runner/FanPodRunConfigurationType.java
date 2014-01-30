package org.fandev.runner;

import com.intellij.execution.LocatableConfigurationType;
import com.intellij.execution.Location;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeUtil;

import javax.swing.*;

import org.fandev.icons.Icons;
import org.jetbrains.annotations.NotNull;

/**
 * Date: Sep 4, 2009
 * Time: 11:57:21 PM
 *
 * @author Dror Bereznitsky
 */
public class FanPodRunConfigurationType extends FanRunConfigurationType {
    public FanPodRunConfigurationType() {
        super();
        configurationFactory = new FanPodConfigurationFactory(this);
    }

    public String getDisplayName() {
        return "Fan Pod";
    }

    public String getConfigurationTypeDescription() {
        return "Fan Pod";
    }

    public Icon getIcon() {
        return Icons.FAN_16;
    }

    @NotNull
    public String getId() {
        return "FanPodRunConfiguration";
    }

    public static FanPodRunConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(FanPodRunConfigurationType.class);
    }
}
