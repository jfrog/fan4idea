package org.fandev.lang.fan.psi.api.statements.typeDefs;

import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;

/**
 * Date: Mar 23, 2009
 * Time: 11:10:58 PM
 * @author Dror Bereznitsky
 */
public interface FanMixinDefinition extends FanTypeDefinition, PsiClass {
    
}
