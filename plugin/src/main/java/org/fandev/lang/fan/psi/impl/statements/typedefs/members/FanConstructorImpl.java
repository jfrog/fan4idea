package org.fandev.lang.fan.psi.impl.statements.typedefs.members;

import com.intellij.lang.ASTNode;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.PsiMethodReceiver;
import com.intellij.psi.PsiType;
import com.intellij.psi.stubs.IStubElementType;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanConstructor;
import org.fandev.lang.fan.psi.stubs.FanConstructorStub;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:57:41 PM
 */
public class FanConstructorImpl extends FanMethodBaseImpl<FanConstructorStub>
        implements FanConstructor, StubBasedPsiElement<FanConstructorStub> {

    public FanConstructorImpl(final FanConstructorStub fanMethodStub, @NotNull final IStubElementType iStubElementType) {
        super(fanMethodStub, iStubElementType);
    }

    public FanConstructorImpl(final ASTNode astNode) {
        super(astNode);
    }

    public boolean isConstructor() {
        return true;
    }

    public PsiMethodReceiver getMethodReceiver() {
        return null;
    }
}