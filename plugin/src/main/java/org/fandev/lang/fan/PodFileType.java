package org.fandev.lang.fan;

import com.intellij.ide.highlighter.ArchiveFileType;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.fandev.icons.Icons;

/**
 * @author Dror Bereznitsky
 * @date Feb 28, 2009 6:09:05 PM
 */
public class PodFileType extends ArchiveFileType {
    public static final PodFileType POD_FILE_TYPE = new PodFileType();
    @NonNls
    public static final String DEFAULT_EXTENSION = "pod";

    public PodFileType() {
        super();
    }

    @Override
    public Icon getIcon() {
        return Icons.FAN_16;
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @NotNull
    @Override
    public String getDescription() {
        return FanBundle.message("fan.filetype.description");
    }

    @NotNull
    @Override
    public String getName() {
        return "Pod";
    }
}
