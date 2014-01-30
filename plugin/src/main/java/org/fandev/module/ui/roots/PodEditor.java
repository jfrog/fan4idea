package org.fandev.module.ui.roots;

import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.roots.ui.configuration.ModuleElementsEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.FieldPanel;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.FanBundle;
import org.fandev.module.FanModuleSettings;
import org.fandev.module.pod.PodFileParser;
import org.fandev.module.pod.PodModel;
import org.fandev.module.wizard.ui.CheckBoxFieldPanel;
import org.fandev.module.wizard.ui.FanFieldPanel;
import org.fandev.utils.FanUtil;
import org.fandev.utils.PodUtil;
import org.fandev.utils.VirtualFileUtil;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Dror Bereznitsky
 * @date Jan 31, 2009 11:34:31 PM
 */
public class PodEditor extends ModuleElementsEditor {
    private JPanel buildScriptPathPanel;
    private JTextField podName;
    private JTextField version;
    private JTextField podDescription;
    private JTextField srcDirs;
    private JTextField resDirs;
    private JTextField outDir;
    private JCheckBox docApi;
    private JCheckBox docSrc;
    private JTextField indexes;
    private JTextField metas;
    private JTextField dependencies;

    private boolean podModified = false;
    private PodModel pod;

    protected PodEditor(final ModuleConfigurationState state) {
        super(state);
        // This is sad, Why can't I find something with a reference to the podModel?!?
        pod = PodFileParser.parse(getModuleSettings().getBuildScript(), getModuleSettings().getModule());
        // and later on, I have to rewrite the damned build file!
    }

