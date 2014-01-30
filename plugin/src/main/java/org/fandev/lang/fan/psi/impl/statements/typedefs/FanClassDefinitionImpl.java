package org.fandev.lang.fan.psi.impl.statements.typedefs;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.psi.impl.InheritanceImplUtil;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanConstructor;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.stubs.FanTypeDefinitionStub;
import org.fandev.lang.fan.psi.impl.PsiImplUtil;
import org.fandev.lang.fan.resolve.CollectClassMembersUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 3:48:09 PM
 */
public class FanClassDefinitionImpl extends FanTypeDefinitionImpl implements FanClassDefinition {
    private final static Logger logger = Logger.getInstance("org.fandev.lang.fan.psi.impl.statements.typedefs.FanClassDefinitionImpl");

    public FanClassDefinitionImpl(final FanTypeDefinitionStub stubElement) {
        super(stubElement, FanElementTypes.CLASS_DEFINITION);
    }

    public FanClassDefinitionImpl(final ASTNode astNode) {
        super(astNode);
    }

    protected Icon getIconInner() {
        if (hasModifierProperty(PsiModifier.ABSTRACT)) {
            return Icons.ABSTRACT_CLASS;
        } else {
            return Icons.CLASS;
        }
    }

    public String toString() {
        return "Class definition";
    }

    public PsiElement setName(@NonNls final String name) throws IncorrectOperationException {
        // TODO rename
        return this;
    }

    public boolean isInterface() {
        return false;
    }

    public boolean isAnnotationType() {
        return false;
    }

    public boolean isEnum() {
        return false;
    }

    public PsiClass[] getInterfaces() {
        return PsiClass.EMPTY_ARRAY;
    }

    @Override
    public void subtreeChanged() {
        this.fanSlots = null;
        this.fanFields = null;
        this.fanMethods = null;
        super.subtreeChanged();
    }

    @NotNull
    public PsiField[] getFields() {
        return getFanFields();
    }

    @NotNull
    public PsiMethod[] getMethods() {
        return getFanMethods();
    }

    @NotNull
    public PsiMethod[] getConstructors() {
        final Set<PsiMethod> constructors = new HashSet<PsiMethod>();
        for (final FanMethod method : getFanMethods()) {
            if (method instanceof FanConstructor) {
                constructors.add(method);
            }
        }
        return constructors.toArray(new PsiMethod[0]);
    }

    @NotNull
    public PsiClass[] getInnerClasses() {
        return PsiClass.EMPTY_ARRAY;
    }

    @NotNull
    public PsiClassInitializer[] getInitializers() {
        return PsiClassInitializer.EMPTY_ARRAY;
    }

    @NotNull
    public PsiField[] getAllFields() {
        return PsiField.EMPTY_ARRAY;
    }

    @NotNull
    public PsiMethod[] getAllMethods() {
        final List<PsiMethod> allMethods = new ArrayList<PsiMethod>();
        getAllMethodsInner(this, allMethods, new HashSet<PsiClass>());

        return allMethods.toArray(new PsiMethod[0]);
    }

    private static void getAllMethodsInner(final PsiClass clazz, final List<PsiMethod> allMethods, final HashSet<PsiClass> visited) {
        if (visited.contains(clazz)) {
            return;
        }
        visited.add(clazz);

        allMethods.addAll(Arrays.asList(clazz.getMethods()));

        /*final PsiField[] fields = clazz.getFields();
        for (final PsiField field : fields) {
            if (field instanceof FanField) {
                final FanField fanField = (FanField) field;
                if (fanField.isProperty()) {
                    final PsiMethod[] getters = fanField.getGetters();
                    if (getters.length > 0) allMethods.addAll(Arrays.asList(getters));
                    final PsiMethod setter = fanField.getSetter();
                    if (setter != null) allMethods.add(setter);
                }
            }
        }*/

        final PsiClass[] supers = clazz.getSupers();
        for (final PsiClass aSuper : supers) {
            getAllMethodsInner(aSuper, allMethods, visited);
        }
    }

    @NotNull
    public PsiClass[] getAllInnerClasses() {
        return PsiClass.EMPTY_ARRAY;
    }

    @Nullable
    public PsiField findFieldByName(final String name, final boolean checkBases) {
        if (!checkBases) {
            for (final FanField field : getFanFields()) {
                if (name.equals(field.getName())) {
                    return field;
                }
            }

            return null;
        }

        final Map<String, CandidateInfo> fieldsMap = CollectClassMembersUtil.getAllFields(this);
        final CandidateInfo info = fieldsMap.get(name);
        return info == null ? null : (PsiField) info.getElement();
    }

    @Nullable
    public PsiMethod findMethodBySignature(final PsiMethod patternMethod, final boolean checkBases) {
        final MethodSignature patternSignature = patternMethod.getSignature(PsiSubstitutor.EMPTY);
        for (final PsiMethod method : findMethodsByName(patternMethod.getName(), checkBases, false)) {
            final PsiClass clazz = method.getContainingClass();
            final PsiSubstitutor superSubstitutor = TypeConversionUtil.getClassSubstitutor(clazz, this, PsiSubstitutor.EMPTY);
            assert superSubstitutor != null;
            final MethodSignature signature = method.getSignature(superSubstitutor);
            if (signature.equals(patternSignature)) {
                return method;
            }
        }

        return null;
    }

