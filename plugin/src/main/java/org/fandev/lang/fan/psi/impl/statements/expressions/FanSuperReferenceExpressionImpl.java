package org.fandev.lang.fan.psi.impl.statements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.util.PsiTreeUtil;
import org.fandev.lang.fan.psi.api.statements.expressions.FanSuperReferenceExpression;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.utils.FanUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Date: Jul 19, 2009
 * Time: 10:24:08 PM
 *
 * @author Dror Bereznitsky
 */
public class FanSuperReferenceExpressionImpl extends FanExpressionImpl implements FanSuperReferenceExpression {
    public FanSuperReferenceExpressionImpl(final ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public FanTypeDefinition getReferencedType() {
        final FanTypeDefinition thisTypeDefinition = FanUtil.getContainingType(this);
        if (thisTypeDefinition != null) {
            return thisTypeDefinition.getSuperType();
        }
        return null;
    }
}
