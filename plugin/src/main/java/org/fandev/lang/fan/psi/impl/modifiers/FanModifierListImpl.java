package org.fandev.lang.fan.psi.impl.modifiers;

import org.fandev.lang.fan.psi.api.modifiers.FanModifierList;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.FanTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.intellij.lang.ASTNode;

/**
 * @author Dror Bereznitsky
 * @date Apr 2, 2009 2:42:18 PM
 */
public class FanModifierListImpl extends FanBaseElementImpl implements FanModifierList {
    public FanModifierListImpl(ASTNode astNode) {
        super(astNode);
    }

    public boolean hasModifierProperty(@Modifier final String modifier) {
        // If no protection keyword is specified, the class defaults to public
        // If no protection keyword is specified, the slot defaults to public
        if (modifier.equals(PsiModifier.PUBLIC)) {
            return findChildByType(FanTokenTypes.PRIVATE_KEYWORD) == null &&
                    findChildByType(FanTokenTypes.PROTECTED_KEYWORD) == null &&
                    findChildByType(FanTokenTypes.INTERNAL_KEYWORD) == null;
        }

        return hasExplicitModifier(modifier);
    }

    //TODO check if we can extends this beyond the modifier set we have in PsiModifier
    public boolean hasExplicitModifier(@Modifier final String name) {
        if (name.equals(PsiModifier.PUBLIC)) return findChildByType(FanTokenTypes.PUBLIC_KEYWORD) != null;
        if (name.equals(PsiModifier.ABSTRACT)) return findChildByType(FanTokenTypes.ABSTRACT_KEYWORD) != null;
        if (name.equals(PsiModifier.NATIVE)) return findChildByType(FanTokenTypes.NATIVE_KEYWORD) != null;
        return hasOtherModifiers(name);
    }

    private boolean hasOtherModifiers(final String name) {
        if (name.equals(PsiModifier.PRIVATE)) return findChildByType(FanTokenTypes.PRIVATE_KEYWORD) != null;
        if (name.equals(PsiModifier.PROTECTED)) return findChildByType(FanTokenTypes.PROTECTED_KEYWORD) != null;
        if (name.equals(PsiModifier.PACKAGE_LOCAL)) return findChildByType(FanTokenTypes.INTERNAL_KEYWORD) != null;
        if (name.equals(PsiModifier.STATIC)) return findChildByType(FanTokenTypes.STATIC_KEYWORD) != null;
        if (name.equals(PsiModifier.FINAL)) return findChildByType(FanTokenTypes.FINAL_KEYWORD) != null;
        return name.equals(PsiModifier.VOLATILE) && findChildByType(FanTokenTypes.VOLATILE_KEYWORD) != null;
    }

    public void setModifierProperty(@Modifier final String name, final boolean value) throws IncorrectOperationException {
        //TODO implement
    }

    public void checkSetModifierProperty(@Modifier final String name, final boolean value) throws IncorrectOperationException {
        //TODO implement
    }

    @NotNull
    public PsiAnnotation[] getAnnotations() {
        return PsiAnnotation.EMPTY_ARRAY;
    }

    @Nullable
    public PsiAnnotation findAnnotation(@NotNull final String qualifiedName) {
        return null;
    }

    @NotNull
    public PsiAnnotation[] getApplicableAnnotations() {
        return new PsiAnnotation[0];
    }

    @NotNull
    public PsiAnnotation addAnnotation(@NotNull final String qualifiedName) {
        return null;
    }

    public String toString() {
        return "Modifiers";
    }
}
