package org.fandev.actions.generation;

import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.IncorrectOperationException;
import com.intellij.CommonBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.fandev.lang.fan.FanFileType;

import javax.swing.*;
import java.util.EnumMap;

/**
 * @author Dror Bereznitsky
 * @date Apr 1, 2009 3:37:03 PM
 */
public abstract class NewFanActionBase extends CreateElementActionBase {
    protected NewFanActionBase(final String text, final String description, final Icon icon) {
        super(text, description, icon);
    }

    @NotNull
    protected PsiElement[] invokeDialog(final Project project, final PsiDirectory directory) {
        final MyInputValidator validator = new MyInputValidator(project, directory);
        Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), Messages.getQuestionIcon(), "", validator);

        return validator.getCreatedElements();
    }

    protected void checkBeforeCreate(final String newName, final PsiDirectory directory) throws IncorrectOperationException {
        //TODO
    }

    protected String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }

    @NotNull
    protected PsiElement[] create(final String newName, final PsiDirectory directory) throws Exception {
        final EnumMap<TemplateProperty, String> parameters = new EnumMap<TemplateProperty, String>(TemplateProperty.class);
        parameters.put(TemplateProperty.NAME, newName);
        parameters.put(TemplateProperty.NAME_LOWER_CASE, newName.toLowerCase());
        addTemplateParams(parameters);
        final PsiFile file = createClassFromTemplate(directory, newName, getTemplateName(), parameters);
        return new PsiElement[]{file};
    }

    protected void addTemplateParams(final EnumMap<TemplateProperty, String> parameters) {}

    protected abstract String getTemplateName();

    protected PsiFile createClassFromTemplate(final PsiDirectory directory, final String className, String templateName,
                                                     @NonNls final EnumMap<TemplateProperty, String> parameters) throws IncorrectOperationException {
        return FanTemplatesFactory.createFromTemplate(directory, className + "." + FanFileType.DEFAULT_EXTENSION, templateName, parameters);
    }

    protected abstract String getDialogPrompt();

    protected abstract String getDialogTitle();
}
