package org.fandev.lang.fan.psi.api.types;

import org.jetbrains.annotations.NotNull;

/**
 * Date: Jul 3, 2009
 * Time: 11:25:02 PM
 *
 * @author Dror Bereznitsky
 */
public interface FanClassTypeElement extends FanTypeElement {
    @NotNull
    FanCodeReferenceElement getReferenceElement();
}
