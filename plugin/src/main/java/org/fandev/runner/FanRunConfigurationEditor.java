package org.fandev.runner;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.RawCommandLineEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Date: Sep 5, 2009
 * Time: 11:40:21 PM
 *
 * @author Dror Bereznitsky
 */
public abstract class FanRunConfigurationEditor extends SettingsEditor<FanRunConfiguration> {
    protected JPanel myMainPanel;
    protected JPanel modulesPanel;
    protected RawCommandLineEditor executionParametersField;
    protected DefaultComboBoxModel myModulesModel;
    protected JComboBox myModulesBox;

    public FanRunConfigurationEditor() {
        myMainPanel = new JPanel(new GridLayout(4, 1));

        addExectuionParamsSection(myMainPanel);
        addExecutablePanel(myMainPanel);
        addModulePanel(myMainPanel);
    }

    protected abstract void addExecutablePanel(final JPanel mainPanel);

    protected void addModulePanel(final JPanel mainPanel) {
        // Modules
        modulesPanel = new JPanel(new GridLayout(2, 1));
        myModulesBox = new JComboBox();
        myModulesModel = new DefaultComboBoxModel();
        modulesPanel.add(new JLabel("Choose classpath and sdk from module"));
        modulesPanel.add(myModulesBox);
        mainPanel.add(modulesPanel, BorderLayout.SOUTH);
    }

    protected void addExectuionParamsSection(final JPanel mainPanel) {
        final JLabel executionParametersLabel = new JLabel("Execution Parameters:");
        executionParametersField = new RawCommandLineEditor();
        executionParametersField.attachLabel(executionParametersLabel);
        executionParametersField.setDialogCaption("Execution Parameters");
        myMainPanel.add(executionParametersLabel, BorderLayout.CENTER);
        myMainPanel.add(executionParametersField, BorderLayout.CENTER);
    }

    protected void resetEditorFrom(final FanRunConfiguration configuration) {
        executionParametersField.setText(configuration.getExecutionParameters());
        myModulesModel.removeAllElements();
        for (final Module module : configuration.getValidModules()) {
            myModulesModel.addElement(module);
        }
    }

    protected void applyEditorTo(final FanRunConfiguration configuration) throws ConfigurationException {
        configuration.setExecutionParameters(executionParametersField.getText());
        configuration.setModule((Module) myModulesBox.getSelectedItem());
    }

    @NotNull
    protected JComponent createEditor() {
        myModulesBox.setModel(myModulesModel);

        myModulesBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                final Module module = (Module) value;
                if (module != null) {
                    setIcon(module.getModuleType().getNodeIcon(false));
                    setText(module.getName());
                }
                return this;
            }
        });

        return myMainPanel;
    }

    protected void disposeEditor() {

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
