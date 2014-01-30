package org.fandev.lang.fan.parsing.statements.declaration;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import static org.fandev.lang.fan.FanTokenTypes.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 5:22:55 PM
 */
public enum DeclarationType {
    CLASS(CLASS_KEYWORD, CLASS_MODIFIERS),
    MIXIN(MIXIN_KEYWORD, MIXIN_MODIFIERS),
    ENUM(ENUM_KEYWORD, PROTECTION),
    CONSTRUCTOR(NEW_KEYWORD, PROTECTION),
    INNER_SET(PROTECTION),
    METHOD(METHOD_MODIFIERS),
    FIELD(FIELD_MODIFIERS);

    private final IElementType keyword;
    private final TokenSet modifiersSet;

    DeclarationType(final IElementType keyword, final TokenSet modifiersSet) {
        this.keyword = keyword;
        this.modifiersSet = modifiersSet;
    }

    DeclarationType(final TokenSet modifiersSet) {
        this.keyword = null;
        this.modifiersSet = modifiersSet;
    }

    public IElementType getKeyword() {
        return keyword;
    }

    public TokenSet getModifiersSet() {
        return modifiersSet;
    }

    public String errorMessage() {
        final StringBuilder builder = new StringBuilder("Modifiers ( ");
        modifiersSet.getTypes();
        boolean first = true;
        for (final IElementType type : modifiersSet.getTypes()) {
            if (!first) {
                builder.append(", ");
            }
            builder.append(type.toString());
            first = false;
        }
        builder.append(" )");
        if (getKeyword() != null) {
            builder.append(" or keyword ").append(getKeyword());
        }
        return builder.toString();
    }
}
