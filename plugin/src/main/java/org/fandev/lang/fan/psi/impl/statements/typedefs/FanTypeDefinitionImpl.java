package org.fandev.lang.fan.psi.impl.statements.typedefs;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.impl.ElementBase;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.ui.RowIcon;
import com.intellij.util.VisibilityIcons;
import com.intellij.util.IncorrectOperationException;
import org.fandev.lang.fan.FanElementTypes;
import org.fandev.lang.fan.FanTokenTypes;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.lang.fan.psi.api.modifiers.FanModifierList;
import org.fandev.lang.fan.psi.api.statements.typeDefs.*;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.types.FanCodeReferenceElement;
import org.fandev.lang.fan.psi.impl.FanBaseElementImpl;
import org.fandev.lang.fan.psi.impl.FanClassReferenceType;
import org.fandev.lang.fan.psi.impl.FanFileImpl;
import org.fandev.lang.fan.psi.impl.synthetic.FanLightIdentifier;
import org.fandev.lang.fan.psi.stubs.FanTypeDefinitionStub;
import org.fandev.utils.PsiUtil;
import org.fandev.index.FanIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dror Bereznitsky
 * @date Jan 9, 2009 11:42:19 PM
 */
public abstract class FanTypeDefinitionImpl extends FanBaseElementImpl<FanTypeDefinitionStub>
        implements FanTypeDefinition {
    protected FanField[] fanFields;
    protected FanMethod[] fanMethods;
    protected FanSlot[] fanSlots;

    private final static Logger logger = Logger.getInstance("org.fandev.lang.fan.psi.impl.statements.typedefs.FanTypeDefinitionImpl");

    public FanTypeDefinitionImpl(final FanTypeDefinitionStub stubElement, @NotNull final IStubElementType iStubElementType) {
        super(stubElement, iStubElementType);
    }

    public FanTypeDefinitionImpl(final ASTNode astNode) {
        super(astNode);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return getName();
            }

            @Nullable
            public String getLocationString() {
                final PsiFile file = getContainingFile();
                if (file instanceof FanFile) {
                    final FanFile fanFile = (FanFile) file;

                    return fanFile.getPodName().length() > 0 ? "(" + fanFile.getPodName() + ")" : "";
                }
                return "";
            }

            @Nullable
            public Icon getIcon(final boolean open) {
                return FanTypeDefinitionImpl.this
                        .getIcon(Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }
        };
    }

    @NotNull
    public PsiClassType[] getExtendsListTypes() {
        final List<PsiClassType> extendsTypes = getReferenceListTypes(getInheritanceClause());
        return extendsTypes.toArray(new PsiClassType[0]);
    }

    @Nullable
    public FanInheritanceClause getInheritanceClause() {
        return findChildByClass(FanInheritanceClause.class);
    }

    @NotNull
    public PsiClassType[] getImplementsListTypes() {
        return new PsiClassType[0];
    }

    public PsiClass getSuperClass() {
        final PsiClassType[] superTypes = getSuperTypes();
        for (final PsiClassType psiClassType : superTypes) {
            final FanTypeDefinition fanType = (FanTypeDefinition) psiClassType.resolve();
            if (fanType instanceof FanClassDefinition) {
                return fanType;
            }
        }
        if (!"Obj".equals(getName())) {
            return getFanObjType();
        }
        return null;
    }

    public FanTypeDefinition getSuperType() {
        return (FanTypeDefinition) getSuperClass();
    }

    @NotNull
    public PsiClass[] getSupers() {
        final PsiClassType[] superTypes = getSuperTypes();
        final List<PsiClass> result = new ArrayList<PsiClass>();
        for (final PsiClassType superType : superTypes) {
            final PsiClass superClass = superType.resolve();
            if (superClass != null) {
                result.add(superClass);
            }
        }

        return result.toArray(new PsiClass[0]);
    }

    @NotNull
    public PsiClassType[] getSuperTypes() {
        return getExtendsListTypes();
    }

    public PsiReferenceList getExtendsList() {
        return null;
    }

    public PsiReferenceList getImplementsList() {
        return null;
    }

    private static List<PsiClassType> getReferenceListTypes(@Nullable FanReferenceList list) {
        final ArrayList<PsiClassType> types = new ArrayList<PsiClassType>();
        if (list != null) {
            for (final FanCodeReferenceElement ref : list.getReferenceElements()) {
                types.add(new FanClassReferenceType(ref));
            }
        }
        return types;
    }

    @NotNull
    public String getPodName() {
        final PsiFile psiFile = getContainingFile();
        if (psiFile instanceof FanFile) {
            return ((FanFile) psiFile).getPodName();
        }
        logger.warn("Fantom type '" + getName() + "' is not in a fan file !");
        return "NotFanFile";
    }

    public int getTextOffset() {
        final PsiIdentifier identifier = getNameIdentifier();
        return identifier == null ? 0 : identifier.getTextRange().getStartOffset();
    }

    @Override
    public String getName() {
        final PsiIdentifier id = this.getNameIdentifier();
        return id != null ? id.getText() : "NO NAME";
    }

    @Nullable
    public PsiIdentifier getNameIdentifier() {
        PsiElement element = null;
        try {
            element = findChildByType(FanElementTypes.NAME_ELEMENT);
        } catch (Exception e) {
            logger.error("Error looking for name element for " + this, e);
        }
        if (element != null) {
            return new FanLightIdentifier(getManager(), getContainingFile(), element.getTextRange());
        }
        return null;
    }

    public String getQualifiedName() {
        return getPodName() + "::" + getName();
    }

    public PsiModifierList getModifierList() {
        return findChildByClass(FanModifierList.class);
    }

    public boolean hasModifierProperty(@Modifier final String name) {
        return getModifierList().hasModifierProperty(name);
    }

    @Nullable
    public Icon getIcon(final int flags) {
        final Icon icon = getIconInner();
        final boolean isLocked = (flags & ICON_FLAG_READ_STATUS) != 0 && !isWritable();
        final RowIcon rowIcon = ElementBase.createLayeredIcon(icon, PsiUtil.getFlags(this, isLocked));
        VisibilityIcons.setVisibilityIcon(getModifierList(), rowIcon);
        return rowIcon;
    }

    @NotNull
    public FanMethod[] getFanMethods() {
        if (fanMethods == null) {
            final FanSlot[] fanSlots = getSlots();
            final List<FanMethod> list = new ArrayList<FanMethod>();
            for (final FanSlot fanSlot : fanSlots) {
                if (fanSlot instanceof FanMethod) {
                    list.add((FanMethod) fanSlot);
                }
            }
            fanMethods = list.toArray(new FanMethod[0]);
        }
        return fanMethods;
    }

    @NotNull
    public FanField[] getFanFields() {
        if (fanFields == null) {
            final FanSlot[] fanSlots = getSlots();
            final List<FanField> list = new ArrayList<FanField>();
            for (final FanSlot fanSlot : fanSlots) {
                if (fanSlot instanceof FanField) {
                    list.add((FanField) fanSlot);
                }
            }
            fanFields = list.toArray(new FanField[0]);
        }
        return fanFields;
    }

    @NotNull
    public FanSlot[] getSlots() {
        if (fanSlots == null) {
            final List<FanSlot> slots = new ArrayList<FanSlot>();
            final PsiElement element = findChildByType(getBodyElementType());
            if (element != null) {
                final PsiElement[] bodyEls = element.getChildren();
                for (final PsiElement bodyEl : bodyEls) {
                    if (bodyEl instanceof FanSlot) {
                        slots.add((FanSlot) bodyEl);
                    }
                }
            }
            fanSlots = slots.toArray(new FanSlot[0]);
        }
        return fanSlots;
    }

    public FanField getFieldByName(final String name) {
        final FanField[] fields = getFanFields();
        for (final FanField field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public FanMethod getMethodByName(final String name) {
        final FanMethod[] methods = getFanMethods();
        for (final FanMethod method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    @NotNull
    public FanSlot[] getSlots(final String modifier) {
        final List<FanSlot> slots = new ArrayList<FanSlot>();
        for (final FanSlot slot : getSlots()) {
            if (slot.hasModifierProperty(modifier)) {
                slots.add(slot);
            }
        }
        return slots.toArray(new FanSlot[0]);
    }

    @NotNull
    public FanMethod[] getFanMethods(final String modifier) {
        final List<FanMethod> methods = new ArrayList<FanMethod>();
        for (final FanMethod method : getFanMethods()) {
            if (method.hasModifierProperty(modifier)) {
                methods.add(method);
            }
        }
        return methods.toArray(new FanMethod[0]);
    }

    @NotNull
    public FanField[] getFanFields(final String modifier) {
        final List<FanField> fields = new ArrayList<FanField>();
        for (final FanField field : getFanFields()) {
            if (field.hasModifierProperty(modifier)) {
                fields.add(field);
            }
        }
        return fields.toArray(new FanField[0]);
    }

    public String getJavaQualifiedName() {
        return "fan." + getPodName() + "." + getName();
    }

    public FanTypeDefinitionBody getBodyElement() {
        return (FanTypeDefinitionBody) findChildByType(getBodyElementType());
    }

    public PsiElement addMemberDeclaration(@NotNull final PsiElement decl, final PsiElement anchorBefore)
            throws IncorrectOperationException {

        final FanTypeDefinitionBody body = getBodyElement();
        if (body == null) {
            throw new IncorrectOperationException("Type definition without a body");
        }
        ASTNode anchorNode;
        if (anchorBefore != null) {
            anchorNode = anchorBefore.getNode();
        } else {
            final PsiElement child = body.getLastChild();
            assert child != null;
            anchorNode = child.getNode();
        }
        final ASTNode bodyNode = body.getNode();
        bodyNode.addChild(decl.getNode(), anchorNode);
        bodyNode.addLeaf(FanTokenTypes.WHITE_SPACE, " ", decl.getNode()); //add whitespaces before and after to hack over incorrect auto reformat
        bodyNode.addLeaf(FanTokenTypes.WHITE_SPACE, " ", anchorNode);
        return decl;
    }

    protected abstract Icon getIconInner();

    protected abstract IElementType getBodyElementType();
}
