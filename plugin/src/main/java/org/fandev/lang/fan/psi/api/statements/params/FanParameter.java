package org.fandev.lang.fan.psi.api.statements.params;

import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.Nullable;
import org.fandev.lang.fan.psi.api.statements.FanDefaultValue;

/**
 * Date: Apr 29, 2009
 * Time: 11:09:40 PM
 *
 * @author Dror Bereznitsky
 */
public interface FanParameter extends PsiParameter {
    @Nullable
    public FanDefaultValue getDefaultValue();
}
