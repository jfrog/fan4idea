package org.fandev.lang.fan;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import org.fandev.lang.fan.psi.api.statements.typeDefs.*;
import org.fandev.lang.fan.psi.stubs.FanReferenceListStub;
import org.fandev.lang.fan.psi.stubs.FanTypeDefinitionStub;
import org.fandev.lang.fan.psi.stubs.elements.*;
import org.fandev.lang.fan.psi.stubs.elements.FanBuildScriptDefinitionElementType;
import org.fandev.lang.fan.types.FanFileElementType;

import java.util.Collection;
import java.util.Arrays;

/**
 * @author Dror
 * @date Dec 11, 2008 11:58:30 PM
 */
public interface FanElementTypes {
    IFileElementType FILE = new FanFileElementType(FanSupportLoader.FAN.getLanguage());

    FanStubElementType<FanTypeDefinitionStub, FanClassDefinition>
            CLASS_DEFINITION = new FanClassDefinitionElementType();
    IElementType CLASS_BODY = new FanElementType("CLASS_BODY");
    IElementType CLASS_TYPE_ELEMENT = new FanElementType("CLASS_TYPE_ELEMENT");

    FanStubElementType<FanTypeDefinitionStub, FanMixinDefinition>
            MIXIN_DEFINITION = new FanMixinDefinitionElementType();
    IElementType MIXIN_BODY = new FanElementType("MIXIN_BODY");

    FanStubElementType<FanTypeDefinitionStub, FanEnumDefinition>
            ENUM_DEFINITION = new FanEnumDefinitionElementType();
    IElementType ENUM_BODY = new FanElementType("ENUM_BODY");
    IElementType ENUM_VALUE = new FanElementType("ENUM_VALUE");

    FanStubElementType<FanTypeDefinitionStub, FanBuildScriptDefinition>
            BUILDSCRIPT_DEFINITION = new FanBuildScriptDefinitionElementType();
    IElementType BUILDSCRIPT_BODY = new FanElementType("BUILDSCRIPT_BODY");

    IElementType STATIC_BLOCK = new FanMethodElementType("static block");

    IElementType CTOR_DEFINITION = new FanConstructorElementType("CTOR_DEFINITION");
    IElementType CTOR_CHAIN = new FanElementType("CTOR_CHAIN");

    IElementType METHOD_DEFINITION = new FanMethodElementType("METHOD_DEFINITION");
    IElementType PARAM_DEFAULT = new FanElementType("Parameter default value");

    IElementType METHOD_BODY = new FanElementType("method block");

    IElementType FIELD_DEFINITION = new FanFieldElementType("FIELD_DEFINITION");
    IElementType FIELD_DEFAULT = new FanElementType("Field default value");
    IElementType GETTER_SETTER_FIELD_DEFINITION = new FanElementType("GETTER_SETTER_FIELD_DEFINITION");
    IElementType GETTER_FIELD_DEFINITION = new FanElementType("GETTER_FIELD_DEFINITION");
    IElementType SETTER_FIELD_DEFINITION = new FanElementType("SETTER_FIELD_DEFINITION");

    FanStubElementType<FanReferenceListStub, FanInheritanceClause> INHERITANCE_CLAUSE =
            new FanInheritanceClauseElementType();

    // TODO: from here the elements are not implemented
    /* -----------------------------------------------------------------------------*/

    IElementType NAME_ELEMENT = new FanElementType("name id");

    IElementType REFERENCE_ELEMENT = new FanElementType("REFERENCE_EXPRESSION");

    IElementType MODIFIERS = new FanElementType("MODIFIERS");

    IElementType BUILT_IN_TYPE = new FanElementType("BUILT_IN_TYPE");

    IElementType NONE = new FanElementType("NO_TOKEN");

    IElementType WRONGWAY = new FanElementType("WRONG_WAY");

    IElementType LCURLY = new FanElementType("{");

    IElementType TYPE = new FanElementType("Full Type wrapper");
    IElementType NULLABLE_TYPE = new FanElementType("mark for ?");

    IElementType LIST_TYPE = new FanElementType("LIST_TYPE");

    IElementType MAP_TYPE = new FanElementType("MAP_TYPE");

    IElementType FUNC_TYPE = new FanElementType("FUNC_TYPE");
    IElementType FORMALS = new FanElementType("FORMALS");
    IElementType FORMAL = new FanElementType("FORMAL");

    IElementType TYPE_PARAMETER_LIST = new FanElementType("TYPE_PARAMETER_LIST");

    IElementType TYPE_PARAMETER = new FanElementType("TYPE_PARAMETER");

    IElementType ARGUMENT_LIST = new FanElementType("args");
    IElementType ARGUMENT_EXPR = new FanElementType("arg exp");

    IElementType FACET = new FanElementType("facet declaration");
    IElementType FACET_VALUE = new FanElementType("facet value");

    IElementType EXPRESSION = new FanElementType("expression");
    IElementType LOCAL_DEF_STATEMENT = new FanElementType("local def");
    IElementType IT_ADD_STATEMENT = new FanElementType("it add");

