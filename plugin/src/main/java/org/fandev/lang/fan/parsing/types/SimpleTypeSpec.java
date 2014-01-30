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
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.FAN_SYS_TYPE;
import static org.fandev.lang.fan.FanTokenTypes.IDENTIFIER_TOKENS_SET;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.ReferenceElement;

/**
 * @author freds
 * @date Apr 3, 2009
 */
public class SimpleTypeSpec {

    public static TypeType parseSimpleType(final PsiBuilder builder, final boolean forLiteral) {
        if (FAN_SYS_TYPE == builder.getTokenType()) {
            return parseBuiltInType(builder, forLiteral);
        } else if (IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
            return parseClassOrInterfaceType(builder, forLiteral);
        }
        return TypeType.NONE;
    }

    public static TypeType parseBuiltInType(final PsiBuilder builder, final boolean forLiteral) {
        final PsiBuilder.Marker builtInTypeMarker = builder.mark();

        if (!ReferenceElement.parseReferenceElement(builder)) {
            builtInTypeMarker.drop();
            return TypeType.NONE;
        }

        final PsiBuilder.Marker arrMarker = builtInTypeMarker.precede();
        builtInTypeMarker.done(FanElementTypes.CLASS_TYPE_ELEMENT);
        TypeType result = TypeSpec.endOfTypeParse(builder, arrMarker, forLiteral, TypeType.SIMPE);
        
        return result;
    }

    static TypeType parseClassOrInterfaceType(final PsiBuilder builder, final boolean forLiteral) {
        final PsiBuilder.Marker arrMarker = builder.mark();
        final PsiBuilder.Marker typeElementMarker = builder.mark();

        if (!ReferenceElement.parseReferenceElement(builder)) {
            typeElementMarker.drop();
            arrMarker.rollbackTo();
            return TypeType.NONE;
        }
        typeElementMarker.done(FanElementTypes.CLASS_TYPE_ELEMENT);
        return TypeSpec.endOfTypeParse(builder, arrMarker, forLiteral, TypeType.SIMPE);
    }
}
