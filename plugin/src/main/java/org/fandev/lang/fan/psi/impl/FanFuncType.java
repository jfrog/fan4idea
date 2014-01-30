package org.fandev.lang.fan.psi.impl;

import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.search.GlobalSearchScope;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.types.FanFuncTypeElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Date: Jul 19, 2009
 * Time: 11:25:33 PM
 *
 * @author Dror Bereznitsky
 */
public class FanFuncType extends PsiType {
    private FanFuncTypeElement element;

    // TODO [Dror] maybe find something better as the constructor argument 
    public FanFuncType(final FanFuncTypeElement element) {
        super(PsiAnnotation.EMPTY_ARRAY);
        this.element = element;
    }

    public PsiType getReturnType() {
        return element.getReturnType().getType();       
    }

    public FanTypeDefinition getFuncType() {
        return element.getFuncType();
    }

    public String getPresentableText() {
        return element.getText();
    }

    public String getCanonicalText() {
        return element.getText();
    }

    public String getInternalCanonicalText() {
        return element.getText();
    }

    public boolean isValid() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean equalsToText(@NonNls final String s) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <A> A accept(final PsiTypeVisitor<A> aPsiTypeVisitor) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GlobalSearchScope getResolveScope() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public PsiType[] getSuperTypes() {
        return new PsiType[0];
    }
}
