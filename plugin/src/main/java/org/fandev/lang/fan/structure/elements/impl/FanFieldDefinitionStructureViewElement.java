package org.fandev.lang.fan.structure.elements.impl;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.fandev.lang.fan.structure.elements.FanStructureViewElement;
import org.fandev.lang.fan.structure.elements.itemsPresentations.impl.FanFieldDefinitionItemPresentation;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 7:27:34 PM
 */
public class FanFieldDefinitionStructureViewElement extends FanStructureViewElement {
    PsiElement myElement;

    protected FanFieldDefinitionStructureViewElement(final PsiElement myElement) {
        super(myElement);
        this.myElement = myElement;
    }

    public ItemPresentation getPresentation() {
        return new FanFieldDefinitionItemPresentation(myElement);
    }

    public TreeElement[] getChildren() {
        return new TreeElement[0];
    }
}