package org.fandev.lang.fan.parsing.auxiliary;

import com.intellij.lang.PsiBuilder;
import static org.fandev.lang.fan.FanTokenTypes.SEPARATOR;

/**
 * @author Dror Bereznitsky
 * @date Jan 6, 2009 11:30:19 PM
 */
public class Separators {
    public static boolean parse(final PsiBuilder builder) {
        boolean result = false;
        while (!builder.eof() && SEPARATOR.contains(builder.getTokenType())) {
            builder.advanceLexer();
            result = true;
        }
        return result;
    }
}
