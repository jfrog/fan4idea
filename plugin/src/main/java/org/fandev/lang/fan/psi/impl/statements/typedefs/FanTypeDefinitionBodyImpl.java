package org.fandev.lang.fan.psi.impl.statements.typedefs;

import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinitionBody;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import com.intellij.lang.ASTNode;

/**
 * Date: Sep 26, 2009
 * Time: 4:27:20 PM
 *
 * @author Dror Bereznitsky
 */
public class FanTypeDefinitionBodyImpl extends FanBaseElementImpl implements FanTypeDefinitionBody {
    public FanTypeDefinitionBodyImpl(final ASTNode astNode) {
        super(astNode);
    }
}
