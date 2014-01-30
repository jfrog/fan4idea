package org.fandev.lang.fan.psi.impl.statements.params;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiParameter;
import org.fandev.lang.fan.psi.api.statements.params.FanFormal;
import org.fandev.lang.fan.psi.api.statements.params.FanFormals;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Date: Aug 20, 2009
 * Time: 1:14:38 PM
 *
 * @author Dror Bereznitsky
 */
public class FanFormalsImpl extends FanBaseElementImpl implements FanFormals {
    public FanFormalsImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public PsiParameter[] getParameters() {
        return findChildrenByClass(FanFormal.class);
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
