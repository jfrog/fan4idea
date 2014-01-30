package org.fandev.lang.fan.structure.elements.impl;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.impl.FanFileImpl;
import org.fandev.lang.fan.structure.elements.FanStructureViewElement;
import org.fandev.lang.fan.structure.elements.itemsPresentations.impl.FanFileItemPresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:34:34 PM
 */
public class FanFileStructureViewElement extends FanStructureViewElement {
    public FanFileStructureViewElement(final PsiElement myElement) {
        super(myElement);
    }

    public ItemPresentation getPresentation() {
        return new FanFileItemPresentation((FanFileImpl) myElement);
    }

    public TreeElement[] getChildren() {
        final List<FanStructureViewElement> children = new ArrayList<FanStructureViewElement>();

        for (final PsiElement element : myElement.getChildren()) {
            if (element instanceof FanTypeDefinition) {
                children.add(new FanTypeDefinitionStructureViewElement((FanTypeDefinition) element));
            }
        }

        return children.toArray(new FanStructureViewElement[0]);
    }
}
