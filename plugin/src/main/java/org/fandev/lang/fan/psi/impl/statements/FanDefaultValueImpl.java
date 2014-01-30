package org.fandev.lang.fan.psi.impl.statements;

import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.api.statements.FanDefaultValue;
import com.intellij.lang.ASTNode;

/**
 * Date: Aug 24, 2009
 * Time: 11:50:03 PM
 *
 * @author Dror Bereznitsky
 */
public class FanDefaultValueImpl extends FanBaseElementImpl implements FanDefaultValue {
    public FanDefaultValueImpl(final ASTNode astNode) {
        super(astNode);
    }
}
