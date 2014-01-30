package org.fandev.lang.fan.psi.api.types;

import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;

/**
 * Date: Aug 20, 2009
 * Time: 11:52:25 PM
 *
 * @author Dror Bereznitsky
 */
public interface FanListTypeElement extends FanTypeElement{
    FanTypeDefinition getListType();

    FanClassTypeElement getTypeElement();
}
