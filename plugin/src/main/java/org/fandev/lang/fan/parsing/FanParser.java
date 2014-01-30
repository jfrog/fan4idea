package org.fandev.lang.fan.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.text.BlockSupport;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.parsing.topLevel.CompilationUnit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror
 * @date Dec 11, 2008 11:54:37 PM
 */
public class FanParser implements PsiParser {
    private final static Logger logger = Logger.getInstance(FanParser.class.getName());

    @NotNull
    public ASTNode parse(final IElementType root, final PsiBuilder psiBuilder) {
        psiBuilder.setDebugMode(true);
        final PsiBuilder.Marker rootMarker = psiBuilder.mark();

        CompilationUnit.parse(psiBuilder);

        // Make sure we ate it all
        if (!psiBuilder.eof()) {
            final PsiBuilder.Marker errorMark = psiBuilder.mark();
            while (!psiBuilder.eof()) {
                psiBuilder.advanceLexer();
            }
            errorMark.error(FanBundle.message("typedef.expected"));
        }

        rootMarker.done(root);
        try {
            return psiBuilder.getTreeBuilt();
        } catch(BlockSupport.ReparsedSuccessfullyException e) {
            throw e;
        } catch (Throwable t) {
            final StringBuilder sb = new StringBuilder();
            while (!psiBuilder.eof()) {
                sb.append(psiBuilder.getTokenText());
                psiBuilder.advanceLexer();
            }
            logger.error("Parsing error, current offset is: " + psiBuilder.getCurrentOffset() + " Remaining text is: " +
                    sb.toString(), t);
            // Notice - this puts the application in an very unstable situation !!!
            // The action that starts the parsing never finish and so other write actions are blocked for good
            // ApplicationManager.getApplication().
            throw new RuntimeException(t);
        }
    }
}
