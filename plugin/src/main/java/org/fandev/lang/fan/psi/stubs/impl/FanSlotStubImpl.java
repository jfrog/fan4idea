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
package org.fandev.lang.fan.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.stubs.FanSlotStub;

/**
 * @author freds
 * @date Feb 17, 2009
 */
public abstract class FanSlotStubImpl<T extends FanSlot> extends StubBase<T> implements FanSlotStub<T> {
    protected final StringRef myName;
    protected final String[] facetNames;

    public FanSlotStubImpl(final StubElement element, final IStubElementType type, final StringRef name, final String[] facetNames) {
        super(element, type);
        this.myName = name;
        this.facetNames = facetNames;
    }

    public String getName() {
        return myName.getString();
    }

    public String[] getFacetNames() {
        return facetNames;
    }
}
