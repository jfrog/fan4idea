package org.fandev.lang.fan.psi.impl.types;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.fandev.lang.fan.FanFileType;
import org.fandev.lang.fan.psi.api.FanResolveResult;
import org.fandev.lang.fan.psi.api.types.FanCodeReferenceElement;
import org.fandev.lang.fan.psi.impl.FanReferenceElementImpl;
import org.fandev.lang.fan.psi.impl.FanResolveResultImpl;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.utils.PsiUtil;
import org.fandev.index.FanIndex;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Dror Bereznitsky
 * @date Feb 19, 2009 11:32:13 PM
 */
public class FanCodeReferenceElementImpl extends FanReferenceElementImpl implements FanCodeReferenceElement {
    private static final FanResolver RESOLVER = new FanResolver();

    public FanCodeReferenceElementImpl(final StubElement stubElement, @NotNull final IStubElementType iStubElementType) {
        super(stubElement, iStubElementType);
    }

    public FanCodeReferenceElementImpl(final ASTNode astNode) {
        super(astNode);
    }

    public String toString() {
        return "Reference element";
    }

    public PsiElement getQualifier() {
        return this;
    }

    public PsiElement resolve() {
        final ResolveResult[] results = getManager().getResolveCache().resolveWithCaching(this, RESOLVER, false, false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    public String getCanonicalText() {
        final PsiElement resolved = resolve();
        if (resolved instanceof PsiClass) {
            return ((PsiClass) resolved).getQualifiedName();
        }
        if (resolved instanceof PsiPackage) {
            return ((PsiPackage) resolved).getQualifiedName();
        }
        return null;
    }

    public boolean isReferenceTo(final PsiElement psiElement) {
        return getManager().areElementsEquivalent(psiElement, resolve());
    }

    public Object[] getVariants() {
        final FanCodeReferenceElement qualifier = (FanCodeReferenceElement) getQualifier();
        if (qualifier != null) {
            final PsiElement resolve = qualifier.resolve();
            if (resolve instanceof PsiClass) {
                final PsiClass clazz = (PsiClass) resolve;
                final List<PsiElement> result = new ArrayList<PsiElement>();

                for (final PsiField field : clazz.getFields()) {
                    if (field.hasModifierProperty(PsiModifier.STATIC)) {
                        result.add(field);
                    }
                }

                for (final PsiMethod method : clazz.getMethods()) {
                    if (method.hasModifierProperty(PsiModifier.STATIC)) {
                        result.add(method);
                    }
                }

                return result.toArray(new PsiElement[0]);
            }
        }
        return new Object[]{};
    }

    public boolean isSoft() {
        return false;
    }

    @NotNull
    public ResolveResult[] multiResolve(final boolean incompleteCode) {
        return getManager().getResolveCache().resolveWithCaching(this, RESOLVER, false, incompleteCode);
    }

    private static class FanResolver implements ResolveCache.PolyVariantResolver<FanCodeReferenceElementImpl> {

        public ResolveResult[] resolve(final FanCodeReferenceElementImpl fanCodeReferenceElement, final boolean incompleteCode) {
            if (fanCodeReferenceElement.getReferenceName() == null) {
                return FanResolveResult.EMPTY_ARRAY;
            }

            final FanResolveResult[] results = _resolve(fanCodeReferenceElement, fanCodeReferenceElement.getManager());

            return results;
        }

        //TODO handle other possbile reference types: Enum, Mixin
        private FanResolveResult[] _resolve(final FanCodeReferenceElementImpl ref, final PsiManager manager) {
            final String refName = ref.getReferenceName();
            final FanCodeReferenceElement qualifier = (FanCodeReferenceElement) ref.getQualifier();
            if (qualifier != null) {
                final List<FanResolveResult> results = new ArrayList<FanResolveResult>();
                ProjectRootManager.getInstance(manager.getProject()).getFileIndex().iterateContent(new ContentIterator() {
                    public boolean processFile(final VirtualFile virtualFile) {
                        if (FanFileType.FAN_FILE_TYPE == virtualFile.getFileType()) {
                            final FanFile psiFile = (FanFile) manager.findFile(virtualFile);
                            final PsiClass[] classes = psiFile.getClasses();
                            for (final PsiClass aClass : classes) {
                                if (refName.equals(aClass.getName())) {
                                    final boolean isAccessible = PsiUtil.isAccessible(ref, aClass);
                                    results.add(new FanResolveResultImpl(aClass, isAccessible));
                                }
                            }
                        }
                        return true;
                    }
                });

                if (results.size() > 0) {
                    return results.toArray(new FanResolveResult[0]);
                }

                final FanIndex fanIndex = (FanIndex) manager.getProject().getComponent(FanIndex.COMPONENT_NAME);
                final PsiFile typeFile = fanIndex.getFanFileByTypeName(refName);
                if (typeFile != null) {
                    final FanFile psiFile = (FanFile) typeFile;
                    final PsiClass[] classes = psiFile.getClasses();
                    for (final PsiClass aClass : classes) {
                        try {
                            if (refName.equals(aClass.getName())) {
                                final boolean isAccessible = PsiUtil.isAccessible(ref, aClass);
                                results.add(new FanResolveResultImpl(aClass, isAccessible));
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }

                return results.toArray(new FanResolveResult[0]);
            }
            return FanResolveResult.EMPTY_ARRAY;
        }
    }
}