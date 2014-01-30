package org.fandev.lang.fan.psi.impl;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.pom.java.LanguageLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanEnumDefinition;

/**
 * Date: Sep 8, 2009
 * Time: 8:51:12 AM
 *
 * @author Dror Bereznitsky
 */
public class FanEnumReferenceType extends PsiClassType {
    private final FanEnumDefinition myEnum;

    public FanEnumReferenceType(final FanEnumDefinition myEnum) {
        this(LanguageLevel.JDK_1_6, myEnum);
    }

    public FanEnumReferenceType(final LanguageLevel languageLevel, final FanEnumDefinition myEnum) {
        super(languageLevel);
        this.myEnum = myEnum;
    }

    public PsiClass resolve() {
        return myEnum;
    }

    public String getClassName() {
        return myEnum.getName();
    }

    @NotNull
    public PsiType[] getParameters() {
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
        return myEnum.getName();
    }

    public String getCanonicalText() {
        return  myEnum.getName();
    }

    public String getInternalCanonicalText() {
        return getCanonicalText();
    }

    public boolean isValid() {
        return myEnum.isValid();
    }

    public boolean equalsToText(@NonNls final String text) {
        return text.endsWith(getPresentableText()) && //optimization
            text.equals(getCanonicalText());
    }

    @NotNull
    public GlobalSearchScope getResolveScope() {
        return myEnum.getResolveScope();
    }

    @NotNull
    public LanguageLevel getLanguageLevel() {
        return myLanguageLevel;
    }

    public PsiClassType setLanguageLevel(final LanguageLevel languageLevel) {
        return new FanEnumReferenceType(languageLevel, myEnum);
    }
}
