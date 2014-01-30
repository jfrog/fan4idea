package org.fandev.lang.fan.psi.impl.types;

import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.impl.FanMapType;
import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.psi.api.types.FanMapTypeElement;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiType;
import com.intellij.lang.ASTNode;

/**
 * Date: Jul 21, 2009
 * Time: 11:48:52 PM
 *
 * @author Dror Bereznitsky
 */
public class FanMapTypeElementImpl extends FanBaseElementImpl implements FanMapTypeElement {
    public FanMapTypeElementImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public PsiType getType() {
        final FanTypeElement[] keyValueTypes = findChildrenByClass(FanTypeElement.class);
        if (keyValueTypes.length == 2) {
            return new FanMapType(this, keyValueTypes[0], keyValueTypes[1]);
        }
        return null;
    }

    public FanTypeDefinition getMapType() {
        return getFanTypeByName("Map");
    }
}
