package org.fandev.lang.fan;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static org.fandev.lang.fan.FanTokenTypes.*;

%%

%class _FanLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
    private boolean canEndWithNewline = false;
%}

DIGIT=[0-9"_"]
HEX_DIGIT=[0-9A-Fa-f"_"]
ONE_NL = \r|\n|\r\n  // NewLines
WHITE_SPACE_CHAR=" "|\t|\f // Whitespaces
NLS={ONE_NL}({ONE_NL}|{WHITE_SPACE_CHAR})*

IDENTIFIER=[:jletter:] [:jletterdigit:]*

INTEGER_LITERAL={DECIMAL_INTEGER_LITERAL}|{HEX_INTEGER_LITERAL}
DECIMAL_INTEGER_LITERAL=({DIGIT})+
HEX_INTEGER_LITERAL=0[Xx]({HEX_DIGIT})*

FLOAT_MARK_LITERAL=("f"|"F")
FLOAT_LITERAL=({FLOATING_POINT_LITERAL1})|({FLOATING_POINT_LITERAL2})|({FLOATING_POINT_LITERAL3})|({FLOATING_POINT_LITERAL4})
FLOATING_POINT_LITERAL1=({DIGIT})+"."({DIGIT})+({EXPONENT_PART})?({FLOAT_MARK_LITERAL})
FLOATING_POINT_LITERAL2="."({DIGIT})+({EXPONENT_PART})?({FLOAT_MARK_LITERAL})
FLOATING_POINT_LITERAL3=({DIGIT})+({EXPONENT_PART})({FLOAT_MARK_LITERAL})
FLOATING_POINT_LITERAL4=({DIGIT})+({FLOAT_MARK_LITERAL})
EXPONENT_PART=[Ee]["+""-"]?({DIGIT})*

DEC_MARK_LITERAL=("d"|"D")
DEC_LITERAL=({DEC_POINT_LITERAL1})|({DEC_POINT_LITERAL2})|({DEC_POINT_LITERAL3})|({DEC_POINT_LITERAL4})
DEC_POINT_LITERAL1=({DIGIT})+"."({DIGIT})+({EXPONENT_PART})?({DEC_MARK_LITERAL})?
DEC_POINT_LITERAL2="."({DIGIT})+({EXPONENT_PART})?({DEC_MARK_LITERAL})?
DEC_POINT_LITERAL3=({DIGIT})+({EXPONENT_PART})({DEC_MARK_LITERAL})?
DEC_POINT_LITERAL4=({DIGIT})+({DEC_MARK_LITERAL})

DURATION_MARK_LITERAL=("ns"|"ms"|"sec"|"min"|"hr"|"day")
DURATION_LITERAL=({DURATION_POINT_LITERAL1})|({DURATION_POINT_LITERAL2})|({DURATION_POINT_LITERAL3})|({DURATION_POINT_LITERAL4})
DURATION_POINT_LITERAL1=({DIGIT})+"."({DIGIT})+({EXPONENT_PART})?({DURATION_MARK_LITERAL})
DURATION_POINT_LITERAL2="."({DIGIT})+({EXPONENT_PART})?({DURATION_MARK_LITERAL})
DURATION_POINT_LITERAL3=({DIGIT})+({EXPONENT_PART})({DURATION_MARK_LITERAL})
DURATION_POINT_LITERAL4=({DIGIT})+({DURATION_MARK_LITERAL})

/* Except uri and char they are multi line strings and raw strings */

