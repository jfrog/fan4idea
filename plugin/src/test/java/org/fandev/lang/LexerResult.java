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
package org.fandev.lang;

import com.intellij.psi.tree.IElementType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author freds
 * @date Jan 16, 2009
 */
public class LexerResult extends BasicResult {
    public int nbTokens;
    public Map<IElementType, Integer> totals = new HashMap<IElementType, Integer>();

    public LexerResult(String fileName, int nbTokens) {
        super(null, ResultStatusCode.OK, fileName);
        this.nbTokens = nbTokens;
    }

    public LexerResult(String errorMsg, ResultStatusCode status, String fileName) {
        super(errorMsg, status, fileName);
    }
}
