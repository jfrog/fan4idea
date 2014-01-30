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
package org.fandev.lang.fan.parsing.statements.typeDefinitions.members;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanElementTypes.METHOD_BODY;
import static org.fandev.lang.fan.FanElementTypes.METHOD_DEFINITION;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.auxiliary.facets.Facet;
import org.fandev.lang.fan.parsing.auxiliary.modifiers.Modifiers;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.statements.Block;
import org.fandev.lang.fan.parsing.types.TypeParameters;
import org.fandev.lang.fan.parsing.types.TypeSpec;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.removeNls;
import static org.fandev.lang.fan.parsing.util.ParserUtils.removeStoppers;

/**
 * <p>Grammar Definition:<ul>
 * <li>&lt;methodDef&gt;      :=  &lt;facets&gt; &lt;methodFlags&gt; &lt;type&gt; &lt;id&gt; "(" &lt;params&gt; ")" &lt;methodBody&gt;</li>
 * <li>&lt;methodFlags&gt;    :=  [&lt;protection&gt;] ["virtual"] ["override"] ["abstract"] ["static"] ["once"]</li>
 * <li>&lt;params&gt;         :=  [&lt;param&gt; ("," &lt;param&gt;)*]</li>
 * <li>&lt;param&gt;          :=  &lt;type&gt; &lt;id&gt; [":=" &lt;expr&gt;]</li>
 * <li>&lt;methodBody&gt;     :=  &lt;eos&gt; | ( "{" &lt;stmts&gt; "}" )</li>
 * </ul></p>
 *
 * @author Fred Simon
 * @date Jan 17, 2009
 */
public class MethodDefinition {
    public static boolean parse(final PsiBuilder builder, final boolean isBuiltInType) {
        final PsiBuilder.Marker declMarker = builder.mark();

        Facet.parse(builder);

        final TokenSet modifiers = Modifiers.parse(builder, DeclarationType.METHOD);
        final boolean modifiersParsed = modifiers.getTypes().length > 0;

        if (!TypeSpec.parse(builder)) {
            declMarker.error(FanBundle.message("type.expected"));
            return false;
        }

        if (!ParserUtils.parseName(builder)) {
            declMarker.drop();
            return false;
        }

        if (FanElementTypes.TYPE_PARAMETER_LIST != TypeParameters.parse(builder)) {
            // TODO: Params (..) expected message
            declMarker.error(FanBundle.message("type.expected"));
            return false;
        }
        if (LBRACE.equals(builder.getTokenType())) {
            Block.parse(builder, METHOD_BODY);
            declMarker.done(METHOD_DEFINITION);
            removeNls(builder);
            return true;
        } else {
            // abstract or native method
            if ((modifiersParsed && (modifiers.contains(ABSTRACT_KEYWORD) || modifiers.contains(NATIVE_KEYWORD))) || isBuiltInType) {
                declMarker.done(METHOD_DEFINITION);
                removeStoppers(builder, SEPARATOR, SEPARATOR);
                return true;
            }
            declMarker.error(FanBundle.message("lcurly.expected"));
            return false;
        }
    }
}
