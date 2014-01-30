package org.fandev.actions.generation;

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanFileType;
import org.fandev.icons.Icons;
import org.jetbrains.annotations.NonNls;

import java.util.EnumMap;

/**
 * Created by IntelliJ IDEA.
 * User: Dror Bereznitsky
 * Date: Aug 30, 2009
 * Time: 6:43:13 PM
 */
public class NewFanBuildScriptAction extends NewFanActionBase {
    protected NewFanBuildScriptAction() {
        super(
                FanBundle.message("newpod.menu.action.text"),
                FanBundle.message("newpod.menu.action.description"),
                Icons.POD);
    }

    protected String getTemplateName() {
        return "FanBuildScript.fan";
    }

    protected String getDialogPrompt() {
        return FanBundle.message("newpod.dlg.prompt");
    }

    protected String getDialogTitle() {
        return FanBundle.message("newpod.dlg.title");
    }

    protected String getCommandName() {
        return FanBundle.message("newpod.command.name");
    }

    protected String getActionName(final PsiDirectory directory, final String newName) {
        return FanBundle.message("newpod.progress.text", newName);
    }

    @Override
    protected void addTemplateParams(final EnumMap<TemplateProperty, String> parameters) {
        parameters.put(TemplateProperty.POD_DEPENDS, ",");
        parameters.put(TemplateProperty.POD_SRC_DIRS, ",");
    }

    @Override
    protected PsiFile createClassFromTemplate(final PsiDirectory directory, final String className, String templateName,
                                                     @NonNls final EnumMap<TemplateProperty, String> parameters) throws IncorrectOperationException {
        return FanTemplatesFactory.createFromTemplate(directory, "build." + FanFileType.DEFAULT_EXTENSION, templateName, parameters);
    }
}
