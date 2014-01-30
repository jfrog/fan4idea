package org.fandev.lang.fan.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.fandev.lang.fan.psi.FanElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 3:45:31 PM
 */
public class FanStubElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements FanElement {
    public FanStubElementImpl(final T t, @NotNull final IStubElementType iStubElementType) {
        super(t, iStubElementType);
    }

    public FanStubElementImpl(final ASTNode astNode) {
        super(astNode);
    }
}
