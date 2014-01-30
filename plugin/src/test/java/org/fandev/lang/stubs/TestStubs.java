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
package org.fandev.lang.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.NamedStub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IStubFileElementType;
import junit.framework.Assert;
import org.fandev.lang.BaseFanTest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author freds
 * @date Jan 25, 2009
 */
public class TestStubs extends BaseFanTest {

    @Before
    public void setUp() {
        setupProject();
    }

    @Test
    public void testHelloWorldStub() throws Throwable {
        final File file = new File("example/src/HelloWorld.fan");
        final String text = readFile(file);
        transform("HelloWorld", text);
    }

    @Test
    public void testFieldParsingStub() throws Throwable {
        final String text = getResourceCharArray("/parser/FieldParsingTest.fan");
        transform("FieldParsingTest", text);
    }

    public String transform(final String testName, final String text) throws Throwable {
        final PsiFile psiFile = parseWithNoTimeout(text);
        Assert.assertNotNull(psiFile);

        final ASTNode node = psiFile.getNode();
        Assert.assertNotNull(node);
        final IElementType type = node.getElementType();
        Assert.assertTrue(type instanceof IStubFileElementType);

        final IStubFileElementType stubFileType = (IStubFileElementType) type;
        final StubBuilder builder = stubFileType.getBuilder();
        final StubElement element = builder.buildStubTree(psiFile);
        final String stubTree = getStubTree(element);
        System.out.println("------------------------ " + testName + " ------------------------");
        System.out.println(stubTree);
        System.out.println("");
        return stubTree;
    }

    private static String getStubTree(final StubElement element) {
        final StringBuffer buffer = new StringBuffer();
        getStubsTreeImpl(element, buffer, "");
        return buffer.toString();
    }

    private static void getStubsTreeImpl(final StubElement element, final StringBuffer buffer, final String offset) {
        final PsiElement psi = element.getPsi();
        buffer.append(offset).append("[").append(psi.toString()).
                append(element instanceof NamedStub ? " : " + ((NamedStub) element).getName() : "").
                append("]\n");
        for (final StubElement stubElement : ((List<StubElement>) element.getChildrenStubs())) {
            final PsiElement child = stubElement.getPsi();
            Assert.assertTrue(child.getParent() == psi);
            getStubsTreeImpl(stubElement, buffer, offset + "  ");
        }
    }
}
