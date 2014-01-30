package org.fandev.idea.overrideImplement;

import com.intellij.lang.LanguageCodeInsightActionHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.fandev.lang.fan.FanFileType;

/**
 * Date: Sep 26, 2009
 * Time: 4:06:16 PM
 *
 * @author Dror Bereznitsky
 */
public class FanOverrideMethodsHandler implements LanguageCodeInsightActionHandler {
    public boolean isValidFor(final Editor editor, final PsiFile psiFile) {
        return psiFile != null && FanFileType.FAN_FILE_TYPE.equals(psiFile.getFileType());
    }

    public void invoke(final Project project, final Editor editor, final PsiFile file) {
        FanOverrideImplementUtil.invokeOverrideImplement(project, editor, file, false);
    }

    public boolean startInWriteAction() {
        return false;
    }
}
