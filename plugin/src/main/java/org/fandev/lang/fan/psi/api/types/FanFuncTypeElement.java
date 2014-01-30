package org.fandev.lang.fan.psi.api.types;

import org.fandev.lang.fan.psi.api.statements.params.FanFormal;
import org.fandev.lang.fan.psi.api.statements.params.FanFormals;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;

/**
 * Date: Aug 20, 2009
 * Time: 12:55:28 PM
 *
 * @author Dror Bereznitsky
 */
public interface FanFuncTypeElement extends FanTypeElement {
    FanFormals getFormals();

    FanTypeElement getReturnType();

    FanTypeDefinition getFuncType();
}
