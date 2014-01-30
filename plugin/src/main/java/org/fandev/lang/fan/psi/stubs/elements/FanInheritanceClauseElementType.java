package org.fandev.lang.fan.psi.stubs.elements;

import org.fandev.lang.fan.FanStubElementType;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.stubs.FanReferenceListStub;
import org.fandev.lang.fan.psi.stubs.index.FanDirectInheritorsIndex;
import org.fandev.lang.fan.psi.stubs.impl.FanReferenceListStubImpl;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanInheritanceClause;
import org.fandev.lang.fan.psi.api.types.FanCodeReferenceElement;
import org.fandev.lang.fan.psi.impl.statements.typedefs.FanInheritanceClauseImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.stubs.*;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.Function;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Dror
 * Date: Mar 20, 2009
 * Time: 4:07:17 PM
 */
public class FanInheritanceClauseElementType extends FanStubElementType<FanReferenceListStub, FanInheritanceClause> {
    public FanInheritanceClauseElementType() {
        super("INHERITANCE_CLAUSE");
    }

    public FanInheritanceClause createPsi(final FanReferenceListStub stub) {
        return new FanInheritanceClauseImpl(stub);
    }

    public FanReferenceListStub createStub(final FanInheritanceClause psi, final StubElement parentStub) {
        final FanCodeReferenceElement[] elements = psi.getReferenceElements();
        final String[] refNames = ContainerUtil.map(elements, new Function<FanCodeReferenceElement, String>() {
            @Nullable
            public String fun(final FanCodeReferenceElement element) {
                return element.getReferenceName();
            }
        }, new String[elements.length]);

        return new FanReferenceListStubImpl(parentStub, FanElementTypes.INHERITANCE_CLAUSE, refNames);
    }

    public void serialize(final FanReferenceListStub stub, final StubOutputStream dataStream) throws IOException {
        final String[] names = stub.getBaseClasses();
        dataStream.writeByte(names.length);
        for (final String s : names) {
            dataStream.writeName(s);
        }
    }

    public FanReferenceListStub deserialize(final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        final byte b = dataStream.readByte();
        final String[] names = new String[b];
        for (int i = 0; i < b; i++) {
            names[i] = dataStream.readName().toString();
        }
        return new FanReferenceListStubImpl(parentStub, FanElementTypes.INHERITANCE_CLAUSE, names);
    }

    public void indexStub(final FanReferenceListStub stub, final IndexSink sink) {
        //TODO find what is wrong with this method implementation - causing IllegalMonitorStateException
        /*for (String name : stub.getBaseClasses()) {
            if (name != null) {
                sink.occurrence(FanDirectInheritorsIndex.KEY, name);
            }
        }*/
    }
}
