package org.fandev.module.wizard.ui;

import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.ui.AbstractFieldPanel;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: bheadley
 * Date: Mar 10, 2010
 * Time: 5:59:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxFieldPanel extends AbstractFieldPanel {
    private final JCheckBox myCheckBox;
    public CheckBoxFieldPanel(final JCheckBox checkBox, final String labelText) {
        super(checkBox, labelText, null,null,null);
        myCheckBox = checkBox;
        createComponent();
    }
    public void createComponent() {
        super.createComponent();
        final ComponentWithBrowseButton.MyDoClickAction doClickAction = getDoClickAction();
        if (doClickAction != null) {
           doClickAction.registerShortcut(getCheckBox());
        }
    }
    public boolean isSelected() {
        return myCheckBox.isSelected();
    }
    public void setSelected(final boolean value) {
        myCheckBox.setSelected(value);
    }
    public JCheckBox getCheckBox() {
        return myCheckBox;
    }
    public String getText() {
        return "";
    }
    public void setText(final String s) {
        // no-op
    }
}
