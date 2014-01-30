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
package org.fandev.lang.fan.parsing.expression.arithmetic;

import com.intellij.lang.PsiBuilder;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.statements.expressions.arguments.Arguments;
import static org.fandev.lang.fan.parsing.util.ParserUtils.*;

/**
 * @author freds
 * @date Mar 2, 2009
 */
public class IdExpression {
    public static boolean parse(final PsiBuilder builder) {
        final PsiBuilder.Marker marker = builder.mark();
        // Remove @ for symbol
        final boolean symbol = getToken(builder, AT);
        // Remove * for field
        final boolean field = getToken(builder, MULT);
        if (IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
            final PsiBuilder.Marker pod = builder.mark();
            builder.advanceLexer();
            if (builder.getTokenType() == COLON_COLON) {
                // It was the Pod Name
                pod.done(POD_REFERENCE);
                builder.advanceLexer();
                // Recursively call the IdExpression
                parse(builder);
            } else {
                pod.drop();
            }
            if (!field && !symbol) {
                boolean res = true;
                while (!builder.eof() && res && firstAfter(builder, NLS) == LPAR) {
                    removeNls(builder);
                    res = Arguments.parse(builder);
                }
                if (firstAfter(builder, NLS) == OR) {
                    removeNls(builder);
                    ClosureExpression.parse(builder);
                }
            }
            marker.done(ID_EXPR);
            return true;
        } else {
            marker.rollbackTo();
            return false;
        }
    }
}
