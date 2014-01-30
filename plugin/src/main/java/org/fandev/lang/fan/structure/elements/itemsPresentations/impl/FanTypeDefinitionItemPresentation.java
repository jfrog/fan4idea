package org.fandev.lang.fan.structure.elements.itemsPresentations.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.impl.ElementBase;
import com.intellij.psi.impl.ElementPresentationUtil;
import com.intellij.ui.RowIcon;
import com.intellij.util.VisibilityIcons;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanMixinDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanEnumDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.structure.elements.itemsPresentations.FanItemPresentation;

import javax.swing.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:55:09 PM
 */
public class FanTypeDefinitionItemPresentation extends FanItemPresentation {
    public FanTypeDefinitionItemPresentation(final PsiElement myElement) {
        super(myElement);
    }

    public String getPresentableText() {
        return ((FanTypeDefinition) myElement).getName();
    }
}