    protected JComponent createComponentImpl() {
        buildScriptPathPanel = new JPanel();
        buildScriptPathPanel.setLayout(new BoxLayout(buildScriptPathPanel,BoxLayout.PAGE_AXIS)); 

        // podName
        podName = new JTextField(pod.getName());
        version = new JTextField(pod.getVersion());
        podDescription = new JTextField(pod.getDescription());
        srcDirs = new JTextField(FanUtil.listPairToFirstString(pod.getSrcDirs()));
        resDirs = new JTextField(FanUtil.listPairToFirstString(pod.getResDirs()));
        dependencies = new JTextField(FanUtil.listToString(pod.getDependencies()));
        outDir = new JTextField(pod.getOutDir());
        docApi = new JCheckBox("", pod.getDocApi());
        docSrc = new JCheckBox("", pod.getDocSrc());
        indexes = new JTextField(FanUtil.listPairToString(pod.getIndexes()));
        metas = new JTextField(FanUtil.listPairToString(pod.getMetas()));

        final VirtualFile moduleFile = getModel().getModule().getModuleFile().getParent();

        // callbacks for srcDirs, resDirs. I expect that the user wants to select
        // multiple directories, but that's b0rked in BrowseFilesListener. Fixed in
        // the actionperformed listener
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
                podModified = true;
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
                podModified = true;
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
        final BrowseFilesListener outDirBrowseListener = new BrowseFilesListener(resDirs,
                FanBundle.message("pod.outDir"),
                FanBundle.message("pod.outDir"),
                resDirsChooser) {
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
                podModified = true;
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
            final VirtualFile v = moduleFile.findChild(pod.getOutDir());
            dependenciesChooser.setRoot(VirtualFileUtil.refreshAndFindFileByLocalPath(
                    VirtualFileUtil.buildUrl(getState().getRootModel().getSdk().getHomePath(), "lib/fan")));
            dependenciesChooser.addRoot(v);
        }
        final BrowseFilesListener dependenciesBrowseListener = new BrowseFilesListener(dependencies,
                FanBundle.message("pod.dependencies"),
                FanBundle.message("pod.dependencies"),
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
        
        podName.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModified = true;
            }
        });
        version.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModified = true;
            }
        });
        podDescription.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModified = true;
            }
        });
        docApi.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModified = true;
            }
        });
        docSrc.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModified = true;
            }
        });
        final ActionListener indexesListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModified = true;
            }
        };
        indexes.addActionListener(indexesListener);
        final ActionListener metasListener =  new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                podModified = true;
            }
        };
        metas.addActionListener(metasListener);

        final FieldPanel podNamePanel = new FieldPanel(podName, FanBundle.message("pod.name"), null, null, null);
        final FieldPanel descriptionPanel = new FieldPanel(podDescription, FanBundle.message("pod.description"), null, null, null);
        final FieldPanel versionPanel = new FieldPanel(version, FanBundle.message("pod.version"), null, null, null);
        final FieldPanel srcDirsPanel = new FieldPanel(srcDirs, FanBundle.message("src.dirs.path"), null, srcDirsBrowseListener, null);
        final FieldPanel resDirsPanel = new FieldPanel(resDirs, FanBundle.message("res.dirs.path"), null, resDirsBrowseListener, null);
        final FieldPanel dependenciesPanel = new FieldPanel(dependencies, FanBundle.message("pod.dependencies"), null, dependenciesBrowseListener, null);
        final FieldPanel outDirPanel = new FieldPanel(outDir, FanBundle.message("pod.outDir"), null, outDirBrowseListener, null);
        final CheckBoxFieldPanel docApiPanel = new CheckBoxFieldPanel(docApi, FanBundle.message("pod.docApi"));
        final CheckBoxFieldPanel docSrcPanel = new CheckBoxFieldPanel(docSrc, FanBundle.message("pod.docSrc"));
        final FanFieldPanel indexesPanel = new FanFieldPanel(indexes, FanBundle.message("pod.indexes"), FanBundle.message("pod.indexes"), indexesListener, null);
        final FanFieldPanel metasPanel = new FanFieldPanel(metas, FanBundle.message("pod.metas"), FanBundle.message("pod.metas"), metasListener, null);

        buildScriptPathPanel.add(podNamePanel);
        buildScriptPathPanel.add(descriptionPanel);
        buildScriptPathPanel.add(versionPanel);
        buildScriptPathPanel.add(srcDirsPanel);
        buildScriptPathPanel.add(resDirsPanel);
        buildScriptPathPanel.add(dependenciesPanel);
        buildScriptPathPanel.add(outDirPanel);
        buildScriptPathPanel.add(docApiPanel);
        buildScriptPathPanel.add(docSrcPanel);
        buildScriptPathPanel.add(indexesPanel);
        buildScriptPathPanel.add(metasPanel);

        return buildScriptPathPanel;
    }

    public void saveData() {
        pod.setName(podName.getText());
        pod.setDescription(podDescription.getText());
        pod.setVersion(version.getText());
        pod.setDocApi(docApi.isSelected());
        pod.setDocSrc(docSrc.isSelected());
        pod.setSrcDirs(FanUtil.firstStringToListPair(srcDirs.getText()));
        pod.setResDirs(FanUtil.firstStringToListPair(resDirs.getText()));
        pod.setDependencies(FanUtil.stringToList(dependencies.getText()));
        pod.setOutDir(outDir.getText());
        pod.setIndexes(FanUtil.stringToListPair(indexes.getText()));
        pod.setMetas(FanUtil.stringToListPair(metas.getText()));

        // Save out a new build.fan?!?
        final Module m = getModel().getModule(); 
        FanUtil.generateBuildScript(m.getModuleFile().getParent().getPath(), m.getProject(), pod);
        podModified = false;
    }

    @Override
    public boolean isModified() {
        return podModified;
    }

    @Nls
    public String getDisplayName() {
        return "Build Script Editor";
    }

    public Icon getIcon() {
        return Icons.FAN_16;
    }

    public String getHelpTopic() {
        return null;
    }

    public static PodEditor createPodEditor(final ModuleConfigurationState state) {
        return new PodEditor(state);
    }

    private FanModuleSettings getModuleSettings() {
        return FanModuleSettings.getInstance(getModel().getModule());
    }
}
