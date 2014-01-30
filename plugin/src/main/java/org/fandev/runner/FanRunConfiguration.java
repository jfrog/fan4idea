package org.fandev.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import org.fandev.index.FanIndex;
import org.fandev.module.FanModuleType;
import org.fandev.sdk.FanSdkType;
import org.fandev.utils.FanUtil;
import org.fandev.utils.TextUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Date: Sep 5, 2009
 * Time: 11:16:49 PM
 *
 * @author Dror Bereznitsky
 */
public abstract class FanRunConfiguration extends ModuleBasedConfiguration {
    protected final ConfigurationFactory factory;
    protected String myModuleName;
    protected String executionParameters;
    protected final FanIndex index;

    public FanRunConfiguration(final String name, final RunConfigurationModule runConfigurationModule, final ConfigurationFactory factory) {
        super(name, runConfigurationModule, factory);
        this.factory = factory;
        this.index = getProject().getComponent(FanIndex.class);
    }

    public RunProfileState getState(@NotNull final Executor executor, @NotNull final ExecutionEnvironment env) throws ExecutionException {
        final JavaCommandLineState state = new JavaCommandLineState(env) {
            protected JavaParameters createJavaParameters() throws ExecutionException {
                final String outDir = getModuleOutDir();

                final JavaParameters params = new JavaParameters();

                params.setJdk(FanUtil.createFanJdk(getSdk()));
                params.getVMParametersList().add("-Dfan.home=" + getSdk().getHomePath());
                params.getVMParametersList().add("-Djava.library.path=" + FanSdkType.getExtDir(getSdk()));
                params.getVMParametersList().add("-Dfan.debug=true");
                params.setWorkingDirectory(outDir);
                params.getClassPath().add(outDir + "/classes");
                params.setMainClass(getMainClass());
                setExecutable(params);
                params.getProgramParametersList().addParametersString(getExecutionParameters());
                params.configureByModule(getModule(), JavaParameters.CLASSES_ONLY);

                return params;
            }
        };
        final TextConsoleBuilder textConsoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(getProject());
        textConsoleBuilder.addFilter(new FanTypeFilter(getProject()));
        state.setConsoleBuilder(textConsoleBuilder);
        return state;
    }

    protected String getMainClass() {
        return "fanx.tools.Fan";
    }

    @Override
    public void readExternal(final Element element) throws InvalidDataException {
        super.readExternal(element);
        readModule(element);
        executionParameters = JDOMExternalizer.readString(element, "parameters");
    }

    @Override
    public void writeExternal(final Element element) throws WriteExternalException {
        super.writeExternal(element);
        writeModule(element);
        JDOMExternalizer.write(element, "parameters", executionParameters);
    }

    public Collection<Module> getValidModules() {
        final Module[] modules = ModuleManager.getInstance(getProject()).getModules();
        final ArrayList<Module> res = new ArrayList<Module>();
        for (final Module module : modules) {
            if (module.getModuleType() instanceof FanModuleType) {
                res.add(module);
            }
        }
        return res;
    }

    public Sdk getSdk() {
        final Module module = getModule();
        if (module != null) {
            if (FanUtil.isFanModuleType(module)) {
                return ModuleRootManager.getInstance(module).getSdk();
            }
        }
        return null;
    }

    @Nullable
    protected Module findModuleByName(@Nullable final String name) {
        if (name == null) {
            return null;
        }

        final Module module = ModuleManager.getInstance(getProject()).findModuleByName(name);
        return module != null && !module.isDisposed() ? module : null;
    }

    protected String getModuleOutDir() {
        final VirtualFile[] outRootDirs = ModuleRootManager.getInstance(getModule()).getRootPaths(OrderRootType.CLASSES_AND_OUTPUT);
        if (outRootDirs != null && outRootDirs.length > 0) {
            return outRootDirs[0].getPath();
        }
        return null;
    }

    @Nullable
    public Module getModule() {
        return findModuleByName(myModuleName);
    }

    @Nullable
    public String getModuleName() {
        return myModuleName;
    }

    public void setModuleName(@Nullable final String moduleName) {
        myModuleName = TextUtil.getAsNotNull(moduleName);
    }

    public void setModule(@Nullable final Module module) {
        setModuleName(module != null ? module.getName() : null);
    }

    public String getExecutionParameters() {
        return executionParameters;
    }

    public void setExecutionParameters(final String executionParameters) {
        this.executionParameters = executionParameters;
    }

    protected abstract void setExecutable(final JavaParameters params);
}
