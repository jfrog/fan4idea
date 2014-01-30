package org.fandev.lang.fan.highlighting;

import com.intellij.openapi.fileTypes.SyntaxHighlighterProvider;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.lang.Language;
import org.jetbrains.annotations.Nullable;
import org.fandev.lang.fan.FanSupportLoader;

/**
 *
 * @author Dror Bereznitsky
 * @date Dec 22, 2008 10:16:38 PM
 */
public class FanSyntaxHighlighterProvider implements SyntaxHighlighterProvider {
    public SyntaxHighlighter create(final FileType fileType, @Nullable final Project project, @Nullable final VirtualFile virtualFile) {
        final Language lang = FanSupportLoader.FAN.getLanguage();
        return SyntaxHighlighterFactory.getSyntaxHighlighter(lang, project, virtualFile);
    }
}
