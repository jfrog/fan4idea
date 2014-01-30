package org.fandev.runner;

import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.fandev.lang.fan.psi.FanFile;

/**
 * @author Dror Bereznitsky
 * @date Jan 20, 2009 9:10:00 PM
 */
public class FanScriptRunConfigurationProducer extends RuntimeConfigurationProducer implements Cloneable {
    private PsiElement mySourceElement;

    public FanScriptRunConfigurationProducer() {
        super(FanScriptRunConfigurationType.getInstance());
    }

    public PsiElement getSourceElement() {
        return mySourceElement;
    }

    protected RunnerAndConfigurationSettingsImpl createConfigurationByElement(final Location location, final ConfigurationContext context) {
        final PsiElement element = location.getPsiElement();

        final PsiFile file = element.getContainingFile();
        if (file instanceof FanFile) {
            final FanFile fanFile = (FanFile) file;
            mySourceElement = element;
            return (RunnerAndConfigurationSettingsImpl) FanScriptRunConfigurationType.getInstance().createConfigurationByLocation(location);

        }

        return null;
    }

    public int compareTo(final Object o) {
        return PREFERED;
    }
}
