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
package org.fandev.lang.fan.parsing.types;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * @author freds
 * @date Apr 3, 2009
 */
public class FuncTypeSpec {
    /**
     * <funcType> :=  "|" <formals> ["->" <type> "|"
     * <formals>  :=  [<formal> ("," <formal>)*]
     * <formal>   :=  <type> [id]
     *
     * @param builder
     * @param forLiteral
     * @return
     */
    public static TypeType parseFuncType(final PsiBuilder builder, final boolean forLiteral) {
        // opening |
        if (!(OR == builder.getTokenType())) {
            return TypeType.NONE;
        }

        final PsiBuilder.Marker funcMarker = builder.mark();
        builder.advanceLexer();
        parseFormals(builder);

        // ->
        if (DYN_CALL == builder.getTokenType()) {
            builder.advanceLexer();
            if (TypeSpec.parseType(builder, false) != TypeType.NONE) {
                return parseClosingOr(builder, funcMarker, forLiteral);
            } else {
                funcMarker.error(FanBundle.message("type.expected"));
            }
        } else {
            return parseClosingOr(builder, funcMarker, forLiteral);
        }
        return TypeType.NONE;
    }

    public static TypeType parseClosingOr(final PsiBuilder builder, final PsiBuilder.Marker funcMarker, final boolean forLiteral) {
        // closing |
        if (OR == builder.getTokenType()) {
            builder.advanceLexer();
            funcMarker.done(FUNC_TYPE);
            return TypeSpec.endOfTypeParse(builder, builder.mark(), forLiteral, TypeType.FUNCTION);
        } else {
            funcMarker.error(FanBundle.message("or.expected"));
            return TypeType.NONE;
        }
    }

    /**
     * Parsing of formals stops on -> or |
     * Types and param names are all optional
     *
     * @param builder
     * @return false on syntax error
     */
    public static boolean parseFormals(final PsiBuilder builder) {
        final PsiBuilder.Marker formalsMarker = builder.mark();
        boolean commaExpected = false;
        while (!builder.eof() && !DYN_CALL.equals(builder.getTokenType())) {
            if (commaExpected) {
                if (COMMA.equals(builder.getTokenType())) {
                    // remove comma and continue
                    builder.advanceLexer();
                } else if (OR.equals(builder.getTokenType())) {
                    break;
                } else {
                    formalsMarker.error(FanBundle.message("comma.expected"));
                    return false;
                }
            } else {
                if (COMMA.equals(builder.getTokenType())) {
                    // should be |,|
                    builder.advanceLexer();
                    break;
                }
            }
            final PsiBuilder.Marker formalMarker = builder.mark();
            if (TypeSpec.parseType(builder, false) != TypeType.NONE) {
                commaExpected = true;
                if (IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
                    ParserUtils.parseName(builder);
                }
                formalMarker.done(FORMAL);
            } else {
                formalMarker.rollbackTo();
                formalsMarker.error(FanBundle.message("type.expected"));
                return false;
            }
        }
        formalsMarker.done(FORMALS);
        return true;
    }
}
