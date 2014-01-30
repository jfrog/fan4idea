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
import com.intellij.openapi.util.Key;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.auxiliary.facets.Facet;
import org.fandev.lang.fan.parsing.auxiliary.modifiers.Modifiers;
import org.fandev.lang.fan.parsing.expression.Expression;
import org.fandev.lang.fan.parsing.statements.Block;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import static org.fandev.lang.fan.parsing.statements.declaration.DeclarationType.INNER_SET;
import org.fandev.lang.fan.parsing.types.TypeSpec;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.*;

/**
 * <p>Grammar Definition:<ul>
 * <li>&lt;fieldDef&gt;       :=  &lt;facets&gt; &lt;fieldFlags&gt; [&lt;type&gt;] &lt;id&gt; [":=" &lt;expr&gt;]
 * [ "{" [&lt;fieldGetter&gt;] [&lt;fieldSetter&gt;] "}" ] &lt;eos&gt;</li>
 * <li>&lt;fieldFlags&gt;     :=  [&lt;protection&gt;] ["readonly"] ["static"]</li>
 * <li>&lt;fieldGetter&gt;    :=  "get" (&lt;eos&gt; | &lt;block&gt;)</li>
 * <li>&lt;fieldSetter&gt;    :=  &lt;protection&gt; "set" (&lt;eos&gt; | &lt;block&gt;)</li>
 * </ul></p>
 *
 * @author Fred Simon
 * @date Jan 17, 2009
 */
public class FieldDefinition {
    public static final Key<String> FIELD_NAME = new Key<String>("fan.parser.fieldName");

    private static final TokenSet FIELD_DEF_STOPPER = TokenSet.create(SEMICOLON, NLS, LBRACE);

    public static boolean parse(final PsiBuilder builder) {
        final PsiBuilder.Marker declMarker = builder.mark();

        Facet.parse(builder);

        final TokenSet modifiers = Modifiers.parse(builder, DeclarationType.FIELD);
        final boolean modifiersParsed = modifiers.getTypes().length > 0;

        final PsiBuilder.Marker beforeType = builder.mark();
        if (!TypeSpec.parse(builder)) {
            declMarker.drop();
            return false;
        }

        if (!IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
            // May be the type took it
            beforeType.rollbackTo();
        } else {
            beforeType.drop();
        }
        if (!parseName(builder)) {
            declMarker.drop();
            return false;
        }

        boolean hasInitValue = false;
        // := and { get set } are allowed after the new line. Need a look ahead
        IElementType firstTokenAfter = ParserUtils.firstAfter(builder, NLS);
        // Default value
        if (COLON_EQ.equals(firstTokenAfter)) {
            removeNls(builder);
            advanceNoNls(builder);
            builder.putUserData(FIELD_NAME, "on");
            Expression.parseExpr(builder, FIELD_DEF_STOPPER, FIELD_DEFAULT);
            builder.putUserData(FIELD_NAME, null);
            firstTokenAfter = ParserUtils.firstAfter(builder, NLS);
            hasInitValue = true;
        }

        // Getter Setter blocks
        if (LBRACE.equals(firstTokenAfter)) {
            removeNls(builder);
            if (hasInitValue) {
                // If no get, set found inside block, it is for the init value
            }
            final PsiBuilder.Marker getterSetter = builder.mark();
            // NLS after { does not count
            advanceNoNls(builder);
            parseGetterSetter(builder, getterSetter);
        }

        if (builder.eof() || !EOS.contains(builder.getTokenType())) {
            declMarker.error(FanBundle.message("separator.expected"));
            return false;
        }

        // Need to remove SEPARATOR
        ParserUtils.removeStoppers(builder, SEPARATOR, SEPARATOR);

        declMarker.done(FIELD_DEFINITION);

        return true;
    }

    private static void parseGetterSetter(final PsiBuilder builder, final PsiBuilder.Marker getterSetter) {
        if (testEndGetterSetter(builder, getterSetter)) {
            return;
        }
        PropertyBlock blockType = findPropertyBlockType(builder);
        if (blockType == PropertyBlock.GETTER) {
            parseGetBlock(builder);
            if (testEndGetterSetter(builder, getterSetter)) {
                return;
            }
            blockType = findPropertyBlockType(builder);
        }
        if (blockType == PropertyBlock.SETTER) {
            removeNls(builder);
            final PsiBuilder.Marker defMark = builder.mark();
            final TokenSet modifiers = Modifiers.parse(builder, INNER_SET);
            // TODO: Enforce name is "set"
            if (parseName(builder)) {
                parseGetSetBlock(builder);
                defMark.done(FanElementTypes.SETTER_FIELD_DEFINITION);
            } else {
                if (modifiers.getTypes().length > 0) {
                    defMark.error("Found modifiers for setter but no set");
                } else {
                    defMark.error("set block error");
                }
            }
        }
        if (!testEndGetterSetter(builder, getterSetter)) {
            getterSetter.error("Did not find } or <eos>");
        }
    }

    public static PropertyBlock findPropertyBlockType(final PsiBuilder builder) {
        // Look all the following IDENTIFIER or MODIFIERS until you find get or set
        // Will stop if anything else pops in {} or else
        PropertyBlock blockType = PropertyBlock.NONE;
        final PsiBuilder.Marker rb = builder.mark();
        removeNls(builder);
        // If getting { removing it to read inside the block
        if (LBRACE == builder.getTokenType()) {
            advanceNoNls(builder);
        }
        while (!builder.eof()) {
            if (IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
                final String anId = builder.getTokenText();
                if ("get".equals(anId)) {
                    blockType = PropertyBlock.GETTER;
                    break;
                }
                if ("set".equals(anId)) {
                    blockType = PropertyBlock.SETTER;
                    break;
                }
                // Just eat the IDENTIFIER
                advanceNoNls(builder);
            } else if (ALL_MODIFIERS.contains(builder.getTokenType()) || NLS == builder.getTokenType()) {
                // Just eat the MODIFIER and NLS
                advanceNoNls(builder);
            } else {
                // Something else than modifier or identifier.. We stop
                break;
            }
        }
        rb.rollbackTo();
        return blockType;
    }

    private static boolean parseGetSetBlock(final PsiBuilder builder) {
        removeNls(builder);
        if (LBRACE.equals(builder.getTokenType())) {
            return Block.parse(builder, METHOD_BODY);
        } else if (SEMICOLON.equals(builder.getTokenType())) {
            // Just eat it
            builder.advanceLexer();
        }
        return true;
    }

    /**
     * @param builder
     * @param getterSetter
     * @return true if end of getter setter block reached
     */
    private static boolean testEndGetterSetter(final PsiBuilder builder, final PsiBuilder.Marker getterSetter) {
        final IElementType firstAfter = ParserUtils.firstAfter(builder, NLS);
        if (RBRACE.equals(firstAfter)) {
            removeNls(builder);
            // finished
            builder.advanceLexer();
            getterSetter.done(GETTER_SETTER_FIELD_DEFINITION);
            return true;
        }
        return false;
    }

    private static boolean parseGetBlock(final PsiBuilder builder) {
        boolean res;
        final PsiBuilder.Marker defMark = builder.mark();
        res = parseName(builder);
        if (res) {
            res = parseGetSetBlock(builder);
        }
        defMark.done(FanElementTypes.GETTER_FIELD_DEFINITION);
        if (!res) {
            builder.error("Expected get block");
        }
        return false;
    }
}
