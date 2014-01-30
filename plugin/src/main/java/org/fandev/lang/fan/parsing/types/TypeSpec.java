package org.fandev.lang.fan.parsing.types;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanElementTypes.LIST_TYPE;
import static org.fandev.lang.fan.FanElementTypes.MAP_TYPE;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 4:40:10 PM
 */

public class TypeSpec {

    public static boolean parse(final PsiBuilder builder) {
        ParserUtils.removeNls(builder);
        final boolean res = parseType(builder, false) != TypeType.NONE;
        return res;
    }

    private static TypeType parseFunctionOrSimpleType(final PsiBuilder builder, final boolean forLiteral) {
        //func type
        if (OR == builder.getTokenType()) {
            return FuncTypeSpec.parseFuncType(builder, forLiteral);
        }
        return SimpleTypeSpec.parseSimpleType(builder, forLiteral);
    }

    /**
     * &lt;mapType&gt; :=  ["["] &lt;type&gt; ":" &lt;type&gt; ["]"]
     *
     * @param builder
     * @return
     */
    public static TypeType parseType(final PsiBuilder builder, final boolean forLiteral) {
        boolean bracketFlag = false;
        TypeType result;

        // TODO: Use lookahead to determined different types
        // TODO: [Question] Can you use Function type as key without brackets for maps
        PsiBuilder.Marker typeMarker = builder.mark();

        boolean forLiteralInnerType = forLiteral;
        if (LBRACKET == builder.getTokenType()) {
            bracketFlag = true;
            builder.advanceLexer();
            // If bracket opened, the literal should be after bracket close
            forLiteralInnerType = false;
        }

        result = parseFunctionOrSimpleType(builder, forLiteralInnerType);
        if (result == TypeType.NONE) {
            typeMarker.rollbackTo();
            return result;
        }
        // Check for : to know if map type
        if (COLON != builder.getTokenType()) {
            if (bracketFlag) {
                // Should have a :
                if (forLiteral) {
                    // May be a list literal
                    typeMarker.rollbackTo();
                    return TypeType.NONE;
                }
                builder.error(FanBundle.message("colon.expected"));
                // Eat ] if exists
                if (ParserUtils.firstAfter(builder, NLS) == RBRACKET) {
                    ParserUtils.removeNls(builder);
                    ParserUtils.advanceNoNls(builder);
                }
            }
            typeMarker.drop();
            return result;
        } else {
            result = TypeType.MAP;
            builder.advanceLexer();
            final TypeType valueType = parseFunctionOrSimpleType(builder, false);
            if (valueType != TypeType.NONE) {
                // TODO: Check [] for list of maps
                // if we have an opening with "[" we need a closing "]"
                if (!RBRACKET.equals(builder.getTokenType()) && bracketFlag) {
                    typeMarker.error(FanBundle.message("rbrack.expected"));
                    return result;
                } else if (RBRACKET.equals(builder.getTokenType()) && !bracketFlag) {
                    typeMarker.error(FanBundle.message("rbrack.no.lbrack"));
                    return result;
                } else if (RBRACKET.equals(builder.getTokenType()) && bracketFlag) {
                    builder.advanceLexer();
                }
                if (LBRACKET == builder.getTokenType() || QUEST == builder.getTokenType()) {
                    PsiBuilder.Marker arrMarker = typeMarker;
                    typeMarker = arrMarker.precede();
                    result = endOfTypeParse(builder, arrMarker, forLiteral, TypeType.MAP);
                }
                typeMarker.done(FanElementTypes.MAP_TYPE);
                return result;
            } else {
                if (bracketFlag) {
                    // TODO: Eat ]
                }
                typeMarker.error(FanBundle.message("type.expected"));
                return result;
            }
        }
    }

    /**
     * Parse for ? nullable type and list declaration []
     *
     * @param builder
     * @param marker
     * @param forLiteral
     * @param defaultType
     * @return
     */
    static TypeType endOfTypeParse(final PsiBuilder builder,
            final PsiBuilder.Marker marker,
            final boolean forLiteral,
            final TypeType defaultType) {
        PsiBuilder.Marker rollTo = builder.mark();
        if (QUEST == builder.getTokenType()) {
            // Should not have whitespaces before
            final int offset = builder.getCurrentOffset();
            if (offset > 0) {
                char c = builder.getOriginalText().charAt(offset - 1);
                if (!Character.isWhitespace(c)) {
                    builder.advanceLexer();
                    rollTo.done(FanElementTypes.NULLABLE_TYPE);
                    rollTo = builder.mark();
                }
            }
        }
        if (!ParserUtils.getToken(builder, LBRACKET)) {
            rollTo.rollbackTo();
            marker.drop();
            return defaultType;
        }
        ParserUtils.removeNls(builder);
        if (!ParserUtils.getToken(builder, RBRACKET)) {
            // In literal mode needs smart analysis to know the type
            if (forLiteral) {
                // First do the simple empty literal
                // If it's the empty list
                if (COMMA == builder.getTokenType()) {
                    rollTo.rollbackTo();
                    marker.done(LIST_TYPE);
                    return TypeType.LIST;
                }
                // Or an empty map
                if (COLON == builder.getTokenType()) {
                    rollTo.rollbackTo();
                    marker.done(MAP_TYPE);
                    return TypeType.MAP;
                }
                // If already declared map or list it will return the type
                if (defaultType != TypeType.MAP && defaultType != TypeType.LIST) {
                    // TODO: Without knowing if it's a type or a variable it's impossible to know if
                    // it's a literal list or indexed expression :(
                    // So, for the moment I go for looking for a comma which is my problem
                    boolean hasComma = false;
                    while (!builder.eof() && builder.getTokenType() != RBRACKET) {
                        if (builder.getTokenType() == COMMA) {
                            hasComma = true;
                            break;
                        }
                        builder.advanceLexer();
                    }
                    if (hasComma) {
                        rollTo.rollbackTo();
                        marker.done(LIST_TYPE);
                        return TypeType.LIST;
                    }
                }
            }
            rollTo.rollbackTo();
            marker.drop();
            return defaultType;
        }
        rollTo.drop();
        ParserUtils.removeNls(builder);
        marker.done(LIST_TYPE);
        final PsiBuilder.Marker newMarker = builder.mark();
        return endOfTypeParse(builder, newMarker, forLiteral, TypeType.LIST);
    }
}
