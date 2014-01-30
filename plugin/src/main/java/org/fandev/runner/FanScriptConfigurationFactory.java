package org.fandev.runner;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;

/**
 * @author Dror Bereznitsky
 * @date Jan 20, 2009 8:52:38 PM
 */
public class FanScriptConfigurationFactory extends ConfigurationFactory {
    protected FanScriptConfigurationFactory(@org.jetbrains.annotations.NotNull final ConfigurationType type) {
        super(type);
    }

    public RunConfiguration createTemplateConfiguration(final Project project) {
        return new FanScriptRunConfiguration("Fantom Script", new RunConfigurationModule(project), this);
    }
}
