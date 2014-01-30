package org.fandev.lang.fan.psi.impl.statements.arguments;

import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.api.statements.arguments.FanArgument;
import org.fandev.lang.fan.psi.api.statements.arguments.FanArgumentList;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Date: Sep 17, 2009
 * Time: 11:48:33 PM
 *
 * @author Dror Bereznitsky
 */
public class FanArgumentImpl extends FanBaseElementImpl implements FanArgument {
    public FanArgumentImpl(final ASTNode astNode) {
        super(astNode);
    }

    public int getIndex() {
        return getArgumentList().indexOf(this);
    }

    @NotNull
    public FanArgumentList getArgumentList() {
        return PsiTreeUtil.getParentOfType(this, FanArgumentList.class);
    }
}
