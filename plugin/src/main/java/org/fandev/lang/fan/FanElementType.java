package org.fandev.lang.fan;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror
 * @date Dec 13, 2008 11:26:41 PM
 */
public class FanElementType extends IElementType {

    public FanElementType(@NonNls @NotNull final String debugName) {
        this(debugName, true);
    }

    public FanElementType(@NonNls @NotNull final String debugName, final boolean register) {
        super(debugName, FanSupportLoader.FAN.getLanguage(), register);
    }

    @SuppressWarnings({"HardCodedStringLiteral"})
    public String toString() {
        return "FAN:" + super.toString();
    }
}
