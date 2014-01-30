package org.fandev.lang.fan.psi.stubs.impl;

import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.stubs.FanTypeDefinitionStub;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.io.StringRef;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 12:34:25 AM
 */
public class FanTypeDefinitionStubImpl extends StubBase<FanTypeDefinition> implements FanTypeDefinitionStub {
    private final StringRef myPodName;
    private final StringRef myName;

    public FanTypeDefinitionStubImpl(final StubElement parent, final IStubElementType elementType,
            final StringRef name, final StringRef podName) {
        super(parent, elementType);
        myName = name;
        myPodName = podName;
    }

    public String getName() {
        return StringRef.toString(myName);
    }

    public String getPodName() {
        return StringRef.toString(myPodName);
    }
}
