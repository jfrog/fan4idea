package org.fandev.module.wizard.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.util.Pair;
import com.intellij.util.Icons;
import org.fandev.utils.FanUtil;
import org.fandev.utils.TextUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * Modified version of com.intellij.ui.AbstractFieldPanel, disassembled by jd.
 * Modification is, we do not have a file browser embedded by a index/meta property editor. 
 */
public abstract class FanAbstractFieldPanel extends JPanel
{
    private static final Logger LOG = Logger.getInstance("#org.fandev.module.wizard.ui");
    private final JTextField myComponent;
    private Runnable myChangeListener;
    protected ArrayList myButtons;
    protected JLabel myLabel;
    private ActionListener myBrowseButtonActionListener;
    private String myViewerDialogTitle;
    private String myLabelText;
    private ComponentWithBrowseButton.MyDoClickAction myDoClickAction;

    public FanAbstractFieldPanel(final JTextField component)
    {
        this(component, null, null, null, null);
    }

    public FanAbstractFieldPanel(final JTextField component, final String labelText, final String viewerDialogTitle, final ActionListener browseButtonActionListener, final Runnable changeListener)
    {
        this.myButtons = new ArrayList(1);

        this.myComponent = component;
        setChangeListener(changeListener);
        setLabelText(labelText);
        setBrowseButtonActionListener(browseButtonActionListener);
        this.myViewerDialogTitle = viewerDialogTitle;
    }

    public abstract String getText();

    public abstract void setText(final String paramString);

    public void setEnabled(final boolean enabled)
    {
        getComponent().setEnabled(enabled);
        if (this.myLabel != null) {
            this.myLabel.setEnabled(enabled);
        }
        for (int i = 0; i < this.myButtons.size(); ++i) {
            final JButton button = (JButton)this.myButtons.get(i);
            button.setEnabled(enabled);
        }
    }

    public boolean isEnabled() {
        return getComponent().isEnabled();
    }

    protected ComponentWithBrowseButton.MyDoClickAction getDoClickAction() {
        return this.myDoClickAction;
    }

    public final JTextField getComponent() {
        return this.myComponent;
    }

