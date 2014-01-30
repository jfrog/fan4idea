package org.fandev.lang.fan.psi.impl.types;

import org.fandev.lang.fan.psi.api.types.FanClassTypeElement;
import org.fandev.lang.fan.psi.api.types.FanCodeReferenceElement;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.impl.FanClassReferenceType;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiType;
import com.intellij.lang.ASTNode;

/**
 * Date: Jul 3, 2009
 * Time: 11:26:44 PM
 *
 * @author Dror Bereznitsky
 */
public class FanClassTypeElementImpl extends FanBaseElementImpl implements FanClassTypeElement {
    public FanClassTypeElementImpl(final ASTNode astNode) {
        super(astNode);
    }

    public String toString() {
        return "Class Type element";
    }

    @NotNull
    public FanCodeReferenceElement getReferenceElement() {
        return findChildByClass(FanCodeReferenceElement.class);
    }

    @NotNull
    public PsiType getType() {
        return new FanClassReferenceType(getReferenceElement());
    }
}
