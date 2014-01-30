package org.fandev.unscramble;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.unscramble.UnscrambleDialog;
import org.fandev.lang.fan.FanBundle;
import org.fandev.runner.FanTypeFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Date: Sep 23, 2009
 * Time: 11:30:32 PM
 *
 * @author Dror Bereznitsky
 */
public class FanUnscrambleDialog extends DialogWrapper {
    private Project project;
    private JPanel stackTracePanel;
    private JTextArea strackTraceArea;

    protected FanUnscrambleDialog(final Project project) {
        super(false);
        this.project = project;

        setTitle(FanBundle.message("unscramble.dialog.title", new Object[0]));
        init();
    }

    protected JComponent createCenterPanel() {
        stackTracePanel = new JPanel(new BorderLayout());
        strackTraceArea = new JTextArea(20, 70);
        final JScrollPane scrollPane = new JScrollPane(strackTraceArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        stackTracePanel.add(scrollPane, BorderLayout.CENTER);

        setText(FanUnscrambleDialog.getTextInClipboard());
        return stackTracePanel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

        // Create the console
        final ConsoleView consoleView =
                TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        consoleView.addMessageFilter(new FanTypeFilter(project));
        consoleView.print(getText(), ConsoleViewContentType.NORMAL_OUTPUT);

        final DefaultActionGroup localDefaultActionGroup = new DefaultActionGroup();

        // Create the console panel
        final MyConsolePanel consolePanel = new MyConsolePanel(consoleView, localDefaultActionGroup);
        
        final RunContentDescriptor runContentDescriptor = new RunContentDescriptor(consoleView, null, consolePanel, FanBundle.message("unscramble.unscrambled.stacktrace.tab", new Object[0])) {
            public boolean isContentReuseProhibited() {
                return true;
            }
        };
        final Executor localExecutor = DefaultRunExecutor.getRunExecutorInstance();

        localDefaultActionGroup.add(new CloseAction(localExecutor, runContentDescriptor, project));

        final AnAction[] arrayOfAnAction = consoleView.createConsoleActions();
        int i1 = arrayOfAnAction.length;
        for (int i2 = 0; i2 < i1; ++i2) {
            final AnAction localAnAction = arrayOfAnAction[i2];
            localDefaultActionGroup.add(localAnAction);
        }

        ExecutionManager.getInstance(project).getContentManager().showRunContent(localExecutor, runContentDescriptor);
    }

    protected void setText(final String text) {
        strackTraceArea.setText(text);
    }

    protected String getText() {
        return strackTraceArea.getText();
    }

    public static String getTextInClipboard() {
        String str = null;
        try {
            final Transferable localTransferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(UnscrambleDialog.class);

            if (localTransferable != null) {
                str = (String) localTransferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception localException) {
        }
        return str;
    }

    private static final class MyConsolePanel extends JPanel {
        public MyConsolePanel(final ExecutionConsole executionConsole, final ActionGroup actionGroup) {
            super(new BorderLayout());

            final JPanel panel = new JPanel(new BorderLayout());

            panel.add(ActionManager.getInstance().createActionToolbar("unknown", actionGroup, false).getComponent());

            add(panel, BorderLayout.WEST);

            add(executionConsole.getComponent(), BorderLayout.CENTER);
        }
    }
}
