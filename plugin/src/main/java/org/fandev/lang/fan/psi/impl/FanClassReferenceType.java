package org.fandev.lang.fan.psi.impl;

import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.fandev.lang.fan.psi.api.types.FanCodeReferenceElement;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Date: Mar 18, 2009
 * Time: 11:17:54 PM
 * @author Dror Bereznitsky
 */
public class FanClassReferenceType extends PsiClassType {
    private final FanCodeReferenceElement myReferenceElement;

    public FanClassReferenceType(final FanCodeReferenceElement ref) {
        this(LanguageLevel.JDK_1_6, ref);
    }

    public FanClassReferenceType(final LanguageLevel languageLevel, final FanCodeReferenceElement ref) {
        super(languageLevel);
        this.myReferenceElement = ref;
    }

    public PsiClass resolve() {
        final ResolveResult[] results = multiResolve();
        if (results.length == 1) {
            final PsiElement only = results[0].getElement();
            return only instanceof PsiClass ? (PsiClass) only : null;
        }

        return null;
    }

    public FanTypeDefinition resolveFanType() {
        return (FanTypeDefinition)resolve();
    }

    //reference resolve is cached
    private ResolveResult[] multiResolve() {
        return myReferenceElement.multiResolve(false);
    }

    public String getClassName() {
        return myReferenceElement.getReferenceName();
    }

    @NotNull
    public PsiType[] getParameters() {
        //todo
        return PsiType.EMPTY_ARRAY;
    }

    @NotNull
    public ClassResolveResult resolveGenerics() {
        return ClassResolveResult.EMPTY;
    }

    @NotNull
    public PsiClassType rawType() {
        return this;
    }

    public String getPresentableText() {
        return myReferenceElement.getReferenceName();
    }

    public String getCanonicalText() {
        return myReferenceElement.getReferenceName();
    }

    public String getInternalCanonicalText() {
        return getCanonicalText();
    }

    public boolean isValid() {
        return myReferenceElement.isValid();
    }

    public boolean equalsToText(@NonNls final String text) {
        return text.endsWith(getPresentableText()) && //optimization
            text.equals(getCanonicalText());
    }

    @NotNull
    public GlobalSearchScope getResolveScope() {
        return myReferenceElement.getResolveScope();
    }

    @NotNull
    public LanguageLevel getLanguageLevel() {
        return myLanguageLevel;
    }

    public PsiClassType setLanguageLevel(final LanguageLevel languageLevel) {
        return new FanClassReferenceType(languageLevel, myReferenceElement);
    }
}
