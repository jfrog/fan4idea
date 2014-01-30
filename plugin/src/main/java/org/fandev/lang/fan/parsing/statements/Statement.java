package org.fandev.lang.fan.parsing.statements;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanBundle.message;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.expression.Expression;
import org.fandev.lang.fan.parsing.types.SimpleTypeSpec;
import org.fandev.lang.fan.parsing.types.TypeSpec;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 6, 2009 11:19:40 PM
 */
public class Statement {
    public static final TokenSet FOR_STOPPERS = TokenSet.create(SEMICOLON, RPAR);
    public static final TokenSet RPAR_STOPPER = TokenSet.create(RPAR);
    public static final TokenSet SWITCH_CASE_STOPPER = TokenSet.create(COLON, RBRACE);
    public static final TokenSet CLOSURE_EOS = TokenSet.orSet(EOS, TokenSet.create(COMMA));

    public static boolean parse(final PsiBuilder builder) {
        return parse(builder, false);
    }

    public static boolean parse(final PsiBuilder builder, final boolean inClosure) {
        IElementType statementType = null;
        final PsiBuilder.Marker statementMark = builder.mark();
        final IElementType tokenType = builder.getTokenType();
        if (BREAK_KEYWORD.equals(tokenType) ||
                CONTINUE_KEYWORD.equals(tokenType)) {
            builder.advanceLexer();
            // Should have EOS
            if (EOS.contains(builder.getTokenType())) {
                removeStoppers(builder, SEPARATOR, SEPARATOR);
                statementType = CONTROL_FLOW;
            } else {
                builder.error(message("separator.expected"));
            }
        } else if (FOR_KEYWORD.equals(tokenType)) {
            statementType = FOR_STATEMENT;
            parseFor(builder);
        } else if (IF_KEYWORD.equals(tokenType)) {
            statementType = IF_STATEMENT;
            parseIf(builder);
        } else if (RETURN_KEYWORD.equals(tokenType)) {
            statementType = RETURN_STATEMENT;
            parseReturnExpression(builder);
        } else if (SWITCH_KEYWORD.equals(tokenType)) {
            statementType = SWITCH_STATEMENT;
            parseSwitch(builder);
        } else if (THROW_KEYWORD.equals(tokenType)) {
            statementType = THROW_STATEMENT;
            parseThrowExpression(builder);
        } else if (WHILE_KEYWORD.equals(tokenType)) {
            statementType = WHILE_STATEMENT;
            parseWhile(builder);
        } else if (TRY_KEYWORD.equals(tokenType)) {
            statementType = TRY_STATEMENT;
            parseTry(builder);
        } else {
            final TokenSet stopper = inClosure ? CLOSURE_EOS : EOS;
            final boolean res = expressionOrLocalDef(builder, stopper, EXPRESSION, LOCAL_DEF_STATEMENT);
            if (inClosure && res && getToken(builder, COMMA)) {
                statementMark.done(IT_ADD_STATEMENT);
            } else {
                statementMark.drop();
            }
            removeStoppers(builder, SEPARATOR, SEPARATOR);
            return res;
        }
        if (statementType != null) {
            statementMark.done(statementType);
            removeStoppers(builder, SEPARATOR, SEPARATOR);
            return true;
        } else {
            statementMark.drop();
            return false;
        }
    }

    private static boolean parseFor(final PsiBuilder builder) {
        if (!getToken(builder, FOR_KEYWORD, message("keywords.expected", FOR_KEYWORD))) {
            return false;
        }
        removeNls(builder);
        getToken(builder, LPAR, message("lpar.expected"));
        expressionOrLocalDef(builder, FOR_STOPPERS, FOR_INIT_EXPR, FOR_INIT_LOCAL_DEF);
        removeNls(builder);
        getToken(builder, SEMICOLON, message("semicolon.expected"));
        removeNls(builder);
        Expression.parseExpr(builder, FOR_STOPPERS, FOR_CONDITION);
        removeNls(builder);
        getToken(builder, SEMICOLON, message("semicolon.expected"));
        removeNls(builder);
        Expression.parseExpr(builder, FOR_STOPPERS, FOR_REPEAT);
        removeNls(builder);
        getToken(builder, RPAR, message("rpar.expected"));
        removeNls(builder);
        Block.parse(builder, FOR_BLOCK);
        removeNls(builder);
        return true;
    }

