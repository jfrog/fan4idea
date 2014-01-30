package org.fandev.lang;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import static junit.framework.Assert.assertEquals;
import org.fandev.lang.fan.FanHighlightingLexer;
import org.fandev.lang.fan.FanTokenTypes;
import org.fandev.util.TestUtils;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Dror Bereznitsky Date: Jan 13, 2009 Time: 10:53:36 AM
 */
public abstract class BaseFanTest {
    protected Project myProject;
    protected Module myModule;
    protected IdeaProjectTestFixture myFixture;

    private static final Logger LOG = Logger.getInstance(BaseFanTest.class.getName());
    private static final byte[] BUFF = new byte[8000];
    protected ExecutorService executorService;

    protected void setupProject() {
        executorService = Executors.newSingleThreadExecutor();
        myFixture = createFixture();

        try {
            myFixture.setUp();
        }
        catch (Exception e) {
            LOG.error(e);
        }
        myModule = myFixture.getModule();
        myProject = myModule.getProject();
    }

    protected IdeaProjectTestFixture createFixture() {
        final TestFixtureBuilder<IdeaProjectTestFixture> fixtureBuilder =
                IdeaTestFixtureFactory.getFixtureFactory().createLightFixtureBuilder();
        return fixtureBuilder.getFixture();
    }

    public static String readFile(final File myTestFile) throws IOException {
        String content = new String(FileUtil.loadFileText(myTestFile));
        Assert.assertNotNull(content);

        content = StringUtil.replace(content, "\r", ""); // for MACs

        Assert.assertTrue("No data found in source file", content.length() > 0);

        return content;
    }

    protected void lexerAllInDir(final File dir, final List<LexerResult> results) throws Throwable {
        final File[] files = dir.listFiles();
        for (final File file : files) {
            if (file.isDirectory()) {
                lexerAllInDir(file, results);
            } else {
                final String fileName = file.getName();
                if (fileName.endsWith(".fan") && !fileName.equals("gamma.fan")) {
                    try {
                        final List<ParserBlock> list = lex(BaseFanTest.readFile(file));
                        checkNoBadCharacters(list);
                        final LexerResult result = new LexerResult(file.getPath(), list.size());
                        for (final ParserBlock block : list) {
                            final Integer t = result.totals.get(block.type);
                            if (t == null) {
                                result.totals.put(block.type, 1);
                            } else {
                                result.totals.put(block.type, t + 1);
                            }
                        }
                        results.add(result);
                    } catch (Throwable throwable) {
                        System.err.println("Error parsing " + file.getAbsolutePath());
                        results.add(new LexerResult(throwable.getMessage(), ResultStatusCode.TIMEOUT, file.getPath()));
                    }
                }
            }
        }
    }

    protected List<ParserBlock> lex(final CharSequence buff) throws Throwable {
        final FanHighlightingLexer lexer = new FanHighlightingLexer();
        lexer.start(buff);
        final List<ParserBlock> blocks = new ArrayList<ParserBlock>();
        ParserBlock block;
        while (lexer.getState() == 0) {
            block = new ParserBlock();
            block.type = lexer.getTokenType();
            if (block.type == null) {
                break;
            }
            block.start = lexer.getTokenStart();
            block.end = lexer.getTokenEnd();
            blocks.add(block);
            try {
                lexer.advance();
            } catch (Throwable e) {
                for (int i = Math.max(blocks.size() - 30, 0); i < blocks.size(); i++) {
                    final ParserBlock rBlock = blocks.get(i);
                    System.out.println(rBlock.toString() + " " + getTextBlock(buff, rBlock));
                }
                e.printStackTrace(System.out);
                junit.framework.Assert.assertTrue("Got Exception parsing " + e.getMessage(), false);
            }
        }
        return blocks;
    }

    protected CharSequence getTextBlock(final CharSequence buff, final ParserBlock rBlock) {
        return buff.subSequence(rBlock.start, rBlock.end);
    }

