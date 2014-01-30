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
package org.fandev.lang.fan.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;

/**
 * @author freds
 * @date Jan 27, 2009
 */
public class FanShortClassNameIndex extends StringStubIndexExtension<FanTypeDefinition> {
    public static final StubIndexKey<String, FanTypeDefinition> KEY =
            StubIndexKey.createIndexKey("fan.class.shortName");

    public StubIndexKey<String, FanTypeDefinition> getKey() {
        return KEY;
    }
}
