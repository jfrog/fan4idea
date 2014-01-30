package org.fandev.lang.fan.psi.impl;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.psi.infos.CandidateInfo;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Date: Sep 29, 2009
 * Time: 11:30:53 PM
 *
 * @author Dror Bereznitsky
 */
public class PsiImplUtil {
    public static PsiMethod[] mapToMethods(@Nullable final List<CandidateInfo> list) {
        if (list == null) {
            return PsiMethod.EMPTY_ARRAY;
        }
        final PsiMethod[] result = new PsiMethod[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (PsiMethod) list.get(i).getElement();

        }
        return result;
    }

    public static boolean isExtendsSignature(final MethodSignature superSignatureCandidate, final MethodSignature subSignature) {
        final String name1 = superSignatureCandidate.getName();
        final String name2 = subSignature.getName();
        if (!name1.equals(name2)) {
            return false;
        }

        final PsiType[] superTypes = superSignatureCandidate.getParameterTypes();
        final PsiType[] subTypes = subSignature.getParameterTypes();
        if (subTypes.length != superTypes.length) {
            return false;
        }
        for (int i = 0; i < subTypes.length - 1; i++) {
            final PsiType superType = TypeConversionUtil.erasure(superTypes[i]);
            final PsiType subType = TypeConversionUtil.erasure(subTypes[i]);
            if (!superType.isAssignableFrom(subType)) {
                return false;
            }
        }

        if (superTypes.length > 0) {
            final PsiType lastSuperType = TypeConversionUtil.erasure(superTypes[superTypes.length - 1]);
            final PsiType lastSubType = TypeConversionUtil.erasure(subTypes[superTypes.length - 1]);
            if (lastSuperType instanceof PsiArrayType && !(lastSubType instanceof PsiArrayType)) {
                final PsiType componentType = ((PsiArrayType) lastSuperType).getComponentType();
                if (!lastSubType.isConvertibleFrom(componentType)) {
                    return false;
                }
            } else {
                if (!lastSuperType.isAssignableFrom(lastSubType)){
                    return false;
                }
            }
        }

        return true;
    }
}
