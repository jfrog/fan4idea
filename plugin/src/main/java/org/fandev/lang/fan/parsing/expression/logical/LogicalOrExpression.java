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

package org.fandev.lang.fan.parsing.expression.logical;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import static org.fandev.lang.fan.FanElementTypes.LOGICAL_OR_EXPR;
import static org.fandev.lang.fan.FanTokenTypes.OROR;

/**
 * @author ilyas
 */
public class LogicalOrExpression extends SeparatorRepeatExpression {
    private static final LogicalOrExpression instance = new LogicalOrExpression();

    public LogicalOrExpression() {
        super(LOGICAL_OR_EXPR, TokenSet.create(OROR));
    }

    public boolean innerParse(final PsiBuilder builder, final TokenSet stopper) {
        return LogicalAndExpression.parse(builder, stopper);
    }

    public static boolean parse(final PsiBuilder builder, final TokenSet stopper) {
        return instance.parseThis(builder, stopper);
    }
}