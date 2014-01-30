package org.fandev.lang.fan.psi.api.statements.expressions;

import com.intellij.psi.PsiElement;

/**
 * Date: Sep 2, 2009
 * Time: 12:22:42 AM
 *
 * @author Dror Bereznitsky
 */
public interface FanIndexExpression extends PsiElement {
    public int getIndex();
}
