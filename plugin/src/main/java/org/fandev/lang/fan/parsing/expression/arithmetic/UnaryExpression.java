/*
 * Copyright 2000-2007 JetBrains s.r.o.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fandev.lang.fan.parsing.expression.arithmetic;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanElementTypes.PREFIX_EXPR;
import org.fandev.lang.fan.FanTokenTypes;
import static org.fandev.lang.fan.parsing.util.ParserUtils.advanceNoNls;
import org.fandev.lang.fan.parsing.expression.ExpressionParser;

/**
 * @author ilyas
 */
public class UnaryExpression implements ExpressionParser {

    public static TokenSet PREFIXES = TokenSet.create(
            FanTokenTypes.EXCL,
            FanTokenTypes.PLUS,
            FanTokenTypes.MINUS,
            FanTokenTypes.TILDE,
            FanTokenTypes.AND,
            FanTokenTypes.PLUSPLUS,
            FanTokenTypes.MINUSMINUS
    );

    private static TokenSet POSTFIXES = TokenSet.create(
            FanTokenTypes.PLUSPLUS,
            FanTokenTypes.MINUSMINUS
    );

    private static UnaryExpression instance = new UnaryExpression();

    public static boolean parse(final PsiBuilder builder, final TokenSet stopper) {
        boolean res = parsePrefixExpression(builder, stopper, instance);
        if (!res) {
            final PsiBuilder.Marker marker = builder.mark();
            final TokenSet newStopper = TokenSet.orSet(stopper, POSTFIXES);
            res = TermExpression.parse(builder, newStopper);
            if (POSTFIXES.contains(builder.getTokenType())) {
                builder.advanceLexer();
                marker.done(FanElementTypes.POSTFIX_EXPR);
            } else {
                marker.done(FanElementTypes.UNARY_EXPR);
            }
        }
        return res;
    }

    public static boolean parsePrefixExpression(final PsiBuilder builder, final TokenSet stopper, final ExpressionParser nextParser) {
        if (PREFIXES.contains(builder.getTokenType())) {
            final PsiBuilder.Marker marker = builder.mark();
            advanceNoNls(builder);
            nextParser.innerParse(builder, stopper);
            marker.done(PREFIX_EXPR);
            return true;
        }
        return false;
    }

    public boolean innerParse(final PsiBuilder builder, final TokenSet stopper) {
        return ParenExpression.parse(builder, stopper);
    }
}