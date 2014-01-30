package org.fandev.lang.fan.parsing.statements.typeDefinitions.members;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanElementTypes.CTOR_DEFINITION;
import static org.fandev.lang.fan.FanElementTypes.METHOD_BODY;
import org.fandev.lang.fan.FanTokenTypes;
import static org.fandev.lang.fan.FanTokenTypes.LBRACE;
import org.fandev.lang.fan.parsing.auxiliary.facets.Facet;
import org.fandev.lang.fan.parsing.auxiliary.modifiers.Modifiers;
import org.fandev.lang.fan.parsing.statements.Block;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.statements.expressions.arguments.Arguments;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.ReferenceElement;
import org.fandev.lang.fan.parsing.types.TypeParameters;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * Grammar Definition:<ul>
 * <li>&lt;ctorDef&gt;        :=  &lt;facets&gt; &lt;ctorFlags&gt; "new" &lt;id&gt; "(" &lt;params&gt; ")" [ctorChain] &lt;methodBody&gt;</li>
 * <li>&lt;ctorFlags&gt;      :=  [&lt;protection&gt;]</li>
 * <li>&lt;ctorChain&gt;      :=  &lt;ctorChainThis&gt; | &lt;ctorChainSuper&gt;</li>
 * <li>&lt;ctorChainThis&gt;  :=  "this" "." &lt;id&gt; "(" &lt;args&gt; ")"</li>
 * <li>&lt;ctorChainSuper&gt; :=  "super" ["." &lt;id&gt;] "(" &lt;args&gt; ")"</li>
 * </ul>
 *
 * @author Dror Bereznitsky
 * @author Fred Simon
 * @date Jan 14, 2009 11:51:37 PM
 */
public class ConstructorDefinition {

    public static boolean parse(final PsiBuilder builder, final boolean isBuiltInType) {
        final PsiBuilder.Marker constructorMarker = builder.mark();

        Facet.parse(builder);

        final TokenSet modifiers = Modifiers.parse(builder, DeclarationType.CONSTRUCTOR);

        if (!FanTokenTypes.NEW_KEYWORD.equals(builder.getTokenType())) {
            constructorMarker.error("Constructor should have <modifiers> new <id> ()");
            return false;
        }

        ParserUtils.advanceNoNls(builder);

        if (!ParserUtils.parseName(builder)) {
            constructorMarker.drop();
            return false;
        }
        ParserUtils.removeNls(builder);

        // parameter list
        if (FanElementTypes.TYPE_PARAMETER_LIST != TypeParameters.parse(builder)) {
            builder.error(FanBundle.message("params.expected"));
            constructorMarker.drop();
            return false;
        }

        // ctor chain
        parseCtorChain(builder);

        ParserUtils.removeNls(builder);

        if (LBRACE.equals(builder.getTokenType())) {
            Block.parse(builder, METHOD_BODY);
            constructorMarker.done(CTOR_DEFINITION);
            ParserUtils.removeNls(builder);
            return true;
        } else if (isBuiltInType) {
            constructorMarker.done(CTOR_DEFINITION);
            ParserUtils.removeNls(builder);
            return true;
        } else {
            constructorMarker.error(FanBundle.message("lcurly.expected"));
            return false;
        }
    }

    /*
        Constructor related methods

        <ctorDef>        :=  <facets> <ctorFlags> "new" <id> "(" <params> ")" [ctorChain] <methodBody>
        <ctorFlags>      :=  [<protection>]
        <ctorChain>      :=  <ctorChainThis> | <ctorChainSuper>
        <ctorChainThis>  :=  "this" "." <id> "(" <args> ")"
        <ctorChainSuper> :=  "super" ["." <id>] "(" <args> ")"
     */


    private static boolean parseCtorChain(final PsiBuilder builder) {
        if (FanTokenTypes.COLON == builder.getTokenType()) {
            final PsiBuilder.Marker ctorChainMarker = builder.mark();

            ParserUtils.advanceNoNls(builder);
            if (FanTokenTypes.SUPER_KEYWORD == builder.getTokenType() ||
                FanTokenTypes.THIS_KEYWORD == builder.getTokenType()) {
                builder.advanceLexer();
                // ["." <id>]
                if (FanTokenTypes.DOT == builder.getTokenType()) {
                    builder.advanceLexer();
                    if (!ReferenceElement.parseReferenceElement(builder)) {
                        ctorChainMarker.error(FanBundle.message("identifier.expected"));
                    }
                }
                // "(" <args> ")"
                if (Arguments.parse(builder)) {
                    ctorChainMarker.done(FanElementTypes.CTOR_CHAIN);
                    return true;
                } else {
                    ctorChainMarker.error(FanBundle.message("argument.expected"));
                }
            } else {
                ctorChainMarker.error(FanBundle.message("super.or.this.expected"));
            }
        }
        return false;
    }
}
