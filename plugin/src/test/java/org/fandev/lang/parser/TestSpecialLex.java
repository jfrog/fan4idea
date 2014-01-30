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

import static junit.framework.Assert.*;
import org.fandev.lang.BaseFanTest;
import org.fandev.lang.ParserBlock;
import org.fandev.lang.fan.FanTokenTypes;
import static org.fandev.lang.fan.FanTokenTypes.CLASS_KEYWORD;
import org.junit.Test;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

/**
 * @author freds
 * @date Jan 12, 2009
 */
public class TestSpecialLex extends BaseFanTest {

    @Test
    public void testExpressions() throws Throwable {
        String buff = getResourceCharArray("/parser/ExpressionTest.fan");
        List<ParserBlock> list = lex(buff);
        checkNoBadCharacters(list);
        for (int i = 0; i < list.size(); i++) {
            ParserBlock block = list.get(i);
            if (block.type == CLASS_KEYWORD) {
                i = testIdentifier(buff, list, i, "ExpressionTest");
                // TODO: more assert
            }
        }
    }

    @Test
    public void testFields() throws Throwable {
        String buff = getResourceCharArray("/parser/FieldParsingTest.fan");
        List<ParserBlock> list = lex(buff);
        checkNoBadCharacters(list);
        int nbClass = 0;
        String[] classNames = {"AstWriter", "TestMethod"};
        int nbOutStream = 0;
        for (int i = 0; i < list.size(); i++) {
            ParserBlock block = list.get(i);
            if (CLASS_KEYWORD.equals(block.type)) {
                i = testIdentifier(buff, list, i, classNames[nbClass++]);
                block = list.get(++i);
                if (nbClass == 1) {
                    assertEquals(FanTokenTypes.NLS, block.type);
                } else if (nbClass == 2) {
                    assertEquals(FanTokenTypes.WHITE_SPACE, block.type);
                    block = list.get(++i);
                    assertEquals(FanTokenTypes.COLON, block.type);
                    i = testIdentifier(buff, list, i, "Method");
                }
            }
            if (FanTokenTypes.IDENTIFIER.equals(block.type) &&
                    "OutStream".equals(getTextBlock(buff, block))) {
                nbOutStream++;
                switch (nbOutStream) {
                    case 1:
                        i = testIdentifier(buff, list, i, "out");
                        block = list.get(++i);
                        assertEquals(FanTokenTypes.WHITE_SPACE, block.type);
                        block = list.get(++i);
                        assertEquals(FanTokenTypes.COLON_EQ, block.type);
                        break;
                    case 2:
                        i = testIdentifier(buff, list, i, "out");
                        block = list.get(++i);
                        assertEquals(FanTokenTypes.NLS, block.type);
                        break;
                    default:
                        fail("Should have only 2 OutStream and got " + nbOutStream);
                }
            }
        }
        assertEquals("OutStream number mismatch", 2, nbOutStream);
    }

    private int testIdentifier(CharSequence buff, List<ParserBlock> list, int i, String idName) {
        ParserBlock block;
        block = list.get(++i);
        assertEquals(FanTokenTypes.WHITE_SPACE, block.type);
        block = list.get(++i);
        assertEquals(FanTokenTypes.IDENTIFIER, block.type);
        assertEquals(idName, getTextBlock(buff, block));
        return i;
    }

