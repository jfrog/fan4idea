package org.fandev.lang.fan.psi.stubs.elements;

import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.impl.statements.typedefs.FanClassDefinitionImpl;
import org.fandev.lang.fan.psi.stubs.FanTypeDefinitionStub;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 12:25:14 AM
 */
public class FanClassDefinitionElementType extends FanTypeDefinitionElementType<FanClassDefinition> {
    public FanClassDefinitionElementType() {
        super("class definition");
    }

    public FanClassDefinition createPsi(final FanTypeDefinitionStub stub) {
        return new FanClassDefinitionImpl(stub);
    }
}
