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
package org.fandev.lang.fan.psi.impl.statements.blocks;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiStatement;
import org.fandev.lang.fan.psi.FanElement;
import org.fandev.lang.fan.psi.api.statements.blocks.FanPsiCodeBlock;
import org.fandev.lang.fan.FanTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author freds
 * @date Feb 18, 2009
 */
public class FanPsiCodeBlockImpl extends ASTWrapperPsiElement implements FanPsiCodeBlock {
    public FanPsiCodeBlockImpl(@NotNull final ASTNode node) {
        super(node);
    }

    @NotNull
    public PsiStatement[] getStatements() {
        return findChildrenByClass(PsiStatement.class);
    }

    public PsiElement getFirstBodyElement() {
        return null;
    }

    public PsiElement getLastBodyElement() {
        return null;
    }

    public PsiJavaToken getLBrace() {
        return null;
    }

    public PsiJavaToken getRBrace() {
        return null;
    }

    public PsiElement getLeftBrace() {
        return findChildByType(FanTokenTypes.LBRACE);
    }

    public PsiElement getRightBrace() {
        return findChildByType(FanTokenTypes.RBRACE);
    }
}
