package org.fandev.lang.fan.resolve;

import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.psi.*;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Key;
import com.intellij.util.containers.HashMap;
import com.intellij.util.containers.HashSet;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;

/**
 * Date: Sep 29, 2009
 * Time: 11:27:44 PM
 *
 * @author Dror Bereznitsky
 */
public class CollectClassMembersUtil {
    private static final Key<CachedValue<Pair<Map<String, CandidateInfo>,
            Map<String, List<CandidateInfo>>>>> CACHED_MEMBERS = Key.create("CACHED_CLASS_MEMBERS");

    private static final Key<CachedValue<Pair<Map<String, CandidateInfo>,
            Map<String, List<CandidateInfo>>>>> CACHED_MEMBERS_INCLUDING_SYNTHETIC = Key.create("CACHED_MEMBERS_INCLUDING_SYNTHETIC");


    public static Map<String, List<CandidateInfo>> getAllMethods(final PsiClass aClass, final boolean includeSynthetic) {
        final Key<CachedValue<Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>>> key = includeSynthetic ?
                CACHED_MEMBERS_INCLUDING_SYNTHETIC : CACHED_MEMBERS;
        CachedValue<Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>> cachedValue = aClass.getUserData(key);
        if (cachedValue == null) {
            cachedValue = buildCache(aClass, includeSynthetic);
        }

        final Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>> value = cachedValue.getValue();
        assert value != null;
        return value.getSecond();
    }

    public static Map<String, CandidateInfo> getAllFields(final PsiClass aClass) {
        CachedValue<Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>> cachedValue = aClass.getUserData(CACHED_MEMBERS);
        if (cachedValue == null) {
            cachedValue = buildCache(aClass, false);
        }

        final Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>> value = cachedValue.getValue();
        assert value != null;
        return value.getFirst();
    }

    private static CachedValue<Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>> buildCache(final PsiClass aClass, final boolean includeSynthetic) {
        return aClass.getManager().getCachedValuesManager().createCachedValue(new CachedValueProvider<Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>>() {
            public Result<Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>> compute() {
                final Map<String, CandidateInfo> allFields = new HashMap<String, CandidateInfo>();
                final Map<String, List<CandidateInfo>> allMethods = new HashMap<String, List<CandidateInfo>>();

                processClass(aClass, allFields, allMethods, new HashSet<PsiClass>(), PsiSubstitutor.EMPTY, includeSynthetic);
                return new Result<Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>>(new Pair<Map<String, CandidateInfo>, Map<String, List<CandidateInfo>>>(allFields, allMethods), PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
            }
        }, false);
    }

    private static void processClass(final PsiClass aClass, final Map<String, CandidateInfo> allFields, final Map<String, List<CandidateInfo>> allMethods, final Set<PsiClass> visitedClasses, final PsiSubstitutor substitutor, final boolean includeSynthetic) {
        if (visitedClasses.contains(aClass)) {
            return;
        }
        visitedClasses.add(aClass);

        for (final PsiField field : aClass.getFields()) {
            final String name = field.getName();
            if (!allFields.containsKey(name)) {
                allFields.put(name, new CandidateInfo(field, substitutor));
            }
        }

        for (final PsiMethod method : includeSynthetic || !(aClass instanceof FanTypeDefinition) ? aClass.getMethods() : ((FanTypeDefinition) aClass).getFanMethods()) {
            addMethod(allMethods, method, substitutor);
        }

        for (final PsiClassType superType : aClass.getSuperTypes()) {
            final PsiClass superClass = superType.resolve();
            if (superClass != null) {
                final PsiSubstitutor superSubstitutor = TypeConversionUtil.getSuperClassSubstitutor(superClass, aClass, substitutor);
                processClass(superClass, allFields, allMethods, visitedClasses, superSubstitutor, includeSynthetic);
            }
        }
    }

    private static void addMethod(final Map<String, List<CandidateInfo>> allMethods, final PsiMethod method, final PsiSubstitutor substitutor) {
        final String name = method.getName();
        List<CandidateInfo> methods = allMethods.get(name);
        if (methods == null) {
            methods = new ArrayList<CandidateInfo>();
            allMethods.put(name, methods);
            methods.add(new CandidateInfo(method, substitutor));
        } else {
            methods.add(new CandidateInfo(method, substitutor));
        }
    }
}
