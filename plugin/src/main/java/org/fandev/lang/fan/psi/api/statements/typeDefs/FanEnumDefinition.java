package org.fandev.lang.fan.psi.api.statements.typeDefs;

import com.intellij.psi.PsiClass;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanEnumValue;

/**
 * Date: Mar 28, 2009
 * Time: 3:36:41 PM
 * @author Dror Bereznitsky
 */
public interface FanEnumDefinition extends FanTypeDefinition, PsiClass {
    FanEnumValue[] getEnumValues();
}
