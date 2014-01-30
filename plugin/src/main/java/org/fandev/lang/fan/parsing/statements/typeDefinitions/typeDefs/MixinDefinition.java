package org.fandev.lang.fan.parsing.statements.typeDefinitions.typeDefs;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.auxiliary.modifiers.Modifiers;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.InheritanceClause;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.blocks.MixinBlock;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * <ul>
 * <li> Mixins are scoped within a pod and globally identified with the qualified name podName::MixinName</li>
 * <li> Mixins contain zero or more uniquely named slots</li>
 * <li> Mixins are implicitly abstract</li>
 * <li> Mixins may inherit zero or more other mixins</li>
 * </ul>
 *
 * @author Dror Bereznitsky
 * @date Jan 15, 2009 12:04:44 AM
 */
public class MixinDefinition {
    public static boolean parse(final PsiBuilder builder) {
        final TokenSet modifers = Modifiers.parse(builder, DeclarationType.MIXIN);
        ParserUtils.removeNls(builder);
        if (!ParserUtils.getToken(builder, MIXIN_KEYWORD)) {
            builder.error(FanBundle.message("keywords.expected", MIXIN_KEYWORD.toString()));
            return false;
        }

        final boolean isBuiltInType = FAN_SYS_TYPE == builder.getTokenType();
        if (!ParserUtils.parseName(builder)) {
            return false;
        }
        ParserUtils.removeNls(builder);

        // Only builtin mixin can be const
        if (modifers.contains(CONST_KEYWORD) && !isBuiltInType) {
            // illegal access modifier
            final String tokenText = builder.getTokenText();
            builder.error(FanBundle.message("illegal.modifier", tokenText, DeclarationType.MIXIN));
        }

        if (COLON.equals(builder.getTokenType())) {
            InheritanceClause.parse(builder);
            ParserUtils.removeNls(builder);
        }

        return MixinBlock.parse(builder, isBuiltInType);
    }
}
