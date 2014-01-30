package org.fandev.module.pod;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.fandev.sdk.FanSdkType;
import org.fandev.utils.VirtualFileUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Dror Bereznitsky
 * @date Jan 26, 2009 11:46:14 PM
 */
public class PodModel {
    private String name;
    private String description;
    private String version;
    private String buildScriptName;
    private VirtualFile buildScriptFile;
    private final List<String> dependencies;
    private final List<Pair<String,String>> srcDirs;
    private final List<Pair<String,String>> resDirs;
    private boolean docApi;
    private boolean docSrc;
    private final List<Pair<String,String>> indexes;
    private final List<Pair<String,String>> metas;
    private String outDir;
    public static final String BUILD_FAN = "build.fan";
    //public static final String POD_FAN = "pod.fan";

    public PodModel() {
        name = "";
        description = "";
        version = "1.0.0";
        setBuildScriptName( BUILD_FAN);
        dependencies = new LinkedList<String>();
        srcDirs = new LinkedList<Pair<String,String>>();
        resDirs = new LinkedList<Pair<String,String>>();
        indexes = new LinkedList<Pair<String,String>>();
        metas = new LinkedList<Pair<String,String>>();
        docSrc=false;
        docApi=true;
        outDir="out/";
        //dependencies.add("sys 1.0");
        //srcDirs.add(new Pair<String,String>("fan/", "fan/"));
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getBuildScriptName() {
        return buildScriptName;
    }

    public void setBuildScriptName(final String buildScriptName) {
        this.buildScriptName = buildScriptName;
    }

    public VirtualFile getBuildScriptFile() {
        return buildScriptFile;
    }

    public void setBuildScriptFile(final VirtualFile buildScriptFile) {
        this.buildScriptFile = buildScriptFile;
    }

    public List<String> getDependencies() {
        if (dependencies.isEmpty()) {
            dependencies.add("sys 1.0");
        }
        return dependencies;
    }

    public List<String> getDeclaredDependencies() {
        return dependencies;
    }

    public void setDependencies(final List<String> dependencies) {
        this.dependencies.clear();
        this.dependencies.addAll(dependencies);
    }

    public void addDependency(final String dependency) {
        this.dependencies.add(dependency);
    }

    public void removeDependency(final String dependency) {
        this.dependencies.remove(dependency);
    }

    public List<Pair<String,String>> getSrcDirs() {
        if (srcDirs.isEmpty()) {
            srcDirs.add(new Pair<String,String>("fan/", "fan/"));
        }
        return srcDirs;
    }

    public void setSrcDirs(final List<Pair<String,String>> srcDirs) {
        this.srcDirs.clear();
        this.srcDirs.addAll(srcDirs);
    }

    public void addSrcDir(final Pair<String,String> srcDir) {
        this.srcDirs.add(srcDir);
    }

    public void removeSrcDir(final Pair<String,String> srcDir) {
        this.srcDirs.remove(srcDir);
    }
    public List<Pair<String,String>> getResDirs() {
        return resDirs;
    }

    public void setResDirs(final List<Pair<String,String>> resDirs) {
        this.resDirs.clear();
        this.resDirs.addAll(resDirs);
    }

    public void addResDir(final Pair<String,String> resDir) {
        this.resDirs.add(resDir);
    }

    public void removeResDir(final Pair<String,String> resDir) {
        this.resDirs.remove(resDir);
    }
    public List<Pair<String,String>> getIndexes() {
        return indexes;
    }

    public void setIndexes(final List<Pair<String,String>> indexes) {
        this.indexes.clear();
        this.indexes.addAll(indexes);
    }

    public void addIndex(final Pair<String,String> index) {
        this.indexes.add(index);
    }

    public void removeIndex(final Pair<String,String> index) {
        this.indexes.remove(index);
    }
    public List<Pair<String,String>> getMetas() {
        return metas;
    }

    public void setMetas(final List<Pair<String,String>> metas) {
        this.metas.clear();
        this.metas.addAll(metas);
    }

    public void addMeta(final Pair<String,String> meta) {
        this.metas.add(meta);
    }

    public void removeMeta(final Pair<String,String> meta) {
        this.metas.remove(meta);
    }
    public void setOutDir(final String value) {
        outDir = value;
    }
    public String getOutDir() {
        return outDir;
    }
    public boolean getDocApi() {
        return docApi;
    }
    public void setDocApi(final boolean value) {
        docApi=value;
    }
    public boolean getDocSrc() {
        return docSrc;
    }
    public void setDocSrc(final boolean value) {
        docSrc=value;
    }
}
