package org.fandev.lang.fan.psi.impl;

import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.psi.api.types.FanMapTypeElement;
import org.fandev.lang.fan.psi.impl.types.FanMapTypeElementImpl;

/**
 * Date: Jul 21, 2009
 * Time: 11:44:07 PM
 *
 * @author Dror Bereznitsky
 */
public class FanMapType extends PsiType {
    private final FanTypeElement keyType;
    private final FanTypeElement valueType;
    private final String text;
    private FanMapTypeElement element;

    public FanMapType(final FanMapTypeElement element, final FanTypeElement keyType, final FanTypeElement valueType) {
        super(new PsiAnnotation[0]);
        this.element = element;
        this.keyType = keyType;
        this.valueType = valueType;
        this.text = "[" + keyType.getType().getPresentableText() + ":" + valueType.getType().getPresentableText() + "]";
    }

    public FanTypeDefinition getMapType() {
        return element.getMapType();
    }

    public String getPresentableText() {
        return text;
    }

    public String getCanonicalText() {
        return text;
    }

    public String getInternalCanonicalText() {
        return text;
    }

    public boolean isValid() {
        return true;
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
        return new PsiType[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
