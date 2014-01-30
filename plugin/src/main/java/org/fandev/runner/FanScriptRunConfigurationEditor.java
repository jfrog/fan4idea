package org.fandev.runner;

import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.module.Module;
import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.ui.FieldPanel;
import com.intellij.ui.RawCommandLineEditor;
import org.jetbrains.annotations.NotNull;
import org.fandev.lang.fan.FanFileType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 20, 2009 8:59:49 PM
 */
public class FanScriptRunConfigurationEditor extends FanRunConfigurationEditor {
    protected JPanel scriptPathPanel;
    protected JTextField scriptPathField;
        
    public FanScriptRunConfigurationEditor() {
        super();
    }

    protected void addExecutablePanel(final JPanel mainPanel) {
        // Script path
        scriptPathPanel = new JPanel(new GridLayout(0,1));
        scriptPathField = new JTextField();
        final BrowseFilesListener scriptBrowseListener = new BrowseFilesListener(scriptPathField,
                "Script Path",
                "Specify path to script",
                new FileChooserDescriptor(true, false, false, false, false, false) {
                    public boolean isFileSelectable(final VirtualFile file) {
                        return file.getFileType() instanceof FanFileType;
                    }
                });
        final FieldPanel scriptFieldPanel = new FieldPanel(scriptPathField, "Script path:", null, scriptBrowseListener, null);
        scriptPathPanel.add(scriptFieldPanel);
        mainPanel.add(scriptPathPanel, BorderLayout.CENTER);
    }

    @Override
    protected void resetEditorFrom(final FanRunConfiguration configuration) {
        super.resetEditorFrom(configuration);
        scriptPathField.setText(((FanScriptRunConfiguration)configuration).getScriptPath());
    }

    @Override
    protected void applyEditorTo(final FanRunConfiguration configuration) throws ConfigurationException {
        super.applyEditorTo(configuration);
        ((FanScriptRunConfiguration)configuration).setScriptPath(scriptPathField.getText());
    }
}
