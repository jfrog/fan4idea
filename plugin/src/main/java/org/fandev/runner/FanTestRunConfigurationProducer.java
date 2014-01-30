package org.fandev.runner;

import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.fandev.lang.fan.psi.FanFile;

/**
 * Date: Sep 16, 2009
 * Time: 11:29:22 PM
 *
 * @author Dror Bereznitsky
 */
public class FanTestRunConfigurationProducer extends RuntimeConfigurationProducer implements Cloneable {
    private PsiElement mySourceElement;

    public FanTestRunConfigurationProducer() {
        super(FanTestRunConfigurationType.getInstance());
    }

    public PsiElement getSourceElement() {
        return mySourceElement;
    }

    protected RunnerAndConfigurationSettingsImpl createConfigurationByElement(final Location location, final ConfigurationContext context) {
        final PsiElement element = location.getPsiElement();

        final PsiFile file = element.getContainingFile();
        if (file instanceof FanFile) {
            mySourceElement = element;
            return (RunnerAndConfigurationSettingsImpl) FanTestRunConfigurationType.getInstance().createConfigurationByLocation(location);
        }

        return null;
    }

    public int compareTo(final Object o) {
        return PREFERED;
    }
}
