package org.fandev.lang.fan.psi.impl.statements.expressions;

import org.fandev.lang.fan.psi.api.statements.expressions.FanTermExpression;
import com.intellij.lang.ASTNode;

/**
 * Date: Aug 24, 2009
 * Time: 11:58:51 PM
 *
 * @author Dror Bereznitsky
 */
public class FanTermExpressionImpl extends FanExpressionImpl implements FanTermExpression {
    public FanTermExpressionImpl(final ASTNode astNode) {
        super(astNode);
    }
}
