package org.fandev.lang.fan.psi.api;

import com.intellij.psi.ResolveResult;
import com.intellij.psi.PsiSubstitutor;

/**
 * @author Dror Bereznitsky
 * @date Feb 21, 2009 3:16:22 PM
 */
public interface FanResolveResult extends ResolveResult {
    public static final FanResolveResult[] EMPTY_ARRAY = new FanResolveResult[0];

    PsiSubstitutor getSubstitutor();
}
