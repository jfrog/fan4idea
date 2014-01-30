package org.fandev.lang.fan.structure.elements;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:32:51 PM
 */

public abstract class FanStructureViewElement implements StructureViewTreeElement {
    final protected PsiElement myElement;

    protected FanStructureViewElement(final PsiElement myElement) {
        this.myElement = myElement;
    }

    public Object getValue() {
        return myElement.isValid() ? myElement : null;
    }

    public void navigate(final boolean b) {
        ((Navigatable) myElement).navigate(b);
    }

    public boolean canNavigate() {
        return ((Navigatable) myElement).canNavigate();
    }

    public boolean canNavigateToSource() {
        return ((Navigatable) myElement).canNavigateToSource();
    }
}
