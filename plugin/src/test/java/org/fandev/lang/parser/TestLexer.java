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
package org.fandev.lang.parser;

import junit.framework.Assert;
import org.fandev.lang.BaseFanTest;
import org.fandev.lang.LexerResult;
import org.fandev.lang.ParserBlock;
import org.fandev.lang.ResultStatusCode;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author freds
 * @date Jan 8, 2009
 */
public class TestLexer extends BaseFanTest {
    @Test
    public void testHelloWorld() throws Throwable {
        final File file = new File("example/src/HelloWorld.fan");
        final List<ParserBlock> list = lex(BaseFanTest.readFile(file));
        checkNoBadCharacters(list);
    }

    @Test
    public void testBuildCompiler() throws Throwable {
        final File file = new File("../fan-1.0/src/compiler/fan/Compiler.fan");
        final List<ParserBlock> list = lex(BaseFanTest.readFile(file));
        checkNoBadCharacters(list);
    }

    @Test
    public void testAllCompilerFiles() throws Throwable {
        final File dir = new File("../fan-1.0/src");
        final ArrayList<LexerResult> results = new ArrayList<LexerResult>();
        lexerAllInDir(dir, results);
        int nbErrors = 0;
        for (final LexerResult result : results) {
            if (result.status != ResultStatusCode.OK) {
                nbErrors++;
                System.out.println("File: " + result.fileName + " Error " + result.errorMsg);
            }
        }
        System.out.println("Ratio " + nbErrors + "/" + results.size() + " " + (nbErrors * 100 / results.size()) + "%");
        Assert.assertEquals(0, nbErrors);
    }

}
