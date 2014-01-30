package org.fandev.lang.fan;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author Dror
 * @date Dec 12, 2008 12:10:22 AM
 */

//TODO Add all FAN_16 token types and cleanup stuff left from JavaScript

public interface FanTokenTypes {
    IElementType IDENTIFIER = new FanElementType("identifier");
    IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    IElementType NLS = new FanElementType("new line");
    IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    IElementType SEMANTIC_LINEFEED = new FanElementType("SEMANTIC_LINEFEED");

    IElementType END_OF_LINE_COMMENT = new FanElementType("end of line comment");
    IElementType FANDOC_LINE_COMMENT = new FanElementType("fandoc comment");
    IElementType C_STYLE_COMMENT = new FanElementType("c style comment");

    // Keywords:

    IElementType ASSERT_KEYWORD = new FanElementType("assert"); // assert
    IElementType BREAK_KEYWORD = new FanElementType("break"); // break
    IElementType CASE_KEYWORD = new FanElementType("case"); // case
    IElementType CATCH_KEYWORD = new FanElementType("catch"); // catch
    IElementType CLASS_KEYWORD = new FanElementType("class"); // class
    IElementType CONTINUE_KEYWORD = new FanElementType("continue"); //continue
    IElementType DEFAULT_KEYWORD = new FanElementType("default"); // default
    IElementType DO_KEYWORD = new FanElementType("do"); // do
    IElementType ELSE_KEYWORD = new FanElementType("else"); // else
    IElementType ENUM_KEYWORD = new FanElementType("enum"); //enum
    IElementType POD_KEYWORD = new FanElementType("pod"); //pod
    IElementType FALSE_KEYWORD = new FanElementType("false"); // false
    IElementType FINALLY_KEYWORD = new FanElementType("finally"); // finally
    IElementType FOR_KEYWORD = new FanElementType("for"); // for
    IElementType FOREACH_KEYWORD = new FanElementType("foreach"); // foreach
    IElementType IF_KEYWORD = new FanElementType("if"); // if
    IElementType MIXIN_KEYWORD = new FanElementType("mixin"); // mixin
    IElementType ONCE_KEYWORD = new FanElementType("once"); // once
    IElementType READONLY_KEYWORD = new FanElementType("readonly"); // readonly
    IElementType RETURN_KEYWORD = new FanElementType("return"); // return
    IElementType SWITCH_KEYWORD = new FanElementType("switch"); // switch
    IElementType THROW_KEYWORD = new FanElementType("throw"); // throw
    IElementType TRUE_KEYWORD = new FanElementType("true"); // true
    IElementType TRY_KEYWORD = new FanElementType("try"); // try
    IElementType USING_KEYWORD = new FanElementType("using"); // using
    IElementType VOLATILE_KEYWORD = new FanElementType("volatile"); // volatile
    IElementType WHILE_KEYWORD = new FanElementType("while");

    //Class modifiers
    IElementType FINAL_KEYWORD = new FanElementType("final"); //final

    //Slot Modifiers
    IElementType ABSTRACT_KEYWORD = new FanElementType("abstract"); //abstract
    IElementType CONST_KEYWORD = new FanElementType("const"); //const
    IElementType NATIVE_KEYWORD = new FanElementType("native"); // native
    IElementType NEW_KEYWORD = new FanElementType("new");
    IElementType OVERRIDE_KEYWORD = new FanElementType("override"); // override
    IElementType STATIC_KEYWORD = new FanElementType("static"); // static
    IElementType VIRTUAL_KEYWORD = new FanElementType("virtual"); // virtual

    // Fields Properties marker (TODO: Usable only if fieldDef state exists in Lexer)
    IElementType GET_KEYWORD = new FanElementType("get");
    IElementType SET_KEYWORD = new FanElementType("set");

    //Protection
    IElementType PUBLIC_KEYWORD = new FanElementType("public"); // public
    IElementType PRIVATE_KEYWORD = new FanElementType("private"); // private
    IElementType PROTECTED_KEYWORD = new FanElementType("protected"); // protected
    IElementType INTERNAL_KEYWORD = new FanElementType("internal"); // internal

    // Literals
    IElementType INT_LITERAL = new FanElementType("integer literal");
    IElementType FLOAT_LITERAL = new FanElementType("float literal");
    IElementType DECIMAL_LITERAL = new FanElementType("decimal literal");
    IElementType DURATION_LITERAL = new FanElementType("duration literal");
    IElementType URI_LITERAL = new FanElementType("URI_LITERAL");
    IElementType STRING_LITERAL = new FanElementType("STRING_LITERAL");
    IElementType DSL_STRING = new FanElementType("DSL_STRING");
    IElementType CHAR_LITERAL = new FanElementType("CHAR_LITERAL");

    IElementType NULL_KEYWORD = new FanElementType("null"); // null
    IElementType THIS_KEYWORD = new FanElementType("this"); // this
    IElementType SUPER_KEYWORD = new FanElementType("super"); // super

    IElementType FAN_SYS_TYPE = new FanElementType("SysType");

