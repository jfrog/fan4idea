package org.fandev.lang.fan.parser;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import static org.fandev.lang.fan.FanElementTypes.*;
import org.fandev.lang.fan.psi.impl.statements.blocks.FanPsiCodeBlockImpl;
import org.fandev.lang.fan.psi.impl.statements.typedefs.*;
import org.fandev.lang.fan.psi.impl.statements.typedefs.members.FanConstructorImpl;
import org.fandev.lang.fan.psi.impl.statements.typedefs.members.FanFieldImpl;
import org.fandev.lang.fan.psi.impl.statements.typedefs.members.FanMethodImpl;
import org.fandev.lang.fan.psi.impl.statements.typedefs.members.FanEnumValueImpl;
import org.fandev.lang.fan.psi.impl.statements.params.*;
import org.fandev.lang.fan.psi.impl.statements.FanVariableImpl;
import org.fandev.lang.fan.psi.impl.statements.FanDefaultValueImpl;
import org.fandev.lang.fan.psi.impl.statements.arguments.FanArgumentListImpl;
import org.fandev.lang.fan.psi.impl.statements.arguments.FanArgumentImpl;
import org.fandev.lang.fan.psi.impl.statements.expressions.*;
import org.fandev.lang.fan.psi.impl.modifiers.FanModifierListImpl;
import org.fandev.lang.fan.psi.impl.types.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 3:05:16 PM
 */
public class FanPsiCreator {
    public static PsiElement createElement(final ASTNode node) {
        final IElementType elem = node.getElementType();

        if (elem == CLASS_DEFINITION) return new FanClassDefinitionImpl(node);
        if (elem == MIXIN_DEFINITION) return new FanMixinDefinitionImpl(node);
        if (elem == ENUM_DEFINITION) return new FanEnumDefinitionImpl(node);
        if (elem == BUILDSCRIPT_DEFINITION) return new FanBuildScriptDefinitionImpl(node);

        if (elem == CLASS_BODY || elem == ENUM_BODY || elem == MIXIN_BODY) return new FanTypeDefinitionBodyImpl(node);

        if (elem == STATIC_BLOCK) return new FanMethodImpl(node);
        if (elem == CTOR_DEFINITION) return new FanConstructorImpl(node);
        if (elem == METHOD_DEFINITION) return new FanMethodImpl(node);

        if (BLOCK_ELEMENTS.contains(elem)) return new FanPsiCodeBlockImpl(node);

        if (elem == FIELD_DEFINITION) return new FanFieldImpl(node);
        if (elem == ENUM_VALUE) return new FanEnumValueImpl(node);

        if (elem == ID_EXPR) return new FanReferenceExpressionImpl(node);

        if (elem == REFERENCE_ELEMENT) return new FanCodeReferenceElementImpl(node);

        if (elem == CLASS_TYPE_ELEMENT) return new FanClassTypeElementImpl(node);

        if (elem == LIST_TYPE) return new FanListTypeElementImpl(node);

        if (elem == FUNC_TYPE) return new FanFuncTypeElementImpl(node);

        if (elem == MAP_TYPE) return new FanMapTypeElementImpl(node);

        if (elem == INHERITANCE_CLAUSE) return new FanInheritanceClauseImpl(node);

        if (elem == MODIFIERS) return new FanModifierListImpl(node);

        if (elem == TYPE_PARAMETER_LIST) return new FanParameterListImpl(node);

        if (elem == TYPE_PARAMETER) return new FanParameterImpl(node);

        if (elem == LOCAL_DEF_STATEMENT) return new FanVariableImpl(node);

        if (elem == THIS_REFERENCE_EXPRESSION) return new FanThisReferenceExpressionImpl(node);

        if (elem == SUPER_REFERENCE_EXPRESSION) return new FanSuperReferenceExpressionImpl(node);

        if (elem == POD_REFERENCE) return new PodReferenceExpressionImpl(node);

        if (elem == CLOSURE_EXPR) return new FanClosureExpressionImpl(node);

        if (elem == FORMALS) return new FanFormalsImpl(node);

        if (elem == FORMAL) return new FanFormalImpl(node);

        if (elem == PARAM_DEFAULT) return new FanDefaultValueImpl(node);

        if (elem == INDEX_EXPR) return new FanIndexExpressionImpl(node);

        if (elem == ARGUMENT_LIST) return new FanArgumentListImpl(node);

        if (elem == ARGUMENT_EXPR) return new FanArgumentImpl(node);

        return new ASTWrapperPsiElement(node);
    }
}
