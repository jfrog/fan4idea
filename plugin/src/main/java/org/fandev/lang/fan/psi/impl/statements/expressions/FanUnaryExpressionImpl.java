package org.fandev.lang.fan.psi.impl.statements.expressions;

import org.fandev.lang.fan.psi.api.statements.expressions.FanUnaryExpression;
import com.intellij.lang.ASTNode;

/**
 * Date: Aug 24, 2009
 * Time: 11:59:36 PM
 *
 * @author Dror Bereznitsky
 */
public class FanUnaryExpressionImpl extends FanExpressionImpl implements FanUnaryExpression{
    public FanUnaryExpressionImpl(final ASTNode astNode) {
        super(astNode);
    }
}
