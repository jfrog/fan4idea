package org.fandev.lang.fan.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.fandev.index.FanIndex;
import org.fandev.lang.fan.FanSupportLoader;
import org.fandev.lang.fan.psi.FanElement;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 3:45:31 PM
 */
public class FanBaseElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements FanElement {
    public FanBaseElementImpl(final T t, @NotNull final IStubElementType iStubElementType) {
        super(t, iStubElementType);
    }

    public FanBaseElementImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public Language getLanguage() {
        return FanSupportLoader.FAN.getLanguage();
    }

    @Override
    public PsiElement getParent() {
        return getParentByStub();
    }

    protected FanTypeDefinition getFanObjType() {
        return getFanTypeByName("Obj");
    }

    protected FanTypeDefinition getVoidType() {
        return getFanTypeByName("Void");
    }

    protected FanTypeDefinition getFanTypeByName(final String name) {
        final FanIndex index = (FanIndex) getProject().getComponent(FanIndex.COMPONENT_NAME);
        final FanFile objFile = index.getFanFileByTypeName(name);

        if (objFile != null) {
            final FanTypeDefinition[] typeDefinitions = objFile.getTypeDefinitions();
            if (typeDefinitions != null && typeDefinitions.length == 1) {
                return typeDefinitions[0];
            }
        }
        return null;
    }
}
