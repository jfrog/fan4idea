/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fandev.lang.fan.parsing.expression.argument;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanBundle.message;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.expression.Expression;
import org.fandev.lang.fan.parsing.types.TypeSpec;
import org.fandev.lang.fan.parsing.types.TypeType;
import static org.fandev.lang.fan.parsing.util.ParserUtils.*;

/**
 * @author freds
 * @date Mar 2, 2009
 */
public class LiteralExpression {
    private static final TokenSet LITERALS = TokenSet.orSet(
            FAN_LITERALS,
            TokenSet.create(
                    NULL_KEYWORD, THIS_KEYWORD, SUPER_KEYWORD
            ));
    private static final TokenSet COLON_STOPPER = TokenSet.create(COLON);
    private static final TokenSet COLON_COMMA_RBRACKET_STOPPER = TokenSet.create(COLON, COMMA, RBRACKET);
    private static final TokenSet RBRACKET_COMMA = TokenSet.create(RBRACKET, COMMA);

    public static boolean parse(final PsiBuilder builder, final TokenSet stopper) {
        // TODO: Question: I removed the simple literal since it should be a callExpression
        PsiBuilder.Marker marker = builder.mark();
        // Listed literals
        if (THIS_KEYWORD.equals(builder.getTokenType())) {
            builder.advanceLexer();
            marker.done(THIS_REFERENCE_EXPRESSION);
            return true;
        } else if (SUPER_KEYWORD.equals(builder.getTokenType())) {
            builder.advanceLexer();
            marker.done(SUPER_REFERENCE_EXPRESSION);
            return true;
        }
        if (LITERALS.contains(builder.getTokenType())) {
            builder.advanceLexer();
            marker.done(LITERAL);
            return true;
        }
        // Slot literals may starts with #
        if (getToken(builder, SHARP)) {
            if (getToken(builder, IDENTIFIER_TOKENS_SET, FanBundle.message("identifier.expected"))) {
                marker.done(LITERAL);
                return true;
            } else {
                marker.drop();
                return false;
            }
        }
        // If we have a type may be literal with # or list and map literal [xxx]
        final TypeType typeType = TypeSpec.parseType(builder, true);
        if (typeType != TypeType.NONE) {
            if (getToken(builder, SHARP)) {
                // May be a slot type literal
                getToken(builder, IDENTIFIER_TOKENS_SET);
                marker.done(LITERAL);
                return true;
            }
            if (getToken(builder, DSL_STRING)) {
                marker.done(LITERAL);
                return true;
            }
            if (LBRACKET == builder.getTokenType()) {
                if (typeType == TypeType.MAP) {
                    return parseListOrMapLiteral(builder, LiteralType.MAP, marker);
                }
                if (typeType == TypeType.LIST) {
                    return parseListOrMapLiteral(builder, LiteralType.LIST, marker);
                }
            }
            marker.rollbackTo();
            marker = builder.mark();
        }
        return parseListOrMapLiteral(builder, LiteralType.UNKNOW, marker);
    }

    enum LiteralType {
        UNKNOW, LIST, MAP, ERROR
    }

    private static boolean parseListOrMapLiteral(final PsiBuilder builder, LiteralType litType, final PsiBuilder.Marker marker) {
        if (!getToken(builder, LBRACKET)) {
            marker.rollbackTo();
            return false;
        }
        removeNls(builder);

        final LiteralType emptyLiteralType = emptyMapOrList(builder, litType);
        switch (emptyLiteralType) {
            case LIST:
                removeNls(builder);
                getToken(builder, RBRACKET, FanBundle.message("rbrack.expected"));
                marker.done(LIST_LITERAL);
                return true;
            case MAP:
                removeNls(builder);
                getToken(builder, RBRACKET, FanBundle.message("rbrack.expected"));
                marker.done(MAP_LITERAL);
                return true;
            case ERROR:
                marker.rollbackTo();
                return false;
        }

        // Literal with values
        litType = mapOrListLiteralWithValues(builder, litType);
        switch (litType) {
            case LIST:
                removeNls(builder);
                getToken(builder, RBRACKET, FanBundle.message("rbrack.expected"));
                marker.done(LIST_LITERAL);
                return true;
            case MAP:
                removeNls(builder);
                getToken(builder, RBRACKET, FanBundle.message("rbrack.expected"));
                marker.done(MAP_LITERAL);
                return true;
        }
        marker.rollbackTo();
        return false;
    }

