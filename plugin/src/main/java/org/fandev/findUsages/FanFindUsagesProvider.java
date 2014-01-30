package org.fandev.findUsages;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiFormatUtil;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.psi.api.statements.expressions.FanReferenceExpression;
import org.fandev.lang.fan.psi.api.statements.FanVariable;

/**
 * Date: Sep 18, 2009
 * Time: 12:22:02 AM
 *
 * @author Dror Bereznitsky
 */
public class FanFindUsagesProvider implements FindUsagesProvider {

    public FanFindUsagesProvider() {}

    @Nullable
    public FanWordsScanner getWordsScanner() {
        return new FanWordsScanner();
    }

    public boolean canFindUsagesFor(@NotNull final PsiElement psiElement) {
        return psiElement instanceof PsiClass ||
                psiElement instanceof PsiMethod ||
                psiElement instanceof FanVariable;
    }

    @Nullable
    public String getHelpId(@NotNull final PsiElement psiElement) {
        return null;
    }

    @NotNull
    public String getType(@NotNull final PsiElement element) {
        if (element instanceof PsiClass) return "class";
        if (element instanceof PsiMethod) return "method";
        if (element instanceof PsiField) return "field";
        if (element instanceof PsiParameter) return "parameter";
        if (element instanceof PsiVariable || element instanceof FanReferenceExpression) return "variable";
        return "";
    }

    @NotNull
    public String getDescriptiveName(@NotNull final PsiElement element) {
        if (element instanceof PsiClass) {
            final PsiClass aClass = (PsiClass) element;
            final String qName = aClass.getQualifiedName();
            return qName == null ? "" : qName;
        } else if (element instanceof PsiMethod) {
            final PsiMethod method = (PsiMethod) element;
            String result = PsiFormatUtil.formatMethod(method,
                    PsiSubstitutor.EMPTY, PsiFormatUtil.SHOW_NAME | PsiFormatUtil.SHOW_PARAMETERS,
                    PsiFormatUtil.SHOW_TYPE);
            final PsiClass clazz = method.getContainingClass();
            if (clazz != null) {
                result += " of " + getDescriptiveName(clazz);
            }

            return result;
        } else if (element instanceof PsiVariable) {
            final String name = ((PsiVariable) element).getName();
            if (name != null) {
                return name;
            }
        }

        return "";
    }

    @NotNull
    public String getNodeText(@NotNull final PsiElement element, final boolean useFullName) {
        if (element instanceof PsiClass) {
            String name = ((PsiClass) element).getQualifiedName();
            if (name == null || !useFullName) {
                name = ((PsiClass) element).getName();
            }
            if (name != null){
                return name;
            }
        } else if (element instanceof PsiMethod) {
            return PsiFormatUtil.formatMethod((PsiMethod) element,
                    PsiSubstitutor.EMPTY,
                    PsiFormatUtil.SHOW_NAME | PsiFormatUtil.SHOW_PARAMETERS,
                    PsiFormatUtil.SHOW_TYPE);
        } else if (element instanceof PsiVariable) {
            final String name = ((PsiVariable) element).getName();
            if (name != null) {
                return name;
            }
        }
        return "";
    }
}

