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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.Bottom;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.psi.stubs.FanFieldStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * @author freds
 * @date Jan 23, 2009
 */
public class FanFieldImpl extends FanSlotElementImpl<FanFieldStub>
        implements FanField, StubBasedPsiElement<FanFieldStub> {
    public FanFieldImpl(final FanFieldStub fanFieldStub, @NotNull final IStubElementType iStubElementType) {
        super(fanFieldStub, iStubElementType);
    }

    public FanFieldImpl(ASTNode astNode) {
        super(astNode);
    }
    @Override
    public Icon getIconInner() {
        return Icons.FIELD;
    }
    public String getName() {
        return "";
    }
    public void setInitializer(@Nullable final PsiExpression initializer) throws IncorrectOperationException {
        // TODO
    }

    @NotNull
    public PsiType getType() {
        final FanTypeElement classTypeElement = findTypeElement();
        if (classTypeElement != null) {
            return classTypeElement.getType();
        }
        return Bottom.BOTTOM;
    }

    public PsiType getTypeNoResolve() {
        return getType();
    }

    protected FanTypeElement findTypeElement() {
        FanTypeElement classTypeElement = (FanTypeElement) findChildByType(FanElementTypes.CLASS_TYPE_ELEMENT);
        if (classTypeElement == null) {
            classTypeElement = (FanTypeElement) findChildByType(FanElementTypes.LIST_TYPE);
            if (classTypeElement == null) {
                classTypeElement = (FanTypeElement) findChildByType(FanElementTypes.MAP_TYPE);
                if (classTypeElement == null) {
                    classTypeElement = (FanTypeElement) findChildByType(FanElementTypes.FUNC_TYPE);
                }
            }
        }
        return classTypeElement;
    }


    public PsiTypeElement getTypeElement() {
        final PsiElement typeEl = findChildByType(FanElementTypes.TYPE);
        return null;
    }

    public PsiExpression getInitializer() {
        final PsiElement initEl = findChildByType(FanElementTypes.FIELD_DEFAULT);
        return null;
    }

    public boolean hasInitializer() {
        return false;
    }

    public void normalizeDeclaration() throws IncorrectOperationException {
    }

    public Object computeConstantValue() {
        return null;
    }
}
