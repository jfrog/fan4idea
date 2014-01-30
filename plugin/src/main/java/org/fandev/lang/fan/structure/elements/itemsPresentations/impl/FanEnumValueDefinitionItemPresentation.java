package org.fandev.lang.fan.structure.elements.itemsPresentations.impl;

import org.fandev.lang.fan.structure.elements.itemsPresentations.FanItemPresentation;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanEnumValue;
import org.fandev.icons.Icons;
import com.intellij.psi.PsiElement;

import javax.swing.*;

/**
 * Date: Apr 1, 2009
 * Time: 12:02:36 AM
 *
 * @author Dror Bereznitsky
 */
public class FanEnumValueDefinitionItemPresentation extends FanItemPresentation {
    public FanEnumValueDefinitionItemPresentation(PsiElement myElement) {
        super(myElement);
    }

    public String getPresentableText() {
        final FanEnumValue element = (FanEnumValue) myElement;
        return element.getName() + ":" + element.getContainingClass().getName();
    }

    @Override
    public Icon getIcon(final boolean open) {
        return Icons.FIELD;
    }
}
