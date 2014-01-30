package org.fandev.lang.fan.psi.api.statements.blocks;

import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import org.fandev.lang.fan.psi.FanElement;

/**
 * Date: Oct 1, 2009
 * Time: 12:32:29 AM
 *
 * @author Dror Bereznitsky
 */
public interface FanPsiCodeBlock extends FanElement, PsiCodeBlock {
    public PsiElement getLeftBrace();

    public PsiElement getRightBrace();
}
