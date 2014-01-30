package org.fandev.utils;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.compiler.CompilerConfiguration;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.psi.api.statements.FanTopLevelDefintion;
import org.fandev.lang.fan.psi.api.types.FanCodeReferenceElement;

/**
 * @author Dror Bereznitsky
 * @date Feb 21, 2009 3:30:29 PM
 */
public class PsiUtil {
    public static boolean isAccessible(final PsiElement place, final PsiMember member) {
        return com.intellij.psi.util.PsiUtil.isAccessible(member, place, null);
    }

    public static int getFlags(final PsiModifierListOwner paramPsiModifierListOwner, final boolean paramBoolean) {
        final PsiFile localPsiFile = paramPsiModifierListOwner.getContainingFile();

        final VirtualFile localVirtualFile = (localPsiFile == null) ? null : localPsiFile.getVirtualFile();

        final int enumFlag = ((paramPsiModifierListOwner instanceof PsiClass) && (((PsiClass) paramPsiModifierListOwner).isEnum())) ? 1 : 0;

        int mainFlag = (((paramPsiModifierListOwner.hasModifierProperty("final")) && (enumFlag == 0)) ? 1024 : 0) |
                (((paramPsiModifierListOwner.hasModifierProperty("static")) && (enumFlag == 0)) ? 512 : 0) |
                ((paramBoolean) ? 2048 : 0) |
                ((isExcluded(localVirtualFile, paramPsiModifierListOwner.getProject())) ? 4096 : 0);

        if (paramPsiModifierListOwner instanceof PsiClass) {
            if ((paramPsiModifierListOwner.hasModifierProperty("abstract")) && (!(((PsiClass) paramPsiModifierListOwner).isInterface()))) {
                mainFlag |= 256;
            }
        }
        return mainFlag;
    }

    public static boolean isExcluded(final VirtualFile paramVirtualFile, final Project paramProject) {
        return ((paramVirtualFile != null) && (ProjectRootManager.getInstance(paramProject).getFileIndex().isInSource(paramVirtualFile)) && (CompilerConfiguration.getInstance(paramProject).isExcludedFromCompilation(paramVirtualFile)));
    }

    @Nullable
    public static FanTopLevelDefintion findPreviousTopLevelElementByThisElement(final PsiElement element) {
        PsiElement parent = element.getParent();

        while (parent != null && !(parent instanceof FanTopLevelDefintion)) {
            parent = parent.getParent();
        }

        if (parent == null){
            return null;
        }
        return ((FanTopLevelDefintion) parent);
    }
}
