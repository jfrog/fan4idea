package org.fandev.module.wizard.ui;

import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.FieldPanel;

import javax.swing.*;

import org.fandev.module.pod.PodModel;
import org.fandev.lang.fan.FanBundle;
import org.fandev.module.wizard.FanModuleBuilder;
import org.fandev.utils.FanUtil;
import org.fandev.utils.PodUtil;
import org.fandev.utils.VirtualFileUtil;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Dror Bereznitsky
 * @date Jan 26, 2009 2:09:05 PM
 */
//TODO have proper binding between view and model
public class FanPodCreatePanel extends JComponent {
    private final JLabel messageLabel;
    private final JTextField podName;
    private final JTextField version;
    private final JTextField podDescription;
    private final JCheckBox docApi;
    private final JCheckBox docSrc;
    private final JTextField indexes;
    private final JTextField outDir;
    private final JTextField metas;
    private final JTextField dependencies;
    private final JTextField srcDirs;
    private final JTextField resDirs;
    private PodModel podModel;

    public FanPodCreatePanel(final FanModuleBuilder builder) {
        podModel = builder.getModulePod();
        VirtualFile moduleFile = null;
        if (builder.getModuleFileDirectory() != null) {
            moduleFile = VirtualFileUtil.refreshAndFindFileByLocalPath(builder.getModuleFileDirectory());
        }
        
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());

        final JPanel myMainPanel = new JPanel();
        myMainPanel.setLayout(new BoxLayout(myMainPanel,BoxLayout.PAGE_AXIS));

        messageLabel = new JLabel();

        podName = new JTextField(podModel.getName());
        version = new JTextField(podModel.getVersion());
        podDescription = new JTextField(podModel.getDescription());
        srcDirs = new JTextField(FanUtil.listPairToFirstString(podModel.getSrcDirs()));
        resDirs = new JTextField(FanUtil.listPairToFirstString(podModel.getResDirs()));
        dependencies = new JTextField(FanUtil.listToString(podModel.getDependencies()));
        outDir = new JTextField(podModel.getOutDir());
        docApi = new JCheckBox("", podModel.getDocApi());
        docSrc = new JCheckBox("", podModel.getDocSrc());
        indexes = new JTextField(FanUtil.listPairToString(podModel.getIndexes()));
        metas = new JTextField(FanUtil.listPairToString(podModel.getMetas()));

