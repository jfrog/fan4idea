package org.fandev.lang.fan.parsing.statements.typeDefinitions.typeDefs;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.auxiliary.modifiers.Modifiers;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.InheritanceClause;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.blocks.EnumBlock;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * <ul>
 * <li> Enums are normal classes with all associated characteristics</li>
 * <li> Enums are implied const</li>
 * <li> Enums are implied final</li>
 * <li> Enums have a fixed range of instances</li>
 * <li> Enums always always inherit from sys::Enum</li>
 * <li> Enums may inherit zero or more mixins</li>
 * </ul>
 *
 * @author Dror Bereznitsky
 * @date Jan 15, 2009 12:03:28 AM
 */
public class EnumDefinition {
    public static boolean parse(final PsiBuilder builder) {
        Modifiers.parse(builder, DeclarationType.ENUM);
        ParserUtils.removeNls(builder);
        if (!ParserUtils.getToken(builder, ENUM_KEYWORD)) {
            builder.error(FanBundle.message("keywords.expected", ENUM_KEYWORD.toString()));
            return false;
        }
        ParserUtils.removeNls(builder);

        // Enum name can be one of the built in types in case of the Fan language sources
        if (!IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
            builder.error(FanBundle.message("identifier.expected"));
            return false;
        }
        final boolean isBuiltInType = FAN_SYS_TYPE == builder.getTokenType();
        final PsiBuilder.Marker idMark = builder.mark();
        builder.advanceLexer();
        idMark.done(FanElementTypes.NAME_ELEMENT);
        ParserUtils.removeNls(builder);

        // Enums may inherit zero or more mixins
        if (COLON.equals(builder.getTokenType())) {
            InheritanceClause.parse(builder);
            ParserUtils.removeNls(builder);
        }
        return EnumBlock.parse(builder, isBuiltInType);
    }
}
