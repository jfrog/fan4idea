package org.fandev.lang.fan.psi.api.statements.expressions;

import org.jetbrains.annotations.Nullable;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;

/**
 * Date: Sep 12, 2009
 * Time: 6:35:50 PM
 *
 * @author Dror Bereznitsky
 */
public interface FanTypeReferenceExpression {
    @Nullable
    FanTypeDefinition getReferencedType();
}
