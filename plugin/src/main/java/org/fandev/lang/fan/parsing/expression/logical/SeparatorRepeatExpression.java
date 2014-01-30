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
package org.fandev.lang.fan.parsing.expression.logical;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import static org.fandev.lang.fan.FanBundle.message;
import static org.fandev.lang.fan.parsing.util.ParserUtils.advanceNoNls;
import org.fandev.lang.fan.parsing.expression.ExpressionParser;
import org.fandev.lang.fan.parsing.expression.arithmetic.UnaryExpression;

/**
 * @author freds
 * @date Mar 1, 2009
 */
public abstract class SeparatorRepeatExpression implements ExpressionParser {
    protected final IElementType expressionType;
    protected final TokenSet separators;
    protected final boolean checkPrefixExpression;

    protected SeparatorRepeatExpression(final IElementType expressionType, final TokenSet separators) {
        this.expressionType = expressionType;
        this.separators = separators;
        // If separators are part of prefix
        boolean needToCheckPrefix = false;
        final IElementType[] prefixes = UnaryExpression.PREFIXES.getTypes();
        for (final IElementType prefix : prefixes) {
            if (separators.contains(prefix)) {
                needToCheckPrefix = true;
                break;
            }
        }
        checkPrefixExpression = needToCheckPrefix;
    }

    protected boolean parseThis(final PsiBuilder builder, final TokenSet stopper) {
        if (stopper.contains(builder.getTokenType())) {
            return false;
        }
        final PsiBuilder.Marker marker = builder.mark();
        final TokenSet newStopper = TokenSet.orSet(stopper, separators);
        if (lheParser(builder, newStopper)) {
            if (separators.contains(builder.getTokenType())) {
                final IElementType separator = builder.getTokenType();
                advanceNoNls(builder);
                if (!rheParse(builder, newStopper, separator)) {
                    marker.error(message("expression.expected"));
                    return false;
                }
                final PsiBuilder.Marker newMarker = marker.precede();
                marker.done(expressionType);
                if (separators.contains(builder.getTokenType())) {
                    subParse(builder, newMarker, newStopper);
                } else {
                    newMarker.drop();
                }
            } else {
                marker.drop();
            }
            return true;
        } else {
            marker.drop();
            return false;
        }
    }

    protected boolean lheParser(final PsiBuilder builder, final TokenSet newStopper) {
        if (checkPrefixExpression) {
            if (separators.contains(builder.getTokenType()) &&
                    UnaryExpression.parsePrefixExpression(builder, newStopper, this)) {
                return true;
            }
        }
        return innerParse(builder, newStopper);
    }

    protected boolean rheParse(final PsiBuilder builder, final TokenSet newStopper, final IElementType separator) {
        if (checkPrefixExpression) {
            if (separators.contains(builder.getTokenType()) &&
                    UnaryExpression.parsePrefixExpression(builder, newStopper, this)) {
                return true;
            }
        }
        return innerParse(builder, newStopper);
    }

    protected void subParse(final PsiBuilder builder, final PsiBuilder.Marker marker, final TokenSet stopper) {
        final IElementType separator = builder.getTokenType();
        advanceNoNls(builder);
        if (!rheParse(builder, stopper, separator)) {
            builder.error(message("expression.expected"));
            marker.drop();
            return;
        }
        final PsiBuilder.Marker newMarker = marker.precede();
        marker.done(expressionType);
        if (separators.contains(builder.getTokenType())) {
            subParse(builder, newMarker, stopper);
        } else {
            newMarker.drop();
        }
    }
}
