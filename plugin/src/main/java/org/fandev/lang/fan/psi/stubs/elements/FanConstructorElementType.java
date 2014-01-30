package org.fandev.lang.fan.psi.stubs.elements;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanConstructor;
import org.fandev.lang.fan.psi.impl.statements.typedefs.members.FanConstructorImpl;
import org.fandev.lang.fan.psi.stubs.FanConstructorStub;
import org.fandev.lang.fan.psi.stubs.impl.FanConstructorStubImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:56:12 PM
 */
public class FanConstructorElementType extends FanSlotElementType<FanConstructor, FanConstructorStub> {
    public FanConstructorElementType(@NotNull final String debugName) {
        super(debugName);
    }

    public FanConstructor createPsi(final FanConstructorStub stub) {
        return new FanConstructorImpl(stub, (IStubElementType) FanElementTypes.CTOR_DEFINITION);
    }

    protected FanConstructorStub createStubImpl(final StubElement element, final StringRef name, final String[] facets) {
        return new FanConstructorStubImpl(element, name, facets);
    }
}