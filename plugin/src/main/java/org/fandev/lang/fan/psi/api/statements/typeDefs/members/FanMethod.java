package org.fandev.lang.fan.psi.api.statements.typeDefs.members;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.lang.ASTNode;
import org.fandev.lang.fan.psi.api.statements.FanParameterOwner;
import org.fandev.lang.fan.psi.api.statements.FanTopLevelDefintion;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:54:59 PM
 */
public interface FanMethod extends FanSlot, FanParameterOwner, PsiMethod {
    FanMethod[] EMPTY_ARRAY = new FanMethod[0];

    void setBlock(final PsiCodeBlock newBlock);
}
