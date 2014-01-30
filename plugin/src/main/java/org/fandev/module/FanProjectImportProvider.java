package org.fandev.module;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.projectImport.ProjectImportProvider;
import org.fandev.module.wizard.FanProjectBuilder;
import org.fandev.module.wizard.ui.FanProjectImportStep;

/**
 * Date: Sep 20, 2009
 * Time: 11:04:37 PM
 *
 * @author Dror Bereznitsky
 */
public class FanProjectImportProvider extends ProjectImportProvider {
    protected FanProjectImportProvider(final FanProjectBuilder projectImportBuilder) {
        super(projectImportBuilder);
    }

    public ModuleWizardStep[] createSteps(final WizardContext wizardContext) {
        final ProjectWizardStepFactory stepFactory = ProjectWizardStepFactory.getInstance();
        return new ModuleWizardStep[]{
                new FanProjectImportStep(wizardContext),
                stepFactory.createNameAndLocationStep(wizardContext)};
    }
}
