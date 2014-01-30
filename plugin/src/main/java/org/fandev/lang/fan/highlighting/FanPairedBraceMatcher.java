package org.fandev.lang.fan.highlighting;

import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.BracePair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.fandev.lang.fan.FanTokenTypes.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dror
 * Date: Mar 13, 2009
 * Time: 3:54:16 PM
 */
public class FanPairedBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] PAIRS = new BracePair[]{
        new BracePair(LBRACE, RBRACE, true),
        new BracePair(LPAR, RPAR, false),
        new BracePair(LBRACKET, RBRACKET, false)
    };

    public BracePair[] getPairs() {
        return PAIRS;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull final IElementType lbraceType, @Nullable final IElementType contextType) {
        return true;
    }

    public int getCodeConstructStart(final PsiFile file, final int openingBraceOffset) {
        return openingBraceOffset;
    }
}
