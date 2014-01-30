package org.fandev.lang.fan.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import org.fandev.lang.fan.FanTokenTypes;
import org.fandev.lang.fan.psi.FanReferenceElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Feb 19, 2009 11:34:46 PM
 */
public abstract class FanReferenceElementImpl extends FanBaseElementImpl implements FanReferenceElement {
    protected FanReferenceElementImpl(final StubElement stubElement, @NotNull final IStubElementType iStubElementType) {
        super(stubElement, iStubElementType);
    }

    protected FanReferenceElementImpl(final ASTNode astNode) {
        super(astNode);
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    public String getReferenceName() {
        final PsiElement nameElement = getReferenceNameElement();
        if (nameElement != null) {
            return nameElement.getText();
        }
        return null;
    }

    public PsiElement getReferenceNameElement() {
        final PsiElement element = findChildByType(FanTokenTypes.IDENTIFIER_TOKENS_SET);
        if (element != null) {
            return element;
        }
        return null;
    }

    public PsiElement getElement() {
        return this;
    }

    public TextRange getRangeInElement() {
        final PsiElement refNameElement = getReferenceNameElement();
        if (refNameElement != null) {
            final int offsetInParent = refNameElement.getStartOffsetInParent();
            return new TextRange(offsetInParent, offsetInParent + refNameElement.getTextLength());
        }
        return new TextRange(0, getTextLength());
    }

    //TODO implement this method
    public PsiElement handleElementRename(final String s) throws IncorrectOperationException {
        return null;
    }

    public PsiElement bindToElement(@NotNull final PsiElement element) throws IncorrectOperationException {
        return null;
    }

    @NotNull
    public PsiType[] getTypeArguments() {
        /*final FanTypeArgumentList typeArgsList = getTypeArgumentList();
        if (typeArgsList == null) return PsiType.EMPTY_ARRAY;

        final FanTypeElement[] args = typeArgsList.getTypeArgumentElements();
        if (args.length == 0) return PsiType.EMPTY_ARRAY;
        PsiType[] result = new PsiType[args.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = args[i].getType();
        }

        return result;*/

        return PsiType.EMPTY_ARRAY;
    }
}
