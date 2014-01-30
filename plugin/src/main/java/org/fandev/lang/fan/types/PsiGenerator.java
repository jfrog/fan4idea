package org.fandev.lang.fan.types;

import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;

/**
 *
 * @author Dror
 * @date Dec 13, 2008 11:29:57 PM
 */
public interface PsiGenerator<T extends PsiElement> {
  T construct(final ASTNode node);
}
