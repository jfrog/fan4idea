package org.fandev.lang.fan.highlighting;

import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanHighlightingLexer;
import org.fandev.lang.fan.FanTokenTypes;
import static org.fandev.lang.fan.FanTokenTypes.FAN_KEYWORDS;
import static org.fandev.lang.fan.FanTokenTypes.FAN_SYS_TYPE;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dror Bereznitsky
 * @date Dec 22, 2008 10:58:32 PM
 */
public class FanHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> keys;

    public static final String FAN_KEYWORD_ID = "FAN.KEYWORD";
    static final TextAttributesKey FAN_KEYWORD = TextAttributesKey.createTextAttributesKey(
            FAN_KEYWORD_ID, SyntaxHighlighterColors.KEYWORD.getDefaultAttributes()
    );

    public static final String FAN_TYPES_ID = "FAN.TYPES";
    static final TextAttributesKey FAN_TYPES = TextAttributesKey.createTextAttributesKey(
            FAN_TYPES_ID, HighlightInfoType.CLASS_NAME.getAttributesKey().getDefaultAttributes()
    );

    public static final String FAN_STRING_ID = "FAN.STRING";
    static final TextAttributesKey FAN_STRING = TextAttributesKey.createTextAttributesKey(
            FAN_STRING_ID, SyntaxHighlighterColors.STRING.getDefaultAttributes()
    );

    public static final String FAN_NUMBER_ID = "FAN.NUMBER";
    static final TextAttributesKey FAN_NUMBER = TextAttributesKey.createTextAttributesKey(
            FAN_NUMBER_ID, SyntaxHighlighterColors.NUMBER.getDefaultAttributes()
    );

    public static final String FAN_PARENTHS_ID = "FAN.PARENTHS";
    static final TextAttributesKey FAN_PARENTHS = TextAttributesKey.createTextAttributesKey(
            FAN_PARENTHS_ID, SyntaxHighlighterColors.PARENTHS.getDefaultAttributes()
    );
    static final TextAttributesKey FAN_BRACKETS = TextAttributesKey.createTextAttributesKey(
            "FAN.BRACKETS", SyntaxHighlighterColors.BRACKETS.getDefaultAttributes()
    );
    static final TextAttributesKey FAN_BRACES = TextAttributesKey.createTextAttributesKey(
            "FAN.BRACES", SyntaxHighlighterColors.BRACES.getDefaultAttributes()
    );

    static final TextAttributesKey FAN_DOC_COMMENT = TextAttributesKey.createTextAttributesKey(
            "FAN.DOC_COMMENT", SyntaxHighlighterColors.DOC_COMMENT.getDefaultAttributes()
    );

    static final TextAttributesKey FAN_LINE_COMMENT = TextAttributesKey.createTextAttributesKey(
            "FAN.LINE_COMMENT", SyntaxHighlighterColors.LINE_COMMENT.getDefaultAttributes()
    );

    static {
        keys = new HashMap<IElementType, TextAttributesKey>();
        fillMap(keys, FAN_KEYWORDS, FAN_KEYWORD);
        fillMap(keys, FanTokenTypes.STRING_LITERALS, FAN_STRING);
        fillMap(keys, TokenSet.create(FAN_SYS_TYPE), FAN_TYPES);
        fillMap(keys, FanTokenTypes.NUMERIC_LITERALS, FAN_NUMBER);

        keys.put(FanTokenTypes.FANDOC_LINE_COMMENT, FAN_DOC_COMMENT);
        keys.put(FanTokenTypes.END_OF_LINE_COMMENT, FAN_LINE_COMMENT);
        keys.put(FanTokenTypes.C_STYLE_COMMENT, FAN_LINE_COMMENT);
    }


    @NotNull
    public Lexer getHighlightingLexer() {
        return new FanHighlightingLexer();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(final IElementType tokenType) {
        return pack(keys.get(tokenType));
    }
}
