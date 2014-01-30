package org.fandev.lang.fan;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 5:33:46 PM
 */
public abstract class FanStubElementType <S extends StubElement, T extends PsiElement> extends IStubElementType<S, T> {
    public FanStubElementType(@NotNull final String debugName) {
        super(debugName, FanSupportLoader.FAN.getLanguage());
    }

    public String getExternalId() {
        return "fan." + super.toString();
    }
}
