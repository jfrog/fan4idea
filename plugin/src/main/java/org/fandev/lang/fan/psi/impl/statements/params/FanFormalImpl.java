package org.fandev.lang.fan.psi.impl.statements.params;

import org.fandev.lang.fan.psi.impl.statements.FanVariableBaseImpl;
import org.fandev.lang.fan.psi.impl.FanFuncType;
import org.fandev.lang.fan.psi.impl.synthetic.FanLightIdentifier;
import org.fandev.lang.fan.psi.api.statements.params.FanFormal;
import org.fandev.lang.fan.psi.api.statements.FanParameterOwner;
import org.fandev.lang.fan.psi.api.statements.expressions.FanClosureExpression;
import org.fandev.lang.fan.psi.api.types.FanFuncTypeElement;
import org.fandev.lang.fan.FanElementTypes;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.Bottom;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Date: Aug 20, 2009
 * Time: 12:43:30 PM
 *
 * @author Dror Bereznitsky
 */
public class FanFormalImpl extends FanVariableBaseImpl implements FanFormal {
    public FanFormalImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public PsiElement getDeclarationScope() {
        final FanFuncTypeElement owner = PsiTreeUtil.getParentOfType(this, FanFuncTypeElement.class);
        assert owner != null;
        return owner;
    }

    public boolean isVarArgs() {
        return false;
    }

    @NotNull
    public PsiAnnotation[] getAnnotations() {
        return PsiAnnotation.EMPTY_ARRAY;
    }

    public PsiType getTypeNoResolve() {
        return getType();
    }

    @Override
    public PsiIdentifier getNameIdentifier() {
        final PsiElement ident = findChildByType(FanElementTypes.NAME_ELEMENT);
        // Formals identifier is not mandatory 
        if (ident != null) {
            return new FanLightIdentifier(getManager(), getContainingFile(), ident.getTextRange());
        }
        return null;
    }

    @Override
    public String getName() {
        final PsiIdentifier identifier = getNameIdentifier();
        if (identifier != null) {
            return identifier.getText();
        }
        return null;
    }
}
