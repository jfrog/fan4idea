package org.fandev.module.pod;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.fandev.sdk.FanSdkType;
import org.fandev.utils.FanUtil;
import org.fandev.utils.VirtualFileUtil;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.LinkedList;

/**
 * @author Dror Bereznitsky
 * @date Feb 23, 2009 11:47:00 PM
 */
public class PodFileParser {
    private static final String DEPENDENCIES_FIELD = "depends";
    private static final String VERSION_FIELD = "version";
    private static final String DEPENDENCIES_DIR_FIELD = "dependsDir";
    private static final String DESCRIPTION_FIELD = "summary";
    private static final String SRC_DIRS_FIELD = "srcDirs";
    private static final String RES_DIRS_FIELD = "resDirs";
    private static final String INDEXES_FIELD = "index";
    private static final String METAS_FIELD = "meta";
    private static final String OUT_DIR_FIELD = "outDir";
    private static final String DOC_API_FIELD = "docApi";
    private static final String DOC_SRC_FIELD = "docSrc";
    private static final String POD_NAME_FIELD = "podName";
    private static final Logger logger = Logger.getInstance("org.fandev.module.pod.PodFileParser");

    public static PodModel parse(final VirtualFile buildScript, final Module module) {
        return parse(buildScript, FanUtil.getSdk(module));
    }

