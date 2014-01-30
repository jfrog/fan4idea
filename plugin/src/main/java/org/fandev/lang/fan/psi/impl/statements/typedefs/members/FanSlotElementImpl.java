/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fandev.lang.fan.psi.impl.statements.typedefs.members;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.*;
import com.intellij.psi.impl.ElementBase;
import com.intellij.psi.impl.ElementPresentationUtil;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStub;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.VisibilityIcons;
import com.intellij.ui.RowIcon;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.modifiers.FanFacet;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.impl.modifiers.FanModifierListImpl;
import org.fandev.lang.fan.psi.impl.synthetic.FanLightIdentifier;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * @author freds
 * @date Jan 24, 2009
 */
public abstract class FanSlotElementImpl<T extends NamedStub> extends FanBaseElementImpl<T>
        implements PsiMember, PsiTypeParameterListOwner, PsiNameIdentifierOwner, PsiDocCommentOwner {
    protected FanSlotElementImpl(final T t, @NotNull final IStubElementType iStubElementType) {
        super(t, iStubElementType);
    }

    protected FanSlotElementImpl(final ASTNode astNode) {
        super(astNode);
    }

    public int getTextOffset() {
        final PsiIdentifier identifier = getNameIdentifier();
        return identifier == null ? 0 : identifier.getTextRange().getStartOffset();
    }

    @Override
    public String getName() {
        final PsiIdentifier psiId = getNameIdentifier();
        return psiId == null ? null : psiId.getText();
    }

    @Nullable
    public PsiIdentifier getNameIdentifier() {
        final PsiElement element = findChildByType(FanElementTypes.NAME_ELEMENT);
        if (element != null) {
            return new FanLightIdentifier(getManager(), getContainingFile(), element.getTextRange());
        }
        return null;
    }

    public PsiElement setName(@NonNls final String name) throws IncorrectOperationException {
        //TODO implement method
        return this;
    }

    public PsiClass getContainingClass() {
        // Parent is body, grand parent is class
        final PsiElement parent = getParent().getParent();
        if (parent instanceof PsiClass) {
            return (PsiClass) parent;
        }
        throw new IllegalStateException("Have a slot " + getName() + " with no class: " + this);
    }

    public PsiDocComment getDocComment() {
        // TODO
        return null;
    }

    public boolean isDeprecated() {
        // TODO
        return false;
    }

    @Nullable
    public PsiModifierList getModifierList() {
        final FanModifierListImpl list = findChildByClass(FanModifierListImpl.class);
        assert list != null;
        return list;
    }

    public boolean hasModifierProperty(@Modifier final String name) {
        final PsiModifierList modifiers = getModifierList();
        if (modifiers != null) {
            return modifiers.hasModifierProperty(name);
        }
        return false;
    }

    public boolean hasTypeParameters() {
        // Always false in Fan
        return false;
    }

    public PsiTypeParameterList getTypeParameterList() {
        // Always null in Fan
        return null;
    }

    @NotNull
    public PsiTypeParameter[] getTypeParameters() {
        return PsiTypeParameter.EMPTY_ARRAY;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FanFacet[] getFacets() {
        return new FanFacet[0];
    }

    @Nullable
    public Icon getIcon(final int flags) {
        final Icon icon = getIconInner();
        final boolean isLocked = (flags & ICON_FLAG_READ_STATUS) != 0 && !isWritable();
        final RowIcon rowIcon = ElementBase.createLayeredIcon(icon, ElementPresentationUtil.getFlags(this, isLocked));
        VisibilityIcons.setVisibilityIcon(getModifierList(), rowIcon);
        return rowIcon;
    }

    protected abstract Icon getIconInner();

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
                return FanSlotElementImpl.this.getIcon(Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }
        };
    }
}
