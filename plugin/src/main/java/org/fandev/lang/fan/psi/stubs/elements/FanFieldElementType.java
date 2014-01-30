package org.fandev.lang.fan.psi.stubs.elements;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.impl.statements.typedefs.members.FanFieldImpl;
import org.fandev.lang.fan.psi.stubs.FanFieldStub;
import org.fandev.lang.fan.psi.stubs.impl.FanFieldStubImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:56:12 PM
 */
public class FanFieldElementType extends FanSlotElementType<FanField, FanFieldStub> {
    public FanFieldElementType(@NotNull String debugName) {
        super(debugName);
    }

    public FanField createPsi(final FanFieldStub stub) {
        return new FanFieldImpl(stub, (IStubElementType) FanElementTypes.FIELD_DEFINITION);
    }

    protected FanFieldStub createStubImpl(final StubElement element, final StringRef name, final String[] facets) {
        return new FanFieldStubImpl(element, name, facets);
    }
}