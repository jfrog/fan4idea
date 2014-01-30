package org.fandev.lang.fan.psi.api.types;

import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;

/**
 * Date: Aug 20, 2009
 * Time: 11:56:52 PM
 *
 * @author Dror Bereznitsky
 */
public interface FanMapTypeElement extends FanTypeElement{
    FanTypeDefinition getMapType();
}
