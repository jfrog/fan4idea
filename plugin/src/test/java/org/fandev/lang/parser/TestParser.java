package org.fandev.lang.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import junit.framework.Assert;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import org.fandev.lang.BaseFanTest;
import org.fandev.lang.ParsingResult;
import org.fandev.lang.ResultStatusCode;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.impl.statements.typedefs.FanClassDefinitionImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dror Bereznitsky Date: Jan 13, 2009 Time: 10:20:12 AM
 */
public class TestParser extends BaseFanTest {

    @Before
    public void setUp() {
        setupProject();
    }

    @Test
    public void testHelloWorld() throws Throwable {
        final File file = new File("example/src/HelloWorld.fan");

        System.out.println("Trying to parse file: " + file.getAbsolutePath());

        final PsiFile psiFile = parseWithNoTimeout(readFile(file));

        for (final PsiElement element : psiFile.getChildren()) {
            if (element instanceof FanClassDefinitionImpl) {
                final FanClassDefinition classDef = (FanClassDefinition) element;
                assertEquals("HelloWorld", classDef.getName());
                assertEquals(2, classDef.getSlots().length);
                assertEquals(1, classDef.getFields().length);
                assertEquals(1, classDef.getMethods().length);
                return;
            }
        }
        fail("Psi tree does not contain a class definition element");
    }

    @Test
    public void testSingleFile() throws Throwable {
        final File file = new File("../fan-1.0/src/flux/flux/fan/Commands.fan");

        System.out.println("Trying to parse file: " + file.getAbsolutePath());

        // Hijacking the system.err to avoid the long psi error messages
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        final PsiFile psiFile = parseWithNoTimeout(readFile(file));
        if (psiFile == null) {
            Assert.assertFalse("[Error] Failed parsing file (timeout): " + file.getAbsolutePath(), true);
        } else {
            Assert.assertEquals("Error elements in " + file.getAbsolutePath(), 0, visitErrorElements(psiFile));
            System.out.println("Parsed file: " + file.getAbsolutePath());
        }
    }

    @Test
    public void testAllCompilerFiles() throws Throwable {
        final FileFilter fileFilter = new FileFilter() {
            public boolean accept(final File pathname) {
                return true;
            }
        };
        parseAllFilesInFilter(fileFilter);
    }

    @Test
    public void testAllPodFiles() throws Throwable {
        final FileFilter fileFilter = new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.isDirectory() || pathname.getName().equals("pod.fan");
            }
        };
        parseAllFilesInFilter(fileFilter);
    }

    private void parseAllFilesInFilter(final FileFilter fileFilter) throws Throwable {
        // Hijacking the system.err to avoid the long psi error messages
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        final File dir = new File("../fan-1.0/src");
        final List<ParsingResult> results = new ArrayList<ParsingResult>();

        parseAllInDir(dir, fileFilter, results);
        int nbErrors = 0;
        for (final ParsingResult result : results) {
            if (result.status != ResultStatusCode.OK) {
                nbErrors++;
                System.out.println("File: " + result.fileName + " Error [" + result.status + "] " + result.errorMsg);
            }
        }
        System.out.println("Ratio " + nbErrors + "/" + results.size() + " " + (nbErrors * 100 / results.size()) + "%");
        Assert.assertEquals(0, nbErrors);
    }

}