    IElementType FFI_NAME = new FanElementType("FFI name");
    IElementType POD_REFERENCE = new FanElementType("Pod or Package Reference");
    IElementType USING_AS_NAME = new FanElementType("Using as name");
    IElementType USING_STATEMENT = new FanElementType("Using statement");
    IElementType CONTROL_FLOW = new FanElementType("Control Flow");
    IElementType FOR_STATEMENT = new FanElementType("For statement");
    IElementType FOR_INIT_EXPR = new FanElementType("For init expression");
    IElementType FOR_INIT_LOCAL_DEF = new FanElementType("For init local def");
    IElementType FOR_CONDITION = new FanElementType("For condition");
    IElementType FOR_REPEAT = new FanElementType("For repeat");
    IElementType FOR_BLOCK = new FanElementType("For block");
    IElementType IF_STATEMENT = new FanElementType("If statement");
    IElementType CONDITION_EXPR = new FanElementType("Condition expression");
    IElementType COND_TRUE_BLOCK = new FanElementType("If block");
    IElementType COND_FALSE_BLOCK = new FanElementType("Else block");
    IElementType RETURN_STATEMENT = new FanElementType("return");
    IElementType THROW_STATEMENT = new FanElementType("throw");
    IElementType SWITCH_STATEMENT = new FanElementType("Switch");
    IElementType SWITCH_VALUE = new FanElementType("Switch value");
    IElementType SWITCH_CASE = new FanElementType("Switch case");
    IElementType SWITCH_CASE_VALUE = new FanElementType("Switch case value");
    IElementType SWITCH_CASE_STATEMENT = new FanElementType("Switch case statement");
    IElementType WHILE_STATEMENT = new FanElementType("while statement");
    IElementType WHILE_CONDITION = new FanElementType("while condition");
    IElementType WHILE_BLOCK = new FanElementType("while block");
    IElementType TRY_STATEMENT = new FanElementType("try statement");
    IElementType TRY_BLOCK = new FanElementType("try block");
    IElementType CATCH_STATEMENT = new FanElementType("catch statement");
    IElementType CATCH_DEFINITION = new FanElementType("catch definition");
    IElementType CATCH_BLOCK = new FanElementType("catch block");
    IElementType FINALLY_STATEMENT = new FanElementType("finally statement");
    IElementType FINALLY_BLOCK = new FanElementType("finally block");

    IElementType ASSIGN_EXPRESSION = new FanElementType("assign expr");
    IElementType ASSIGN_LEFT_EXPR = new FanElementType("assign left expr");
    IElementType ASSIGN_RIGHT_EXPR = new FanElementType("assign right expr");
    IElementType COND_EXPR = new FanElementType("assign expr");
    IElementType LOGICAL_OR_EXPR = new FanElementType("logical OR expr");
    IElementType LOGICAL_AND_EXPR = new FanElementType("logical AND expr");
    IElementType EQUALITY_EXPR = new FanElementType("equality expr");
    IElementType RELATIONAL_EXPR = new FanElementType("relational expr");
    IElementType ELVIS_EXPR = new FanElementType("elvis expr");
    IElementType RANGE_EXPR = new FanElementType("range expr");
    IElementType BIT_OR_EXPR = new FanElementType("bit or expr");
    IElementType BIT_AND_EXPR = new FanElementType("bit and expr");
    IElementType SHIFT_EXPR = new FanElementType("shift expr");
    IElementType ADD_EXPR = new FanElementType("add expr");
    IElementType MULT_EXPR = new FanElementType("mult expr");
    IElementType PAREN_EXPR = new FanElementType("parent expr");
    IElementType CAST_EXPR = new FanElementType("cast expr");
    IElementType GROUPED_EXPR = new FanElementType("grouped expr");
    IElementType UNARY_EXPR = new FanElementType("unary expr");
    IElementType PREFIX_EXPR = new FanElementType("prefix expr");
    IElementType POSTFIX_EXPR = new FanElementType("postfix expr");
    IElementType TERM_EXPR = new FanElementType("term expr");
    IElementType TERM_CHAIN_EXPR = new FanElementType("termchain expr");
    IElementType LITERAL = new FanElementType("literal");
    IElementType THIS_REFERENCE_EXPRESSION = new FanElementType("this");
    IElementType SUPER_REFERENCE_EXPRESSION = new FanElementType("super");
    IElementType SIMPLE_LITERAL = new FanElementType("simple literal");
    IElementType LIST_LITERAL = new FanElementType("list literal");
    IElementType LIST_ITEM = new FanElementType("list item");
    IElementType MAP_LITERAL = new FanElementType("map literal");
    IElementType MAP_ITEM = new FanElementType("map item");
    IElementType MAP_ITEM_KEY = new FanElementType("map item key");
    IElementType MAP_ITEM_VALUE = new FanElementType("map item value");
    IElementType ID_EXPR = new FanElementType("idExpr");
    IElementType CLOSURE_EXPR = new FanElementType("closure");
    IElementType CLOSURE_BODY = new FanElementType("closure body");
    IElementType IT_BODY = new FanElementType("it body");
    IElementType WITH_BLOCK_EXPR = new FanElementType("withBlock expr");
    IElementType INDEX_EXPR = new FanElementType("index expr");

    Collection<IElementType> BLOCK_ELEMENTS = Arrays.asList(
            METHOD_BODY, CLOSURE_BODY, FOR_BLOCK, WHILE_BLOCK, TRY_BLOCK, CATCH_BLOCK, FINALLY_BLOCK, STATIC_BLOCK);
}
