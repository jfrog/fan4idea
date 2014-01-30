package org.fandev.runner;

import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.fandev.lang.fan.psi.FanFile;

/**
 * Date: Sep 5, 2009
 * Time: 12:06:57 AM
 *
 * @author Dror Bereznitsky
 */
public class FanPodRunConfigurationProducer extends RuntimeConfigurationProducer implements Cloneable {
    private PsiElement mySourceElement;

    public FanPodRunConfigurationProducer() {
        super(FanPodRunConfigurationType.getInstance());
    }

    public PsiElement getSourceElement() {
        return mySourceElement;
    }

    protected RunnerAndConfigurationSettingsImpl createConfigurationByElement(final Location location, final ConfigurationContext context) {
        final PsiElement element = location.getPsiElement();

        final PsiFile file = element.getContainingFile();
        if (file instanceof FanFile) {
            mySourceElement = element;
            return (RunnerAndConfigurationSettingsImpl) FanPodRunConfigurationType.getInstance().createConfigurationByLocation(location);
        }

        return null;
    }

    public int compareTo(final Object o) {
        return PREFERED;
    }
}
