package org.fandev.runner;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

/**
 * You can use any of the following formats to execute a method in an installed pod
 *
 * fan <pod> [args]
 * fan <pod>::<type> [args]
 * fan <pod>::<type>.<method> [args]
 *
 * The following steps are take to execute the method:
 *
 *    1. if only a pod name is specified then assume <pod>::Main.main
 *    2. if only a type name is specified then assume <pod>::<type>.main
 *    3. resolve the qualified name of the method
 *    4. if the method is not static then call on an instance created via Type.make
 *    5. if the method has a Str[] parameter then invoke it with Sys.args, otherwise invoke with no arguments.
 *    6. if main returns an Int, return that as exit code
 * 
 * Date: Sep 5, 2009
 * Time: 11:24:44 PM
 * @author Dror Bereznitsky
 */
public class FanPodRunConfiguration extends FanRunConfiguration {
    protected String executableType;

    public FanPodRunConfiguration(final String name, final RunConfigurationModule runConfigurationModule, final ConfigurationFactory factory) {
        super(name, runConfigurationModule, factory);
    }

    protected void setExecutable(final JavaParameters params) {
        final String typeToExecute = executableType == null || "".equals(executableType) ? "Main" : executableType;
        params.getProgramParametersList().add(getModuleName() + "::" + typeToExecute);
    }

    protected ModuleBasedConfiguration createInstance() {
        return new FanPodRunConfiguration(getName(), new RunConfigurationModule(getConfigurationModule().getProject()), factory);
    }

    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new FanPodRunConfigurationEditor();
    }

    @Override
    public void readExternal(final Element element) throws InvalidDataException {
        super.readExternal(element);
        executableType = JDOMExternalizer.readString(element, "type");
    }

    @Override
    public void writeExternal(final Element element) throws WriteExternalException {
        super.writeExternal(element);
        JDOMExternalizer.write(element, "type", executableType);
    }

    public String getExecutableType() {
        return executableType;
    }

    public void setExecutableType(final String executableType) {
        this.executableType = executableType;
    }
}
