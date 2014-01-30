package org.fandev.unscramble;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;

/**
 * Date: Sep 23, 2009
 * Time: 11:20:50 PM
 *
 * @author Dror Bereznitsky
 */
public class FanUnscrambleAction extends AnAction {
    public void actionPerformed(final AnActionEvent paramAnActionEvent) {
        final Project localProject = PlatformDataKeys.PROJECT.getData(paramAnActionEvent.getDataContext());

        final FanUnscrambleDialog localUnscrambleDialog = new FanUnscrambleDialog(localProject);

        localUnscrambleDialog.show();
    }

    public void update(final AnActionEvent paramAnActionEvent) {
        final Presentation localPresentation = paramAnActionEvent.getPresentation();

        final Project localProject = PlatformDataKeys.PROJECT.getData(paramAnActionEvent.getDataContext());
        localPresentation.setEnabled(localProject != null);
    }
}