        // actionListeners by the dozen
        podName.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModel.setName(podName.getText());
            }
        });
        version.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModel.setVersion(version.getText());
            }
        });
        podDescription.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModel.setDescription(podDescription.getText());
            }
        });
        docApi.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModel.setDocApi(docApi.isSelected());
            }
        });
        docSrc.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModel.setDocSrc(docSrc.isSelected());
            }
        });
        final ActionListener indexesListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModel.setIndexes(FanUtil.stringToListPair(indexes.getText()));
            }
        };
        indexes.addActionListener(indexesListener);
        final ActionListener metasListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModel.setMetas(FanUtil.stringToListPair(metas.getText()));
            }
        };
        metas.addActionListener(metasListener);

        // We have the same-style file browsers here as in PodEditor, for the same reason.
        // BrowseFileListener is broken insofar as it does not handle multiple file selections.
        final FileChooserDescriptor srcDirsChooser = new FileChooserDescriptor(false, true, false, false, false, true) {
            public boolean isFileSelectable(final VirtualFile file) {
                return file.isDirectory();
            }
        };
        if (moduleFile != null) {
            srcDirsChooser.setRoot(moduleFile);
        }
        final BrowseFilesListener srcDirsBrowseListener = new BrowseFilesListener(srcDirs,
                FanBundle.message("src.dirs.path"),
                FanBundle.message("src.dirs.path.description"),
                srcDirsChooser) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final VirtualFile fileToSelect = getFileToSelect();
                final VirtualFile[] files = (fileToSelect != null)
                        ? FileChooser.chooseFiles(srcDirs, srcDirsChooser, fileToSelect)
                        : FileChooser.chooseFiles(srcDirs, srcDirsChooser);
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < files.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    final String file = files[i].getName();
                    sb.append(file);
                    if (!file.endsWith("/")) {
                        sb.append('/');
                    }
                }
                srcDirs.setText(sb.toString());
            }
        };

        final FileChooserDescriptor resDirsChooser = new FileChooserDescriptor(false, true, false, false, false, true) {
            public boolean isFileSelectable(final VirtualFile file) {
                return file.isDirectory();
            }
        };
        if (moduleFile != null) {
            resDirsChooser.setRoot(moduleFile);
        }
        final BrowseFilesListener resDirsBrowseListener = new BrowseFilesListener(resDirs,
                FanBundle.message("res.dirs.path"),
                FanBundle.message("res.dirs.path.description"),
                resDirsChooser) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final VirtualFile fileToSelect = getFileToSelect();
                final VirtualFile[] files = (fileToSelect != null)
                        ? FileChooser.chooseFiles(resDirs, resDirsChooser, fileToSelect)
                        : FileChooser.chooseFiles(resDirs, resDirsChooser);
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < files.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    final String file = files[i].getName();
                    sb.append(file);
                    if (!file.endsWith("/")) {
                        sb.append('/');
                    }
                }
                resDirs.setText(sb.toString());
            }
        };

        // We did not need to have the same ornate callback, but: I wanted to make
        // sure that we placed the directory's relative name, and that it furthermore
        // has a trailing "/".
        final FileChooserDescriptor outDirChooser = new FileChooserDescriptor(false, true, false, false, false, false) {
            public boolean isFileSelectable(final VirtualFile file) {
                return file.isDirectory();
            }
        };
        if (moduleFile != null) {
            outDirChooser.setRoot(moduleFile);
        }
        final BrowseFilesListener outDirBrowseListener = new BrowseFilesListener(outDir,
                FanBundle.message("out.dir.path"),
                FanBundle.message("out.dir.path.description"),
                outDirChooser) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final VirtualFile fileToSelect = getFileToSelect();
                final VirtualFile[] files = (fileToSelect != null)
                        ? FileChooser.chooseFiles(outDir, outDirChooser, fileToSelect)
                        : FileChooser.chooseFiles(outDir, outDirChooser);
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < files.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    final String file = files[i].getName();
                    sb.append(file);
                    if (!file.endsWith("/")) {
                        sb.append('/');
                    }
                }
                outDir.setText(sb.toString());
            }
        };

        // Dependencies callback is wild because we open up every pod file chosen,
        // pull out its version ID, and ensure that only the name of the file (without
        // extension) is posted to th textfield.
        final FileChooserDescriptor dependenciesChooser = new FileChooserDescriptor(true, false, false, false, false, true) {
            public boolean isFileSelectable(final VirtualFile file) {
                return file.getExtension().equals("pod");
            }
        };
        if (moduleFile != null) {
            // we want a list of pod files, both from our "outDir" and from the sdk's lib/fan
            // directory. Now, here we're stuck: since I allow a string to contain outDir(),
            // it COULD be a Fan expression. Why not; build.fan is source code, not metadata!
            // I'll just pretend it has a legit directory. If I am wrong, LOL, too bad!
            final VirtualFile v = moduleFile.findChild(podModel.getOutDir());
            dependenciesChooser.setRoot(VirtualFileUtil.refreshAndFindFileByLocalPath(
                    VirtualFileUtil.buildUrl(builder.getSdk().getHomePath(), "lib/fan")));
            dependenciesChooser.addRoot(v);
        }
        final BrowseFilesListener dependenciesBrowseListener = new BrowseFilesListener(dependencies,
                FanBundle.message("out.dir.path"),
                FanBundle.message("out.dir.path.description"),
                dependenciesChooser) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final VirtualFile fileToSelect = getFileToSelect();
                final VirtualFile[] files = (fileToSelect != null)
                        ? FileChooser.chooseFiles(dependencies, dependenciesChooser, fileToSelect)
                        : FileChooser.chooseFiles(dependencies, dependenciesChooser);
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < files.length; i++ ) {
                    final String file = files[i].getNameWithoutExtension();
                    final String version = PodUtil.getPodVersion(files[i]);
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(file);
                    sb.append(' ');
                    sb.append(version);
                }
                dependencies.setText(sb.toString());
            }
        };

        final FieldPanel podNameFieldPanel = new FieldPanel(podName, FanBundle.message("pod.name"), null, null, null);
        final FieldPanel versionFieldPanel = new FieldPanel(version, FanBundle.message("pod.version"), null, null, null);
        final FieldPanel podDescriptionFieldPanel = new FieldPanel(podDescription, FanBundle.message("pod.description"), null, null, null);
        final FieldPanel srcDirsFieldPanel = new FieldPanel(srcDirs, FanBundle.message("pod.srcDirs"), null, srcDirsBrowseListener, null);
        final FieldPanel resDirsFieldPanel = new FieldPanel(resDirs, FanBundle.message("pod.resDirs"), null, resDirsBrowseListener, null);
        final FieldPanel outDirFieldPanel = new FieldPanel(outDir, FanBundle.message("pod.outDir"), null, outDirBrowseListener, null);
        final FieldPanel dependenciesFieldPanel = new FieldPanel(dependencies, FanBundle.message("pod.dependencies"), null, dependenciesBrowseListener, null);
        final CheckBoxFieldPanel docApiFieldPanel = new CheckBoxFieldPanel(docApi, FanBundle.message("pod.docApi"));
        final CheckBoxFieldPanel docSrcFieldPanel = new CheckBoxFieldPanel(docSrc, FanBundle.message("pod.docSrc"));
        final FanFieldPanel indexesFieldPanel = new FanFieldPanel(indexes, FanBundle.message("pod.indexes"), FanBundle.message("pod.indexes"), indexesListener, null);
        final FanFieldPanel metasFieldPanel = new FanFieldPanel(metas, FanBundle.message("pod.metas"), FanBundle.message("pod.metas"), metasListener, null);

        myMainPanel.add(messageLabel);
        myMainPanel.add(podNameFieldPanel);
        myMainPanel.add(versionFieldPanel);
        myMainPanel.add(podDescriptionFieldPanel);
        myMainPanel.add(dependenciesFieldPanel);
        myMainPanel.add(srcDirsFieldPanel);
        myMainPanel.add(resDirsFieldPanel);
        myMainPanel.add(outDirFieldPanel);
        myMainPanel.add(docApiFieldPanel);
        myMainPanel.add(docSrcFieldPanel);
        myMainPanel.add(indexesFieldPanel);
        myMainPanel.add(metasFieldPanel);
        add(myMainPanel, BorderLayout.PAGE_START);
    }

    void setMessage(final String message) {
        messageLabel.setText(message);
    }

    void setModel(final PodModel podModel) {
        this.podModel = podModel;
        podName.setText(podModel.getName());
        version.setText(podModel.getVersion());
        podDescription.setText(podModel.getDescription());
        dependencies.setText(FanUtil.listToString(podModel.getDependencies()));
        docApi.setSelected(podModel.getDocApi());
        docSrc.setSelected(podModel.getDocSrc());
        indexes.setText(FanUtil.listPairToString(podModel.getIndexes()));
        metas.setText(FanUtil.listPairToString(podModel.getMetas()));
        srcDirs.setText(FanUtil.listPairToFirstString(podModel.getSrcDirs()));
        resDirs.setText(FanUtil.listPairToFirstString(podModel.getResDirs()));
        outDir.setText(podModel.getOutDir());
    }

    void updateModel() {
        podModel.setName(podName.getText());
        podModel.setVersion(version.getText());
        podModel.setDescription(podDescription.getText());
        podModel.setDependencies(FanUtil.stringToList(dependencies.getText()));
        podModel.setDocApi(docApi.isSelected());
        podModel.setDocSrc(docSrc.isSelected());
        podModel.setIndexes(FanUtil.stringToListPair(indexes.getText()));
        podModel.setMetas(FanUtil.stringToListPair(metas.getText()));
        podModel.setSrcDirs(FanUtil.firstStringToListPair(srcDirs.getText()));
        podModel.setResDirs(FanUtil.firstStringToListPair(resDirs.getText()));
        podModel.setOutDir(outDir.getText());
    }
}
