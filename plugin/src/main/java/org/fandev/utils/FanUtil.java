package org.fandev.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import fan.sys.Map;
import fan.sys.Env;
import org.fandev.actions.generation.FanTemplatesFactory;
import org.fandev.actions.generation.TemplateProperty;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.lang.fan.psi.api.statements.FanVariable;
import org.fandev.lang.fan.psi.api.statements.expressions.FanClosureExpression;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanBuildScriptDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanEnumDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanField;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;
import org.fandev.lang.fan.psi.impl.FanListReferenceType;
import org.fandev.lang.fan.psi.impl.FanMapType;
import org.fandev.module.FanModuleType;
import org.fandev.module.pod.PodModel;
import org.fandev.module.wizard.FanModuleBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 21, 2009 4:48:30 PM
 */
public class FanUtil {
    private static final Logger logger = Logger.getInstance("org.fandev.utils.FanUtil");

    static {
        System.setProperty("fan.debug", "true");
    }

    public static boolean isFanModuleType(@Nullable final Module module) {
        return module != null && FanModuleType.getInstance() == module.getModuleType();
    }

    public static Sdk getSdk(final Module module) {
        if (module != null) {
            if (FanUtil.isFanModuleType(module)) {
                return ModuleRootManager.getInstance(module).getSdk();
            }
        }
        return null;
    }

    public static void setFanHome(final Module module) {
        setFanHome(getSdk(module));
    }

    public static void setFanHome(@NotNull final Sdk moduleSdk) {
        System.setProperty("fan.home", moduleSdk.getHomePath());
    }

    public static void setFanHome(@NotNull final String home) {
        System.setProperty("fan.home", home);
    }

    @Nullable
    public static URLClassLoader getSysClassloader(@NotNull final String sdkHome) {
        final VirtualFile sysJar = VirtualFileUtil.refreshAndFindFileByLocalPath(sdkHome + "/lib/java/sys.jar");
        try {
            if (sysJar.exists()) {
                return new URLClassLoader(new URL[]{new java.io.File(sysJar.getPath()).toURI().toURL()});
            }
        } catch (Exception e) {
            logger.error("Could load sys.jar", e);
        }
        return null;
    }

    public static java.io.File getJdkHome(final Sdk moduleSdk) {
        setFanHome(moduleSdk);
        final Map env = Env.cur().vars();
        String fanJavaHome = (String) env.get("java.home");
        if (fanJavaHome != null) {
            // Fallback to current Java version
            fanJavaHome = System.getProperty("java.home");
        }
        return new java.io.File(fanJavaHome);
    }

    public static Sdk createFanJdk(final Sdk moduleSdk) {
        return JavaSdk.getInstance().createJdk("Fantom JDK", getJdkHome(moduleSdk).getAbsolutePath());
    }

    public static VariableKind getVariableKind(@NotNull final PsiVariable paramPsiVariable) {
        if (paramPsiVariable instanceof PsiField) {
            if (paramPsiVariable.hasModifierProperty("static")) {
                if (paramPsiVariable.hasModifierProperty("final")) {
                    return VariableKind.STATIC_FINAL_FIELD;
                }
                return VariableKind.STATIC_FIELD;
            }
            return VariableKind.FIELD;
        }

        if (paramPsiVariable instanceof PsiParameter) {
            if (((PsiParameter) paramPsiVariable).getDeclarationScope() instanceof PsiForeachStatement) {
                return VariableKind.LOCAL_VARIABLE;
            }
            return VariableKind.PARAMETER;
        }
        if (paramPsiVariable instanceof PsiLocalVariable) {
            return VariableKind.LOCAL_VARIABLE;
        }
        return VariableKind.LOCAL_VARIABLE;
    }

