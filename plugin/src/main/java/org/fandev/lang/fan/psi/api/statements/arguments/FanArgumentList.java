package org.fandev.lang.fan.psi.api.statements.arguments;

import org.fandev.lang.fan.psi.FanElement;
import org.jetbrains.annotations.NotNull;

/**
 * Date: Sep 15, 2009
 * Time: 10:35:13 PM
 *
 * @author Dror Bereznitsky
 */
public interface FanArgumentList extends FanElement {
    FanArgument[] getArguments();

    int indexOf(@NotNull final FanArgument arg);
}
