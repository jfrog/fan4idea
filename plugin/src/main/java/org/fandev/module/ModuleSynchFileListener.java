package org.fandev.module;

import com.intellij.openapi.vfs.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.LightColors;
import org.fandev.lang.fan.FanBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Dror Bereznitsky
 * @date Feb 23, 2009 11:18:52 PM
 */
public class ModuleSynchFileListener extends VirtualFileAdapter {
    private VirtualFile listeningTo;
    private Module module;


    private static final Key<JComponent> PANEL_KEY = new Key<JComponent>("ModuleSynchFileListener");

    public ModuleSynchFileListener(final VirtualFile podFile, final Module module) {
        this.listeningTo = podFile;
        this.module = module;
    }

    @Override
    public void contentsChanged(final VirtualFileEvent virtualFileEvent) {
        if (virtualFileEvent.getFile().equals(listeningTo)) {
            for (final FileEditor e : getFileEditorManager().getEditors(listeningTo)) {
                if (e.getUserData(PANEL_KEY) != null){
                    continue;
                }

                final JComponent panel = createNotifierPanel();
                e.putUserData(PANEL_KEY, panel);
                getFileEditorManager().addTopComponent(e, panel);
            }
        }
    }

    private JComponent createNotifierPanel() {
        final JPanel panel =new JPanel(new BorderLayout());
        panel.setBackground(LightColors.YELLOW);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

        final JButton button = new JButton(FanBundle.message("button.synchronize"));

        panel.add(new JLabel(FanBundle.message("pod.build.file.changed")), BorderLayout.WEST);
        panel.add(button, BorderLayout.EAST);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        synchModule();
                    }
                });              
            }
        });

        return panel;
    }

    private void synchModule() {
        try {
            FanModuleSettings.getInstance(module).synchModule();
        } finally {
            resetNotificationPanel();
        }
    }

    private void resetNotificationPanel() {
        for (final FileEditor e : getFileEditorManager().getEditors(listeningTo)) {
            final JComponent panel = e.getUserData(PANEL_KEY);
            if (panel == null) {
                continue;
            }
            getFileEditorManager().removeTopComponent(e, panel);
            e.putUserData(PANEL_KEY, null);
        }
    }

    private FileEditorManager getFileEditorManager() {
        return FileEditorManager.getInstance(module.getProject());
    }
}
