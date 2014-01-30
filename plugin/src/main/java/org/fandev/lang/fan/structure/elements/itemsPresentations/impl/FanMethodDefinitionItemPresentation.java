package org.fandev.lang.fan.structure.elements.itemsPresentations.impl;

import org.fandev.lang.fan.structure.elements.itemsPresentations.FanItemPresentation;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.icons.Icons;
import com.intellij.psi.PsiElement;

import javax.swing.*;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 7:29:09 PM
 */
public class FanMethodDefinitionItemPresentation  extends FanItemPresentation {
    public FanMethodDefinitionItemPresentation(final PsiElement myElement) {
        super(myElement);
    }

    public String getPresentableText() {
        return ((FanMethod)myElement).getName();
    }
}