    private static boolean expressionOrLocalDef(final PsiBuilder builder, final TokenSet stopper,
            final IElementType exprType, final IElementType localDefType) {
        removeNls(builder);
        // Initialization it can only be an Type IDENTIFIER follow by := So { is a stopper
        final TokenSet lookAheadStoppers = TokenSet.orSet(stopper, TokenSet.create(LBRACE));
        if (ParserUtils.lookAheadForElement(builder, COLON_EQ, lookAheadStoppers)) {
            return parseLocalDef(builder, stopper, localDefType);
        } else {
            // Simple expression
            return Expression.parseExpr(builder, stopper, exprType);
        }
    }

    private static boolean parseLocalDef(final PsiBuilder builder, final TokenSet stopper, final IElementType localDefType) {
        boolean res = true;
        // Local def
        final PsiBuilder.Marker localDef = builder.mark();
        // Type is optional, find if there is one by doing without it and if it fails fallback to type
        final PsiBuilder.Marker nameMark = builder.mark();
        if (getToken(builder, IDENTIFIER_TOKENS_SET) && COLON_EQ == builder.getTokenType()) {
            nameMark.done(NAME_ELEMENT);
        } else {
            nameMark.rollbackTo();
            res = TypeSpec.parse(builder);
            if (res) {
                removeNls(builder);
                res = ParserUtils.parseName(builder);
            }
        }
        if (res && getToken(builder, COLON_EQ, FanBundle.message("localDef.assign.expected"))) {
            removeNls(builder);
            res = Expression.parseExpr(builder, stopper, PARAM_DEFAULT);
        }
        if (res) {
            localDef.done(localDefType);
            return true;
        } else {
            // TODO: Check if I should drop it?
            localDef.done(localDefType);
            return false;
        }
    }

    private static boolean parseIf(final PsiBuilder builder) {
        if (!getToken(builder, IF_KEYWORD, message("keywords.expected", IF_KEYWORD))) {
            return false;
        }
        boolean res;
        parseIfCondition(builder);
        res = Block.parse(builder, COND_TRUE_BLOCK);
        removeNls(builder);
        while (res && !builder.eof() && getToken(builder, ELSE_KEYWORD)) {
            removeNls(builder);
            if (getToken(builder, IF_KEYWORD)) {
                parseIfCondition(builder);
                res = Block.parse(builder, COND_TRUE_BLOCK);
            } else {
                res = Block.parse(builder, COND_FALSE_BLOCK);
            }
            removeNls(builder);
        }
        return res;
    }

    private static void parseIfCondition(final PsiBuilder builder) {
        removeNls(builder);
        getToken(builder, LPAR, message("lpar.expected"));
        removeNls(builder);
        Expression.parseExpr(builder, RPAR_STOPPER, CONDITION_EXPR);
        getToken(builder, RPAR, message("rpar.expected"));
        removeNls(builder);
    }

    private static boolean parseReturnExpression(final PsiBuilder builder) {
        if (!getToken(builder, RETURN_KEYWORD, message("keywords.expected", RETURN_KEYWORD))) {
            return false;
        }
        boolean res = true;
        if (!EOS.contains(builder.getTokenType())) {
            res = Expression.parseExpr(builder, EOS, EXPRESSION);
            removeStoppers(builder, SEPARATOR, SEPARATOR);
        }
        return res;
    }

    private static boolean parseThrowExpression(final PsiBuilder builder) {
        if (!getToken(builder, THROW_KEYWORD, message("keywords.expected", THROW_KEYWORD))) {
            return false;
        }
        boolean res = Expression.parseExpr(builder, EOS, EXPRESSION);
        removeStoppers(builder, SEPARATOR, SEPARATOR);
        return res;
    }

