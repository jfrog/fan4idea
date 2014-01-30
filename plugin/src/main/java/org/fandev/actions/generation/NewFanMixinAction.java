package org.fandev.actions.generation;

import org.fandev.lang.fan.FanBundle;
import org.fandev.icons.Icons;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * Date: Apr 1, 2009
 * Time: 11:35:32 PM
 *
 * @author Dror Bereznitsky
 */
public class NewFanMixinAction extends NewFanActionBase {
    protected NewFanMixinAction() {
        super(
                FanBundle.message("newmixin.menu.action.text"),
                FanBundle.message("newmixin.menu.action.description"),
                Icons.MIXIN);
    }

    protected String getTemplateName() {
        return "FanMixin.fan";
    }

    protected String getDialogPrompt() {
        return FanBundle.message("newmixin.dlg.prompt");
    }

    protected String getDialogTitle() {
        return FanBundle.message("newmixin.dlg.title");
    }

    protected String getCommandName() {
        return FanBundle.message("newmixin.command.name");
    }

    protected String getActionName(final PsiDirectory directory, final String newName) {
        return FanBundle.message("newmixin.progress.text", newName);
    }
}