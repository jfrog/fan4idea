package org.fandev.module;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import org.fandev.module.wizard.FanModuleBuilder;
import org.fandev.module.wizard.ui.FanSdkSelectStep;
import org.fandev.module.wizard.ui.FanPodCreateStep;
import org.fandev.lang.fan.FanBundle;
import org.fandev.icons.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Dror Bereznitsky
 * @date Jan 19, 2009 9:57:23 AM
 */
public class FanModuleType extends ModuleType<FanModuleBuilder> implements ApplicationComponent {
    private static final String FAN_MODULE = "FAN_MODULE";
    public static final String FAN_MODULE_TYPE = "FanModuleType";

    public FanModuleType() {
        super(FAN_MODULE);
    }

    @Override
    public FanModuleBuilder createModuleBuilder() {
        return new FanModuleBuilder();
    }

    @Override
    public String getName() {
        return FanBundle.message("fan.module.title");    
    }

    @Override
    public String getDescription() {
        return FanBundle.message("fan.module.description");  
    }

    @Override
    public Icon getBigIcon() {
        return Icons.FAN_24;
    }

    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return isOpened? Icons.FAN_MODULE_OPEN : Icons.FAN_MODULE_CLOSE;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(final WizardContext wizardContext, final FanModuleBuilder moduleBuilder, final ModulesProvider modulesProvider) {
        final ArrayList<ModuleWizardStep> steps = new ArrayList<ModuleWizardStep>();
        
        steps.add(new FanSdkSelectStep(moduleBuilder, wizardContext.getProject()));
        steps.add(new FanPodCreateStep(moduleBuilder));
        //steps.add(new FanSourcePathsStep(moduleBuilder, Icons.FAN_16, "fan/"));
        //steps.add(new FanResourcePathsStep(moduleBuilder, Icons.FAN_16, "res/"));

        return steps.toArray(new ModuleWizardStep[0]);
    }

    @NotNull
    public String getComponentName() {
        return FAN_MODULE_TYPE;
    }

    @NotNull
    public static FanModuleType getInstance(){
        return ApplicationManager.getApplication().getComponent(FanModuleType.class);
    }

    public void initComponent() {
        ModuleTypeManager.getInstance().registerModuleType(this);
    }

    public void disposeComponent() {
        // dispose component
    }
}