    @NotNull
    public PsiMethod[] findMethodsBySignature(final PsiMethod patternMethod, final boolean checkBases) {
        return findMethodsBySignature(patternMethod, checkBases, true);
    }

    private PsiMethod[] findMethodsBySignature(final PsiMethod patternMethod, final boolean checkBases, final boolean includeSynthetic) {
        final ArrayList<PsiMethod> result = new ArrayList<PsiMethod>();
        final MethodSignature patternSignature = patternMethod.getSignature(PsiSubstitutor.EMPTY);
        for (final PsiMethod method : findMethodsByName(patternMethod.getName(), checkBases, includeSynthetic)) {
            final PsiClass clazz = method.getContainingClass();
            if (clazz == null) {
                continue;
            }
            final PsiSubstitutor superSubstitutor = TypeConversionUtil.getClassSubstitutor(clazz, this, PsiSubstitutor.EMPTY);
            assert superSubstitutor != null;
            final MethodSignature signature = method.getSignature(superSubstitutor);
            if (signature.equals(patternSignature)) //noinspection unchecked
            {
                result.add(method);
            }
        }
        return result.toArray(new PsiMethod[0]);
    }


    @NotNull
    public PsiMethod[] findMethodsByName(@NonNls final String name, final boolean checkBases) {
        return findMethodsByName(name, checkBases, true);
    }

    private PsiMethod[] findMethodsByName(final String name, final boolean checkBases, final boolean includeSyntheticAccessors) {
        if (!checkBases) {
            final List<PsiMethod> result = new ArrayList<PsiMethod>();
            for (final PsiMethod method : includeSyntheticAccessors ? getMethods() : getFanMethods()) {
                if (name.equals(method.getName())) {
                    result.add(method);
                }
            }

            return result.toArray(new PsiMethod[0]);
        }

        final Map<String, List<CandidateInfo>> methodsMap = CollectClassMembersUtil.getAllMethods(this, includeSyntheticAccessors);
        return PsiImplUtil.mapToMethods(methodsMap.get(name));
    }

    @NotNull
    public List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName(final String name, final boolean checkBases) {
        final ArrayList<Pair<PsiMethod, PsiSubstitutor>> result = new ArrayList<Pair<PsiMethod, PsiSubstitutor>>();

        if (!checkBases) {
            final PsiMethod[] methods = findMethodsByName(name, false);
            for (final PsiMethod method : methods) {
                result.add(new Pair<PsiMethod, PsiSubstitutor>(method, PsiSubstitutor.EMPTY));
            }
        } else {
            final Map<String, List<CandidateInfo>> map = CollectClassMembersUtil.getAllMethods(this, true);
            final List<CandidateInfo> candidateInfos = map.get(name);
            if (candidateInfos != null) {
                for (final CandidateInfo info : candidateInfos) {
                    final PsiElement element = info.getElement();
                    result.add(new Pair<PsiMethod, PsiSubstitutor>((PsiMethod) element, info.getSubstitutor()));
                }
            }
        }

        return result;
    }

    @NotNull
    public List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors() {
        final Map<String, List<CandidateInfo>> allMethodsMap = CollectClassMembersUtil.getAllMethods(this, true);
        final List<Pair<PsiMethod, PsiSubstitutor>> result = new ArrayList<Pair<PsiMethod, PsiSubstitutor>>();
        for (final List<CandidateInfo> infos : allMethodsMap.values()) {
            for (final CandidateInfo info : infos) {
                result.add(new Pair<PsiMethod, PsiSubstitutor>((PsiMethod) info.getElement(), info.getSubstitutor()));
            }
        }

        return result;
    }

    public PsiClass findInnerClassByName(@NonNls final String name, final boolean checkBases) {
        return null;
    }

    public PsiJavaToken getLBrace() {
        return null;
    }

    public PsiJavaToken getRBrace() {
        return null;
    }

    public PsiElement getScope() {
        return getParent();
    }

    public boolean isInheritor(@NotNull final PsiClass baseClass, final boolean checkDeep) {
        return InheritanceImplUtil.isInheritor(this, baseClass, checkDeep);
    }

    public boolean isInheritorDeep(final PsiClass baseClass, @Nullable final PsiClass classToByPass) {
        return InheritanceImplUtil.isInheritorDeep(this, baseClass, classToByPass);
    }

    public PsiClass getContainingClass() {
        return null;
    }

    @NotNull
    public Collection<HierarchicalMethodSignature> getVisibleSignatures() {
        return Collections.emptyList();
    }

    public PsiDocComment getDocComment() {
        return null;
    }

    public boolean isDeprecated() {
        return false;
    }

    public boolean hasTypeParameters() {
        return getTypeParameters().length > 0;
    }

    public PsiTypeParameterList getTypeParameterList() {
        return findChildByClass(PsiTypeParameterList.class);
    }

    @NotNull
    public PsiTypeParameter[] getTypeParameters() {
        final PsiTypeParameterList list = getTypeParameterList();
        if (list != null) {
            return list.getTypeParameters();
        }

        return PsiTypeParameter.EMPTY_ARRAY;
    }

    protected IElementType getBodyElementType() {
        return FanElementTypes.CLASS_BODY;
    }
}
