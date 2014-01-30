package org.fandev.lang.fan.structure.elements.itemsPresentations;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:49:41 PM
 */
public abstract class FanItemPresentation implements ItemPresentation {
    protected final PsiElement myElement;

    protected FanItemPresentation(final PsiElement myElement) {
        this.myElement = myElement;
    }

    @Nullable
    public String getLocationString() {
        return null;
    }

    @Nullable
    public Icon getIcon(final boolean open) {
        return myElement.getIcon(Iconable.ICON_FLAG_OPEN);
    }

    @Nullable
    public TextAttributesKey getTextAttributesKey() {
        return null;
    }
}
