package org.fandev.debugger;

import com.intellij.debugger.NoDataException;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.CompoundPositionManager;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.requests.ClassPrepareRequestor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.ClassPrepareRequest;
import org.fandev.index.FanIndex;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: Sep 2, 2009
 * Time: 11:40:51 PM
 *
 * @author Dror Bereznitsky
 */
public class FanPositionManager implements PositionManager {
    private final DebugProcess myDebugProcess;

    public FanPositionManager(final DebugProcess debugProcess) {
        myDebugProcess = debugProcess;
    }

    public DebugProcess getDebugProcess() {
        return myDebugProcess;
    }

    public SourcePosition getSourcePosition(final Location location) throws NoDataException {
        if (location == null) {
            throw new NoDataException();
        }

        final PsiFile psiFile = getPsiFileByLocation(getDebugProcess().getProject(), location);
        if (psiFile == null) {
            throw new NoDataException();
        }

        int lineNumber = calcLineIndex(location);
        if (lineNumber < 0) {
            throw new NoDataException();
        }
        return SourcePosition.createFromLine(psiFile, lineNumber);
    }

    @NotNull
    public List<ReferenceType> getAllClasses(final SourcePosition classPosition) throws NoDataException {
        final List<ReferenceType> result = ApplicationManager.getApplication().runReadAction(new Computable<List<ReferenceType>>() {
            public List<ReferenceType> compute() {
                final List<ReferenceType> result = new ArrayList<ReferenceType>();
                final PsiFile file = classPosition.getFile();
                if (file instanceof FanFile) {
                    final FanTypeDefinition[] typeDefinitions = ((FanFile)file).getTypeDefinitions();
                    for (final FanTypeDefinition def : typeDefinitions) {
                        final String enclosingName = def.getJavaQualifiedName();
                        result.addAll(myDebugProcess.getVirtualMachineProxy().classesByName(enclosingName));
                    }
                }
                return result;
            }
        });
        return result;
    }

    @NotNull
    public List<Location> locationsOfLine(final ReferenceType type, final SourcePosition position) throws NoDataException {
        try {
            int line = position.getLine() + 1;
            final List<Location> locations = getDebugProcess().getVirtualMachineProxy().versionHigher("1.4")
                    ? type.locationsOfLine(DebugProcessImpl.JAVA_STRATUM, null, line)
                    : type.locationsOfLine(line);
            if (locations == null || locations.isEmpty()) {
                throw new NoDataException();
            }
            return locations;
        }
        catch (AbsentInformationException e) {
            throw new NoDataException();
        }
    }

    public ClassPrepareRequest createPrepareRequest(final ClassPrepareRequestor requestor, final SourcePosition position) throws NoDataException {
        String qName;
        ClassPrepareRequestor waitRequestor;
        String waitPrepareFor = "";

        final FanTypeDefinition typeDefinition = findEnclosingTypeDefinition(position);
        if (typeDefinition == null){
            return null;
        }
        qName = typeDefinition.getJavaQualifiedName();

        waitPrepareFor = qName;
        waitRequestor = new ClassPrepareRequestor() {
            public void processClassPrepare(final DebugProcess debuggerProcess, final ReferenceType referenceType) {
                final CompoundPositionManager positionManager = ((DebugProcessImpl)debuggerProcess).getPositionManager();
                if (positionManager.locationsOfLine(referenceType, position).size() > 0) {
                    requestor.processClassPrepare(debuggerProcess, referenceType);
                } else {
                    final List<ReferenceType> positionClasses = positionManager.getAllClasses(position);
                    if (positionClasses.contains(referenceType)) {
                        requestor.processClassPrepare(debuggerProcess, referenceType);
                    }
                }
            }
        };
        return myDebugProcess.getRequestsManager().createClassPrepareRequest(waitRequestor, waitPrepareFor);
    }

    @Nullable
    private PsiFile getPsiFileByLocation( final Project project, final Location location) {
        if (location == null) {
            return null;
        }

        final ReferenceType refType = location.declaringType();
        if (refType == null) {
            return null;
        }

        // Currently deal only with Fan types
        if (!refType.name().startsWith("fan")) {
            return null;
        }

        String name = refType.name().substring(refType.name().lastIndexOf(".") + 1);
        final String[] names = name.split("\\$"); //Closures and functions
        name = names[0];
        return ((FanIndex)project.getComponent(FanIndex.COMPONENT_NAME)).getFanFileByTypeName(name);
    }

    private int calcLineIndex(final Location location) {
        if (location == null) {
            return -1;
        }

        try {
            return location.lineNumber() - 1;
        } catch (InternalError e) {
            return -1;
        }
    }

    private FanTypeDefinition findEnclosingTypeDefinition(final SourcePosition position) {
        final PsiFile file = position.getFile();
        if (!(file instanceof FanFile)) {
            return null;
        }
        final PsiElement element = file.findElementAt(position.getOffset());
        if (element == null) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(element, FanTypeDefinition.class);
    }
}
