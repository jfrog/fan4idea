package org.fandev.lang.fan.psi.impl.statements;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.*;
import com.intellij.lang.ASTNode;
import com.intellij.util.IncorrectOperationException;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.impl.synthetic.FanLightIdentifier;
import org.fandev.lang.fan.psi.api.statements.FanVariable;
import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.FanElementType;
import org.fandev.lang.fan.FanElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * Date: Apr 28, 2009
 * Time: 5:22:06 PM
 *
 * @author Dror Bereznitsky
 */
public abstract class FanVariableBaseImpl<T extends StubElement> extends FanBaseElementImpl<T> implements FanVariable {
    public FanVariableBaseImpl(final T t, @NotNull final IStubElementType iStubElementType) {
        super(t, iStubElementType);
    }

    public FanVariableBaseImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public PsiType getType() {
        final PsiType type = getDeclaredType();
        return type != null ? type : Bottom.BOTTOM;
    }

    @Nullable
    public FanTypeElement getTypeElementFan() {
        return findChildByClass(FanTypeElement.class);
    }

    @Nullable
    public PsiType getDeclaredType() {
        final FanTypeElement typeElement = getTypeElementFan();
        if (typeElement != null) {
            return typeElement.getType();
        }

        return null;
    }

    public PsiIdentifier getNameIdentifier() {
        final PsiElement ident = findChildByType(FanElementTypes.ID_EXPR);
        assert ident != null;
        return new FanLightIdentifier(getManager(), getContainingFile(), ident.getTextRange());
    }

    @Override
    public String getName() {
        return getNameIdentifier().getText();
    }

    @Nullable
    public PsiModifierList getModifierList() {
        //TODO [Dror] implement
        return null;
    }

    public boolean hasModifierProperty(@Modifier final String property) {
        final PsiModifierList modifierList = getModifierList();
        return modifierList != null && modifierList.hasModifierProperty(property);
    }

    public PsiElement setName(@NonNls final String name) throws IncorrectOperationException {
        //TODO [Dror] implement
        return this;
    }

    @Nullable
    public PsiTypeElement getTypeElement() {
        return null;
    }

    @Nullable
    public PsiExpression getInitializer() {
        return null;
    }

    public boolean hasInitializer() {
        return false;
    }

    @Nullable
    public Object computeConstantValue() {
        return null;
    }

    public void normalizeDeclaration() throws IncorrectOperationException {

    }
}
