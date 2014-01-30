package org.fandev.lang.fan;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.highlighting.FanHighlighter;

/**
 *
 * @author Dror
 * @date Dec 12, 2008 12:04:58 AM
 */
public class FanLanguage extends Language {
    public static final FanLanguage FAN_LANGUAGE = new FanLanguage();

    private FanLanguage() {
        super("Fantom", "text/fan", "application/fan");

        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
            @Override
			@NotNull
            protected SyntaxHighlighter createHighlighter() {
                return new FanHighlighter();
            }
        });
    }
}
