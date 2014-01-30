package org.fandev.lang.fan.psi.impl.statements.params;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.util.PsiTreeUtil;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.FanDefaultValue;
import org.fandev.lang.fan.psi.api.statements.FanParameterOwner;
import org.fandev.lang.fan.psi.api.statements.params.FanParameter;
import org.fandev.lang.fan.psi.impl.statements.FanVariableBaseImpl;
import org.fandev.lang.fan.psi.impl.synthetic.FanLightIdentifier;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.lang.ASTNode;

/**
 * Date: Apr 29, 2009
 * Time: 11:08:53 PM
 *
 * @author Dror Bereznitsky
 */
public class FanParameterImpl extends FanVariableBaseImpl implements FanParameter {
    public FanParameterImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public PsiElement getDeclarationScope() {
        final FanParameterOwner owner = PsiTreeUtil.getParentOfType(this, FanParameterOwner.class);
        assert owner != null;
        return owner;
    }

    public boolean isVarArgs() {
        return false;
    }

    @NotNull
    public PsiAnnotation[] getAnnotations() {
        //TODO [Dror] Implement
        return new PsiAnnotation[0];
    }

    @Override
    public String toString() {
        return "Parameter";
    }

    @Override
    public PsiIdentifier getNameIdentifier() {
        final PsiElement ident = findChildByType(FanElementTypes.NAME_ELEMENT);
        assert ident != null;
        return new FanLightIdentifier(getManager(), getContainingFile(), ident.getTextRange());
    }

    @Override
    public String getName() {
        return getNameIdentifier().getText();
    }

    public PsiType getTypeNoResolve() {
        return getType();
    }

    public FanDefaultValue getDefaultValue() {
        return findChildByClass(FanDefaultValue.class);
    }
}
