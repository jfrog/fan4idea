package org.fandev.lang.fan.structure.elements.impl;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.fandev.lang.fan.structure.elements.FanStructureViewElement;
import org.fandev.lang.fan.structure.elements.itemsPresentations.impl.FanEnumValueDefinitionItemPresentation;

/**
 * Date: Apr 1, 2009
 * Time: 12:01:26 AM
 *
 * @author Dror Bereznitsky
 */
public class FanEnumValueDefinitionStructureViewElement extends FanStructureViewElement {
    PsiElement myElement;

    protected FanEnumValueDefinitionStructureViewElement(final PsiElement myElement) {
        super(myElement);
        this.myElement = myElement;
    }

    public ItemPresentation getPresentation() {
        return new FanEnumValueDefinitionItemPresentation(myElement);
    }

    public TreeElement[] getChildren() {
        return new TreeElement[0];
    }
}