    private static LiteralType mapOrListLiteralWithValues(final PsiBuilder builder, LiteralType litType) {
        final PsiBuilder.Marker valMark = builder.mark();
        // First find literal type by parsing the first expression and finding the separator
        boolean res = Expression.parseExpr(builder, COLON_COMMA_RBRACKET_STOPPER, EXPRESSION);
        if (res) {
            litType = findLiteralType(builder, litType);
        }
        if (!res || litType == LiteralType.ERROR) {
            valMark.drop();
            return LiteralType.ERROR;
        }
        if (litType == LiteralType.LIST) {
            valMark.done(LIST_ITEM);
            if (COMMA.equals(builder.getTokenType())) {
                advanceNoNls(builder);
            }
            while (res && !builder.eof() && RBRACKET != builder.getTokenType()) {
                res = Expression.parseExpr(builder, RBRACKET_COMMA, LIST_ITEM);
                removeNls(builder);
                if (COMMA.equals(builder.getTokenType())) {
                    advanceNoNls(builder);
                }
            }
            return LiteralType.LIST;
        }
        if (litType == LiteralType.MAP) {
            PsiBuilder.Marker mapEntryMark = valMark.precede();
            valMark.done(MAP_ITEM_KEY);
            advanceNoNls(builder);
            if (!Expression.parseExpr(builder, RBRACKET_COMMA, MAP_ITEM_VALUE)) {
                mapEntryMark.drop();
                return LiteralType.ERROR;
            }
            mapEntryMark.done(MAP_ITEM);
            removeNls(builder);
            if (COMMA.equals(builder.getTokenType())) {
                advanceNoNls(builder);
            }
            while (!builder.eof() && RBRACKET != builder.getTokenType()) {
                mapEntryMark = builder.mark();
                if (Expression.parseExpr(builder, COLON_STOPPER, MAP_ITEM_KEY)) {
                    advanceNoNls(builder);
                    if (Expression.parseExpr(builder, RBRACKET_COMMA, MAP_ITEM_VALUE)) {
                        mapEntryMark.done(MAP_ITEM);
                        removeNls(builder);
                        if (COMMA.equals(builder.getTokenType())) {
                            advanceNoNls(builder);
                        }
                    } else {
                        mapEntryMark.drop();
                        return LiteralType.MAP;
                    }
                } else {
                    mapEntryMark.drop();
                    return LiteralType.MAP;
                }
            }
            return LiteralType.MAP;
        }
        return LiteralType.ERROR;
    }

    private static LiteralType findLiteralType(final PsiBuilder builder, LiteralType litType) {
        switch (litType) {
            case LIST:
                if (!RBRACKET_COMMA.contains(builder.getTokenType())) {
                    builder.error(message("comma.rbracket.expected"));
                    litType = LiteralType.ERROR;
                }
                break;
            case MAP:
                if (!COLON.equals(builder.getTokenType())) {
                    builder.error(message("colon.expected"));
                    litType = LiteralType.ERROR;
                }
                break;
            case UNKNOW:
            case ERROR: // Is it good?
            default:
                if (RBRACKET_COMMA.contains(builder.getTokenType())) {
                    litType = LiteralType.LIST;
                } else if (COLON.equals(builder.getTokenType())) {
                    litType = LiteralType.MAP;
                } else {
                    builder.error(message("literal.listOrMap.expected"));
                    litType = LiteralType.ERROR;
                }
        }
        return litType;
    }

    private static LiteralType emptyMapOrList(final PsiBuilder builder, final LiteralType litType) {
        LiteralType res = LiteralType.UNKNOW;
        if (getToken(builder, COMMA)) {
            // Empty list literal should have ] just after
            if (litType == LiteralType.UNKNOW) {
                res = LiteralType.LIST;
            } else if (litType != LiteralType.LIST) {
                // TODO: An error for me ???
                // builder.error(message("literal.list.unexpected"));
                // Forcing list literal anyway
                res = LiteralType.LIST;
            } else {
                res = litType;
            }
        } else if (getToken(builder, COLON)) {
            // Empty map literal should have ] just after
            if (litType == LiteralType.UNKNOW) {
                res = LiteralType.MAP;
            } else if (litType != LiteralType.MAP) {
                builder.error(message("literal.map.unexpected"));
                // Forcing map literal anyway
                res = LiteralType.MAP;
            } else {
                res = litType;
            }
        }
        return res;
    }
}