    @Test
    public void testStrings() throws Throwable {
        String buff = getResourceCharArray("/parser/StringTest.fan");
        List<ParserBlock> list = lex(buff);
        checkNoBadCharacters(list);
        // First block is C_COMMENT with the text /* My test of comments in it
        ParserBlock commentBlock = list.get(0);
        String s = getTextBlock(buff, commentBlock).toString();
        assertEquals(FanTokenTypes.C_STYLE_COMMENT, commentBlock.type);
        assertTrue(s.contains("* My test of comments"));
        assertTrue(s.contains("/* Another test of comments"));
        // Removing white space and comments
        for (Iterator<ParserBlock> iterator = list.iterator(); iterator.hasNext();) {
            ParserBlock block = iterator.next();
            if (block.type == FanTokenTypes.WHITE_SPACE || FanTokenTypes.COMMENTS.contains(block.type)) {
                iterator.remove();
            }
        }
        boolean gotClass = false;
        boolean getStrings = false;
        boolean getIntegers = false;
        boolean getUris = false;
        for (int i = 0; i < list.size(); i++) {
            ParserBlock block = list.get(i);
            if (block.type == CLASS_KEYWORD) {
                // next one is StringTest
                ParserBlock next = list.get(++i);
                assertEquals(FanTokenTypes.IDENTIFIER, next.type);
                assertEquals("StringTest", getTextBlock(buff, next));
                gotClass = true;
            } else if (block.type == FanTokenTypes.IDENTIFIER) {
                String id = getTextBlock(buff, block).toString();
                if ("strings".equals(id)) {
                    assertTrue(gotClass);
                    assertFalse(getIntegers);
                    assertFalse(getStrings);
                    assertFalse(getUris);
                    getStrings = true;
                } else if ("integers".equals(id)) {
                    assertTrue(gotClass);
                    assertFalse(getIntegers);
                    assertFalse(getStrings);
                    assertFalse(getUris);
                    getIntegers = true;
                } else if ("uris".equals(id)) {
                    assertTrue(gotClass);
                    assertFalse(getIntegers);
                    assertFalse(getStrings);
                    assertFalse(getUris);
                    getUris = true;
                }
            } else if (getStrings) {
                ParserBlock next;
                // 3 Strings
                int nbStrings = 0;
                int nbNls = 0;
                // Starts counting after [
                while (list.get(++i).type != FanTokenTypes.LBRACKET) {
                    assertTrue(list.size() > i + 1);
                }
                next = list.get(++i);
                while (next.type != FanTokenTypes.RBRACKET) {
                    if (next.type == FanTokenTypes.STRING_LITERAL) {
                        nbStrings++;
                    } else if (next.type == FanTokenTypes.NLS) {
                        nbNls++;
                    } else if (next.type == FanTokenTypes.COMMA) {
                        // OK
                    } else {
                        fail("Got block " + next + " should string literal or comma");
                    }
                    next = list.get(++i);
                }
                assertEquals(4, nbStrings);
                assertEquals(5, nbNls);
                getStrings = false;
            } else if (getIntegers) {
                ParserBlock next;
                // 12 chars
                int nbInts = 0;
                int nbNls = 0;
                // Starts counting after [
                while (list.get(++i).type != FanTokenTypes.LBRACKET) {
                    assertTrue(list.size() > i + 1);
                }
                next = list.get(++i);
                while (next.type != FanTokenTypes.RBRACKET) {
                    if (next.type == FanTokenTypes.CHAR_LITERAL) {
                        nbInts++;
                    } else if (next.type == FanTokenTypes.NLS) {
                        nbNls++;
                    } else if (next.type == FanTokenTypes.COMMA) {
                        // OK
                    } else {
                        fail("Got block " + next + " should char literal or comma");
                    }
                    next = list.get(++i);
                }
                assertEquals(12, nbInts);
                assertEquals(2, nbNls);
                getIntegers = false;
            } else if (getUris) {
                ParserBlock next;
                // 4 uris
                int nbUris = 0;
                int nbNls = 0;
                // Starts counting after [
                while (list.get(++i).type != FanTokenTypes.LBRACKET) {
                    assertTrue(list.size() > i + 1);
                }
                next = list.get(++i);
                while (next.type != FanTokenTypes.RBRACKET) {
                    if (next.type == FanTokenTypes.URI_LITERAL) {
                        nbUris++;
                    } else if (next.type == FanTokenTypes.NLS) {
                        nbNls++;
                    } else if (next.type == FanTokenTypes.COMMA) {
                        // OK
                    } else {
                        fail("Got block " + next + " should be uri literal or comma");
                    }
                    next = list.get(++i);
                }
                assertEquals(4, nbUris);
                assertEquals(1, nbNls);
                getUris = false;
            }
        }
    }

}
