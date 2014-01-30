package org.fandev.util;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.LocalTimeCounter;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 13, 2009 10:41:30 AM
 */
public class TestUtils {
    public static final String TEMP_FILE = "temp.fan";

    public static PsiFile createPseudoPhysicalFanFile(final Project project, final String text) throws IncorrectOperationException {
        return createPseudoFanFile(project, TEMP_FILE, text);
    }

    public static PsiFile createPseudoFanFile(final Project project, final String fileName, final String text) throws IncorrectOperationException {
        return PsiFileFactory.getInstance(project).createFileFromText(
                fileName,
                FileTypeManager.getInstance().getFileTypeByFileName(fileName),
                text,
                LocalTimeCounter.currentTime(),
                true);
    }
}
