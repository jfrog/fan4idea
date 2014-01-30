package org.fandev.utils;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: bheadley
 * Date: Mar 17, 2010
 * Time: 4:57:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class PodUtil {
    public static String getPodVersion(final VirtualFile podFile) {
        ZipFile zipFile = null;
        BufferedReader is = null;
        try {
            zipFile = new ZipFile(podFile.getPath());
            ZipEntry zipEntry = zipFile.getEntry("meta.props");
            if (zipEntry == null) {
                return "";
            }
            is = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEntry)));
            while (true) {
                final String line = is.readLine();
                if (line.startsWith("pod.version")) {
                    final String[] tokens = line.split("=");
                    final String version = tokens[1].trim();
                    return version;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (is != null) {
                try { is.close(); } catch (Exception e1) {}
            }
            if (zipFile != null) {
                try {zipFile.close();} catch (Exception e1) {}
            }
        }
        return "";
    }
}
