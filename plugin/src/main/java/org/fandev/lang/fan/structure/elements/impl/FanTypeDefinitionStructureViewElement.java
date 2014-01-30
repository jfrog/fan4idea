package org.fandev.lang.fan.structure.elements.impl;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanEnumDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanEnumValue;
import org.fandev.lang.fan.structure.elements.FanStructureViewElement;
import org.fandev.lang.fan.structure.elements.itemsPresentations.impl.FanTypeDefinitionItemPresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:43:19 PM
 */
public class FanTypeDefinitionStructureViewElement extends FanStructureViewElement {
    FanTypeDefinition myElement;

    public FanTypeDefinitionStructureViewElement(final FanTypeDefinition element) {
        super(element);
        myElement = element;
    }

    public ItemPresentation getPresentation() {
        return new FanTypeDefinitionItemPresentation(myElement);
    }

    public TreeElement[] getChildren() {
        final List<FanStructureViewElement> children = new ArrayList<FanStructureViewElement>();
        final FanSlot[] fanSlots = myElement.getSlots();
        for (final FanSlot fanSlot : fanSlots) {
            if (fanSlot instanceof FanMethod) {
                children.add(new FanMethodDefinitionStructureViewElement(fanSlot));
            } else if (fanSlot instanceof FanField) {
                children.add(new FanFieldDefinitionStructureViewElement(fanSlot));
            }
        }
        if (myElement instanceof FanEnumDefinition) {
            final FanEnumValue[] enumValues = ((FanEnumDefinition)myElement).getEnumValues();
            for (final FanEnumValue enumValue : enumValues) {
                children.add(new FanEnumValueDefinitionStructureViewElement(enumValue));
            }
        }
        return children.toArray(new FanStructureViewElement[0]);
    }
}
