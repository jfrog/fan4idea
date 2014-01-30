package org.fandev.runner;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.ui.FieldPanel;

import javax.swing.*;
import java.awt.*;

import org.fandev.lang.fan.FanFileType;

/**
 * Date: Sep 5, 2009
 * Time: 11:43:28 PM
 *
 * @author Dror Bereznitsky
 */
public class FanPodRunConfigurationEditor extends FanRunConfigurationEditor {
    protected JPanel typePanel;
    protected JTextField typeField;

    public FanPodRunConfigurationEditor() {
        super();
    }

    protected void addExecutablePanel(final JPanel mainPanel) {
        typePanel = new JPanel(new GridLayout(0,1));
        typeField = new JTextField();
        final FieldPanel scriptFieldPanel = new FieldPanel(typeField, "Type:", null, null, null);
        typePanel.add(scriptFieldPanel);
        mainPanel.add(typePanel, BorderLayout.CENTER);
    }

    @Override
    protected void resetEditorFrom(final FanRunConfiguration configuration) {
        super.resetEditorFrom(configuration);
        typeField.setText(((FanPodRunConfiguration)configuration).getExecutableType());
    }

    @Override
    protected void applyEditorTo(final FanRunConfiguration configuration) throws ConfigurationException {
        super.applyEditorTo(configuration);
        ((FanPodRunConfiguration)configuration).setExecutableType(typeField.getText());
    }
}
