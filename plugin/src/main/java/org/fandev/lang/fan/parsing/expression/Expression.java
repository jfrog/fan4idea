/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fandev.lang.fan.parsing.expression;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.expression.logical.LogicalOrExpression;
import org.fandev.lang.fan.parsing.expression.arithmetic.UnaryExpression;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.*;

/**
 * @author freds
 * @date Jan 13, 2009
 */
public class Expression {

    public static boolean parseExpr(final PsiBuilder builder, final TokenSet stopper, final IElementType expressionType) {
        final PsiBuilder.Marker m = builder.mark();
        final TokenSet newStopper = TokenSet.orSet(stopper, ASSIGN_OP, TokenSet.create(QUEST));
        boolean res = LogicalOrExpression.parse(builder, newStopper);
        if (!res) {
            m.drop();
            return false;
        }
        if (firstAfter(builder, NLS) == QUEST) {
            final PsiBuilder.Marker condExprMarker = m.precede();
            final PsiBuilder.Marker exprWrapper = condExprMarker.precede();
            m.done(CONDITION_EXPR);
            removeNls(builder);
            advanceNoNls(builder);
            res = parseExpr(builder, TokenSet.orSet(stopper, TokenSet.create(COLON)), COND_TRUE_BLOCK);
            final IElementType firstAfter = firstAfter(builder, NLS);
            if (res && firstAfter == COLON) {
                removeNls(builder);
                ParserUtils.advanceNoNls(builder);
                final PsiBuilder.Marker falseBlock = builder.mark();
                res = parseExpr(builder, stopper, COND_FALSE_BLOCK);
                falseBlock.done(COND_FALSE_BLOCK);
            }
            condExprMarker.done(COND_EXPR);
            exprWrapper.done(expressionType);
        } else if (ASSIGN_OP.contains(builder.getTokenType())) {
            final PsiBuilder.Marker assignExprMarker = m.precede();
            final PsiBuilder.Marker exprWrapper = assignExprMarker.precede();
            m.done(ASSIGN_LEFT_EXPR);
            advanceNoNls(builder);
            res = parseExpr(builder, stopper, ASSIGN_RIGHT_EXPR);
            assignExprMarker.done(ASSIGN_EXPRESSION);
            exprWrapper.done(expressionType);
        } else {
            m.done(expressionType);
        }
        return res;
    }
}
