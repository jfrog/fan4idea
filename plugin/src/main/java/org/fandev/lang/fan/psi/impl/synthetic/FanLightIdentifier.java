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
package org.fandev.lang.fan.psi.impl.synthetic;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightIdentifier;
import com.intellij.lang.ASTNode;

/**
 * @author freds
 * @date Jan 22, 2009
 */
public class FanLightIdentifier extends LightIdentifier {
    private PsiFile myFile;
    private TextRange myRange;

    public FanLightIdentifier(final PsiManager manager, final PsiFile file, final TextRange range) {
        super(manager, file.getText().substring(range.getStartOffset(), range.getEndOffset()));
        myFile = file;
        myRange = range;
    }

    public TextRange getTextRange() {
        return myRange;
    }

    public PsiFile getContainingFile() {
        return myFile;
    }
}
