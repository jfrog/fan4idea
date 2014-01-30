package org.fandev.settings;

/**
 * Date: Aug 12, 2009
 * Time: 11:33:03 PM
 *
 * @author Dror Bereznitsky
 */
public enum SettingAttribute {
    BUILD_SCRIPT("buildScript", ""),
    POD_FILE("podFile", "");

    public final String attributeKey;
    public final String defaultValue;

    SettingAttribute(final String attributeKey, final String defaultValue) {
        this.attributeKey = attributeKey;
        this.defaultValue = defaultValue;
    }

    public static SettingAttribute byAttributeKey(final String key) {
        for (final SettingAttribute value : values()) {
            if (value.attributeKey.equals(key)) {
                return value;
            }
        }
        return null;
    }
}
