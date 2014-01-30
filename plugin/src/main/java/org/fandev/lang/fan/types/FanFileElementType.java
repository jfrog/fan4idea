package org.fandev.lang.fan.types;

import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NonNls;

/**
 *
 * @author Dror
 * @date Dec 11, 2008 11:59:10 PM
 */
public class FanFileElementType extends IStubFileElementType {
    public static final int VERSION = 1;

    public FanFileElementType(final Language language) {
        super(language);
    }

    public FanFileElementType(@NonNls final String s, final Language language) {
        super(s, language);
    }

    @Override
    public String getExternalId() {
        return getLanguage() + ":" + toString();
    }

    @Override
    public int getStubVersion() {
        return VERSION;
    }
}
