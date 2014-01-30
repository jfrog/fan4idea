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
package org.fandev.lang.fan.psi.stubs.elements;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import org.fandev.lang.fan.FanStubElementType;
import org.fandev.lang.fan.psi.api.modifiers.FanFacet;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.stubs.FanSlotStub;
import org.fandev.lang.fan.psi.stubs.index.FanFacetNameSlotIndex;
import org.fandev.lang.fan.psi.stubs.index.FanSlotNameIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author freds
 * @date Feb 17, 2009
 */
public abstract class FanSlotElementType<T extends FanSlot, S extends FanSlotStub<T>> extends FanStubElementType<S, T> {
    protected FanSlotElementType(@NotNull final String debugName) {
        super(debugName);
    }

    public S createStub(final T t, final StubElement element) {
        final FanFacet[] facets = t.getFacets();
        final String[] facetNames = new String[facets.length];
        for (int i = 0; i < facets.length; i++) {
            facetNames[i] = facets[i].getName();
        }
        return createStubImpl(element, StringRef.fromString(t.getName()), facetNames);
    }

    public void serialize(final S stub, final StubOutputStream stream) throws IOException {
        stream.writeName(stub.getName());
        final String[] facets = stub.getFacetNames();
        stream.writeByte(facets.length);
        for (final String s : facets) {
            stream.writeName(s);
        }
    }

    public S deserialize(final StubInputStream stream, final StubElement element) throws IOException {
        final StringRef name = stream.readName();
        final byte b = stream.readByte();
        final String[] facets = new String[b];
        for (int i = 0; i < b; i++) {
            facets[i] = stream.readName().toString();
        }
        return createStubImpl(element, name, facets);
    }

    protected abstract S createStubImpl(final StubElement element, final StringRef name, final String[] facets);

    public void indexStub(final S stub, final IndexSink sink) {
        final String name = stub.getName();
        if (name != null) {
            sink.occurrence(FanSlotNameIndex.KEY, name);
        }
        for (final String facet : stub.getFacetNames()) {
            if (facet != null) {
                sink.occurrence(FanFacetNameSlotIndex.KEY, facet);
            }
        }
    }
}
