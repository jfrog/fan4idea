package org.fandev.lang.fan;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.fandev.icons.Icons;

import javax.swing.*;

/**
 *
 * @author Dror
 * @date Dec 12, 2008 12:01:51 AM
 */
public class FanFileType extends LanguageFileType {
    public static final FanFileType FAN_FILE_TYPE = new FanFileType();
    @NonNls public static final String DEFAULT_EXTENSION = "fan";
    
    private FanFileType() {
        super(FanLanguage.FAN_LANGUAGE);
    }

    @NotNull
    public String getName() {
        return "Fantom";
    }

    @NotNull
    public String getDescription() {
        return FanBundle.message("fan.filetype.description");
    }

    @NotNull
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public Icon getIcon() {
        return Icons.FAN_16;
    }

    @Override
    public boolean isJVMDebuggingSupported() {
        return true;
    }
}
