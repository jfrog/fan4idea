package org.fandev.lang.fan.psi.impl.statements.typedefs;

import org.fandev.lang.fan.psi.api.statements.typeDefs.FanEnumDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanEnumValue;
import org.fandev.lang.fan.psi.stubs.FanTypeDefinitionStub;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.icons.Icons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.impl.ElementBase;
import com.intellij.psi.impl.ElementPresentationUtil;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.VisibilityIcons;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.RowIcon;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Date: Mar 28, 2009
 * Time: 3:38:11 PM
 * @author Dror Bereznitsky
 */
public class FanEnumDefinitionImpl extends FanTypeDefinitionImpl implements FanEnumDefinition {
    FanEnumValue[] fanEnumValues;
    
    public FanEnumDefinitionImpl(final FanTypeDefinitionStub stubElement) {
        super(stubElement, FanElementTypes.CLASS_DEFINITION);
    }

    public FanEnumDefinitionImpl(final ASTNode astNode) {
        super(astNode);
    }

    public String toString() {
        return "Enum definition";
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

    public FanEnumValue[] getEnumValues() {
        if (fanEnumValues == null) {
            final PsiElement element = findChildByType(FanElementTypes.ENUM_BODY);
            final List<FanEnumValue> list = new ArrayList<FanEnumValue>();
            if (element != null) {
                final PsiElement[] bodyEls = element.getChildren();
                for (final PsiElement bodyEl : bodyEls) {
                    if (bodyEl instanceof FanEnumValue) {
                        list.add((FanEnumValue) bodyEl);
                    }
                }
            }
            fanEnumValues = list.toArray(new FanEnumValue[0]);
        }
        return fanEnumValues;
    }

    @NotNull
    public PsiMethod[] getMethods() {
        return getFanMethods();
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
        return Icons.ENUM;
    }

    protected IElementType getBodyElementType() {
        return FanElementTypes.ENUM_BODY;
    }
}
