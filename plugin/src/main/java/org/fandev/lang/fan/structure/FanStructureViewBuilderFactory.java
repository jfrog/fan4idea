package org.fandev.lang.fan.structure;

import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.structure.elements.FanStructureViewModel;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:23:57 PM
 */
public class FanStructureViewBuilderFactory implements PsiStructureViewFactory {
    public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile) {
        return new TreeBasedStructureViewBuilder() {
            @NotNull
            public StructureViewModel createStructureViewModel() {
                return new FanStructureViewModel(psiFile);
            }
        };
    }
}
