package org.fandev.lang.fan.parsing.util;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 6, 2009 2:33:06 PM
 */
public class ParserUtils {
    public static boolean getToken(final PsiBuilder builder, final IElementType elem) {
        if (elem.equals(builder.getTokenType())) {
            builder.advanceLexer();
            return true;
        }
        return false;
    }

    public static boolean getToken(final PsiBuilder builder, final TokenSet tokens) {
        if (tokens.contains(builder.getTokenType())) {
            builder.advanceLexer();
            return true;
        }
        return false;
    }

    public static boolean getToken(final PsiBuilder builder, final IElementType elem, final String errorMsg) {
        if (elem.equals(builder.getTokenType())) {
            builder.advanceLexer();
            return true;
        } else {
            if (errorMsg != null) {
                builder.error(errorMsg);
            }
            return false;
        }
    }

    public static boolean getToken(final PsiBuilder builder, final TokenSet tokens, final String errorMsg) {
        if (tokens.contains(builder.getTokenType())) {
            builder.advanceLexer();
            return true;
        } else {
            if (errorMsg != null) {
                builder.error(errorMsg);
            }
            return false;
        }
    }

    public static IElementType firstAfter(final PsiBuilder builder, final IElementType... elems) {
        final TokenSet ignored = TokenSet.create(elems);
        IElementType result;
        if (ignored.contains(builder.getTokenType())) {
            final PsiBuilder.Marker rb = builder.mark();
            while (!builder.eof() && ignored.contains(builder.getTokenType())) {
                builder.advanceLexer();
            }
            result = builder.getTokenType();
            rb.rollbackTo();
        } else {
            result = builder.getTokenType();
        }
        return result;
    }

    public static boolean lookAhead(final PsiBuilder builder, final IElementType... elems) {
        if (!elems[0].equals(builder.getTokenType())) {
            return false;
        }

        if (elems.length == 1) {
            return true;
        }

        final PsiBuilder.Marker rb = builder.mark();
        builder.advanceLexer();
        int i = 1;
        while (!builder.eof() && i < elems.length && elems[i].equals(builder.getTokenType())) {
            builder.advanceLexer();
            i++;
        }
        rb.rollbackTo();
        return i == elems.length;
    }

    public static boolean lookAheadForElement(final PsiBuilder builder, final IElementType elem, final IElementType... stopElements) {
        final TokenSet stopElem = TokenSet.create(stopElements);
        return lookAheadForElement(builder, elem, stopElem);
    }

    public static boolean lookAheadForElement(final PsiBuilder builder, final IElementType elem, final TokenSet stopElem) {
        final PsiBuilder.Marker rb = builder.mark();
        while (!builder.eof() && !(elem == builder.getTokenType()) && !stopElem.contains(builder.getTokenType())) {
            builder.advanceLexer();
        }
        if (builder.eof()) {
            rb.rollbackTo();
            return false;
        } else if (stopElem.contains(builder.getTokenType())) {
            rb.rollbackTo();
            return false;
        } else {
            rb.rollbackTo();
            return true;
        }
    }

    public static IElementType eatElement(final PsiBuilder builder, final IElementType elem) {
        final PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        marker.done(elem);
        return elem;
    }

    public static void advanceToBlockEnd(final PsiBuilder builder) {
        advanceToBlockEnd(builder, LBRACE, RBRACE);
    }

    public static void advanceToArgumentsEnd(final PsiBuilder builder) {
        advanceToBlockEnd(builder, LPAR, RPAR);
    }

    public static void advanceToArrayEnd(final PsiBuilder builder) {
        advanceToBlockEnd(builder, LBRACKET, RBRACKET);
    }

    public static void advanceToBlockEnd(final PsiBuilder builder, final IElementType opening, final IElementType closing) {
        int openLeft = 1;
        while (!builder.eof() && openLeft > 0) {
            builder.advanceLexer();
            if (builder.getTokenType() == opening) {
                openLeft++;
            } else if (builder.getTokenType() == closing) {
                openLeft--;
            }
        }
        builder.advanceLexer();
    }

    public static boolean advanceTo(final PsiBuilder builder, final TokenSet stopper) {
        while (!builder.eof() && !stopper.contains(builder.getTokenType())) {
            builder.advanceLexer();
        }
        return stopper.contains(builder.getTokenType());
    }

    public static void advanceNoNls(final PsiBuilder builder) {
        builder.advanceLexer();
        removeNls(builder);
    }

    public static void removeNls(final PsiBuilder builder) {
        while (NLS.equals(builder.getTokenType())) {
            builder.advanceLexer();
        }
    }

    public static void cleanAfterErrorInBlock(final PsiBuilder builder) {
        final PsiBuilder.Marker em = builder.mark();
        advanceToBlockEnd(builder);
        em.error(FanBundle.message("separator.expected"));
    }

    public static void cleanAfterErrorInArguments(final PsiBuilder builder) {
        final PsiBuilder.Marker em = builder.mark();
        advanceToArgumentsEnd(builder);
        em.error(FanBundle.message("separator.expected"));
    }

    public static void cleanAfterErrorInArray(final PsiBuilder builder) {
        final PsiBuilder.Marker em = builder.mark();
        advanceToArrayEnd(builder);
        em.error(FanBundle.message("separator.expected"));
    }

    public static boolean parseName(final PsiBuilder builder) {
        // id
        if (!IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
            builder.error(FanBundle.message("identifier.expected"));
            return false;
        }
        final PsiBuilder.Marker idMark = builder.mark();
        builder.advanceLexer();
        idMark.done(FanElementTypes.NAME_ELEMENT);
        return true;
    }

    public static void removeStoppers(final PsiBuilder builder, final TokenSet stopper, final TokenSet reccuring) {
        if (stopper.contains(builder.getTokenType())) {
            builder.advanceLexer();
        }
        while (reccuring.contains(builder.getTokenType())) {
            builder.advanceLexer();
        }
    }
}