    private static boolean parseSwitch(final PsiBuilder builder) {
        if (!getToken(builder, SWITCH_KEYWORD, message("keywords.expected", SWITCH_KEYWORD))) {
            return false;
        }
        removeNls(builder);
        getToken(builder, LPAR, message("lpar.expected"));
        removeNls(builder);
        Expression.parseExpr(builder, RPAR_STOPPER, SWITCH_VALUE);
        getToken(builder, RPAR, message("rpar.expected"));
        removeNls(builder);
        getToken(builder, LBRACE, message("lcurly.expected"));
        removeNls(builder);
        boolean hasDefault = false;
        while (!builder.eof() && !RBRACE.equals(builder.getTokenType())) {
            final PsiBuilder.Marker inSwitchMark = builder.mark();
            if (getToken(builder, CASE_KEYWORD)) {
                if (hasDefault) {
                    builder.error(message("case.after.default"));
                }
                Expression.parseExpr(builder, SWITCH_CASE_STOPPER, SWITCH_CASE_VALUE);
            } else if (getToken(builder, DEFAULT_KEYWORD)) {
                hasDefault = true;
            } else {
                inSwitchMark.error(message("case.default.expected"));
                advanceNoNls(builder);
                continue;
            }
            if (getToken(builder, COLON, message("colon.expected"))) {
                removeNls(builder);
                final PsiBuilder.Marker mark = builder.mark();
                while (!builder.eof() && !SWITCH_BLOCK_TOKENS.contains(builder.getTokenType())) {
                    Statement.parse(builder);
                }
                mark.done(SWITCH_CASE_STATEMENT);
            }
            inSwitchMark.done(SWITCH_CASE);
            removeNls(builder);
        }
        getToken(builder, RBRACE, message("rcurly.expected"));
        removeNls(builder);
        return true;
    }

    private static boolean parseWhile(final PsiBuilder builder) {
        if (!getToken(builder, WHILE_KEYWORD, message("keywords.expected", WHILE_KEYWORD))) {
            return false;
        }
        removeNls(builder);
        getToken(builder, LPAR, message("lpar.expected"));
        removeNls(builder);
        Expression.parseExpr(builder, RPAR_STOPPER, WHILE_CONDITION);
        getToken(builder, RPAR, message("rpar.expected"));
        removeNls(builder);
        Block.parse(builder, WHILE_BLOCK);
        removeNls(builder);
        return true;
    }

    private static boolean parseTry(final PsiBuilder builder) {
        if (!getToken(builder, TRY_KEYWORD, message("keywords.expected", TRY_KEYWORD))) {
            return false;
        }
        removeNls(builder);
        Block.parse(builder, TRY_BLOCK);
        removeNls(builder);
        if (!TRY_BLOCK_TOKENS.contains(builder.getTokenType())) {
            builder.error(message("catch.finally.expected"));
        } else {
            boolean hasFinally = false;
            while (!builder.eof() && TRY_BLOCK_TOKENS.contains(builder.getTokenType())) {
                final PsiBuilder.Marker catchMark = builder.mark();
                if (getToken(builder, CATCH_KEYWORD)) {
                    if (hasFinally) {
                        builder.error(message("catch.after.finally"));
                    }
                    removeNls(builder);
                    // Catch definition: All Exceptions no obj is empty
                    if (getToken(builder, LPAR)) {
                        removeNls(builder);
                        SimpleTypeSpec.parseSimpleType(builder, false);
                        removeNls(builder);
                        final PsiBuilder.Marker nameMarker = builder.mark();
                        if (getToken(builder, IDENTIFIER_TOKENS_SET, message("identifier.expected"))) {
                            nameMarker.done(NAME_ELEMENT);
                        } else {
                            nameMarker.drop();
                        }
                        removeNls(builder);
                        getToken(builder, RPAR, message("rpar.expected"));
                        removeNls(builder);
                    }
                    Block.parse(builder, CATCH_BLOCK);
                    catchMark.done(CATCH_STATEMENT);
                } else if (getToken(builder, FINALLY_KEYWORD)) {
                    hasFinally = true;
                    removeNls(builder);
                    Block.parse(builder, FINALLY_BLOCK);
                    catchMark.done(FINALLY_STATEMENT);
                } else {
                    catchMark.drop();
                    break;
                }
                removeNls(builder);
            }
        }
        return true;
    }

}
