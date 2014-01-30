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

import org.fandev.lang.BaseFanTest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author freds
 * @date Jan 13, 2009
 */
public class TestSpecialParse extends BaseFanTest {
    @Before
    public void setUp() {
        setupProject();
    }

    @Override
    protected int getTimeoutSecs() {
        return 120;
    }

    @Test
    public void testParseFields() throws Throwable {
        String text = getResourceCharArray("/parser/FieldParsingTest.fan");
        parseWithNoTimeout(text);
    }

    @Test
    public void testParseExpressions() throws Throwable {
        String text = getResourceCharArray("/parser/ExpressionTest.fan");
        parse(text);
    }

    @Test
    public void testParseStrings() throws Throwable {
        String text = getResourceCharArray("/parser/StringTest.fan");
        parseWithNoTimeout(text);
    }

    @Test
    public void testFirstInFan() throws Throwable {
        File file = new File("../fan-1.0/src/testSys/fan/SerializationTest.fan");
        parseWithNoTimeout(readFile(file));
    }
}
