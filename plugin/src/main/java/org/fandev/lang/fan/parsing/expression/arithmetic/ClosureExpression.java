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
import org.fandev.lang.fan.parsing.types.FuncTypeSpec;
import org.fandev.lang.fan.parsing.types.TypeType;
import static org.fandev.lang.fan.parsing.util.ParserUtils.getToken;
import static org.fandev.lang.fan.parsing.util.ParserUtils.removeNls;
import org.fandev.lang.fan.parsing.statements.Block;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanTokenTypes.LBRACE;

/**
 * @author freds
 * @date Mar 2, 2009
 */
public class ClosureExpression {
    public static boolean parse(final PsiBuilder builder) {
        final PsiBuilder.Marker marker = builder.mark();
        // TODO: [Question] Can it be a list of closures?
        if (FuncTypeSpec.parseFuncType(builder, false) == TypeType.FUNCTION) {
            removeNls(builder);
            if (LBRACE == builder.getTokenType()) {
                Block.parse(builder, FanElementTypes.CLOSURE_BODY);
            } else {
                builder.error(FanBundle.message("lcurly.expected"));
            }
            marker.done(FanElementTypes.CLOSURE_EXPR);
            return true;
        }
        marker.rollbackTo();
        return false;
    }
}