    public static PodModel parse(final VirtualFile buildScript, final Sdk sdk) {
         if (buildScript == null) {
            logger.warn("Invalid build script");
            return null;
        }

        if (sdk == null || !sdk.getSdkType().equals(FanSdkType.getInstance())) {
            logger.warn("Module has no Fantom sdk");
            return null;
        }

        FanUtil.setFanHome(sdk);

        URLClassLoader cl = FanUtil.getSysClassloader(sdk.getHomePath());

        try {
            final Class envClass = Class.forName("fan.sys.Env", true, cl);
            final Class versionClass = Class.forName("fan.sys.Version", true, cl);
            final Class typeClass = Class.forName("fan.sys.Type", true, cl);
            final Class fieldClass = Class.forName("fan.sys.Field", true, cl);
            final Class uriClass = Class.forName("fan.sys.Uri", true, cl);
            final Class listClass = Class.forName("fan.sys.List", true, cl);
            final Class fileClass = Class.forName("fan.sys.File", true, cl);
            final Class mapClass = Class.forName("fan.sys.Map", true, cl);

            final PodModel model = new PodModel();
            model.setBuildScriptFile(buildScript);

            FanUtil.setFanHome(sdk);

            final Object uri = uriClass.getDeclaredMethod("fromStr", String.class).invoke(null, VirtualFileUtil.constructLocalUrl(buildScript.getPath()));
            final Object file = fileClass.getDeclaredMethod("make", uriClass).invoke(null, uri);
            final Object envClassInstance = envClass.getDeclaredMethod("cur").invoke(null);

            final Method fieldMethod = typeClass.getDeclaredMethod("field", String.class);
            final Method getMethod = fieldClass.getDeclaredMethod("get", Object.class);
            final Method getAtMethod = listClass.getDeclaredMethod("get", long.class);
            final Method sizeMethod = listClass.getDeclaredMethod("size");
            final Method uriToStrMethod = uriClass.getDeclaredMethod("toStr");
            final Method mapKeysMethod = mapClass.getDeclaredMethod("keys");
            final Method mapGetKeyMethod = mapClass.getDeclaredMethod("get", Object.class);

            final Object buildScriptType = envClass.getDeclaredMethod("compileScript", fileClass).invoke(envClassInstance, file);
            if (buildScriptType == null) {
                logger.warn("Unable to compile script file: " + buildScript.getPath() + " could not synchronize module state.");
                return null;
            }

            final Object buildClassInstance = typeClass.getDeclaredMethod("make").invoke(buildScriptType); // fan.sys.Type

            // pod name
            Object field = fieldMethod.invoke(buildScriptType, POD_NAME_FIELD);
            String s = (String) getMethod.invoke(field, buildClassInstance);
            s = s != null ? s : "";
            model.setName(s);

            // description
            field = fieldMethod.invoke(buildScriptType, DESCRIPTION_FIELD);
            s = (String) getMethod.invoke(field, buildClassInstance);
            s = s != null ? s : "";
            model.setDescription(s);

            // version
            field = fieldMethod.invoke(buildScriptType, VERSION_FIELD);
            Object o = getMethod.invoke(field, buildClassInstance);
            s = "";
            if (null != o) {
               s =  (String) versionClass.getDeclaredMethod("toStr").invoke(o);
            }
            model.setVersion(s);

            // outDir
            field = fieldMethod.invoke(buildScriptType, OUT_DIR_FIELD);
            o = getMethod.invoke(field, buildClassInstance);
            if (o != null) {
                model.setOutDir((String)uriToStrMethod.invoke(o));
            } else {
                model.setOutDir(null);
            }

            // docApi
            field = fieldMethod.invoke(buildScriptType, DOC_API_FIELD);
            Boolean b = (Boolean) getMethod.invoke(field, buildClassInstance);
            model.setDocApi(b == null ? false : b);

            // docSrc
            field = fieldMethod.invoke(buildScriptType, DOC_SRC_FIELD);
            b = (Boolean) getMethod.invoke(field, buildClassInstance);
            model.setDocSrc(b == null ? false : b);

            // dependencies dir
            field = fieldMethod.invoke(buildScriptType, DEPENDENCIES_DIR_FIELD);
            final Object dependsDir = fieldClass.getDeclaredMethod("get", Object.class).invoke(field, buildClassInstance); // fan.sys.Uri
            VirtualFile dependsDirFile;
            if (dependsDir != null) {
                String dependsDirPath = (String) uriClass.getDeclaredMethod("toStr").invoke(dependsDir);
                dependsDirFile = VirtualFileUtil.findFileByLocalPath(dependsDirPath);
            } else {
                String dependsDirPath = sdk.getHomePath() + VirtualFileUtil.VFS_PATH_SEPARATOR + FanSdkType.getFanLibDir();
                dependsDirFile = VirtualFileUtil.findFileByLocalPath(dependsDirPath);
            }
            final String fanLibPath = dependsDirFile.getPath();

            // dependencies
            field = fieldMethod.invoke(buildScriptType, DEPENDENCIES_FIELD);
            o = getMethod.invoke(field, buildClassInstance);
            if (o != null) {
                final Long size = (Long)sizeMethod.invoke(o);
                for (int i = 0; i < size; i++) {
                    s = (String) getAtMethod.invoke(o, i);
                    if (s != null) {
                        model.addDependency(s);
                    }
                }
            } else {
                model.setDependencies(new LinkedList<String>());
            }

            // source dirs
            field = fieldMethod.invoke(buildScriptType, SRC_DIRS_FIELD);
            o = getMethod.invoke(field, buildClassInstance);
            if (o != null) {
                final Long size = (Long)sizeMethod.invoke(o);
                for (int i = 0; i < size; i++) {
                    Object srcDir = getAtMethod.invoke(o, i);
                    s = (String) uriToStrMethod.invoke(srcDir);
                    Pair<String,String> p = new Pair<String,String>(s, s);
                    model.addSrcDir(p);
                }
            } else {
                model.setSrcDirs(new LinkedList<Pair<String,String>>());
            }
            
            // res dirs
            field = fieldMethod.invoke(buildScriptType, RES_DIRS_FIELD);
            o = getMethod.invoke(field, buildClassInstance);
            if (o != null) {
                final Long size = (Long)sizeMethod.invoke(o);
                for (int i = 0; i < size; i++) {
                    Object resDir = getAtMethod.invoke(o, i);
                    s = (String) uriToStrMethod.invoke(resDir);
                    Pair<String,String> p = new Pair<String,String>(s, s);
                    model.addResDir(p);
                }
            } else {
                model.setResDirs(new LinkedList<Pair<String,String>>());
            }

            // indexes
            field = fieldMethod.invoke(buildScriptType, INDEXES_FIELD);
            o = getMethod.invoke(field, buildClassInstance);
            if (o != null) {
                Object keys = mapKeysMethod.invoke(o);
                final Long size = (Long)sizeMethod.invoke(keys);
                for (int i = 0; i < size; i++) {
                    final String key = (String) getAtMethod.invoke(keys, i);
                    final String val = (String) mapGetKeyMethod.invoke(o, key);
                    model.addIndex(new Pair<String,String>(key,val));
                }
            } else {
                model.setIndexes(new LinkedList<Pair<String,String>>());
            }

            // metas
            field = fieldMethod.invoke(buildScriptType, METAS_FIELD);
            o = getMethod.invoke(field, buildClassInstance);
            if (o != null) {
                Object keys = mapKeysMethod.invoke(o);
                final Long size = (Long)sizeMethod.invoke(keys);
                for (int i = 0; i < size; i++) {
                    final String key = (String) getAtMethod.invoke(keys, i);
                    final String val = (String) mapGetKeyMethod.invoke(o, key);
                    model.addMeta(new Pair<String,String>(key,val));
                }
            } else {
                model.setMetas(new LinkedList<Pair<String,String>>());
            }
            return model;
        } catch (Exception e) {
            logger.warn("Failed to parse build script metadata", e);
            return null;
        } finally {
            cl = null;
        }
    }
}
