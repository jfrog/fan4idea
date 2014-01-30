package org.fandev.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import static com.intellij.openapi.util.io.FileUtil.toSystemDependentName;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.fandev.lang.fan.FanBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Roman Chernyatchik
 * @date: Nov 12, 2007
 */
public class OSUtil {
    private static final  String UNIX_PATH_NAME = "PATH";
    private static final String WINDOWS_PATH_NAME = "Path";

    //svn
    private static final String SVN_DEFAULT_UNIX_PATH = "/usr/bin";
    private static final String SVN_DEFAULT_MAC_PATH = "/usr/local/bin";
    private static final String SVN_DEFAULT_WIN_PATH = "c:";

    private static final Logger LOG = Logger.getInstance(OSUtil.class.getName());

    @Nullable
    public static String getPATHenvVariableName() {
        if (SystemInfo.isWindows) {
            return WINDOWS_PATH_NAME;
        }
        if (SystemInfo.isUnix){
            return UNIX_PATH_NAME;
        }
        LOG.error(FanBundle.message("os.not.supported"));
        return null;
    }

    public static String appendToPATHenvVariable(@Nullable final String path,
                                                 @NotNull final String additionalPath) {
        final String pathValue;
        if (TextUtil.isEmpty(path)) {
            pathValue = additionalPath;
        } else {
            pathValue = path + File.pathSeparatorChar + additionalPath;
        }
        return toSystemDependentName(pathValue);
    }

    @NotNull
    public static String getDefaultSVNPath() {
        if (SystemInfo.isWindows) {
            return SVN_DEFAULT_WIN_PATH;
        }
        if (SystemInfo.isMac) {
            return SVN_DEFAULT_MAC_PATH;
        }
        if (SystemInfo.isUnix){
            return SVN_DEFAULT_UNIX_PATH;
        }
        LOG.error(FanBundle.message("os.not.supported"));
        return "";
    }

    public static String getIdeaSystemPath() {
        return System.getenv().get(OSUtil.getPATHenvVariableName());
    }

    /**
     * Finds executable by name in standart path environment
     * @param exeName executable name, gem for example
     * @return path if found
     */
    @Nullable
    public static String findExecutableByName(@NotNull final String exeName){
        final String path = getIdeaSystemPath();
        final VirtualFileManager manager = VirtualFileManager.getInstance();
        if (path!=null){
            final StringTokenizer st = new StringTokenizer(path, File.pathSeparator);

            //tokens - are pathes with system-dependent slashes
            while (st.hasMoreTokens()){
                final String s = VirtualFileUtil.convertToVFSPathAndNormalizeSlashes(st.nextToken());
                final String possible_path = s + VirtualFileUtil.VFS_PATH_SEPARATOR + exeName;
                if (manager.findFileByUrl(VirtualFileUtil.constructLocalUrl(possible_path))!=null){
                    return possible_path;
                }
            }
        }
        return null;
    }

}
