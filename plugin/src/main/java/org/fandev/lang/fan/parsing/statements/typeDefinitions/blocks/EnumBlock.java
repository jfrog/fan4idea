package org.fandev.lang.fan.parsing.statements.typeDefinitions.blocks;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.EnumValueDefinition;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.SlotDefinition;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * @author Dror Bereznitsky
 * @date Jan 14, 2009 11:23:21 PM
 */
public class EnumBlock {
    /*
        <enumBody>       :=  "{" <enumValDefs> <slotDefs> "}"
        <enumValDefs>    :=  <enumValDef> ("," <enumValDef>)* <eos>
        <enumValDef>     :=  <id> ["(" <args> ")"]         
     */
    public static boolean parse(final PsiBuilder builder, final boolean isBuiltinType) {
        ParserUtils.removeNls(builder);
        final PsiBuilder.Marker blockMark = builder.mark();
        if (!ParserUtils.getToken(builder, LBRACE)) {
            blockMark.error(FanBundle.message("lcurly.expected"));
            return false;
        }
        ParserUtils.removeNls(builder);
        // Parse enum values
        while (EnumValueDefinition.parse(builder)) {
            if (!ParserUtils.getToken(builder, COMMA)) {
                break;
            }
            eatCommas(builder);
        }
        eatCommas(builder);
        ParserUtils.getToken(builder, SEMICOLON);

        // parse enum slots
        while (!builder.eof() && builder.getTokenType() != RBRACE) {
            if (!SlotDefinition.parse(builder, DeclarationType.ENUM, isBuiltinType)) {
                break;
            }
            ParserUtils.removeNls(builder);
        }
        if (ParserUtils.getToken(builder, RBRACE, FanBundle.message("rcurly.expected"))) {
            blockMark.done(FanElementTypes.ENUM_BODY);
            return true;
        } else {
            ParserUtils.cleanAfterErrorInBlock(builder);
            blockMark.done(FanElementTypes.ENUM_BODY);
            return false;
        }
    }

    private static void eatCommas(final PsiBuilder builder) {
        ParserUtils.removeNls(builder);
        while (COMMA == builder.getTokenType()) {
            builder.error(FanBundle.message("enum.value.expected"));
            ParserUtils.getToken(builder, COMMA);
            ParserUtils.removeNls(builder);
        }
    }
}
