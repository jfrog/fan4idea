package org.fandev.lang.fan.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.stubs.FanFieldStub;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 7:10:03 PM
 */
public class FanFieldStubImpl extends FanSlotStubImpl<FanField> implements FanFieldStub {

    public FanFieldStubImpl(final StubElement parent, final StringRef name, final String[] facetNames) {
        super(parent, (IStubElementType) FanElementTypes.FIELD_DEFINITION, name, facetNames);
    }
}