    public final JLabel getFieldLabel() {
        if (this.myLabel == null) {
            this.myLabel = new JLabel(this.myLabelText);
            add(this.myLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 0), 0, 0));
            this.myLabel.setLabelFor(getComponent());
        }
        return this.myLabel;
    }

    public final Runnable getChangeListener() {
        return this.myChangeListener;
    }

    public final void setChangeListener(final Runnable runnable) {
        this.myChangeListener = runnable;
    }

    public JButton[] getButtons() {
        return ((JButton[])(JButton[])this.myButtons.toArray(new JButton[0]));
    }

    public void createComponent() {
        removeAll();
        setLayout(new GridBagLayout());

        if (this.myLabelText != null) {
            this.myLabel = new JLabel(this.myLabelText);
            add(this.myLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 0), 0, 0));
            this.myLabel.setLabelFor(this.myComponent);
        }

        add(this.myComponent, new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(0, 0, 0, 0), 0, 0));

        if (this.myBrowseButtonActionListener != null) {
            final FixedSizeButton browseButton = new FixedSizeButton(getComponent());
            this.myDoClickAction = new ComponentWithBrowseButton.MyDoClickAction(browseButton);
            browseButton.setFocusable(false);
            //myComponent.addActionListener(myBrowseButtonActionListener);
            //browseButton.addActionListener(this.myBrowseButtonActionListener);
            this.myButtons.add(browseButton);
            add(browseButton, new GridBagConstraints(-1, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 2, 0, 0), 0, 0));
            browseButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final FanAbstractFieldPanel.Viewer viewer = new FanAbstractFieldPanel.Viewer();
                    viewer.setTitle(FanAbstractFieldPanel.this.myViewerDialogTitle);
                    viewer.show();
                }
            });
        }
    }

    public void setBrowseButtonActionListener(final ActionListener browseButtonActionListener) {
        this.myBrowseButtonActionListener = browseButtonActionListener;
    }

    public void setViewerDialogTitle(final String viewerDialogTitle) {
        this.myViewerDialogTitle = viewerDialogTitle;
    }

    public void setLabelText(final String labelText) {
        this.myLabelText = labelText;
    }

    public void setDisplayedMnemonic(final char c) {
        getFieldLabel().setDisplayedMnemonic(c);
    }

    public void setDisplayedMnemonicIndex(final int i) {
        getFieldLabel().setDisplayedMnemonicIndex(i);
    }

    protected class Viewer extends DialogWrapper {
        private JButton addButton;
        private JButton deleteButton;
        private JList pairList;
        private JScrollPane pairScrollPane;
        private JSeparator sep;
        private JTextField keyTextField;
        private JTextField valueTextField;
        private DefaultListModel listModel;
        private List<Pair<String,String>> list;

        public Viewer() {
            super(FanAbstractFieldPanel.this.getComponent(), true);
            list = FanUtil.stringToListPair(FanAbstractFieldPanel.this.getText());
            init();
        }

        protected Action[] createActions() {
            return new Action[] { getOKAction(), getCancelAction() };
        }

        public JTextField getPreferredFocusedComponent() {
            return this.keyTextField;
        }

        protected void doOKAction() {
            getListPair();
            FanAbstractFieldPanel.this.setText(FanUtil.listPairToString(list));
            super.doOKAction();
        }

        protected JComponent createCenterPanel() {
            final JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

            final JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

            final JPanel topLeftPanel = new JPanel();
            keyTextField = new JTextField(30);
            valueTextField = new JTextField(30);
            final JLabel colon = new JLabel(":", SwingConstants.CENTER);
            topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.LINE_AXIS));
            topLeftPanel.add(keyTextField);
            topLeftPanel.add(colon);
            topLeftPanel.add(valueTextField);
            leftPanel.add(topLeftPanel);

            sep = new JSeparator(SwingConstants.HORIZONTAL);
            leftPanel.add(sep);

            listModel = new DefaultListModel();
            populateListModel();
            pairList = new JList(listModel);
            pairList.setLayoutOrientation(SwingConstants.VERTICAL);
            pairList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            pairList.setVisibleRowCount(-1);
            pairScrollPane = new JScrollPane(pairList);
            pairScrollPane.setPreferredSize(new Dimension(80,80));
            leftPanel.add(pairScrollPane);

            final JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
            addButton = new JButton("Add");
            deleteButton = new JButton("Delete");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (keyTextField.getText() != null && !keyTextField.getText().equals("") &&
                        valueTextField.getText() != null && !valueTextField.getText().equals("")) {
                            final String key = keyTextField.getText().trim();
                            final String value = valueTextField.getText().trim();
                            listModel.addElement(key + " : " + value);
                            deleteButton.setEnabled(true);
                        }
                }
            });
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    int index = pairList.getSelectedIndex();
                    listModel.remove(index);
                    int size = listModel.getSize();
                    if (size ==0) {
                        deleteButton.setEnabled(false);
                    }
                    else {
                        if (index == listModel.getSize()) {
                            index--;
                        }
                        pairList.setSelectedIndex(index);
                        pairList.ensureIndexIsVisible(index);
                    }
                }
            });
            rightPanel.add(addButton);
            rightPanel.add(deleteButton);

            panel.add(leftPanel);
            panel.add(rightPanel);
            panel.setVisible(true);
            new AnAction() {
                public void actionPerformed(final AnActionEvent e) {
                    FanAbstractFieldPanel.Viewer.this.doOKAction();
                }
            }
            .registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(10, 0)), this.keyTextField);

            return panel;
        }
        public Icon getIcon() {
            return org.fandev.icons.Icons.FAN_16;
        }

        private void populateListModel() {
            listModel.clear();
            for (final Pair<String,String> pair : list) {
                final StringBuilder sb = new StringBuilder();
                sb.append(pair.getFirst());
                sb.append(" : ");
                sb.append(pair.getSecond());
                listModel.addElement(sb.toString());
            }
        }
        private void getListPair() {
            list.clear();
            final Enumeration<?> e = listModel.elements();
            while (e.hasMoreElements()) {
                final String s = (String) e.nextElement();
                if (!TextUtil.isEmpty(s)) {
                    final String[] p = s.split(":{1}");
                    if (p.length > 1) {
                        String t = p[1];
                        if (p.length > 2) {
                            final StringBuilder sb = new StringBuilder();
                            for (int i = 1; i< p.length; i++) {
                                if (TextUtil.isEmpty(p[i])) {
                                    sb.append("::");
                                } else {
                                    sb.append(p[i]);
                                }
                            }
                            t = sb.toString();
                        }
                        final Pair<String,String> pair = new Pair<String,String>(p[0].trim(),t.trim());
                        list.add(pair);
                    }
                }
            }
        }
    }
}