    public static VirtualFile generateBuildScript(final String contentPath, final Project project, final PodModel pod) {
        final VirtualFile contentEntryPath = VirtualFileManager.getInstance().refreshAndFindFileByUrl(
                VirtualFileUtil.constructLocalUrl(contentPath));
        VirtualFile buildScript = null;
        try {
             buildScript = ApplicationManager.getApplication().runWriteAction(new Computable<VirtualFile>() {
                public VirtualFile compute() {
                    final EnumMap<TemplateProperty, String> parameters = new EnumMap<TemplateProperty, String>(TemplateProperty.class);
                    parameters.put(TemplateProperty.NAME, pod.getName());
                    parameters.put(TemplateProperty.VERSION, pod.getVersion());
                    parameters.put(TemplateProperty.DESCRIPTION, pod.getDescription());
                    parameters.put(TemplateProperty.OUT_DIR, "`" + pod.getOutDir() + "`");
                    parameters.put(TemplateProperty.DOC_API, String.valueOf(pod.getDocApi()));
                    parameters.put(TemplateProperty.DOC_SRC, String.valueOf(pod.getDocSrc()));
                    parameters.put(TemplateProperty.POD_DEPENDS, listToString(pod.getDependencies()));
                    parameters.put(TemplateProperty.POD_SRC_DIRS, listPairToUrl(pod.getSrcDirs()));
                    parameters.put(TemplateProperty.POD_RES_DIRS, listPairToUrl(pod.getResDirs()));
                    parameters.put(TemplateProperty.METAS, listPairToString(pod.getMetas()));
                    parameters.put(TemplateProperty.INDEXES, listPairToString(pod.getIndexes()));

                    final PsiFile buildScript = FanTemplatesFactory.createFromTemplate(
                            PsiDirectoryFactory.getInstance(project).createDirectory(contentEntryPath),
                            pod.getBuildScriptName(), "FanBuildScript.fan", parameters);
                    return buildScript.getVirtualFile();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        pod.setBuildScriptFile(buildScript);
        return buildScript;
    }

    @Nullable
    public static FanTypeDefinition getContainingType(@NotNull final PsiElement element) {
        final PsiElement maybeClazz = PsiTreeUtil.getParentOfType(element, FanTypeDefinition.class, FanFile.class);
        if (FanUtil.isFanType(maybeClazz)) {
            return (FanTypeDefinition) maybeClazz;
        }
        return null;
    }

    public static boolean isFanTypeDefinition(final PsiElement element) {
        return isOfType(element, FanTypeDefinition.class);
    }

    public static boolean isFanEnumDefinition(final PsiElement element) {
        return isOfType(element, FanEnumDefinition.class);
    }

    public static boolean isFanClosure(final PsiElement element) {
         return isOfType(element, FanClosureExpression.class);
    }

    public static boolean isPsiCodeBlock(final PsiElement element) {
        return isOfType(element, PsiCodeBlock.class);
    }

    public static boolean isFanMethod(final PsiElement element) {
        return isOfType(element, FanMethod.class);
    }

    public static boolean isFanField(final PsiElement element) {
        return isOfType(element, FanField.class);
    }

    public static boolean isFanVariable(final PsiElement element) {
        return isOfType(element, FanVariable.class);
    }

    public static boolean isFanFile(final PsiElement element) {
        return isOfType(element, FanFile.class);
    }

    public static boolean isFanType(final PsiElement element) {
        return isOfType(element, FanTypeDefinition.class);
    }

    public static boolean isFanBuildScript(final PsiElement element) {
        return isOfType(element, FanBuildScriptDefinition.class);
    }

    public static boolean isFanMapType(final PsiType element) {
        return isOfType(element, FanMapType.class);
    }

    public static boolean isFanListType(final PsiType element) {
        return isOfType(element, FanListReferenceType.class);
    }

    public static boolean isOfType(final PsiElement element, final Class<?> type) {
        return element != null && type.isAssignableFrom(element.getClass());
    }

    public static boolean isOfType(final PsiType element, final Class<?> type) {
        return element != null && type.isAssignableFrom(element.getClass());
    }
    // helpers for converting FanModel parameter settings to and from strings to sets.
    public static String listToString(final List<String> set) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (final String s : set) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("\"");
            sb.append(s);
            sb.append("\"");
            i++;
        }
        return sb.toString();
    }
    public static String listPairToUrl(final List<Pair<String,String>> list) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (final Pair<String,String> s : list) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("`");
            sb.append(s.getFirst());
            if (!s.getFirst().endsWith("/")) {
                sb.append("/");
            }
            sb.append("`");
            i++;
        }
        return sb.toString();
    }
    public static String listPairToString(final List<Pair<String,String>> set) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (final Pair<String,String> s : set) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("\"");
            sb.append(s.getFirst());
            sb.append("\"");
            sb.append(" : ");
            sb.append("\"");
            sb.append(s.getSecond());
            sb.append("\"");
            i++;
        }
        return sb.toString();
    }
    public static String listPairToFirstString(final List<Pair<String,String>> set) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (final Pair<String,String> s : set) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(s.getFirst());
            i++;
        }
        return sb.toString();
    }
    public static List<String> stringToList(String value) {
        final List<String> list = new LinkedList<String>();
        if (TextUtil.isEmpty(value)) {
            return list;
        }
        value = value.replaceAll("\"", "");
        final String[] values = value.split(",");
        for (int i = 0; i < values.length; i++) {
            list.add(values[i].trim());
        }
        return list;
    }
    public static List<Pair<String,String>> urlToListPair(String value) {
        final List<Pair<String,String>> list = new LinkedList<Pair<String,String>>();
        if (TextUtil.isEmpty(value)) {
            return list;
        }
        value = value.replaceAll("`", "");
        final String[] values = value.split(",");
        for (int i = 0; i < values.length; i++) {
            final String s = values[i].trim();
            list.add(new Pair<String,String>(s,s));
        }
        return list;
    }
    public static List<Pair<String,String>> stringToListPair(String value) {
        final List<Pair<String,String>> list = new LinkedList<Pair<String,String>>();
        if (TextUtil.isEmpty(value)) {
            return list;
        }
        value = value.replaceAll("\"", "");
        final String[] values = value.split(",");
        for (int i = 0; i < values.length; i++) {
            final String[] element = values[i].split(":{1}?");
            if (element.length > 1) {
                String t = element[1];
                if (element.length > 2) {
                    final StringBuilder sb = new StringBuilder();
                    for (int j = 1; j< element.length; j++) {
                        if (TextUtil.isEmpty(element[j])) {
                            sb.append("::");
                        } else {
                            sb.append(element[j]);
                        }
                    }
                    t = sb.toString();
                }
                final Pair<String,String> pair = new Pair<String,String>(element[0].trim(),t.trim());
                list.add(pair);
            }
        }
        return list;
    }
    public static List<Pair<String,String>> firstStringToListPair(String value) {
        final List<Pair<String,String>> list = new LinkedList<Pair<String,String>>();
        if (TextUtil.isEmpty(value)) {
            return list;
        }
        value = value.replaceAll("\"", "");
        final String[] values = value.split(",");
        for (int i = 0; i < values.length; i++) {
            final String s = values[i].trim();
            final Pair<String,String> pair = new Pair<String,String>(s,s);
            list.add(pair);
        }
        return list;
    }
}
