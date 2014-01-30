package org.fandev.lang.fan.parsing.statements.typeDefinitions.members;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.FanTokenTypes;
import org.fandev.lang.fan.FanBundle;
import static org.fandev.lang.fan.FanElementTypes.METHOD_BODY;
import static org.fandev.lang.fan.FanElementTypes.METHOD_DEFINITION;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.auxiliary.facets.Facet;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import org.fandev.lang.fan.parsing.statements.Block;
import org.fandev.lang.fan.parsing.statements.declaration.DeclarationType;

import java.util.Set;
import java.util.HashSet;

/**
 * <p>Grammar Definition:<ul>
 * <li>&lt;slotDefs&gt;       :=  &lt;slotDef&gt;*</li>
 * <li>&lt;slotDef&gt;        :=  &lt;fieldDef&gt; | &lt;methodDef&gt; | &lt;ctorDef&gt;</li>
 * </ul></p>
 *
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 4:25:33 PM
 */
public class SlotDefinition {
    public static boolean parse(final PsiBuilder builder, final DeclarationType type, final boolean isBuiltInType) {
        ParserUtils.removeNls(builder);

        // TODO: find a better way than rollbacking
        PsiBuilder.Marker rb = builder.mark();

        // All slot types have facets
        Facet.parse(builder);

        // All slot types have modifiers
        final Set<IElementType> modifiers = new HashSet<IElementType>();
        while (FanTokenTypes.ALL_SLOT_MODIFIERS.contains(builder.getTokenType())) {
            modifiers.add(builder.getTokenType());

            ParserUtils.advanceNoNls(builder);
            if (type == DeclarationType.MIXIN && FanTokenTypes.ONCE_KEYWORD == builder.getTokenType()) {
                rb.error(FanBundle.message("mixins.cannot.declare.once.methods"));
                rb = builder.mark();
            }
        }

        // Find out what kind of slot: field, method, constructor, static init
        if (LBRACE.equals(builder.getTokenType())) {
            // expecting static {...}
            rb.rollbackTo();
            final PsiBuilder.Marker staticInitMark = builder.mark();
            if (!STATIC_KEYWORD.equals(builder.getTokenType())) {
                staticInitMark.error(FanBundle.message("expecting.keyword.static"));
                return false;
            }
            final PsiBuilder.Marker idMark = builder.mark();
            ParserUtils.advanceNoNls(builder);
            idMark.done(FanElementTypes.NAME_ELEMENT);
            if (!LBRACE.equals(builder.getTokenType())) {
                staticInitMark.error(FanBundle.message("expecting.static"));
                return false;
            }
            Block.parse(builder, METHOD_BODY);
            staticInitMark.done(METHOD_DEFINITION);
            return true;
        } else if (NEW_KEYWORD.equals(builder.getTokenType())) {
            // New keyword enforce constructor
            if (type == DeclarationType.MIXIN) {
                rb.error(FanBundle.message("mixins.cannot.declare.constructors"));
                return false;
            } else if (type == DeclarationType.ENUM) {
                if (!modifiers.contains(FanTokenTypes.PRIVATE_KEYWORD)) {
                    rb.error(FanBundle.message("enums.must.have.private.constructors"));
                    return false;    
                }
            }
            rb.rollbackTo();
            return ConstructorDefinition.parse(builder, isBuiltInType);
        } else if (ParserUtils.lookAheadForElement(builder, LPAR, LBRACE, SEMICOLON, NLS, COLON_EQ)) {
            // Found a ( before <eos> so it's a method
            rb.rollbackTo();
            return MethodDefinition.parse(builder, isBuiltInType);
        } else {
            rb.rollbackTo();
            return FieldDefinition.parse(builder);
        }
    }
}
