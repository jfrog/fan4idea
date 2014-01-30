package org.fandev.lang.fan.editor;

import com.intellij.codeInsight.editorActions.QuoteHandler;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.tree.IElementType;
import org.fandev.lang.fan.FanTokenTypes;

/**
 * Created by IntelliJ IDEA.
 * User: Dror
 * Date: Mar 13, 2009
 * Time: 9:45:40 AM
 */
public class FanQuoteHandler implements QuoteHandler {
    public boolean isClosingQuote(final HighlighterIterator iterator, final int offset) {
        final IElementType tokenType = iterator.getTokenType();

        if (tokenType == FanTokenTypes.STRING_LITERAL) {
            final int start = iterator.getStart();
            final int end = iterator.getEnd();
            return end - start >= 1 && offset == end - 1;
        }
        return false;
    }

    public boolean isOpeningQuote(final HighlighterIterator iterator, final int offset) {
        final IElementType tokenType = iterator.getTokenType();

        //TODO use a more fine grained token type
        if (tokenType == FanTokenTypes.BAD_CHARACTER) {
            final int start = iterator.getStart();
            return offset == start;
        }
        return false;
    }

    public boolean hasNonClosedLiteral(final Editor editor, final HighlighterIterator iterator, final int offset) {
        return true;
    }

    public boolean isInsideLiteral(final HighlighterIterator iterator) {
        final IElementType tokenType = iterator.getTokenType();
        return tokenType == FanTokenTypes.STRING_LITERAL;
    }
}