    // Call operators
    IElementType DOT = new FanElementType(".");// .
    IElementType DYN_CALL = new FanElementType("->");// ->
    IElementType SAFE_DOT = new FanElementType("?.");// .
    IElementType SAFE_DYN_CALL = new FanElementType("?->");// ->

    // Punctuators
    IElementType COLON_EQ = new FanElementType(":=");// :=
    IElementType SEMICOLON = new FanElementType(";");// ;
    IElementType COMMA = new FanElementType(",");// ,
    IElementType COLON = new FanElementType(":");// :
    IElementType OR = new FanElementType("|");// |
    IElementType LBRACE = new FanElementType("{");// {
    IElementType RBRACE = new FanElementType("}");// }
    IElementType LPAR = new FanElementType("(");// (
    IElementType RPAR = new FanElementType(")");// )
    IElementType LBRACKET = new FanElementType("[");// [
    IElementType RBRACKET = new FanElementType("]");// ]

    // Assignement operators <assignOp>
    IElementType EQ = new FanElementType("=");// =
    IElementType PLUSEQ = new FanElementType("+=");// +=
    IElementType MINUSEQ = new FanElementType("-=");// -=
    IElementType MULTEQ = new FanElementType("*=");// *=
    IElementType DIVEQ = new FanElementType("/="); // /=
    IElementType PERCEQ = new FanElementType("%=");// %=
    IElementType LTLTEQ = new FanElementType("<<=");// <<=
    IElementType GTGTEQ = new FanElementType(">>=");// >>=
    IElementType ANDEQ = new FanElementType("&=");// &=
    IElementType OREQ = new FanElementType("|=");// |=
    IElementType XOREQ = new FanElementType("^=");// ^=

    IElementType SHABENG = new FanElementType("#!");// #!
    IElementType SHARP = new FanElementType("#");// #
    IElementType AT = new FanElementType("@");// @
    IElementType QUEST = new FanElementType("?");// ?
    IElementType QUEST_COLON = new FanElementType("?:");// ?:

    // Equality Operator <equalityOp>
    IElementType EQEQ = new FanElementType("==");// ==
    IElementType NE = new FanElementType("!=");// !=
    IElementType EQEQEQ = new FanElementType("===");// ===
    IElementType NEEQ = new FanElementType("!==");// !==

    // Relational Operators <relationalOp>
    IElementType IS_KEYWORD = new FanElementType("is"); // is
    IElementType AS_KEYWORD = new FanElementType("as"); // as
    IElementType ISNOT_KEYWORD = new FanElementType("isnot"); // isnot
    IElementType LT = new FanElementType("<");// <
    IElementType GT = new FanElementType(">");// >
    IElementType LE = new FanElementType("<=");// <=
    IElementType GE = new FanElementType(">=");// >=
    IElementType COMPARE = new FanElementType("<=>");// <=>

    IElementType PLUS = new FanElementType("+");// +
    IElementType MINUS = new FanElementType("-");// -
    IElementType MULT = new FanElementType("*");// *
    IElementType DIV = new FanElementType("/"); // /
    IElementType PERC = new FanElementType("%");// %
    IElementType PLUSPLUS = new FanElementType("++");// ++
    IElementType MINUSMINUS = new FanElementType("--");// --
    IElementType LTLT = new FanElementType("<<");// <<
    IElementType GTGT = new FanElementType(">>");// >>
    IElementType AND = new FanElementType("&");// &
    IElementType XOR = new FanElementType("^");// ^
    IElementType EXCL = new FanElementType("!");// !
    IElementType TILDE = new FanElementType("~");// ~
    IElementType ANDAND = new FanElementType("&&");// &&
    IElementType OROR = new FanElementType("||");// ||


    IElementType COLON_COLON = new FanElementType("::"); // ::

    IElementType RANGE_SEP_INCL = new FanElementType(".."); // ..
    IElementType RANGE_SEP_EXCL = new FanElementType("..."); // ...

    TokenSet COMMENTS = TokenSet.create(C_STYLE_COMMENT, END_OF_LINE_COMMENT, FANDOC_LINE_COMMENT);

    TokenSet FAN_KEYWORDS =
            TokenSet.create(ABSTRACT_KEYWORD, AS_KEYWORD, ASSERT_KEYWORD, BREAK_KEYWORD, CASE_KEYWORD, CATCH_KEYWORD,
                    CLASS_KEYWORD, CONST_KEYWORD, CONTINUE_KEYWORD, DEFAULT_KEYWORD,
                    DO_KEYWORD, ELSE_KEYWORD, ENUM_KEYWORD, FALSE_KEYWORD, FINAL_KEYWORD, FINALLY_KEYWORD, FOR_KEYWORD,
                    FOREACH_KEYWORD, IF_KEYWORD, INTERNAL_KEYWORD, IS_KEYWORD, ISNOT_KEYWORD, MIXIN_KEYWORD,
                    NATIVE_KEYWORD, NEW_KEYWORD,
                    NULL_KEYWORD, ONCE_KEYWORD, OVERRIDE_KEYWORD, PRIVATE_KEYWORD, PROTECTED_KEYWORD, PUBLIC_KEYWORD,
                    READONLY_KEYWORD, RETURN_KEYWORD, STATIC_KEYWORD, SUPER_KEYWORD, SWITCH_KEYWORD, THIS_KEYWORD,
                    THROW_KEYWORD,
                    TRUE_KEYWORD, TRY_KEYWORD, USING_KEYWORD, VIRTUAL_KEYWORD, VOLATILE_KEYWORD,
                    WHILE_KEYWORD

            );

