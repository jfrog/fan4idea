package org.fandev.lang.fan.psi.stubs.elements;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.impl.statements.typedefs.members.FanMethodImpl;
import org.fandev.lang.fan.psi.stubs.FanMethodStub;
import org.fandev.lang.fan.psi.stubs.impl.FanMethodStubImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:56:12 PM
 */
public class FanMethodElementType extends FanSlotElementType<FanMethod, FanMethodStub> {
    public FanMethodElementType(@NotNull final String debugName) {
        super(debugName);
    }

    public FanMethod createPsi(final FanMethodStub stub) {
        return new FanMethodImpl(stub, (IStubElementType) FanElementTypes.METHOD_DEFINITION);
    }

    protected FanMethodStubImpl createStubImpl(final StubElement element, final StringRef name, final String[] facets) {
        return new FanMethodStubImpl(element, name, facets);
    }
}
