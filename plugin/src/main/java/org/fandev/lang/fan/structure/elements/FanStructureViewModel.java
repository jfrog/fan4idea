package org.fandev.lang.fan.structure.elements;

import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiElement;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.structure.elements.impl.FanFileStructureViewElement;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:25:13 PM
 */
public class FanStructureViewModel extends TextEditorBasedStructureViewModel {
    private PsiElement myRoot;

    public FanStructureViewModel(@NotNull final PsiFile psiFile) {
        super(psiFile);
        myRoot = psiFile;
    }

    public FanStructureViewModel(final PsiElement psiFile, final Editor editor) {
        super(editor);
        myRoot = psiFile;
    }

    protected PsiFile getPsiFile() {
        return myRoot.getContainingFile();
    }

    @NotNull
    public StructureViewTreeElement getRoot() {
        return new FanFileStructureViewElement(myRoot);
    }

    @NotNull
    public Grouper[] getGroupers() {
        return Grouper.EMPTY_ARRAY;
    }

    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @NotNull
    public Filter[] getFilters() {
        return Filter.EMPTY_ARRAY;
    }
}
