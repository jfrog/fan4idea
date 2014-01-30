package org.fandev.lang.fan.parsing.topLevel;

import com.intellij.lang.PsiBuilder;
import static org.fandev.lang.fan.FanBundle.message;
import static org.fandev.lang.fan.FanElementTypes.*;
import static org.fandev.lang.fan.FanTokenTypes.*;
import org.fandev.lang.fan.parsing.statements.typeDefinitions.typeDefs.TypeDefinition;
import org.fandev.lang.fan.parsing.util.ParserUtils;
import static org.fandev.lang.fan.parsing.util.ParserUtils.getToken;

/**
 * <ul>
 * <li>&lt;compilationUnit&gt; :=  &lt;using&gt;* &lt;typeDef&gt;* </li>
 * <li>&lt;using&gt;          :=  &lt;usingPod&gt; | &lt;usingType&gt; | &lt;usingAs&gt;</li>
 * <li>&lt;usingPod&gt;       :=  "using" &lt;podSpec&gt; &lt;eos&gt;</li>
 * <li>&lt;usingType&gt;      :=  "using" &lt;podSpec&gt; "::" &lt;id&gt; &lt;eos&gt;</li>
 * <li>&lt;usingAs&gt;        :=  "using" &lt;podSpec&gt; "::" &lt;id&gt; "as" &lt;id&gt; &lt;eos&gt;</li>
 * <li>&lt;podSpec&gt;        :=  [ffi] &lt;id&gt; ("." &lt;id&gt;)*</li>
 * <li>&lt;ffi&gt;            :=  "[" &lt;id&gt; "]"</li>
 * </ol>
 *
 * @author Dror Bereznitsky
 * @author Fred Simon
 * @date Jan 6, 2009 11:25:52 PM
 */
public class CompilationUnit {
    public static void parse(final PsiBuilder builder) {
        // Shabeng should be the first token of the file
        if (SHABENG == builder.getTokenType()) {
            final PsiBuilder.Marker shBeng = builder.mark();
            if (!ParserUtils.advanceTo(builder, EOL)) {
                shBeng.error(message("separator.expected"));
                return;
            } else {
                shBeng.done(SHABENG);
            }
        }
        ParserUtils.removeNls(builder);

        while (USING_KEYWORD == builder.getTokenType()) {
            parseUsing(builder);
        }

        while (!builder.eof()) {
            if (!TypeDefinition.parse(builder)) {
                ParserUtils.cleanAfterErrorInBlock(builder);
            }
        }
    }

    private static void parseUsing(final PsiBuilder builder) {
        final PsiBuilder.Marker usingStatement = builder.mark();
        final PsiBuilder.Marker usingKeyword = builder.mark();
        builder.advanceLexer();
        usingKeyword.done(USING_KEYWORD);
        if (LBRACKET.equals(builder.getTokenType())) {
            // A FFI import
            builder.advanceLexer();
            final PsiBuilder.Marker ffiMark = builder.mark();
            if (getToken(builder, IDENTIFIER_TOKENS_SET, message("identifier.expected")))
                ffiMark.done(FFI_NAME);
            else
                ffiMark.drop();
            getToken(builder, RBRACKET, message("rbrack.expected"));
        }

        // A list of id with dots :)
        final PsiBuilder.Marker podIdExpr = builder.mark();
        final PsiBuilder.Marker podRefMark = builder.mark();
        do {
            getToken(builder, IDENTIFIER_TOKENS_SET, message("identifier.expected"));
        } while (getToken(builder, DOT));
        podRefMark.done(POD_REFERENCE);

        if (getToken(builder, COLON_COLON)) {
            final PsiBuilder.Marker usingType = builder.mark();
            final PsiBuilder.Marker m = builder.mark();
            getToken(builder, IDENTIFIER_TOKENS_SET, message("identifier.expected"));
            m.done(NAME_ELEMENT);
            usingType.done(ID_EXPR);
        }
        podIdExpr.done(ID_EXPR);

        if (getToken(builder, AS_KEYWORD)) {
            final PsiBuilder.Marker m = builder.mark();
            getToken(builder, IDENTIFIER_TOKENS_SET, message("identifier.expected"));
            m.done(USING_AS_NAME);
        }
        usingStatement.done(USING_STATEMENT);

        if (!EOL.contains(builder.getTokenType())) {
            builder.error(message("separator.expected"));
            ParserUtils.advanceTo(builder,EOL);
        } else {
            builder.advanceLexer();
        }
        ParserUtils.removeNls(builder);
    }
}
