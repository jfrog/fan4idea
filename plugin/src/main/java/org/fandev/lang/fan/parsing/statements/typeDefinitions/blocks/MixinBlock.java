package org.fandev.lang.fan.parsing.statements.typeDefinitions.blocks;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.LBRACE;
import static org.fandev.lang.fan.FanTokenTypes.RBRACE;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.SlotDefinition;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * @author Dror Bereznitsky
 * @date Jan 14, 2009 11:23:29 PM
 */
public class MixinBlock {
    /*
        <mixinBody> := "{" <slotDefs> "}"   
     */
    public static boolean parse(final PsiBuilder builder, final boolean isBuiltInType) {
        final PsiBuilder.Marker cbMarker = builder.mark();

        if (!ParserUtils.getToken(builder, LBRACE)) {
            builder.error(FanBundle.message("lcurly.expected"));
            cbMarker.rollbackTo();
            return false;
        }

        ParserUtils.removeNls(builder);
        while (!builder.eof() && builder.getTokenType() != RBRACE) {
            if (!SlotDefinition.parse(builder, DeclarationType.MIXIN, isBuiltInType)) {
                break;
            }
            ParserUtils.removeNls(builder);
        }

        if (ParserUtils.getToken(builder, RBRACE, FanBundle.message("rcurly.expected"))) {
            cbMarker.done(FanElementTypes.MIXIN_BODY);
            return true;
        } else {
            ParserUtils.cleanAfterErrorInBlock(builder);
            cbMarker.done(FanElementTypes.MIXIN_BODY);
            return false;
        }
    }
}
