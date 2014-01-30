package org.fandev.module.wizard.ui;

import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.ide.util.projectWizard.JdkChooserPanel;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.projectImport.ProjectImportWizardStep;
import com.intellij.ui.FieldPanel;
import org.fandev.lang.fan.FanBundle;
import org.fandev.lang.fan.FanFileType;
import org.fandev.sdk.FanSdkType;
import org.fandev.module.wizard.FanProjectBuilder;
import org.fandev.utils.TextUtil;
import org.fandev.utils.VirtualFileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Date: Sep 20, 2009
 * Time: 11:06:00 PM
 *
 * @author Dror Bereznitsky
 */
public class FanProjectImportStep extends ProjectImportWizardStep {
    protected JPanel myMainPanel;
    private JPanel buildScriptPathPanel;
    private JTextField buildScriptPathField;
    private JdkChooserPanel sdkChooserPanel;

    private static final Logger logger = Logger.getInstance("Project import");

    public FanProjectImportStep(final WizardContext wizardContext) {
        super(wizardContext);
    }

    public JComponent getComponent() {
        final Project project = getWizardContext().getProject();
                
        myMainPanel = new JPanel(new BorderLayout());

        buildScriptPathPanel = new JPanel(new GridLayout(1, 1));

        // Script path
        buildScriptPathField = new JTextField();

        final FileChooserDescriptor fileChooser = new FileChooserDescriptor(true, false, false, false, false, false) {
            public boolean isFileSelectable(final VirtualFile file) {
                return file.getFileType() instanceof FanFileType;
            }
        };

        if (!getWizardContext().isCreatingNewProject()) {
            fileChooser.setRoot(project.getBaseDir());
        }

        final BrowseFilesListener scriptBrowseListener = new BrowseFilesListener(buildScriptPathField,
                FanBundle.message("build.script.path"),
                FanBundle.message("build.script.path.description"),
                fileChooser) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                super.actionPerformed(e);
            }
        };
        final FieldPanel scriptFieldPanel =
                new FieldPanel(buildScriptPathField, FanBundle.message("build.script.path"), null, scriptBrowseListener,
                        null);
        buildScriptPathPanel.add(scriptFieldPanel);
        myMainPanel.add(buildScriptPathPanel, BorderLayout.PAGE_START);

        final JPanel sdkPanel = new JPanel(new BorderLayout());
        final JLabel label = new JLabel(FanBundle.message("module.fan.select.sdk"));
        sdkChooserPanel = new JdkChooserPanel(project);
        sdkChooserPanel.setAllowedJdkTypes(new SdkType[]{FanSdkType.getInstance()});

        final Sdk selectedJdk = project == null
                ? null
                : ProjectRootManager.getInstance(project).getProjectJdk();
        sdkChooserPanel.updateList(selectedJdk, FanSdkType.getInstance());
        sdkPanel.add(label, BorderLayout.PAGE_START);
        sdkPanel.add(sdkChooserPanel, BorderLayout.CENTER);
        myMainPanel.add(sdkPanel, BorderLayout.CENTER);
       
        return myMainPanel;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return buildScriptPathField;
    }

    @Override
    public boolean validate() throws ConfigurationException {
        if (TextUtil.isEmpty(buildScriptPathField.getText())) {
            throw new ConfigurationException("Please, specify build script for Fantom project to import");
        }
        if (sdkChooserPanel.getChosenJdk() == null) {
            throw new ConfigurationException("Please, specify a Fantom SDK for Fantom project to import");
        }
        return true;
    }

    public void updateDataModel() {
        final String buildScriptPath = buildScriptPathField.getText();
        final Sdk sdk = sdkChooserPanel.getChosenJdk();
        final FanProjectBuilder builder = (FanProjectBuilder)getBuilder();
        builder.setBuildScriptPath(buildScriptPath);
        builder.setSdk(sdk);

        final VirtualFile buildScriptFile = VirtualFileUtil.findFileByLocalPath(buildScriptPath);
        if (buildScriptFile != null) {
            final VirtualFile parent = buildScriptFile.getParent();
            suggestProjectNameAndPath(parent.getPath(), parent.getName());            
        }
    }
}
