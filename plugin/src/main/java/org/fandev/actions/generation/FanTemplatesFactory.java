package org.fandev.actions.generation;

import com.intellij.ide.fileTemplates.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.fandev.module.pod.PodModel;
import org.fandev.utils.VirtualFileUtil;
import org.jetbrains.annotations.NonNls;
import org.fandev.icons.Icons;
import org.fandev.lang.fan.FanBundle;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author Dror Bereznitsky
 * @date Apr 1, 2009 3:52:01 PM
 */
public class FanTemplatesFactory implements FileTemplateGroupDescriptorFactory {
    @NonNls
    public static final String[] TEMPLATES = {
            "FanClass.fan", "FanMixin.fan", "FanEnum.fan", "FanBuildScript.fan"};
    private ArrayList<String> myCustomTemplates = new ArrayList<String>();
    private static final Logger logger = Logger.getInstance("org.fandev.actions.generation.FanTemplatesFactory");

    private static FanTemplatesFactory myInstance = new FanTemplatesFactory();

    public static FanTemplatesFactory getInstance() {
        return myInstance;
    }

    public void registerCustomTemplates(final String... templates) {
        for (final String template : templates) {
            myCustomTemplates.add(template);
        }
    }

    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        final FileTemplateGroupDescriptor group =
                new FileTemplateGroupDescriptor(FanBundle.message("file.template.group.title.fan"), Icons.FAN_16);
        final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        for (final String template : TEMPLATES) {
            group.addTemplate(new FileTemplateDescriptor(template, fileTypeManager.getFileTypeByFileName(template).getIcon()));
        }
        // register custom templates
        for (final String template : getInstance().getCustomTemplates()) {
            group.addTemplate(new FileTemplateDescriptor(template, fileTypeManager.getFileTypeByFileName(template).getIcon()));
        }
        return group;
    }


    public static PsiFile createFromTemplate(final PsiDirectory directory,
                                             final String fileName,
                                             final String templateName,
                                             @NonNls final EnumMap<TemplateProperty, String> parameters) throws IncorrectOperationException {
        final FileTemplate template = FileTemplateManager.getInstance().getDefaultTemplate(templateName);

        final Properties properties = new Properties(FileTemplateManager.getInstance().getDefaultProperties());
        for (final TemplateProperty propertyName : parameters.keySet()) {
            properties.setProperty(propertyName.toString(), parameters.get(propertyName));
        }
        String text;
        try {
            text = template.getText(properties);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to load template for " + FileTemplateManager.getInstance().internalTemplateToSubject(templateName),
                    e);
        }

        // Get rid of parameters with null settings from being written to the build.fan file.
        if (PodModel.BUILD_FAN.equals(fileName)) {
            Pattern p = Pattern.compile("^.*\\[\\]$", Pattern.MULTILINE);
            Matcher m = p.matcher(text);
            text = m.replaceAll("");
            p = Pattern.compile("^.*= $", Pattern.MULTILINE);
            m = p.matcher(text);
            text = m.replaceAll("");
            p = Pattern.compile("^.*= \"\"$", Pattern.MULTILINE);
            m = p.matcher(text);
            text = m.replaceAll("");
            p = Pattern.compile("^.*= Version.fromStr\\(\"\"\\)$", Pattern.MULTILINE);
            m = p.matcher(text);
            text = m.replaceAll("");
        }
        final VirtualFile v =
                VirtualFileUtil.refreshAndFindFileByLocalPath(
                        VirtualFileUtil.buildUrl(directory.getVirtualFile().getPath(), fileName));
        try {
            v.setBinaryContent(text.getBytes());
            final PsiManager manager = PsiManager.getInstance(directory.getProject());
            final PsiFile file = manager.findFile(v);
            return file;
        }
        catch (IOException e) {
            logger.error("IOException writing to build.fan!");
        }
        return null;
    }

    public String[] getCustomTemplates() {
        return myCustomTemplates.toArray(new String[0]);
    }
}
