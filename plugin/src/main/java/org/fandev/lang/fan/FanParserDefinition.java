package org.fandev.lang.fan;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import static com.intellij.lang.ParserDefinition.SpaceRequirements.MAY;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.fandev.lang.fan.parser.FanPsiCreator;
import org.fandev.lang.fan.parsing.FanParser;
import org.fandev.lang.fan.psi.impl.FanFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror
 * @date Dec 11, 2008 11:50:55 PM
 */
public class FanParserDefinition implements ParserDefinition {
    @NotNull
    public Lexer createLexer(final Project project) {
        return new FanParsingLexer();
    }

    public PsiParser createParser(final Project project) {
        return new FanParser();
    }

    public IFileElementType getFileNodeType() {
        return FanElementTypes.FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(FanTokenTypes.WHITE_SPACE);
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return FanTokenTypes.COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return FanTokenTypes.STRING_LITERALS;
    }

    @NotNull
    public PsiElement createElement(final ASTNode astNode) {
        return FanPsiCreator.createElement(astNode);
    }

    public PsiFile createFile(final FileViewProvider fileViewProvider) {
        return new FanFileImpl(fileViewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(final ASTNode astNode, final ASTNode astNode1) {
        return MAY;
    }
}
