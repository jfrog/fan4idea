package org.fandev.lang.fan.parsing.auxiliary.modifiers;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanElementTypes.MODIFIERS;
import org.fandev.lang.fan.FanTokenTypes;
import static org.fandev.lang.fan.FanTokenTypes.IDENTIFIER_TOKENS_SET;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * @author Dror Bereznitsky
 * @date Jan 6, 2009 2:24:23 PM
 */
public class Modifiers {

    public static TokenSet parse(final PsiBuilder builder, final DeclarationType stmtType) {
        TokenSet modifiers = TokenSet.create();

        ParserUtils.removeNls(builder);
        PsiBuilder.Marker modifiersMarker = builder.mark();

        while (!builder.eof()) {
            // Either a modifier or the keyword/identifier...
            if (stmtType.getKeyword() != null) {
                if (stmtType.getKeyword().equals(builder.getTokenType())) {
                    modifiersMarker.done(MODIFIERS);
                    return modifiers;
                }
            } else {
                if (IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
                    modifiersMarker.done(MODIFIERS);
                    return modifiers;
                }
            }
            final IElementType possibleModifier = builder.getTokenType();
            if (!Modifier.parse(builder, stmtType)) {
                if (FanTokenTypes.ALL_MODIFIERS.contains(possibleModifier)) {
                    // illegal access modifier
                    final String tokenText = builder.getTokenText();
                    builder.error(FanBundle.message("illegal.modifier", tokenText, stmtType));
                    builder.advanceLexer();
                } else {
                    modifiersMarker.done(MODIFIERS);
                    break;
                }
            } else {
                modifiers = TokenSet.orSet(modifiers, TokenSet.create(possibleModifier));
            }
        }
        return modifiers;
    }
}
