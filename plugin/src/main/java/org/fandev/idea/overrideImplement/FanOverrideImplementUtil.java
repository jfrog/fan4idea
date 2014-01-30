package org.fandev.idea.overrideImplement;

import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsParameterImpl;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.text.CharArrayUtil;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanTokenTypes;
import org.fandev.lang.fan.psi.FanPsiElementFactory;
import org.fandev.lang.fan.psi.api.statements.FanTopLevelDefintion;
import org.fandev.lang.fan.psi.api.statements.blocks.FanPsiCodeBlock;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinitionBody;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.topLevel.FanTopStatement;
import org.fandev.utils.PsiUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * Date: Sep 26, 2009
 * Time: 4:08:00 PM
 *
 * @author Dror Bereznitsky
 */
public class FanOverrideImplementUtil {
    private static final Logger LOG = Logger.getInstance("Override method");

    public static void invokeOverrideImplement(final Project project, final Editor editor, final PsiFile file, boolean isImplement) {
        final int offset = editor.getCaretModel().getOffset();

        PsiElement parent = file.findElementAt(offset);
        if (parent == null) {
            return;
        }

        while (!(parent instanceof FanTypeDefinition)) {
            parent = parent.getParent();
            if (parent == null) {
                return;
            }
        }

        final FanTypeDefinition aClass = (FanTypeDefinition) parent;

        if (isImplement && aClass.isInterface()) {
            return;
        }

        final Collection<CandidateInfo> candidates = getMethodsToOverrideImplement(aClass, isImplement);
        if (candidates.isEmpty()) {
            return;
        }

        final List<PsiMethodMember> classMembers = new ArrayList<PsiMethodMember>();
        for (final CandidateInfo candidate : candidates) {
            classMembers.add(new PsiMethodMember(candidate));
        }


        final MemberChooser<PsiMethodMember> chooser = new MemberChooser<PsiMethodMember>(classMembers.toArray(new PsiMethodMember[0]), false, true, project);
        chooser.setTitle(isImplement ? FanBundle.message("select.methods.to.implement") : FanBundle.message("select.methods.to.override"));
        chooser.show();

        final List<PsiMethodMember> selectedElements = chooser.getSelectedElements();
        if (selectedElements == null || selectedElements.size() == 0) {
            return;
        }

        for (final PsiMethodMember methodMember : selectedElements) {
            final PsiMethod method = methodMember.getElement();
            final PsiSubstitutor substitutor = methodMember.getSubstitutor();

            final boolean isAbstract = method.hasModifierProperty(PsiModifier.ABSTRACT);

            final String templName = isAbstract ? JavaTemplateUtil.TEMPLATE_IMPLEMENTED_METHOD_BODY : JavaTemplateUtil.TEMPLATE_OVERRIDDEN_METHOD_BODY;

            final FileTemplate template = FileTemplateManager.getInstance().getCodeTemplate(templName);
            final FanMethod result = createOverrideImplementMethodSignature(project, method, substitutor, aClass);

            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                public void run() {
                    try {
                        final PsiModifierList modifierList = result.getModifierList();
                        modifierList.setModifierProperty(PsiModifier.ABSTRACT, false);
                        modifierList.setModifierProperty("virtual", false);

                        setupOverridingMethodBody(project, method, result, template, substitutor, editor);

                        final PsiElement classBody = aClass.getBodyElement();
                        final PsiMethod[] methods = aClass.getMethods();

                        PsiElement anchor = null;

                        final int caretPosition = editor.getCaretModel().getOffset();
                        final PsiElement thisCaretPsiElement = file.findElementAt(caretPosition);

                        final FanTopLevelDefintion previousTopLevelElement = PsiUtil.findPreviousTopLevelElementByThisElement(thisCaretPsiElement);

                        if (thisCaretPsiElement != null && thisCaretPsiElement.getParent() instanceof FanTypeDefinitionBody) {
                            if (FanTokenTypes.LBRACE.equals(thisCaretPsiElement.getNode().getElementType())) {
                                anchor = thisCaretPsiElement.getNextSibling();
                            } else if (FanTokenTypes.RBRACE.equals(thisCaretPsiElement.getNode().getElementType())) {
                                anchor = thisCaretPsiElement.getPrevSibling();
                            } else {
                                anchor = thisCaretPsiElement;
                            }

                        } else if (previousTopLevelElement != null && previousTopLevelElement instanceof FanMethod) {
                            final PsiElement nextElement = previousTopLevelElement.getNextSibling();
                            if (nextElement != null) {
                                anchor = nextElement;
                            }
                        } else if (methods.length != 0) {
                            final PsiMethod lastMethod = methods[methods.length - 1];
                            if (lastMethod != null) {
                                final PsiElement nextSibling = lastMethod.getNextSibling();
                                if (nextSibling != null) {
                                    anchor = nextSibling;
                                }
                            }

                        } else {
                            final PsiElement firstChild = classBody.getFirstChild();
                            assert firstChild != null;
                            final PsiElement nextElement = firstChild.getNextSibling();
                            assert nextElement != null;

                            anchor = nextElement;
                        }

                        aClass.addMemberDeclaration(result, anchor);

                        //PsiUtil.shortenReferences(result);
                        positionCaret(editor, result);
                    } catch (IncorrectOperationException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }

    public static Collection<CandidateInfo> getMethodsToOverrideImplement(PsiClass clazz, boolean flag) {
        final Map<MethodSignature, CandidateInfo> candidates = new HashMap<MethodSignature, CandidateInfo>();

        final PsiMethod[] allMethods = clazz.getAllMethods();
        for (final PsiMethod method : allMethods) {
            if ((method.hasModifierProperty("virtual") || method.hasModifierProperty(PsiModifier.ABSTRACT)) && PsiUtil.isAccessible(method, clazz)) {
                final MethodSignature signature =
                        MethodSignatureUtil.createMethodSignature(method.getName(), method.getParameterList(), method.getTypeParameterList(), PsiSubstitutor.EMPTY);
                final CandidateInfo candidateInfo = new CandidateInfo(method, PsiSubstitutor.EMPTY);
                candidates.put(signature, candidateInfo);
            }
        }
        
        return candidates.values();
    }

    private static void positionCaret(final Editor editor, final FanMethod result) {
        final FanPsiCodeBlock body = (FanPsiCodeBlock) result.getBody();
        if (body == null) {
            return;
        }

        final PsiElement lBrace = body.getLeftBrace();

        assert lBrace != null;
        final PsiElement l = lBrace.getNextSibling();
        assert l != null;

        final PsiElement rBrace = body.getRightBrace();

        assert rBrace != null;
        final PsiElement r = rBrace.getPrevSibling();
        assert r != null;

        LOG.assertTrue(!PsiDocumentManager.getInstance(result.getProject()).isUncommited(editor.getDocument()));
        final String text = editor.getDocument().getText();

        int start = l.getTextRange().getStartOffset();
        start = CharArrayUtil.shiftForward(text, start, "\n\t ");
        int end = r.getTextRange().getEndOffset();
        end = CharArrayUtil.shiftBackward(text, end - 1, "\n\t ") + 1;

        editor.getCaretModel().moveToOffset(Math.min(start, end));
        editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
        if (start < end) {
            //Not an empty body
            editor.getSelectionModel().setSelection(start, end);
        }
    }

    private static boolean writeMethodModifiers(final StringBuffer text, final PsiModifierList modifierList, final String[] modifiers) {
        boolean wasAddedModifiers = false;
        for (final String modifierType : modifiers) {
            if (modifierList.hasModifierProperty(modifierType)) {
                text.append(modifierType);
                text.append(" ");
                wasAddedModifiers = true;
            }
        }
        return wasAddedModifiers;
    }


    private static final String[] FAN_MODIFIERS = new String[]{
            "new",
            "internal",
            PsiModifier.NATIVE,
            PsiModifier.PUBLIC,
            PsiModifier.PROTECTED,
            PsiModifier.STATIC,
    };


    private static FanMethod createOverrideImplementMethodSignature(final Project project, final PsiMethod method, final PsiSubstitutor substitutor, final PsiClass aClass) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("class foo {");
        writeMethodModifiers(buffer, method.getModifierList(), FAN_MODIFIERS);

        final PsiType returnType = substitutor.substitute(method.getReturnType());

        if (method.isConstructor()) {
            buffer.append(aClass.getName());

        } else {
            if (returnType != null) {
                buffer.append(returnType.getCanonicalText());
                buffer.append(" ");
            }

            buffer.append(method.getName());
        }
        buffer.append(" ");

        buffer.append("(");
        final PsiParameter[] parameters = method.getParameterList().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            final PsiParameter parameter = parameters[i];
            final PsiType parameterType = substitutor.substitute(parameter.getType());
            buffer.append(parameterType.getCanonicalText());
            buffer.append(" ");
            final String paramName = parameter.getName();
            if (paramName != null) {
                buffer.append(paramName);
            } else if (parameter instanceof ClsParameterImpl) {
                final ClsParameterImpl clsParameter = (ClsParameterImpl) parameter;
                buffer.append(((PsiParameter) clsParameter.getMirror()).getName());
            }
        }

        buffer.append(")");
        buffer.append(" ");

        buffer.append("{");
        buffer.append("}");
        buffer.append("}");
        final FanTypeDefinition top = (FanTypeDefinition) FanPsiElementFactory.getInstance(project).createTopElementFromText(buffer.toString());
        return top.getMethodByName(method.getName());
    }

    private static void setupOverridingMethodBody(final Project project, final PsiMethod method, final FanMethod resultMethod, final FileTemplate template, final PsiSubstitutor substitutor, final Editor editor) {
        final PsiType returnType = substitutor.substitute(method.getReturnType());

        String returnTypeText = "";
        if (returnType != null) {
            returnTypeText = returnType.getPresentableText();
        }
        final Properties properties = new Properties();

        properties.setProperty(FileTemplate.ATTRIBUTE_RETURN_TYPE, returnTypeText);
        properties.setProperty(FileTemplate.ATTRIBUTE_DEFAULT_RETURN_VALUE, PsiTypesUtil.getDefaultValueOfType(returnType));
        properties.setProperty(FileTemplate.ATTRIBUTE_CALL_SUPER, callSuper(method, resultMethod));
        JavaTemplateUtil.setClassAndMethodNameProperties(properties, method.getContainingClass(), resultMethod);

        try {
            final String bodyText = template.getText(properties);
            final PsiCodeBlock newBody = FanPsiElementFactory.getInstance(project).createMethodBodyFromText("\n" + bodyText + "\n");

            resultMethod.setBlock(newBody);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    @NotNull
    private static String callSuper(final PsiMethod superMethod, final PsiMethod overriding) {
        @NonNls final StringBuilder buffer = new StringBuilder();
        if (!superMethod.isConstructor() && superMethod.getReturnType() != PsiType.VOID) {
            buffer.append("return ");
        }
        buffer.append("super");
        final PsiParameter[] parms = overriding.getParameterList().getParameters();
        if (!superMethod.isConstructor()) {
            buffer.append(".");
            buffer.append(superMethod.getName());
        }
        buffer.append("(");
        for (int i = 0; i < parms.length; i++) {
            final String name = parms[i].getName();
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(name);
        }
        buffer.append(")");
        return buffer.toString();
    }
}
