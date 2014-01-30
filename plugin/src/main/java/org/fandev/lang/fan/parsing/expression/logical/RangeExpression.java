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
import static org.fandev.lang.fan.FanElementTypes.RANGE_EXPR;
import static org.fandev.lang.fan.FanTokenTypes.RANGE_SEP_INCL;
import static org.fandev.lang.fan.FanTokenTypes.RANGE_SEP_EXCL;

/**
 * @author freds
 * @date Mar 1, 2009
 */
public class RangeExpression extends SeparatorRepeatExpression {
    private static final RangeExpression instance = new RangeExpression();

    public RangeExpression() {
        super(RANGE_EXPR, TokenSet.create(RANGE_SEP_INCL, RANGE_SEP_EXCL));
    }

    public boolean innerParse(final PsiBuilder builder, final TokenSet stopper) {
        return BitOrExpression.parse(builder, stopper);
    }

    public static boolean parse(final PsiBuilder builder, final TokenSet stopper) {
        return instance.parseThis(builder, stopper);
    }
}
