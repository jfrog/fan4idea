package org.fandev.lang.fan.psi.impl.statements.typedefs.members;

import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanEnumValue;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanEnumDefinition;
import org.fandev.lang.fan.psi.stubs.FanEnumValueStub;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.impl.FanEnumReferenceType;
import org.fandev.lang.fan.psi.impl.synthetic.FanLightIdentifier;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.icons.Icons;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import com.intellij.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.IncorrectOperationException;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import javax.swing.*;

/**
 * Date: Mar 31, 2009
 * Time: 11:24:38 PM
 *
 * @author Dror Bereznitsky
 */
public class FanEnumValueImpl extends FanBaseElementImpl<FanEnumValueStub> implements FanEnumValue {
    public FanEnumValueImpl(final FanEnumValueStub fanEnumValueStub, @NotNull final IStubElementType iStubElementType) {
        super(fanEnumValueStub, iStubElementType);
    }

    public FanEnumValueImpl(final ASTNode astNode) {
        super(astNode);
    }

    public void setInitializer(@Nullable final PsiExpression initializer) throws IncorrectOperationException {
        //TODO implement
    }

    @NotNull
    public PsiType getType() {
        return new FanEnumReferenceType((FanEnumDefinition) getContainingClass());
    }

    public PsiType getTypeNoResolve() {
        return getType();
    }

    public PsiTypeElement getTypeElement() {
        //TODO implement
        return null;
    }

    @Override
    public int getTextOffset() {
        final PsiIdentifier identifier = getNameIdentifier();
        return identifier == null ? 0 : identifier.getTextRange().getStartOffset();
    }

    public PsiExpression getInitializer() {
        //TODO implement
        return null;
    }

    public boolean hasInitializer() {
        //TODO implement
        return false;
    }

    public void normalizeDeclaration() throws IncorrectOperationException {
        //TODO implement
    }

    @Nullable
    public Object computeConstantValue() {
        return null;
    }

    @Override
    public String getName() {
        final PsiIdentifier psiId = getNameIdentifier();
        return psiId == null ? null : psiId.getText();
    }

    @NotNull
    public PsiIdentifier getNameIdentifier() {
        final PsiElement element = findChildByType(FanElementTypes.NAME_ELEMENT);
        if (element != null) {
            return new FanLightIdentifier(getManager(), getContainingFile(), element.getTextRange());
        }
        return null;
    }

    public PsiClass getContainingClass() {
        // Parent is body, grand parent is class
        final PsiElement parent = getParent().getParent();
        if (parent instanceof FanEnumDefinition) {
            return (PsiClass) parent;
        }
        throw new IllegalStateException("Have an enum value " + getName() + " with no enum: " + this);
    }

    public boolean isDeprecated() {
        return false;
    }

    @Nullable
    public PsiModifierList getModifierList() {
        return null;
    }

    public boolean hasModifierProperty(@Modifier final String name) {
        // TODO
        return false;
    }

    public PsiElement setName(@NonNls final String name) throws IncorrectOperationException {
        //TODO implement method
        return this;
    }

    @Nullable
    public PsiDocComment getDocComment() {
        //TODO implement method
        return null;
    }

    @Override
    public Icon getIcon(final int flags) {
        return Icons.ENUM;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return getName();
            }

            @Nullable
            public String getLocationString() {
                final PsiClass clazz = getContainingClass();
                final String name = clazz.getQualifiedName();
                assert name != null;
                return "(in " + name + ")";
            }

            @Nullable
            public Icon getIcon(final boolean open) {
                return FanEnumValueImpl.this.getIcon(Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }
        };
    }
}
