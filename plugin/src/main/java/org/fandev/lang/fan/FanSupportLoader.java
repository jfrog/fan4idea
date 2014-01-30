package org.fandev.lang.fan;

import com.intellij.openapi.fileTypes.*;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Dror
 * @date Dec 12, 2008 12:00:55 AM
 */
public class FanSupportLoader extends FileTypeFactory {
    public static final LanguageFileType FAN = FanFileType.FAN_FILE_TYPE;
    public static final PodFileType POD = PodFileType.POD_FILE_TYPE;

    @Override
	public void createFileTypes(@NotNull final FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(FAN, FAN.getDefaultExtension());
        fileTypeConsumer.consume(POD, POD.getDefaultExtension());
    }

    public String getFileExtension() {
      return "fan";
    }
}
