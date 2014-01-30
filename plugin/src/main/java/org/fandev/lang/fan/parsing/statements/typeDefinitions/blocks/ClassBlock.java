package org.fandev.lang.fan.parsing.statements.typeDefinitions.blocks;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.LBRACE;
import static org.fandev.lang.fan.FanTokenTypes.RBRACE;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.members.SlotDefinition;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 4:20:41 PM
 */
public class ClassBlock {
    /*
        <classBody>      :=  "{" <slotDefs> "}"    
     */
    public static boolean parse(final PsiBuilder builder, final boolean isBuiltInType) {
        ParserUtils.removeNls(builder);
        final PsiBuilder.Marker blockMark = builder.mark();
        if (!ParserUtils.getToken(builder, LBRACE)) {
            blockMark.error(FanBundle.message("lcurly.expected"));
            return false;
        }
        ParserUtils.removeNls(builder);
        while (!builder.eof() && builder.getTokenType() != RBRACE) {
            if (!SlotDefinition.parse(builder, DeclarationType.CLASS, isBuiltInType)) {
                break;
            }
            ParserUtils.removeNls(builder);
        }
        if (ParserUtils.getToken(builder, RBRACE, FanBundle.message("rcurly.expected"))) {
            blockMark.done(FanElementTypes.CLASS_BODY);
            return true;
        } else {
            ParserUtils.cleanAfterErrorInBlock(builder);
            blockMark.done(FanElementTypes.CLASS_BODY);
            return false;
        }
    }
}
