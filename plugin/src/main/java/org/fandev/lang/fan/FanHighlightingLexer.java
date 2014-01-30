package org.fandev.lang.fan;

import com.intellij.lexer.LayeredLexer;

/**
 *
 * @author Dror Bereznitsky
 * @date Dec 22, 2008 11:07:23 PM
 */
public class FanHighlightingLexer extends LayeredLexer {
    public FanHighlightingLexer() {
        super(new FanFlexAdapter());
    }
}
