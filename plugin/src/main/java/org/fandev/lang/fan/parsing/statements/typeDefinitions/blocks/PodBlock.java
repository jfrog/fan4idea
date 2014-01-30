package org.fandev.lang.fan.parsing.statements.typeDefinitions.blocks;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.SlotDefinition;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.EnumValueDefinition;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.FieldDefinition;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;

/**
 * @author Dror Bereznitsky
 * @date Jan 14, 2009 11:23:21 PM
 */
public class PodBlock {
    /*
<symbolName> := <symbolVal>
     */
    public static boolean parse(final PsiBuilder builder) {
        ParserUtils.removeNls(builder);
        final PsiBuilder.Marker blockMark = builder.mark();
        if (!ParserUtils.getToken(builder, LBRACE)) {
            blockMark.error(FanBundle.message("lcurly.expected"));
            return false;
        }
        ParserUtils.removeNls(builder);

        // parse pod fields declaration => symbols
        while (!builder.eof() && builder.getTokenType() != RBRACE) {
            if (!FieldDefinition.parse(builder)) {
                break;
            }
            ParserUtils.removeNls(builder);
        }
        if (ParserUtils.getToken(builder, RBRACE, FanBundle.message("rcurly.expected"))) {
            blockMark.done(FanElementTypes.BUILDSCRIPT_BODY);
            return true;
        } else {
            ParserUtils.cleanAfterErrorInBlock(builder);
            blockMark.done(FanElementTypes.BUILDSCRIPT_BODY);
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