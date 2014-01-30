package org.fandev.lang.fan.psi.stubs.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.IStubElementType;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanReferenceList;
import org.fandev.lang.fan.psi.stubs.FanReferenceListStub;

/**
 * Created by IntelliJ IDEA.
 * User: Dror
 * Date: Mar 20, 2009
 * Time: 4:17:33 PM
 */
public class FanReferenceListStubImpl extends StubBase<FanReferenceList> implements FanReferenceListStub {
    private final String[] myRefNames;

    public FanReferenceListStubImpl(final StubElement parent, final IStubElementType elementType, final String[] refNames) {
        super(parent, elementType);
        myRefNames = refNames;
    }

    public String[] getBaseClasses() {
        return myRefNames;
    }
}
