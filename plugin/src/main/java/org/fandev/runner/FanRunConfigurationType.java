package org.fandev.runner;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.fandev.lang.fan.psi.FanFile;

/**
 * Date: Sep 4, 2009
 * Time: 11:58:04 PM
 *
 * @author Dror Bereznitsky
 */
public abstract class FanRunConfigurationType implements LocatableConfigurationType {
    protected ConfigurationFactory configurationFactory;

    public RunnerAndConfigurationSettings createConfigurationByLocation(final Location location) {
        final PsiElement element = location.getPsiElement();
        final PsiClass clazz = getScriptClass(element);
        if (clazz == null) {
            return null;
        }
        return createConfiguration(clazz);
    }

    protected RunnerAndConfigurationSettings createConfiguration(final PsiClass aClass) {
        final Project project = aClass.getProject();
        final RunnerAndConfigurationSettings settings = RunManagerEx.getInstanceEx(project).createConfiguration("", configurationFactory);
        final ModuleBasedConfiguration configuration = (ModuleBasedConfiguration) settings.getConfiguration();
        final PsiFile file = aClass.getContainingFile();
        final PsiDirectory dir = file.getContainingDirectory();
        assert dir != null;

        //configuration.setWorkDir(dir.getVirtualFile().getPath());
        final VirtualFile vFile = file.getVirtualFile();
        assert vFile != null;
        //configuration.scriptPath = vFile.getPath();
        final RunConfigurationModule module = configuration.getConfigurationModule();


        final String name = FanRunConfigurationType.getConfigurationName(aClass, module);
        configuration.setName(name);
        configuration.setModule(JavaExecutionUtil.findModule(aClass));
        return settings;
    }

    protected static String getConfigurationName(final PsiClass aClass, final RunConfigurationModule module) {
        final String qualifiedName = aClass.getQualifiedName();
        final Project project = module.getProject();
        if (qualifiedName != null) {
            final PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName.replace('$', '.'), GlobalSearchScope.projectScope(project));
            if (psiClass != null) {
                return psiClass.getName();
            } else {
                final int lastDot = qualifiedName.lastIndexOf('.');
                if (lastDot == -1 || lastDot == qualifiedName.length() - 1) {
                    return qualifiedName;
                }
                return qualifiedName.substring(lastDot + 1, qualifiedName.length());
            }
        }
        return module.getModuleName();
    }

    protected PsiClass getScriptClass(final PsiElement element) {
        final PsiFile file = element.getContainingFile();
        if (!(file instanceof FanFile)) {
            return null;
        }
        return null; //((FanFile) file).getScriptClass();
    }

    public boolean isConfigurationByLocation(final RunConfiguration configuration, final Location location) {
        return false;
    }

    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{configurationFactory};
    }
}
