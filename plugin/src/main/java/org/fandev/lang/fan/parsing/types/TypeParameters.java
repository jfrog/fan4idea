package org.fandev.lang.fan.parsing.types;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.expression.Expression;
import org.fandev.lang.fan.parsing.statements.expressions.arguments.Arguments;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.*;
import static org.fandev.lang.fan.parsing.util.ParserUtils.getToken;
import static org.fandev.lang.fan.parsing.util.ParserUtils.removeNls;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:14:27 PM
 */
public class TypeParameters {
    public static IElementType parse(final PsiBuilder builder) {
        // <params> :=  [<param> ("," <param>)*]
        if (LPAR == builder.getTokenType()) {
            final PsiBuilder.Marker marker = builder.mark();
            getToken(builder, LPAR);
            removeNls(builder);
            if (!getToken(builder, RPAR)) {
                while (parseTypeParameter(builder) != FanElementTypes.WRONGWAY) {
                    if (!getToken(builder, COMMA)) {
                        break;
                    }
                    eatCommas(builder);
                }
                eatCommas(builder);
                if (!getToken(builder, RPAR)) {
                    builder.error(FanBundle.message("rpar.expected"));
                    ParserUtils.cleanAfterErrorInArguments(builder);
                }
            }
            removeNls(builder);
            marker.done(FanElementTypes.TYPE_PARAMETER_LIST);
            return FanElementTypes.TYPE_PARAMETER_LIST;
        }

        return FanElementTypes.WRONGWAY;
    }

    private static void eatCommas(final PsiBuilder builder) {
        removeNls(builder);
        while (COMMA == builder.getTokenType()) {
            builder.error(FanBundle.message("type.parameter.expected"));
            getToken(builder, COMMA);
            removeNls(builder);
        }
    }


    // <param> := <type> <id> [":=" <expr>]
    private static IElementType parseTypeParameter(final PsiBuilder builder) {
        final PsiBuilder.Marker marker = builder.mark();
        if (TypeSpec.parse(builder)) { // <type>
            // <id>
            if (parseName(builder)) {
                removeNls(builder);
                if (COLON_EQ.equals(builder.getTokenType())) {
                    // Default param initializer
                    ParserUtils.advanceNoNls(builder);
                    Expression.parseExpr(builder, Arguments.ARGUMENTS_STOPPER, FanElementTypes.PARAM_DEFAULT);
                }
                marker.done(FanElementTypes.TYPE_PARAMETER);
                return FanElementTypes.TYPE_PARAMETER;
            }
        }
        marker.error(FanBundle.message("type.parameter.expected"));
        return FanElementTypes.WRONGWAY;
    }
}
