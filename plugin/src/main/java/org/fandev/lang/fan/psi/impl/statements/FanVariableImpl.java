package org.fandev.lang.fan.psi.impl.statements;

import org.fandev.lang.fan.psi.api.statements.FanVariable;
import org.fandev.lang.fan.psi.api.statements.FanDefaultValue;
import org.fandev.lang.fan.psi.api.statements.expressions.FanUnaryExpression;
import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.psi.impl.synthetic.FanLightIdentifier;
import org.fandev.lang.fan.FanElementTypes;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.Bottom;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.lang.ASTNode;

/**
 * Date: May 2, 2009
 * Time: 3:17:34 PM
 *
 * @author Dror Bereznitsky
 */
public class FanVariableImpl extends FanVariableBaseImpl implements FanVariable {
    public FanVariableImpl(final StubElement stubElement, @NotNull final IStubElementType iStubElementType) {
        super(stubElement, iStubElementType);
    }

    public FanVariableImpl(final ASTNode astNode) {
        super(astNode);
    }

    @Override
    public PsiIdentifier getNameIdentifier() {
        final PsiElement ident = findChildByType(FanElementTypes.NAME_ELEMENT);
        assert ident != null;
        return new FanLightIdentifier(getManager(), getContainingFile(), ident.getTextRange());
    }

    public PsiType getTypeNoResolve() {
        // What does it mean no resolve?
        return Bottom.BOTTOM;
    }

    @Override
    public String getName() {
        return getNameIdentifier().getText();
    }

    @Override
    public FanTypeElement getTypeElementFan() {
        final FanTypeElement type = super.getTypeElementFan();
        if (type == null) {
            //TODO [Dror] handle type inference - probably any expression should have a getType method
        }
        return type;
    }
}
