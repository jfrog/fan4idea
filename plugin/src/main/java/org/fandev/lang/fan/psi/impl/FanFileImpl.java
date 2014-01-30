package org.fandev.lang.fan.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.fandev.lang.fan.FanSupportLoader;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanClassDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.api.statements.FanTopLevelDefintion;
import org.fandev.module.pod.PodModel;
import org.fandev.utils.VirtualFileUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 2:50:33 PM
 */
public class FanFileImpl extends PsiFileBase implements FanFile {
    private String podName;

    private final static Logger logger = Logger.getInstance("org.fandev.lang.fan.psi.impl.FanFileImpl");

    public FanFileImpl(final FileViewProvider fileViewProvider) {
        super(fileViewProvider, FanSupportLoader.FAN.getLanguage());
    }

    @NotNull
    public FileType getFileType() {
        return FanSupportLoader.FAN;
    }

    public String getPodName() {
        if (podName != null) {
            return podName;
        }

        // If I'm a build.fan get it
        if (PodModel.BUILD_FAN.equals(getName())) {
            // Ugly Hack until we can execute Fan script (TODO: use the PodFileParser)
            final FanClassDefinition[] classDef = this.findChildrenByClass(FanClassDefinition.class);
            if (classDef.length > 0) {
                final FanMethod[] methods = classDef[0].getFanMethods();
                if (methods.length > 0) {
                    for (final FanMethod method : methods) {
                        // TODO: Even uglier hack until we have Expression parsing
                        if (method.getName().equals("setup")) {
                            final String setupBody = method.getBody().getText();
                            final int podNameIdx = setupBody.indexOf("podName");
                            if (podNameIdx != -1) {
                                final int firstDQ = setupBody.indexOf('"', podNameIdx+"podName".length()+1);
                                final int lastDQ = setupBody.indexOf('"', firstDQ+1);
                                podName = setupBody.substring(firstDQ+1,lastDQ);
                                return podName;
                            }
                        }
                    }
                }
            }
            logger.warn("Did not find pod name in " + (getVirtualFile() != null ? getVirtualFile().getPath() : this.toString()));
            return "NotFound";
        }

        // If the file is inside a pod
        final VirtualFile myVirtualFile = getVirtualFile();
        if (myVirtualFile != null && myVirtualFile.getUrl().contains(".pod")) {
            final String url = myVirtualFile.getUrl();
            final String podPath = url.substring(0, url.indexOf(".pod"));
            int podNameStartIndex = podPath.lastIndexOf(VirtualFileUtil.VFS_PATH_SEPARATOR);
            podNameStartIndex = podNameStartIndex < 0 ? 0 : podNameStartIndex + 1;
            podName = podPath.substring(podNameStartIndex);
            return podName;
        }

        // Go up until the directory contain a build.fan file
        PsiDirectory psiDirectory = getParent();
        int upMax = 4;
        while (psiDirectory != null && upMax > 0) {
            final PsiFile[] files = psiDirectory.getFiles();
            if (files != null) {
                for (final PsiFile file : files) {
                    if (PodModel.BUILD_FAN.equals(file.getName())) {
                        podName = ((FanFile)file).getPodName();
                        return podName;
                    }
                }
            }
            psiDirectory = psiDirectory.getParentDirectory();
            upMax--;
        }
        return "NotFound";
    }

    public FanTypeDefinition[] getTypeDefinitions() {
        return findChildrenByClass(FanTypeDefinition.class);
    }

    public FanTypeDefinition getTypeByName(String name) {
        for (final FanTypeDefinition typeDef : getTypeDefinitions()) {
            if (name.equals(typeDef.getName())) {
                return typeDef;
            }
        }
        return null;
    }

    @NotNull
    public PsiClass[] getClasses() {
        return getTypeDefinitions();
    }

    public String getPackageName() {
        return podName;
    }

    public void setPackageName(final String s) throws IncorrectOperationException {
        podName = s;
    }

    public FanTopLevelDefintion[] getTopLevelDefinitions() {
        return findChildrenByClass(FanTopLevelDefintion.class);
    }
}
