package org.fandev.lang.fan;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;

import java.io.Reader;

/**
 *
 * @author Dror Bereznitsky
 * @date Dec 21, 2008 4:01:40 PM
 */
public class FanFlexAdapter extends FlexAdapter {
   public FanFlexAdapter() {
    this(new _FanLexer((Reader)null));
  }

  public FanFlexAdapter(final FlexLexer lexer) {
    super(lexer);
    assert lexer instanceof _FanLexer;
  }
}