    public String getResourceCharArray(final String resourceName) throws IOException {
        final InputStream is = getClass().getResourceAsStream(resourceName);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int nbRead;
        while ((nbRead = is.read(BUFF)) > 0) {
            bos.write(BUFF, 0, nbRead);
        }
        return bos.toString();
    }

    protected void checkNoBadCharacters(final List<ParserBlock> list) {
        for (final ParserBlock block : list) {
            junit.framework.Assert.assertNotSame(block.type, FanTokenTypes.BAD_CHARACTER);
        }
    }

    protected void parseAllInDir(final File dir, FileFilter fileFilter, final List<ParsingResult> results) throws Throwable {
        if (fileFilter == null) {
            fileFilter = new FileFilter() {
                public boolean accept(final File pathname) {
                    return true;
                }
            };
        }
        final File[] files = dir.listFiles(fileFilter);
        for (final File file : files) {
            if (file.isDirectory()) {
                parseAllInDir(file, fileFilter, results);
            } else {
                final String fileName = file.getName();
                if (fileName.endsWith(".fan") && !fileName.equals("gamma.fan")) {
                    try {
                        System.out.println("Parsing file: " + file.getAbsolutePath());
                        final PsiFile psiFile = parse(readFile(file));
                        if (psiFile == null) {
                            System.out.println("[Error] Failed parsing file (timeout): " + file.getAbsolutePath());
                            results.add(new ParsingResult(ResultStatusCode.TIMEOUT, file.getPath(), "timeout", null));
                        } else {
                            final int errors = visitErrorElements(psiFile);
                            System.out.println("Parsed file: " + file.getAbsolutePath());
                            if (errors == 0) {
                                results.add(new ParsingResult(ResultStatusCode.OK, file.getPath(), "", psiFile));
                            } else {
                                results.add(
                                        new ParsingResult(ResultStatusCode.PARSING_ERROR, file.getPath(), "", psiFile));
                            }
                        }
                    } catch (Throwable throwable) {
                        String msg = throwable.getMessage();
                        System.out.println("[Error] Error parsing " + file.getAbsolutePath() + ": " + msg);
                        int firstReturn = msg.indexOf("\n");
                        if (firstReturn != -1) {
                            msg = msg.substring(0, firstReturn);
                        }
                        results.add(new ParsingResult(ResultStatusCode.EXCEPTION, file.getPath(), msg, null));
                    }
                }
            }
        }
    }

    protected int visitErrorElements(final PsiFile psiFile) {
        final PsiRecursiveErrorElementVisitor recursiveErrorElementVisitor = new PsiRecursiveErrorElementVisitor();
        psiFile.accept(recursiveErrorElementVisitor);
        return recursiveErrorElementVisitor.getErrors();
    }

    public PsiFile parse(final String text) throws Throwable {
        final Callable<PsiFile> testBlock = new Callable<PsiFile>() {
            public PsiFile call() throws Exception {
                try {
                    return TestUtils.createPseudoPhysicalFanFile(myProject, text);
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }
            }
        };

        final Future<PsiFile> fileFuture = executorService.submit(testBlock);
        try {
            return fileFuture.get(getTimeoutSecs(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            return null;
        } finally {
            if (fileFuture != null && !fileFuture.isDone()) {
                fileFuture.cancel(true);
            }
        }
    }

    protected int getTimeoutSecs() {
        return 20;
    }

    public PsiFile parseWithNoTimeout(final String text) throws Throwable {
        final PsiFile psiFile = TestUtils.createPseudoPhysicalFanFile(myProject, text);
        assertEquals(0, visitErrorElements(psiFile));
        return psiFile;
    }

    private class PsiRecursiveErrorElementVisitor extends PsiRecursiveElementVisitor {
        private int errors = 0;

        @Override
        public void visitErrorElement(final PsiErrorElement element) {
            errors++;
            final String textFile = element.getContainingFile().getText();
            final int offset = element.getTextOffset();
            System.out.println("\tError element: " + element.getErrorDescription() +
                    " for pos " + offset +
                    " around '" +
                    textFile.substring(Math.max(0, offset - 20), Math.min(textFile.length(), offset + 20)) + "'");
        }

        public int getErrors() {
            return errors;
        }
    }
}
