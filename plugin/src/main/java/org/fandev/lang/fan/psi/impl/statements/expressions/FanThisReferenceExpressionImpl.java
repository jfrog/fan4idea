package org.fandev.lang.fan.psi.impl.statements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.util.PsiTreeUtil;
import org.fandev.lang.fan.psi.api.statements.expressions.FanThisReferenceExpression;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.FanElement;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.utils.FanUtil;

/**
 * Date: Jul 16, 2009
 * Time: 11:06:40 PM
 *
 * @author Dror Bereznitsky
 */
public class FanThisReferenceExpressionImpl extends FanExpressionImpl implements FanThisReferenceExpression {
    public FanThisReferenceExpressionImpl(final ASTNode astNode) {
        super(astNode);
    }

    public FanTypeDefinition getReferencedType() {
        return FanUtil.getContainingType(this);
    }
}
