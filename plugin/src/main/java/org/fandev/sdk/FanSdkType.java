package org.fandev.sdk;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.fandev.lang.fan.FanBundle;
import org.fandev.utils.OSUtil;
import org.fandev.utils.FanUtil;
import org.fandev.icons.Icons;

import javax.swing.*;
import java.io.File;

/**
 * @author Dror Bereznitsky
 * @date Jan 18, 2009 4:27:47 PM
 */
public class FanSdkType extends SdkType implements ApplicationComponent {
    private static final String FAN_SDK_NAME = "FAN_SDK";
    private static final String FAN_SDK_TYPE = "FanSdkType";

    public static final String FAN_LAUNCHER_WIN ="fan.exe";
    public static final String FAN_LAUNCHER_UNIX = "fan";
    public static final String JSTUB_WIN ="jstub.exe";
    public static final String JSTUB_UNIX = "fanlaunch";
    public static final String FAN_LAUNCHER_DIR = "bin";
    public static final String FAN_LIB_DIR = "lib/fan";
    public static final String SRC_DIR = "src";
    public static final String FAN_SRC_DIR = "fan";

    private static final Logger log = Logger.getInstance("org.fandev.sdk.FanSdkType");

    public FanSdkType() {
        super(FAN_SDK_NAME);
    }

    public FanSdkType(@NonNls final String name) {
        super(name);
    }

    @Override
    public Icon getIcon() {
        return Icons.FAN_16;
    }

    @Override
    public Icon getIconForAddAction() {
        return getIcon();
    }

    @Override
    public String getVersionString(@NotNull final String sdkHome) {
        FanUtil.setFanHome(sdkHome);
        ClassLoader cl = FanUtil.getSysClassloader(sdkHome);
        try {
            final Class podClass = Class.forName("fan.sys.Pod", true, cl);
            final Class versionClass = Class.forName("fan.sys.Version", true, cl);
            final Object o = podClass.getDeclaredMethod("find", String.class, boolean.class).invoke(null, "sys", Boolean.TRUE);
            final Object versionObj = podClass.getDeclaredMethod("version").invoke(o);
            return (String)versionClass.getDeclaredMethod("toStr").invoke(versionObj);
        } catch (Exception e) {
            log.warn("Could not determine Fantom SDK version", e);            
        } finally {
            cl = null;
        }
        return "";
    }

    public String suggestHomePath() {
        final String path = findFanLauncher();
        if (path != null) {
            return path;
        }

        if (SystemInfo.isWindows){
            return "C:/dev/fan";
        } else if (SystemInfo.isMac){
            return "/applications/fan";
        } else if (SystemInfo.isUnix || SystemInfo.isLinux) {
            return "/usr/share/fantom";
        }

        throw new IllegalStateException(FanBundle.message("os.not.supported"));
    }

    public boolean isValidSdkHome(final String path) {
        return (new File(path + File.separator + getFanLauncherPath())).exists();
    }

    public String suggestSdkName(final String currentSdkName, final String sdkHome) {
        return "Fantom SDK " + getVersionString(sdkHome);
    }

    public AdditionalDataConfigurable createAdditionalDataConfigurable(final SdkModel sdkModel, final SdkModificator sdkModificator) {
        return new FanSdkConfigurable();
    }

    public void saveAdditionalData(final SdkAdditionalData additionalData, final Element additional) {

    }

    public String getPresentableName() {
        return FanBundle.message("fan.sdk.title");
    }

    public static FanSdkType getInstance(){
        return ApplicationManager.getApplication().getComponent(FanSdkType.class);
    }

    @NotNull
    public String getComponentName() {
        return FAN_SDK_TYPE;
    }

    public void initComponent() {
        // init code goes here
    }

    public void disposeComponent() {
        // dispose code goes here
    }

    @Override
    public void setupSdkPaths(final Sdk sdk) {
        if (sdk == null) {
            throw new IllegalArgumentException("Sdk should not be null");
        }
        
        final VirtualFile sysJar = sdk.getHomeDirectory().findFileByRelativePath("lib/java/sys.jar");
        if (sysJar != null) {
            VirtualFile sysSources = sdk.getHomeDirectory().findFileByRelativePath("src/sys/java");
            if (sysSources == null) {
                // For older versions of fan
                sysSources = sdk.getHomeDirectory().findFileByRelativePath("src/jfan");
            }
            final SdkModificator sdkModificator = sdk.getSdkModificator();

            sdkModificator.addRoot( sysJar, OrderRootType.CLASSES);
            if (sysSources == null) {
                sdkModificator.addRoot(sysSources, OrderRootType.SOURCES);
            }

            final VirtualFile extDir = getExtDir(sdk);
            // Add all jars in <FAN_HOME>/lib/java/ext/<OS> dir
            if (extDir.exists()) {
                for (final VirtualFile lib : extDir.getChildren()) {
                    if ("jar".equals(lib.getExtension())) {
                        sdkModificator.addRoot(lib, OrderRootType.CLASSES);    
                    }
                }
            }

            sdkModificator.commitChanges();
        }
    }

    public static VirtualFile getExtDir(final Sdk sdk) {
        if (sdk == null) {
            throw new IllegalArgumentException("Sdk should not be null");
        }
        // This needs more work. E.g., 64-bit linux, win32 versions of swt.jar
        // But right now these are the packaged directory names in 1.0.51
        final String os = SystemInfo.isWindows ? "win32-x86" : SystemInfo.isMac ? "macosx-x86_64" : "linux-x86";
        //os += SystemInfo.is32Bit ? "-x86" : "-x86_64";
        return sdk.getHomeDirectory().findFileByRelativePath("lib/java/ext/" + os);
    }

    public static String getFanLauncher() {
        if (SystemInfo.isWindows){
            return FAN_LAUNCHER_WIN;
        } else if (SystemInfo.isUnix){
            return FAN_LAUNCHER_UNIX;
        }
        throw new IllegalStateException(FanBundle.message("os.not.supported"));
    }

    public static String getFanLauncherPath() {
        if (SystemInfo.isWindows){
            return FAN_LAUNCHER_DIR + File.separator + FAN_LAUNCHER_WIN;
        } else if (SystemInfo.isUnix){
            return FAN_LAUNCHER_DIR + File.separator + FAN_LAUNCHER_UNIX;
        }
        throw new IllegalStateException(FanBundle.message("os.not.supported"));
    }

    public static String getJStubPath() {
        if (SystemInfo.isWindows){
            return FAN_LAUNCHER_DIR + File.separator + JSTUB_WIN;
        } else if (SystemInfo.isUnix){
            return FAN_LAUNCHER_DIR + File.separator + JSTUB_UNIX;
        }
        throw new IllegalStateException(FanBundle.message("os.not.supported"));
    }

    public static String getFanLibDir() {
        return FAN_LIB_DIR;
    }

    public static String getSrcDir() {
        return SRC_DIR;
    }

    public static String getFanSrcDir() {
        return FAN_SRC_DIR;
    }

    @Nullable
    private static String findFanLauncher() {
        final String exePath = getFanLauncherPath();
        final String interpreterName = getFanLauncher();

        final String pathByName = OSUtil.findExecutableByName(interpreterName);
        if (pathByName == null) {
            return null;
        }
        return pathByName.endsWith(exePath) ? pathByName.substring(0, pathByName.length() - exePath.length()) : null;
    }
}
