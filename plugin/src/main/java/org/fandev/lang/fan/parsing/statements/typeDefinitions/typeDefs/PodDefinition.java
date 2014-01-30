package org.fandev.lang.fan.parsing.statements.typeDefinitions.typeDefs;

import com.intellij.lang.PsiBuilder;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanElementTypes;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.blocks.PodBlock;
import org.fandev.lang.fan.parsing.util.ParserUtils;

/**
 * pod metadata declaration grammar:<br/><pre>
 * ** fandoc
 * @ facets
 * pod &lt;podName>
 * {
 *   &lt;symbolName> := &lt;symbolVal>
 *   ...
 * }
 * </pre>
 *
 * @author Fred Simon
 * @date Jan 15, 2009 12:03:28 AM
 */
public class PodDefinition {
    public static boolean parse(final PsiBuilder builder) {
        if (!ParserUtils.getToken(builder, POD_KEYWORD)) {
            builder.error(FanBundle.message("keywords.expected", POD_KEYWORD.toString()));
            return false;
        }
        ParserUtils.removeNls(builder);

        // Pod name can be one of the built in types in case of the Fan language sources
        if (!IDENTIFIER_TOKENS_SET.contains(builder.getTokenType())) {
            builder.error(FanBundle.message("identifier.expected"));
            return false;
        }
        final PsiBuilder.Marker idMark = builder.mark();
        builder.advanceLexer();
        idMark.done(FanElementTypes.NAME_ELEMENT);
        ParserUtils.removeNls(builder);

        return PodBlock.parse(builder);
    }
}