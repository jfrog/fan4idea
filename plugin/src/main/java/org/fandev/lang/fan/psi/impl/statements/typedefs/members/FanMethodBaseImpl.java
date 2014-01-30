package org.fandev.lang.fan.psi.impl.statements.typedefs.members;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiSuperMethodImplUtil;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStub;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.psi.util.MethodSignatureUtil;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.psi.api.statements.params.FanParameterList;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.types.FanTypeElement;
import org.fandev.lang.fan.psi.impl.PsiImplUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:59:12 PM
 */
public abstract class FanMethodBaseImpl<T extends NamedStub> extends FanSlotElementImpl<T> implements FanMethod {
    public FanMethodBaseImpl(final T t, @NotNull final IStubElementType iStubElementType) {
        super(t, iStubElementType);
    }

    public FanMethodBaseImpl(final ASTNode astNode) {
        super(astNode);
    }

    @Override
    public Icon getIconInner() {
        return Icons.METHOD;
    }

    public PsiType getReturnType() {
        final FanTypeElement typeElement = findChildByClass(FanTypeElement.class);
        if (typeElement != null) {
            return typeElement.getType();
        }
        return PsiType.VOID;
    }

    public PsiType getReturnTypeNoResolve() {
        return getReturnType();
    }

    public PsiTypeElement getReturnTypeElement() {
        return null;
    }

    @NotNull
    public PsiParameterList getParameterList() {
        final FanParameterList parameterList = findChildByClass(FanParameterList.class);
        assert parameterList != null;
        return parameterList;
    }

    @NotNull
    public PsiReferenceList getThrowsList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PsiCodeBlock getBody() {
        return findChildByClass(PsiCodeBlock.class);
    }

    public boolean isVarArgs() {
        return false;
    }

    @NotNull
    public MethodSignature getSignature(@NotNull final PsiSubstitutor substitutor) {
        return MethodSignatureBackedByPsiMethod.create(this, substitutor);
    }

    @NotNull
    public PsiMethod[] findSuperMethods() {
        final PsiClass containingClass = getContainingClass();
        if (containingClass == null) {
            return PsiMethod.EMPTY_ARRAY;
        }

        final Set<PsiMethod> methods = new HashSet<PsiMethod>();
        findSuperMethodRecursilvely(methods, containingClass, false, new HashSet<PsiClass>(), createMethodSignature(this),
                new HashSet<MethodSignature>());

        return methods.toArray(new PsiMethod[0]);
    }

    @NotNull
    public PsiMethod[] findSuperMethods(final boolean checkAccess) {
        final PsiClass containingClass = getContainingClass();

        final Set<PsiMethod> methods = new HashSet<PsiMethod>();
        findSuperMethodRecursilvely(methods, containingClass, false, new HashSet<PsiClass>(), createMethodSignature(this),
                new HashSet<MethodSignature>());

        return methods.toArray(new PsiMethod[0]);
    }

    @NotNull
    public PsiMethod[] findSuperMethods(final PsiClass parentClass) {
        final Set<PsiMethod> methods = new HashSet<PsiMethod>();
        findSuperMethodRecursilvely(methods, parentClass, false, new HashSet<PsiClass>(), createMethodSignature(this),
                new HashSet<MethodSignature>());
        return methods.toArray(new PsiMethod[0]);
    }

    @NotNull
    public List<MethodSignatureBackedByPsiMethod> findSuperMethodSignaturesIncludingStatic(final boolean checkAccess) {
        final PsiClass containingClass = getContainingClass();

        final Set<PsiMethod> methods = new HashSet<PsiMethod>();
        final MethodSignature signature = createMethodSignature(this);
        findSuperMethodRecursilvely(methods, containingClass, true, new HashSet<PsiClass>(), signature, new HashSet<MethodSignature>());

        final List<MethodSignatureBackedByPsiMethod> result = new ArrayList<MethodSignatureBackedByPsiMethod>();
        for (final PsiMethod method : methods) {
            result.add(method.getHierarchicalMethodSignature());
        }

        return result;
    }

    /*
  * @deprecated use {@link #findDeepestSuperMethods()} instead
  */
    public PsiMethod findDeepestSuperMethod() {
        return null;
    }

    @NotNull
    public PsiMethod[] findDeepestSuperMethods() {
        return new PsiMethod[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public HierarchicalMethodSignature getHierarchicalMethodSignature() {
        return PsiSuperMethodImplUtil.getHierarchicalMethodSignature(this);
    }

    public void setBlock(final PsiCodeBlock newBlock) {
        final ASTNode newNode = newBlock.getNode().copyElement();
        final PsiCodeBlock oldBlock = getBody();
        if (oldBlock == null) {
            getNode().addChild(newNode);
            return;
        }
        getNode().replaceChild(oldBlock.getNode(), newNode);
    }

    public static MethodSignature createMethodSignature(final PsiMethod method) {
        final PsiParameter[] parameters = method.getParameterList().getParameters();
        final PsiType[] types = new PsiType[parameters.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = parameters[i].getType();
        }
        return MethodSignatureUtil.createMethodSignature(method.getName(), types, PsiTypeParameter.EMPTY_ARRAY, PsiSubstitutor.EMPTY);
    }

    private void findSuperMethodRecursilvely(final Set<PsiMethod> methods,
                                             final PsiClass psiClass,
                                             final boolean allowStatic,
                                             final Set<PsiClass> visited,
                                             final MethodSignature signature,
                                             @NotNull final Set<MethodSignature> discoveredSupers) {
        if (psiClass == null) return;
        if (visited.contains(psiClass)) return;
        visited.add(psiClass);
        final PsiClassType[] superClassTypes = psiClass.getSuperTypes();

        for (final PsiClassType superClassType : superClassTypes) {
            final PsiClass resolvedSuperClass = superClassType.resolve();

            if (resolvedSuperClass == null) continue;
            final PsiMethod[] superClassMethods = resolvedSuperClass.getMethods();
            final HashSet<MethodSignature> supers = new HashSet<MethodSignature>(3);

            for (final PsiMethod superClassMethod : superClassMethods) {
                final MethodSignature superMethodSignature = createMethodSignature(superClassMethod);

                if (PsiImplUtil.isExtendsSignature(superMethodSignature, signature)) {
                    if (allowStatic || !superClassMethod.getModifierList().hasExplicitModifier(PsiModifier.STATIC)) {
                        methods.add(superClassMethod);
                        supers.add(superMethodSignature);
                        discoveredSupers.add(superMethodSignature);
                    }
                }
            }

            findSuperMethodRecursilvely(methods, resolvedSuperClass, allowStatic, visited, signature, discoveredSupers);
            discoveredSupers.removeAll(supers);
        }
    }
}
