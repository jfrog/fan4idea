package org.fandev.lang.fan.psi.impl;

import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiType;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.types.FanListTypeElement;
import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.psi.api.types.FanClassTypeElement;
import org.fandev.lang.fan.psi.impl.types.FanListTypeElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Date: Jul 17, 2009
 * Time: 11:54:02 PM
 *
 * @author Dror Bereznitsky
 */
public class FanListReferenceType extends PsiArrayType {
    private FanListTypeElement element;

    public FanListReferenceType(final FanListTypeElement element, @NotNull final PsiType psiType) {
        super(psiType);
        this.element = element;
    }

    public FanTypeDefinition getListType() {
        return element.getListType();
    }

    public PsiType getType() {
        return element.getType();
    }

    public FanClassTypeElement getTypeElement() {
        return element.getTypeElement();    
    }
}
