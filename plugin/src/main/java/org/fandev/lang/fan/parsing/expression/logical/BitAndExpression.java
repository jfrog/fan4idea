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

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import static org.fandev.lang.fan.FanElementTypes.BIT_AND_EXPR;
import static org.fandev.lang.fan.FanTokenTypes.AND;
import org.fandev.lang.fan.parsing.expression.arithmetic.ShiftExpression;
import org.fandev.lang.fan.parsing.expression.arithmetic.UnaryExpression;

/**
 * @author freds
 * @date Mar 1, 2009
 */
public class BitAndExpression extends SeparatorRepeatExpression {
    private static final BitAndExpression instance = new BitAndExpression();

    public BitAndExpression() {
        super(BIT_AND_EXPR, TokenSet.create(AND));
    }

    public boolean innerParse(final PsiBuilder builder, final TokenSet stopper) {
        return ShiftExpression.parse(builder, stopper);
    }

    public static boolean parse(final PsiBuilder builder, final TokenSet stopper) {
        return instance.parseThis(builder, stopper);
    }
}