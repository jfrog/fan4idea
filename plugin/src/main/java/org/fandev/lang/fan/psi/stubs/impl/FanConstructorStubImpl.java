package org.fandev.lang.fan.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanConstructor;
import org.fandev.lang.fan.psi.stubs.FanConstructorStub;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 7:10:03 PM
 */
public class FanConstructorStubImpl extends FanSlotStubImpl<FanConstructor> implements FanConstructorStub {

    public FanConstructorStubImpl(final StubElement parent, final StringRef name, final String[] facetNames) {
        super(parent, (IStubElementType) FanElementTypes.CTOR_DEFINITION, name, facetNames);
    }
}