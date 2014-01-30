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
package org.fandev.lang.fan.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanBundle.message;
import static org.fandev.lang.fan.FanElementTypes.WITH_BLOCK_EXPR;
import static org.fandev.lang.fan.FanTokenTypes.LBRACE;
import static org.fandev.lang.fan.FanTokenTypes.RBRACE;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.getToken;
import static org.fandev.lang.fan.parsing.util.ParserUtils.removeNls;

/**
 * @author freds
 * @date Feb 24, 2009
 */
public class Block {
    public static boolean parse(final PsiBuilder builder, final IElementType statementType) {
        final PsiBuilder.Marker m = builder.mark();
        if (getToken(builder, LBRACE)) {
            removeNls(builder);
            while (!builder.eof() && !RBRACE.equals(builder.getTokenType())) {
                if (!Statement.parse(builder, statementType == WITH_BLOCK_EXPR)) {
                    // Eat until RBRACE in force
                    builder.error(FanBundle.message("rcurly.expected"));
                    ParserUtils.advanceNoNls(builder);
                } else {
                    removeNls(builder);
                }
            }
            getToken(builder, RBRACE, message("rcurly.expected"));
            m.done(statementType);
            return true;
        } else {
            // Try one statement block
            if (Statement.parse(builder)) {
                m.done(statementType);
                return true;
            } else {
                m.drop();
                return false;
            }
        }
    }
}
