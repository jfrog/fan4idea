package org.fandev.lang.fan.psi.impl.statements.params;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiParameter;
import org.fandev.lang.fan.psi.api.statements.params.FanParameterList;
import org.fandev.lang.fan.psi.api.statements.params.FanParameter;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Date: Jul 8, 2009
 * Time: 11:40:03 PM
 *
 * @author Dror Bereznitsky
 */
public class FanParameterListImpl extends FanBaseElementImpl implements FanParameterList {
    public FanParameterListImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public PsiParameter[] getParameters() {
        return findChildrenByClass(FanParameter.class);
    }

    public int getParameterIndex(final PsiParameter psiParameter) {
        final PsiParameter[] parameters = getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].equals(psiParameter)) {
                return i;
            }
        }

        return -1;
    }

    public int getParametersCount() {
        return getParameters().length;
    }
}
