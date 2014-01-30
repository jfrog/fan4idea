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
package org.fandev.lang.fan.parsing.expression.logical;

import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiBuilder;
import static org.fandev.lang.fan.FanTokenTypes.XOR;
import static org.fandev.lang.fan.FanTokenTypes.OR;
import static org.fandev.lang.fan.FanElementTypes.BIT_OR_EXPR;
import org.fandev.lang.fan.parsing.expression.arithmetic.UnaryExpression;
import org.fandev.lang.fan.parsing.expression.arithmetic.ClosureExpression;
import org.fandev.lang.fan.parsing.expression.arithmetic.TermExpression;

/**
 * @author freds
 * @date Mar 1, 2009
 */
public class BitOrExpression extends SeparatorRepeatExpression {
    private static final BitOrExpression instance = new BitOrExpression();

    public BitOrExpression() {
        super(BIT_OR_EXPR, TokenSet.create(XOR, OR));
    }

    public boolean innerParse(final PsiBuilder builder, final TokenSet stopper) {
        if (XOR == builder.getTokenType()) {
            // If already ^ let's jump to unary prefix expression
            return UnaryExpression.parse(builder, stopper);
        }
        if (OR == builder.getTokenType()) {
            // If already a | it's a closure or type literal go there
            return TermExpression.parse(builder, stopper);
        }
        return BitAndExpression.parse(builder, stopper);
    }

    public static boolean parse(final PsiBuilder builder, final TokenSet stopper) {
        return instance.parseThis(builder, stopper);
    }
}
