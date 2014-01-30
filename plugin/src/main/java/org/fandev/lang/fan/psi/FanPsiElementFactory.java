package org.fandev.lang.fan.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;
import org.fandev.lang.fan.psi.api.topLevel.FanTopStatement;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.FanFileType;

/**
 * Date: Sep 26, 2009
 * Time: 4:33:50 PM
 *
 * @author Dror Bereznitsky
 */
public class FanPsiElementFactory {
    private Project myProject;

    private static String DUMMY = "dummy.";

    public FanPsiElementFactory(final Project project) {
        myProject = project;
    }

    public static FanPsiElementFactory getInstance(final Project project) {
        return ServiceManager.getService(project, FanPsiElementFactory.class);
    }

    @Nullable
    public FanTopStatement createTopElementFromText(final String text) {
        final PsiFile dummyFile = PsiFileFactory.getInstance(myProject).createFileFromText(DUMMY + FanFileType.FAN_FILE_TYPE.getDefaultExtension(),
                text);
        final PsiElement firstChild = dummyFile.getFirstChild();
        if (!(firstChild instanceof FanTopStatement)){
            return null;
        }

        return (FanTopStatement) firstChild;
    }

    public PsiCodeBlock createMethodBodyFromText(final String text) {
        final StringBuilder sb = new StringBuilder();
        sb.append("class foo {\n");
        sb.append("public Void bar() {\n");
        sb.append(text);
        sb.append("}");
        final FanFile file = createDummyFile(sb.toString());
        final FanTypeDefinition type = (FanTypeDefinition) file.getTopLevelDefinitions()[0];
        final PsiMethod method = type.getMethods()[0];
        return method.getBody();
    }

    private FanFile createDummyFile(final String s, final boolean isPhisical) {
        return (FanFile) PsiFileFactory.getInstance(myProject).createFileFromText("DUMMY__." + FanFileType.FAN_FILE_TYPE.getDefaultExtension(), FanFileType.FAN_FILE_TYPE, s, System.currentTimeMillis(), isPhisical);
    }

    private FanFile createDummyFile(String s) {
        return createDummyFile(s, false);
    }
}
