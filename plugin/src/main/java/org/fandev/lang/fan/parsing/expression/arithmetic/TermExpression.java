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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.expression.Expression;
import org.fandev.lang.fan.parsing.expression.argument.LiteralExpression;
import org.fandev.lang.fan.parsing.statements.Block;
import org.fandev.lang.fan.parsing.statements.expressions.arguments.Arguments;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.FieldDefinition;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.PropertyBlock;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.*;

/**
 * @author ilyas
 */
public class TermExpression {
    private static final TokenSet DOTS = TokenSet.create(
            DOT,
            DYN_CALL,
            SAFE_DOT,
            SAFE_DYN_CALL
    );

    public static boolean parse(final PsiBuilder builder, final TokenSet stopper) {
        final PsiBuilder.Marker marker = builder.mark();
        final TokenSet newStopper = TokenSet.orSet(stopper, DOTS);
        boolean res = parseBase(builder, newStopper);
        if (res && (hasDot(builder) || !stopper.contains(builder.getTokenType()))) {
            res = parseTermChainLoop(builder, stopper);
        }
        if (res) {
            // Check for with block with look ahead
            if (parseWithBlock(builder) == null) {
                res = false;
            }
        }
        marker.done(TERM_EXPR);
        return res;
    }

    public static boolean parseTermChainLoop(final PsiBuilder builder, final TokenSet stopper) {
        boolean res = true;
        if (!stopper.contains(NLS)) {
            removeNls(builder);
        }
        while (res && !builder.eof() && (hasDot(builder) || !stopper.contains(builder.getTokenType()))) {
            res = parseTermChain(builder, stopper);
            if (!stopper.contains(NLS)) {
                // Remove NLS if not part of the stoppers (Meaning NLS has no meaning in this expression)
                removeNls(builder);
            }
        }
        return res;
    }

    public static boolean parseBase(final PsiBuilder builder, final TokenSet stopper) {
        if (LiteralExpression.parse(builder, stopper)) {
            return true;
        }
        if (IdExpression.parse(builder)) {
            return true;
        }
        return ClosureExpression.parse(builder);
    }

    private static boolean parseTermChain(final PsiBuilder builder, final TokenSet stopper) {
        if (hasDot(builder)) {
            // Dot call
            removeNls(builder);
            advanceNoNls(builder);
            // Can be .super for named super
            if (SUPER_KEYWORD == builder.getTokenType()) {
                builder.advanceLexer();
            } else {
                IdExpression.parse(builder);
            }
        } else if (LBRACKET.equals(builder.getTokenType())) {
            // Index expression
            advanceNoNls(builder);
            boolean res = true;
            while (res && !builder.eof() && RBRACKET != builder.getTokenType()) {
                res = Expression.parseExpr(builder, TokenSet.create(RBRACKET), FanElementTypes.INDEX_EXPR);
                removeNls(builder);
            }
            getToken(builder, RBRACKET, FanBundle.message("rbrack.expected"));
        } else if (LPAR.equals(builder.getTokenType())) {
            // Arguments
            removeNls(builder);
            Arguments.parse(builder);
            removeNls(builder);
            // May have closure after
            ClosureExpression.parse(builder);
        } else {
            return parseWithBlock(builder) == WITH_BLOCK_EXPR;
        }
        return true;
    }

    private static boolean hasDot(final PsiBuilder builder) {
        // TODO: Mege dots and with block lookahead without stopper test
        return DOTS.contains(ParserUtils.firstAfter(builder,NLS));
    }

    private static IElementType parseWithBlock(final PsiBuilder builder) {
        if (ParserUtils.firstAfter(builder, NLS) == LBRACE) {
            if (builder.getUserData(FieldDefinition.FIELD_NAME) != null) {
                // May be get and set block
                final PropertyBlock block = FieldDefinition.findPropertyBlockType(builder);
                if (block != PropertyBlock.NONE) {
                    // Should be parsed in FieldDefinition
                    return WRONGWAY;
                }
            }
            removeNls(builder);
            if (Block.parse(builder, WITH_BLOCK_EXPR)) {
                return WITH_BLOCK_EXPR;
            } else {
                return null;
            }
        }
        return WRONGWAY;
    }
}
