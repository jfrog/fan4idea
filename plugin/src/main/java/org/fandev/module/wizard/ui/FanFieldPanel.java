package org.fandev.module.wizard.ui;

import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.InsertPathAction;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
 
public class FanFieldPanel extends FanAbstractFieldPanel
{
  private final JTextField myTextField;

  public FanFieldPanel()
  {
    this(new JTextField(30));
  }

  protected FanFieldPanel(final JTextField textField) {
    super(textField);
    this.myTextField = textField;
    createComponent();
  }

  public FanFieldPanel(final String labelText, final String viewerDialogTitle, final ActionListener browseButtonActionListener, final Runnable documentListener) {
    this(new JTextField(30), labelText, viewerDialogTitle, browseButtonActionListener, documentListener);
  }

  public FanFieldPanel(final JTextField textField, final String labelText, final String viewerDialogTitle, final ActionListener browseButtonActionListener, final Runnable documentListener) {
    super(textField, labelText, viewerDialogTitle, browseButtonActionListener, documentListener);
    this.myTextField = textField;
    createComponent();
  }

  public void createComponent() {
    super.createComponent();
    final ComponentWithBrowseButton.MyDoClickAction doClickAction = getDoClickAction();
    if (doClickAction != null) {
      doClickAction.registerShortcut(getTextField());
    }

    this.myTextField.getDocument().addDocumentListener(new DocumentAdapter() {
      public void textChanged(final DocumentEvent event) {
        if (FanFieldPanel.this.getChangeListener() != null) {
          FanFieldPanel.this.getChangeListener().run();
        }
      }
    });
  }

  public String getText()
  {
    return this.myTextField.getText();
  }

    public void setText(final String text) {
        this.myTextField.setText(text);
        myTextField.postActionEvent();
    }

  public JTextField getTextField() {
    return this.myTextField;
  }

  public static FanFieldPanel create(final String labelText, final String viewerDialogTitle) {
    return create(labelText, viewerDialogTitle, null, null);
  }

  public static FanFieldPanel withPaths(final String labelText, final String viewerDialogTitle) {
    return withPaths(labelText, viewerDialogTitle, null, null);
  }

  public static FanFieldPanel withPaths(final String labelText, final String viewerDialogTitle, final ActionListener browseButtonActionListener, final Runnable documentListener) {
    final FanFieldPanel fieldPanel = create(labelText, viewerDialogTitle, browseButtonActionListener, documentListener);
    InsertPathAction.addTo(fieldPanel.myTextField);
    return fieldPanel;
  }

  private static FanFieldPanel create(final String labelText, final String viewerDialogTitle, final ActionListener browseButtonActionListener, final Runnable documentListener) {
    return new FanFieldPanel(labelText, viewerDialogTitle, browseButtonActionListener, documentListener);
  }

  public void setEditable(final boolean editable) {
    this.myTextField.setEditable(editable);
    for (int i = 0; i < this.myButtons.size(); ++i) {
      final JButton button = (JButton)this.myButtons.get(i);
      button.setEnabled(editable);
    }
  }
}
