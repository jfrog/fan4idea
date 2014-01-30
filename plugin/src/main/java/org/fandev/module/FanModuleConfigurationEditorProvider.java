package org.fandev.module;

import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.roots.ui.configuration.DefaultModuleConfigurationEditorFactory;
import org.jetbrains.annotations.NotNull;
import org.fandev.module.ui.roots.PodEditor;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Dror Bereznitsky
 * @date Jan 22, 2009 11:56:01 PM
 */
public class FanModuleConfigurationEditorProvider implements ModuleComponent, ModuleConfigurationEditorProvider {
    public static final String COMPONENT_NAME = "FanModuleConfigurationEditorProvider";

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

     public ModuleConfigurationEditor[] createEditors(final ModuleConfigurationState state) {
      //final DefaultModuleConfigurationEditorFactory editorFactory = DefaultModuleConfigurationEditorFactory.getInstance();
      final List<ModuleConfigurationEditor> editors = new ArrayList<ModuleConfigurationEditor>();

      editors.add(PodEditor.createPodEditor(state));
      //editors.add(editorFactory.createModuleContentRootsEditor(state));
      //editors.add(editorFactory.createOutputEditor(state));
      //editors.add(editorFactory.createClasspathEditor(state));

      return editors.toArray(new ModuleConfigurationEditor[0]);
    }

    public void projectOpened() {}

    public void projectClosed() {}

    public void moduleAdded() {}

    public void initComponent() {}

    public void disposeComponent() {}
}
