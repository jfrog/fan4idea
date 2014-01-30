package org.fandev.sdk.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MultiLineLabelUI;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.ide.util.projectWizard.JdkChooserPanel;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.fandev.lang.fan.FanBundle;
import org.fandev.sdk.FanSdkType;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dror Bereznitsky
 * @date Jan 19, 2009 2:57:10 PM
 */
public class FanSdkChooserPanel extends JComponent {
    private JdkChooserPanel myJdkChooser;

    public FanSdkChooserPanel(final Project project) {
        myJdkChooser = new JdkChooserPanel(project);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEtchedBorder());

        final JLabel label = new JLabel(FanBundle.message("module.fan.select.sdk"));
        label.setUI(new MultiLineLabelUI());
        add(label, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(8, 10, 8, 10), 0, 0));

        final JLabel jdklabel = new JLabel(FanBundle.message("module.fan.prompt.label.project.sdk"));
        jdklabel.setFont(UIUtil.getLabelFont().deriveFont(Font.BOLD));
        add(jdklabel, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(8, 10, 0, 10), 0, 0));

        add(myJdkChooser, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 10, 10, 5), 0, 0));
        final JButton configureButton = new JButton(FanBundle.message("button.configure"));
        add(configureButton, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 0, 10, 5), 0, 0));

        configureButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                myJdkChooser.editJdkTable();
            }
        });

        myJdkChooser.setAllowedJdkTypes(new SdkType[]{FanSdkType.getInstance()});

        final Sdk selectedJdk = project == null
                ? null
                : ProjectRootManager.getInstance(project).getProjectJdk();
        myJdkChooser.updateList(selectedJdk, null);
    }

    @Nullable
    public Sdk getChosenJdk() {
        return myJdkChooser.getChosenJdk();
    }

    public JComponent getPreferredFocusedComponent() {
        return myJdkChooser;
    }

    public void selectSdk(@Nullable final Sdk sdk) {
        myJdkChooser.selectJdk(sdk);
    }
}