    TokenSet IDENTIFIER_TOKENS_SET = TokenSet.create(IDENTIFIER, FAN_SYS_TYPE, POD_KEYWORD);

    // Modifiers

    TokenSet PROTECTION = TokenSet.create(PRIVATE_KEYWORD, PROTECTED_KEYWORD, PUBLIC_KEYWORD, INTERNAL_KEYWORD);

    // <classFlags>     :=  [<protection>] ["abstract"] ["final"] ["const"]
    TokenSet CLASS_MODIFIERS =
            TokenSet.orSet(PROTECTION, TokenSet.create(ABSTRACT_KEYWORD, FINAL_KEYWORD, CONST_KEYWORD));

    // <mixinFlags>     :=  [<protection>] ["const"]
    TokenSet MIXIN_MODIFIERS =
            TokenSet.orSet(PROTECTION, TokenSet.create(CONST_KEYWORD));

    // <ctorFlags>      :=  [<protection>]
    TokenSet CTOR_MODIFIERS = PROTECTION;

    // <methodFlags>    :=  [<protection>] ["virtual"] ["override"] ["abstract"] ["static"] ["once"]
    /// Missing from Grammar ["native"]
    TokenSet METHOD_MODIFIERS = TokenSet.orSet(PROTECTION,
            TokenSet.create(ABSTRACT_KEYWORD, FINAL_KEYWORD, ONCE_KEYWORD, OVERRIDE_KEYWORD, STATIC_KEYWORD,
                    VIRTUAL_KEYWORD,
                    NATIVE_KEYWORD));

    // <fieldFlags>     :=  [<protection>] ["readonly"] ["static"]
    /// Missing from Grammar ["const"] ["native"] ["volatile"] ["override"] ["virtual"] ["final"]
    TokenSet FIELD_MODIFIERS =
            TokenSet.orSet(PROTECTION,
                    TokenSet.create(READONLY_KEYWORD, STATIC_KEYWORD, CONST_KEYWORD, NATIVE_KEYWORD, OVERRIDE_KEYWORD,
                            VIRTUAL_KEYWORD, VOLATILE_KEYWORD, ABSTRACT_KEYWORD, FINAL_KEYWORD));

    TokenSet ALL_SLOT_MODIFIERS =
            TokenSet.orSet(METHOD_MODIFIERS, FIELD_MODIFIERS);

    TokenSet ALL_MODIFIERS = TokenSet.orSet(
            PROTECTION, TokenSet.create(
                    ABSTRACT_KEYWORD, CONST_KEYWORD, FINAL_KEYWORD, ONCE_KEYWORD, NATIVE_KEYWORD, READONLY_KEYWORD,
                    OVERRIDE_KEYWORD, STATIC_KEYWORD, VIRTUAL_KEYWORD
            ));
    // Types

    TokenSet EOL = TokenSet.create(NLS);
    TokenSet SEPARATOR = TokenSet.create(SEMICOLON, NLS);
    TokenSet EOS = TokenSet.create(SEMICOLON, RBRACE, NLS);
    TokenSet SWITCH_BLOCK_TOKENS = TokenSet.create(RBRACE, CASE_KEYWORD, DEFAULT_KEYWORD);
    TokenSet TRY_BLOCK_TOKENS = TokenSet.create(CATCH_KEYWORD, FINALLY_KEYWORD);

    TokenSet ASSIGN_OP = TokenSet.create(EQ, MULTEQ, DIVEQ, PERCEQ, PLUSEQ, MINUSEQ,
            GTGTEQ, LTLTEQ, ANDEQ, XOREQ, OREQ);
    TokenSet EQUALITY_OP = TokenSet.create(EQEQ, NE, EQEQEQ, NEEQ);
    TokenSet TYPE_COMPARE = TokenSet.create(IS_KEYWORD, AS_KEYWORD, ISNOT_KEYWORD);
    TokenSet RELATIONAL_OP = TokenSet.orSet(TYPE_COMPARE, TokenSet.create(LT, LE, GT, GE, COMPARE));
    TokenSet COMPARISON_OP = TokenSet.orSet(EQUALITY_OP, RELATIONAL_OP);

    TokenSet BOOL_LITERALS = TokenSet.create(TRUE_KEYWORD, FALSE_KEYWORD);
    TokenSet STRING_LITERALS =
            TokenSet.create(URI_LITERAL, STRING_LITERAL, DSL_STRING, CHAR_LITERAL);
    TokenSet NUMERIC_LITERALS =
            TokenSet.create(INT_LITERAL, FLOAT_LITERAL, DECIMAL_LITERAL, DURATION_LITERAL);
    TokenSet FAN_LITERALS =
            TokenSet.orSet(BOOL_LITERALS, STRING_LITERALS, NUMERIC_LITERALS);
}
