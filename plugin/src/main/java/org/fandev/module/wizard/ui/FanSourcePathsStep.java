package org.fandev.module.wizard.ui;

import com.intellij.ide.util.newProjectWizard.SourcePathsStep;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import org.fandev.module.wizard.FanModuleBuilder;
import org.fandev.module.FanModuleSettings;
import org.fandev.utils.FanUtil;
import org.fandev.utils.VirtualFileUtil;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dror Bereznitsky
 * @date Jan 27, 2009 12:23:16 AM
 */
public class FanSourcePathsStep extends SourcePathsStep {
    private FanModuleBuilder mySettingsHolder;
    private String suggestedSource;

    public FanSourcePathsStep(final FanModuleBuilder sourcePathsBuilder, final Icon icon, @org.jetbrains.annotations.NonNls final String s) {
        super(sourcePathsBuilder, icon, s);
        mySettingsHolder = sourcePathsBuilder;
        suggestedSource = s;
        mySettingsHolder.setSourcePaths(mySettingsHolder.getModulePod().getSrcDirs());
    }

    @Override
    protected String suggestSourceDirectoryName() {
        return suggestedSource;
    }
    @Override
    public void updateDataModel() {
        super.updateDataModel();
        final List<Pair<String,String>> spList = mySettingsHolder.getSourcePaths();
        final List<Pair<String,String>> srcList = new LinkedList<Pair<String,String>>();
        for (final Pair<String,String> p : spList) {
            final VirtualFile v = VirtualFileUtil.refreshAndFindFileByLocalPath(p.getFirst());
            final String s = v.getName();
            srcList.add(new Pair<String,String>(s, s));
        }
        mySettingsHolder.getModulePod().setSrcDirs(srcList);
    }

    /*@Override
    protected void onFinished(List<Pair<String, String>> pairs, boolean b) {
        super.onFinished(pairs, b);
        // TODO [Bryan] Moved over to FanModuleBuilder.commitModule()
        //TODO [Dror] find a better place to perform the build script generation
        //mySettingsHolder.getModulePod().setBuildScriptFile(FanUtil.generateBuildScript(mySettingsHolder, project, this));
    }*/
}
