package org.fandev.lang.fan.psi.impl.statements.typedefs;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.IncorrectOperationException;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanBuildScriptDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.stubs.FanTypeDefinitionStub;
import org.fandev.utils.FanUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date: July 29, 2009
 * @author Fred Simon
 */
public class FanBuildScriptDefinitionImpl extends FanTypeDefinitionImpl implements FanBuildScriptDefinition {
    FanField[] fanFields;

    public FanBuildScriptDefinitionImpl(final FanTypeDefinitionStub stubElement) {
        super(stubElement, FanElementTypes.BUILDSCRIPT_DEFINITION);
    }

    public FanBuildScriptDefinitionImpl(final ASTNode astNode) {
        super(astNode);
    }

    public String toString() {
        return "BuildScript definition";
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
        this.fanFields = null;
        super.subtreeChanged();
    }

    @NotNull
    public PsiField[] getFields() {
        return getFanFields();
    }

    @NotNull
    @Override
    public FanField[] getFanFields() {
        if (fanFields == null) {
            final List<FanField> list = new ArrayList<FanField>();
            final PsiElement element = findChildByType(getBodyElementType());
            if (element != null) {
                final PsiElement[] bodyEls = element.getChildren();
                for (final PsiElement bodyEl : bodyEls) {
                    if (FanUtil.isFanField(bodyEl)) {
                        list.add((FanField) bodyEl);
                    }
                }
            }
            fanFields = list.toArray(new FanField[0]);
        }
        return fanFields;
    }

    @NotNull
    public PsiMethod[] getMethods() {
        return FanMethod.EMPTY_ARRAY;
    }

    @NotNull
    public FanMethod[] getFanMethods() {
        return FanMethod.EMPTY_ARRAY;
    }

    @NotNull
    public FanSlot[] getSlots() {
        return getFanFields();
    }

    @NotNull
    public PsiMethod[] getConstructors() {
        return PsiMethod.EMPTY_ARRAY;
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
        return PsiMethod.EMPTY_ARRAY;
    }

    @NotNull
    public PsiClass[] getAllInnerClasses() {
        return PsiClass.EMPTY_ARRAY;
    }

    public PsiField findFieldByName(@NonNls final String name, final boolean checkBases) {
        return null;
    }

    public PsiMethod findMethodBySignature(final PsiMethod patternMethod, final boolean checkBases) {
        return null;
    }

    @NotNull
    public PsiMethod[] findMethodsBySignature(final PsiMethod patternMethod, final boolean checkBases) {
        return PsiMethod.EMPTY_ARRAY;
    }

    @NotNull
    public PsiMethod[] findMethodsByName(@NonNls final String name, final boolean checkBases) {
        return PsiMethod.EMPTY_ARRAY;
    }

    @NotNull
    public List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName(@NonNls final String name,
                                                                                       final boolean checkBases) {
        return null;
    }

    @NotNull
    public List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors() {
        return null;
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
        return null;
    }

    public boolean isInheritor(@NotNull final PsiClass baseClass, final boolean checkDeep) {
        return false;
    }

    public boolean isInheritorDeep(final PsiClass baseClass, @Nullable final PsiClass classToByPass) {
        return false;
    }

    public PsiClass getContainingClass() {
        return null;
    }

    @NotNull
    public Collection<HierarchicalMethodSignature> getVisibleSignatures() {
        return null;
    }

    public PsiDocComment getDocComment() {
        return null;
    }

    public boolean isDeprecated() {
        return false;
    }

    public boolean hasTypeParameters() {
        return false;
    }

    public PsiTypeParameterList getTypeParameterList() {
        return null;
    }

    @NotNull
    public PsiTypeParameter[] getTypeParameters() {
        return PsiTypeParameter.EMPTY_ARRAY;
    }

    @Override
    protected Icon getIconInner() {
        return Icons.POD;
    }

    protected IElementType getBodyElementType() {
        return FanElementTypes.BUILDSCRIPT_BODY;
    }

    @Override
    public boolean hasModifierProperty(@Modifier final String name) {
        return false;
    }
}
