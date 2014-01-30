package org.fandev.lang.fan.psi.impl.types;

import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.psi.api.types.FanClassTypeElement;
import org.fandev.lang.fan.psi.api.types.FanListTypeElement;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.impl.FanListReferenceType;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiType;
import com.intellij.lang.ASTNode;

/**
 * Date: Jul 17, 2009
 * Time: 11:45:15 PM
 *
 * @author Dror Bereznitsky
 */
public class FanListTypeElementImpl extends FanBaseElementImpl implements FanListTypeElement {
    public FanListTypeElementImpl(final ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public PsiType getType() {
        final FanClassTypeElement fanTypeElem = getTypeElement();
        return new FanListReferenceType(this, fanTypeElem.getType());
    }
    
    @NotNull
    public FanClassTypeElement getTypeElement() {
        return findChildByClass(FanClassTypeElement.class);
    }

    public FanTypeDefinition getListType() {
        return getFanTypeByName("List");
    }
}
