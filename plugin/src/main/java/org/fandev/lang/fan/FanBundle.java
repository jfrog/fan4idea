package org.fandev.lang.fan;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import com.intellij.CommonBundle;

/**
 * 
 * @author Dror Bereznitsky
 * @date Dec 22, 2008 10:28:13 PM
 */
public class FanBundle {
    private static Reference<ResourceBundle> ourBundle;

    @NonNls
    public static final String BUNDLE = "org.fandev.lang.fan.FanBundle";

    private FanBundle() {
    }

    public static String message(@NonNls @PropertyKey(resourceBundle = BUNDLE) final String key, final Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = null;
        if (ourBundle != null) {
            bundle = ourBundle.get();
        }
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE);
            ourBundle = new SoftReference<ResourceBundle>(bundle);
        }
        return bundle;
    }
}
