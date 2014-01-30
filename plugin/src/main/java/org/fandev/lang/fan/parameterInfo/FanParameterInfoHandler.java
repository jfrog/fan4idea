package org.fandev.lang.fan.parameterInfo;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.completion.JavaCompletionUtil;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.lang.parameterInfo.*;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.fandev.lang.fan.psi.FanElement;
import org.fandev.lang.fan.psi.api.FanResolveResult;
import org.fandev.lang.fan.psi.api.statements.FanVariable;
import org.fandev.lang.fan.psi.api.statements.FanDefaultValue;
import org.fandev.lang.fan.psi.api.statements.params.FanParameter;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.arguments.FanArgumentList;
import org.fandev.lang.fan.psi.api.statements.arguments.FanArgument;
import org.fandev.lang.fan.psi.api.statements.expressions.FanReferenceExpression;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: Sep 14, 2009
 * Time: 11:42:19 PM
 *
 * @author Dror Bereznitsky
 */
public class FanParameterInfoHandler implements ParameterInfoHandler<FanElement, FanResolveResult> {
    public boolean couldShowInLookup() {
        return true;
    }

    public Object[] getParametersForLookup(final LookupElement item, final ParameterInfoContext context) {
        final List<? extends PsiElement> elements = JavaCompletionUtil.getAllPsiElements((LookupItem) item);

        if (elements != null) {
            final List<PsiMethod> methods = new ArrayList<PsiMethod>();
            for (final PsiElement element : elements) {
                if (element instanceof PsiMethod) {
                    methods.add((PsiMethod) element);
                }
            }
            return methods.toArray(new Object[0]);
        }

        return null;
    }

    public Object[] getParametersForDocumentation(final FanResolveResult p, final ParameterInfoContext context) {
        return new Object[0];
    }

    public FanElement findElementForParameterInfo(final CreateParameterInfoContext context) {
        return findAnchorElement(context.getEditor().getCaretModel().getOffset(), context.getFile());
    }

    public void showParameterInfo(@NotNull final FanElement place, final CreateParameterInfoContext context) {
        final PsiElement parent = place.getParent();
        FanResolveResult[] variants = FanResolveResult.EMPTY_ARRAY;
        if (parent instanceof FanReferenceExpression) {
            variants = ((FanReferenceExpression) parent).getSameNameVariants();
        }
        context.setItemsToShow(variants);
        context.showHint(place, place.getTextRange().getStartOffset(), this);
    }

    public FanElement findElementForUpdatingParameterInfo(final UpdateParameterInfoContext context) {
        return findAnchorElement(context.getEditor().getCaretModel().getOffset(), context.getFile());
    }

    public void updateParameterInfo(@NotNull final FanElement o, final UpdateParameterInfoContext context) {

    }

    public String getParameterCloseChars() {
        return ",){}";
    }

    public boolean tracksParameterIndex() {
        return true;
    }

    public void updateUI(final FanResolveResult resolveResult, final ParameterInfoUIContext context) {
        final CodeInsightSettings settings = CodeInsightSettings.getInstance();

        final PsiNamedElement element = (PsiNamedElement) resolveResult.getElement();
        if (element == null || !element.isValid()) {
            context.setUIComponentEnabled(false);
            return;
        }

        int highlightStartOffset = -1;
        int highlightEndOffset = -1;

        final StringBuffer buffer = new StringBuffer();

        if (element instanceof PsiMethod) {
            final FanMethod method = (FanMethod) element;
            if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
                if (!method.isConstructor()) {
                    final PsiType returnType = method.getReturnType();
                    if (returnType != null) {
                        buffer.append(returnType.getPresentableText());
                        buffer.append(" ");
                    }
                }
                buffer.append(element.getName());
                buffer.append("(");
            }

            final int currentParameter = context.getCurrentParameterIndex();

            final PsiParameter[] parms = method.getParameterList().getParameters();
            int numParams = parms.length;
            if (numParams > 0) {
                final PsiSubstitutor substitutor = resolveResult.getSubstitutor();
                for (int j = 0; j < numParams; j++) {
                    final FanParameter parm = (FanParameter) parms[j];

                    final int startOffset = buffer.length();

                    appendParameterText(parm, substitutor, buffer);

                    final int endOffset = buffer.length();

                    if (j < numParams - 1) {
                        buffer.append(", ");
                    }

                    if (context.isUIComponentEnabled() && j == currentParameter) {
                        highlightStartOffset = startOffset;
                        highlightEndOffset = endOffset;
                    }
                }
            } else {
                buffer.append("no parameters");
            }

            if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
                buffer.append(")");
            }

        } else if (element instanceof PsiClass) {
            buffer.append("no parameters");
        } else if (element instanceof FanVariable) {
            final PsiType type = ((FanVariable) element).getType();
            //TODO [dror] hanlde closures here
        }

        final boolean isDeprecated = resolveResult instanceof PsiDocCommentOwner && ((PsiDocCommentOwner) resolveResult).isDeprecated();

        context.setupUIComponentPresentation(
                buffer.toString(),
                highlightStartOffset,
                highlightEndOffset,
                !context.isUIComponentEnabled(),
                isDeprecated,
                false,
                context.getDefaultParameterColor()
        );
    }

    private FanElement findAnchorElement(final int offset, final PsiFile file) {
        final PsiElement element = file.findElementAt(offset);
        if (element == null) {
            return null;
        }

        final FanArgument arg = PsiTreeUtil.getParentOfType(element, FanArgument.class);
        if (arg != null) {
            final FanElement argList = arg.getArgumentList();
            if (argList != null) {
                return argList;
            }
        }
        return null;
    }

    private void appendParameterText(final FanParameter parameter, final PsiSubstitutor substitutor, final StringBuffer buffer) {
        final PsiType t = parameter.getType();
        final PsiType paramType = substitutor.substitute(t);
        buffer.append(paramType.getPresentableText());
        final String name = parameter.getName();
        if (name != null) {
            buffer.append(" ");
            buffer.append(name);
        }
        final FanDefaultValue defaultValue = parameter.getDefaultValue();
        if (defaultValue != null) {
            buffer.append(" (");
            buffer.append(defaultValue.getText());
            buffer.append(")");
        }
    }
}
