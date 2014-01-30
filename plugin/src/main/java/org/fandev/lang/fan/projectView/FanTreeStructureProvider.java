package org.fandev.lang.fan.projectView;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.ide.projectView.impl.nodes.NamedLibraryElement;
import com.intellij.ide.projectView.impl.nodes.LibraryGroupNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiElement;
import com.intellij.codeInsight.CodeInsightBundle;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;

import org.fandev.index.FanIndex;
import org.fandev.lang.fan.psi.FanFile;

/**
 * Date: Mar 13, 2009
 * Time: 4:09:00 PM
 * @author Dror Bereznitsky
 */
public class FanTreeStructureProvider implements SelectableTreeStructureProvider {
    private Project myProject;
    private FanIndex fanIndex;

    public FanTreeStructureProvider(final Project project) {
        myProject = project;
        this.fanIndex = (FanIndex) project.getComponent(FanIndex.COMPONENT_NAME);
    }

    public Collection<AbstractTreeNode> modify(final AbstractTreeNode parent, final Collection<AbstractTreeNode> children, final ViewSettings settings) {
        final Collection<AbstractTreeNode> result = new ArrayList<AbstractTreeNode>();
        // Add pod types to library tree view
        if (parent != null) {
            Object o = parent.getValue();
            if (o instanceof NamedLibraryElement) {
                addLibraryTypes(settings, result, o);
            }
        }

        result.addAll(children);
        return result;
    }

    private void addLibraryTypes(final ViewSettings settings, final Collection<AbstractTreeNode> result, final Object o) {
        final NamedLibraryElement libraryElement = (NamedLibraryElement) o;
        final Set<PsiFile> types = fanIndex.getLibraryPsiFiles(libraryElement.getName());
        for (final PsiFile typeVirtualFile : types) {
            final FanFile psiFile = (FanFile) typeVirtualFile;
            final PsiClass[] classes = psiFile.getClasses();
            for (final PsiClass aClass : classes) {
                result.add(new FanTypeTreeNode(myProject, aClass, settings));
            }
        }
    }

    public Object getData(final Collection<AbstractTreeNode> selected, final String dataName) {
        return null;
    }

    public PsiElement getTopLevelElement(final PsiElement element) {
        return null;
    }

    private class FanTypeTreeNode extends ClassTreeNode {
        private FanTypeTreeNode(final Project project, final PsiClass psiClass, final ViewSettings viewSettings) {
            super(project, psiClass, viewSettings);
        }

        // original calcTooltip causes a major slow down
        @Override
        protected String calcTooltip() {
            return ""; //TODO [Dror] return a proper tooltip value
        }
    }
}