URI_LITERAL="`"([^\\"`"] | \\\` | \\[^"`"])*"`"
CHAR_LITERAL="'"([^\\"'"] | \\\' | \\[^"'"])*"'"
DSL_STRING="<|"[^"|>"]*"|>"
TRIPLE_STRING_LITERAL=\"\"\"([^\"] | \"(\")?[^\"])*\"\"\"
STRING_LITERAL=\"([^\\\"] | \\\" | \\[^\"])*\"

C_STYLE_COMMENT="/""*"([^"*"] | "*"[^"/"])*"*""/"
END_OF_LINE_COMMENT="/""/"[^\r\n]*
FANDOC_LINE_COMMENT="*""*"[^\r\n]*

SHABENG="#!"[^\n]*

%xstate IN_VAR_DECLERATION

%%

<YYINITIAL> ":="                  { return COLON_EQ; }
<YYINITIAL> "using"               { return USING_KEYWORD; }

<YYINITIAL> {NLS}+                { return NLS; }
<YYINITIAL> {WHITE_SPACE_CHAR}+   { return WHITE_SPACE; }

<YYINITIAL> {INTEGER_LITERAL}     { yybegin(YYINITIAL); return INT_LITERAL; }
<YYINITIAL> {FLOAT_LITERAL}       { yybegin(YYINITIAL); return FLOAT_LITERAL; }
<YYINITIAL> {DEC_LITERAL}         { yybegin(YYINITIAL); return DECIMAL_LITERAL; }
<YYINITIAL> {DURATION_LITERAL}    { yybegin(YYINITIAL); return DURATION_LITERAL; }

<YYINITIAL> {URI_LITERAL}         { yybegin(YYINITIAL); return URI_LITERAL; }
<YYINITIAL> {DSL_STRING}  { yybegin(YYINITIAL); return DSL_STRING; }
<YYINITIAL> {CHAR_LITERAL}        { yybegin(YYINITIAL); return CHAR_LITERAL; }
<YYINITIAL> {TRIPLE_STRING_LITERAL}      { yybegin(YYINITIAL); return STRING_LITERAL; }
<YYINITIAL> {STRING_LITERAL}      { yybegin(YYINITIAL); return STRING_LITERAL; }

/* Keywords */

<YYINITIAL> "abstract"            { yybegin(YYINITIAL); return ABSTRACT_KEYWORD; }
<YYINITIAL> "as"                  { yybegin(YYINITIAL); return AS_KEYWORD; }
<YYINITIAL> "assert"              { yybegin(YYINITIAL); return ASSERT_KEYWORD; }
<YYINITIAL> "break"               { yybegin(YYINITIAL); return BREAK_KEYWORD; }
<YYINITIAL> "case"                { yybegin(YYINITIAL); return CASE_KEYWORD; }
<YYINITIAL> "catch"               { yybegin(YYINITIAL); return CATCH_KEYWORD; }
<YYINITIAL> "class"               { yybegin(YYINITIAL); return CLASS_KEYWORD; }
<YYINITIAL> "const"               { yybegin(YYINITIAL); return CONST_KEYWORD; }
<YYINITIAL> "continue"            { yybegin(YYINITIAL); return CONTINUE_KEYWORD; }
<YYINITIAL> "default"             { yybegin(YYINITIAL); return DEFAULT_KEYWORD; }
<YYINITIAL> "do"                  { yybegin(YYINITIAL); return DO_KEYWORD; }
<YYINITIAL> "else"                { yybegin(YYINITIAL); return ELSE_KEYWORD; }
<YYINITIAL> "pod"                 { yybegin(YYINITIAL); return POD_KEYWORD; }
<YYINITIAL> "enum"                { yybegin(YYINITIAL); return ENUM_KEYWORD; }
<YYINITIAL> "false"               { yybegin(YYINITIAL); return FALSE_KEYWORD; }
<YYINITIAL> "final"               { yybegin(YYINITIAL); return FINAL_KEYWORD; }
<YYINITIAL> "finally"             { yybegin(YYINITIAL); return FINALLY_KEYWORD; }
<YYINITIAL> "for"                 { yybegin(YYINITIAL); return FOR_KEYWORD; }
<YYINITIAL> "foreach"             { yybegin(YYINITIAL); return FOREACH_KEYWORD; }
<YYINITIAL> "if"                  { yybegin(YYINITIAL); return IF_KEYWORD; }
<YYINITIAL> "internal"            { yybegin(YYINITIAL); return INTERNAL_KEYWORD; }
<YYINITIAL> "is"                  { yybegin(YYINITIAL); return IS_KEYWORD; }
<YYINITIAL> "isnot"               { yybegin(YYINITIAL); return ISNOT_KEYWORD; }
<YYINITIAL> "mixin"               { yybegin(YYINITIAL); return MIXIN_KEYWORD; }
<YYINITIAL> "native"              { yybegin(YYINITIAL); return NATIVE_KEYWORD; }
<YYINITIAL> "new"                 { yybegin(YYINITIAL); return NEW_KEYWORD; }
<YYINITIAL> "null"                { yybegin(YYINITIAL); return NULL_KEYWORD; }
<YYINITIAL> "once"                { yybegin(YYINITIAL); return ONCE_KEYWORD; }
<YYINITIAL> "override"            { yybegin(YYINITIAL); return OVERRIDE_KEYWORD; }
<YYINITIAL> "private"             { yybegin(YYINITIAL); return PRIVATE_KEYWORD; }
<YYINITIAL> "protected"           { yybegin(YYINITIAL); return PROTECTED_KEYWORD; }
<YYINITIAL> "public"              { yybegin(YYINITIAL); return PUBLIC_KEYWORD; }
<YYINITIAL> "readonly"            { yybegin(YYINITIAL); return READONLY_KEYWORD; }
<YYINITIAL> "return"              { yybegin(YYINITIAL); return RETURN_KEYWORD; }
<YYINITIAL> "static"              { yybegin(YYINITIAL); return STATIC_KEYWORD; }
<YYINITIAL> "super"               { yybegin(YYINITIAL); return SUPER_KEYWORD; }
<YYINITIAL> "switch"              { yybegin(YYINITIAL); return SWITCH_KEYWORD; }
<YYINITIAL> "this"                { yybegin(YYINITIAL); return THIS_KEYWORD; }
<YYINITIAL> "throw"               { yybegin(YYINITIAL); return THROW_KEYWORD; }
<YYINITIAL> "true"                { yybegin(YYINITIAL); return TRUE_KEYWORD; }
<YYINITIAL> "try"                 { yybegin(YYINITIAL); return TRY_KEYWORD; }
<YYINITIAL> "virtual"             { yybegin(YYINITIAL); return VIRTUAL_KEYWORD; }
<YYINITIAL> "volatile"            { yybegin(YYINITIAL); return VOLATILE_KEYWORD; }
<YYINITIAL> "while"               { yybegin(YYINITIAL); return WHILE_KEYWORD; }

/* Type Literals */

<YYINITIAL> "Actor"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "ActorPool"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "ArgErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Bool"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Buf"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "CancelledErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "CastErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Charset"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "ConstErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Context"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Date"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "DateTime"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Decimal"             { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Depend"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "DirUriSpace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "DirNamespace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Duration"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Enum"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Err"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "FanScheme"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Field"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "File"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "FileScheme"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "FileBuf"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Float"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Func"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Future"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "IndexErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "InStream"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Int"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "InterruptedErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "IOErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "List"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Locale"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "LocalFile"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Log"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "LogLevel"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "LogRecord"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Map"                 { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "MmapBuf"                 { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Method"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "MemBuf"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "MimeType"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Month"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "NameErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "NotImmutableErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "NullErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Num"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Obj"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "OutStream"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Param"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "ParamErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "ParseErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Pod"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Process"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Range"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "ReadonlyErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Regex"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "RegexMatcher"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Repo"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "RootUriSpace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "RootNamespace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Service"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Slot"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Str"                 { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "StrBuf"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Symbol"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Sys"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "SysInStream"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "SysOutStream"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "SysUriSpace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "SysNamespace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Test"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "TestErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "This"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Time"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "TimeoutErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "TimeZone"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Type"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Unit"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnknownPodErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnknownServiceErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnknownSlotErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnknownSymbolErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnknownTypeErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnknownThreadErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnresolvedErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Unsafe"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UnsupportedErr"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Uri"                 { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "UriScheme"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
/* Namespace should be removed */
<YYINITIAL> "UriSpace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Namespace"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Uuid"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Version"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Void"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Weekday"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "Zip"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }
<YYINITIAL> "ZipEntryFile"                { yybegin(YYINITIAL); return FAN_SYS_TYPE; }

<YYINITIAL> {IDENTIFIER}          { yybegin(YYINITIAL); return IDENTIFIER; }

<YYINITIAL> {C_STYLE_COMMENT}     { return C_STYLE_COMMENT; }
<YYINITIAL> {END_OF_LINE_COMMENT} { return END_OF_LINE_COMMENT; }
<YYINITIAL> {FANDOC_LINE_COMMENT}+ { return FANDOC_LINE_COMMENT; }

<YYINITIAL> {SHABENG}             { yybegin(YYINITIAL); return SHABENG; }

<YYINITIAL> "|"                   { yybegin(YYINITIAL); return OR; }
<YYINITIAL> "("                   { yybegin(YYINITIAL); return LPAR; }
<YYINITIAL> ")"                   { yybegin(YYINITIAL); return RPAR; }
<YYINITIAL> "{"                   { yybegin(YYINITIAL); return LBRACE; }
<YYINITIAL> "}"                   { yybegin(YYINITIAL); return RBRACE; }
<YYINITIAL> "["                   { yybegin(YYINITIAL); return LBRACKET; }
<YYINITIAL> "]"                   { yybegin(YYINITIAL); return RBRACKET; }

<YYINITIAL> "."                   { yybegin(YYINITIAL); return DOT; }
<YYINITIAL> ".."                  { yybegin(YYINITIAL); return RANGE_SEP_INCL; }
<YYINITIAL> "..<"                 { yybegin(YYINITIAL); return RANGE_SEP_EXCL; }
<YYINITIAL> "->"                  { yybegin(YYINITIAL); return DYN_CALL; }
<YYINITIAL> "?."                  { yybegin(YYINITIAL); return SAFE_DOT; }
<YYINITIAL> "?->"                 { yybegin(YYINITIAL); return SAFE_DYN_CALL; }

<YYINITIAL> "="                   { yybegin(YYINITIAL); return EQ; }

<YYINITIAL> ";"                   { yybegin(YYINITIAL); return SEMICOLON; }
<YYINITIAL> ","                   { yybegin(YYINITIAL); return COMMA; }

<YYINITIAL> "::"                  { yybegin(YYINITIAL); return COLON_COLON; }
<YYINITIAL> ":"                   { yybegin(YYINITIAL); return COLON; }
<YYINITIAL> "#"                   { yybegin(YYINITIAL); return SHARP; }
<YYINITIAL> "@"                   { yybegin(YYINITIAL); return AT; }
<YYINITIAL> "?"                   { yybegin(YYINITIAL); return QUEST; }
<YYINITIAL> "?:"                  { yybegin(YYINITIAL); return QUEST_COLON; }
<YYINITIAL> "=="                  { yybegin(YYINITIAL); return EQEQ; }
<YYINITIAL> "!="                  { yybegin(YYINITIAL); return NE; }
<YYINITIAL> "==="                 { yybegin(YYINITIAL); return EQEQEQ; }
<YYINITIAL> "!=="                 { yybegin(YYINITIAL); return NEEQ; }

<YYINITIAL> "<"                   { yybegin(YYINITIAL); return LT; }
<YYINITIAL> ">"                   { yybegin(YYINITIAL); return GT; }
<YYINITIAL> "<="                  { yybegin(YYINITIAL); return LE; }
<YYINITIAL> ">="                  { yybegin(YYINITIAL); return GE; }
<YYINITIAL> "<=>"                 { yybegin(YYINITIAL); return COMPARE; }
<YYINITIAL> "+"                   { yybegin(YYINITIAL); return PLUS; }
<YYINITIAL> "-"                   { yybegin(YYINITIAL); return MINUS; }
<YYINITIAL> "*"                   { yybegin(YYINITIAL); return MULT; }
<YYINITIAL> "%"                   { yybegin(YYINITIAL); return PERC; }
<YYINITIAL> "++"                  { yybegin(YYINITIAL); return PLUSPLUS; }
<YYINITIAL> "--"                  { yybegin(YYINITIAL); return MINUSMINUS; }
<YYINITIAL> "<<"                  { yybegin(YYINITIAL); return LTLT; }
<YYINITIAL> ">>"                  { yybegin(YYINITIAL); return GTGT; }
<YYINITIAL> "&"                   { yybegin(YYINITIAL); return AND; }
<YYINITIAL> "^"                   { yybegin(YYINITIAL); return XOR; }
<YYINITIAL> "!"                   { yybegin(YYINITIAL); return EXCL; }
<YYINITIAL> "~"                   { yybegin(YYINITIAL); return TILDE; }
<YYINITIAL> "&&"                  { yybegin(YYINITIAL); return ANDAND; }
<YYINITIAL> "||"                  { yybegin(YYINITIAL); return OROR; }
<YYINITIAL> "+="                  { yybegin(YYINITIAL); return PLUSEQ; }
<YYINITIAL> "-="                  { yybegin(YYINITIAL); return MINUSEQ; }
<YYINITIAL> "*="                  { yybegin(YYINITIAL); return MULTEQ; }
<YYINITIAL> "%="                  { yybegin(YYINITIAL); return PERCEQ; }
<YYINITIAL> "<<="                 { yybegin(YYINITIAL); return LTLTEQ; }
<YYINITIAL> ">>="                 { yybegin(YYINITIAL); return GTGTEQ; }
<YYINITIAL> "&="                  { yybegin(YYINITIAL); return ANDEQ; }
<YYINITIAL> "|="                  { yybegin(YYINITIAL); return OREQ; }
<YYINITIAL> "^="                  { yybegin(YYINITIAL); return XOREQ; }
<YYINITIAL> "/"                   { yybegin(YYINITIAL); return DIV; }
<YYINITIAL> "/="                  { yybegin(YYINITIAL); return DIVEQ; }

// Unknown symbol is using for debug goals.
.                                         {   return BAD_CHARACTER; }
