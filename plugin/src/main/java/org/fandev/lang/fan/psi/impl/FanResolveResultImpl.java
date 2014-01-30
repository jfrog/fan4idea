package org.fandev.lang.fan.psi.impl;

import org.fandev.lang.fan.psi.api.FanResolveResult;
import org.fandev.lang.fan.psi.FanElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiSubstitutor;

/**
 * @author Dror Bereznitsky
 * @date Feb 21, 2009 3:22:52 PM
 */
public class FanResolveResultImpl implements FanResolveResult {
    private PsiElement myElement;
    private boolean myIsAccessible;
    private boolean myIsStaticsOK;
    private PsiSubstitutor mySubstitutor;

    private FanElement myCurrentFileResolveContext;

    public FanResolveResultImpl(final PsiElement element, final boolean isAccessible) {
        this(element, null, PsiSubstitutor.EMPTY, isAccessible, true);
    }

    public FanResolveResultImpl(final PsiElement element,
                                final FanElement context,
                                final PsiSubstitutor substitutor,
                                final boolean isAccessible,
                                final boolean staticsOK) {
        myCurrentFileResolveContext = context;
        myElement = element;
        myIsAccessible = isAccessible;
        mySubstitutor = substitutor;
        myIsStaticsOK = staticsOK;
    }

    public PsiElement getElement() {
        return myElement;
    }

    public boolean isValidResult() {
        return isAccessible();
    }

    public boolean isAccessible() {
        return myIsAccessible;
    }

    public PsiSubstitutor getSubstitutor() {
        return mySubstitutor;
    }
}
