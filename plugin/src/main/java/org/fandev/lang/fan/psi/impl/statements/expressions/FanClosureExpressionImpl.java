package org.fandev.lang.fan.psi.impl.statements.expressions;

import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.api.statements.expressions.FanClosureExpression;
import org.fandev.lang.fan.psi.api.types.FanFuncTypeElement;
import com.intellij.lang.ASTNode;

/**
 * Date: Aug 18, 2009
 * Time: 12:04:08 AM
 *
 * @author Dror Bereznitsky
 */
public class FanClosureExpressionImpl extends FanBaseElementImpl implements FanClosureExpression {
    public FanClosureExpressionImpl(final ASTNode astNode) {
        super(astNode);
    }

    public FanFuncTypeElement getFunction() {
        return findChildByClass(FanFuncTypeElement.class);
    }
}
