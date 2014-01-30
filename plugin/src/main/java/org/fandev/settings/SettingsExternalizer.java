package org.fandev.settings;

import com.intellij.openapi.diagnostic.Logger;
import org.fandev.utils.TextUtil;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: oleg
 * @date: Nov 8, 2006
 */
public abstract class SettingsExternalizer {
    @NonNls
    public static final String NAME = "name";
    @NonNls
    public static final String VALUE = "value";

    private static final Logger logger = Logger.getInstance("Settings");

    /**
     * @return Special ID for each externalizer
     */
    public abstract String getID();

    /**
     * Creates Map, that contains options by name
     *
     * @param elem element to extract options
     * @return Map, that contains options by name
     */
    protected EnumMap<SettingAttribute, String> buildOptionsByElement(@NotNull final Element elem) {
        //noinspection unchecked
        return buildOptionsByName(elem.getChildren(getID()));
    }

    protected EnumMap<SettingAttribute, String> buildOptionsByName(final List<Element> children) {
        final EnumMap<SettingAttribute, String> options = new EnumMap<SettingAttribute, String>(SettingAttribute.class);

        for (final Element elem : children) {
            final SettingAttribute settingAttribute =
                    SettingAttribute.byAttributeKey(getAttributeFromElement(NAME, elem));
            if (settingAttribute != null) {
                options.put(settingAttribute, getAttributeFromElement(VALUE, elem));
            } else {
                logger.warn(String.format("Unknown settings attribute: %s", elem.getAttribute(NAME).getValue()));
            }
        }

        return options;
    }

    /**
     * Gets attribute from Element.
     *
     * @param key     attribute Name
     * @param element xml element
     * @return value
     */
    @NotNull
    protected String getAttributeFromElement(@NotNull final String key,
            @NotNull final Element element) {
        final Attribute attr = element.getAttribute(key);
        return attr != null ? attr.getValue() : TextUtil.EMPTY_STRING;
    }

    /**
     * Stores attribute in Element
     *
     * @param key     attribute name
     * @param value   value
     * @param element xml element
     */
    protected void storeAttributeInElement(@NotNull final String key,
            @Nullable final String value,
            @NotNull final Element element) {
        element.setAttribute(key, value != null ? value : TextUtil.EMPTY_STRING);
    }

    /**
     * Writes option to given element
     *
     * @param attribute name of option
     * @param value     value of option
     * @param elem      elem to write
     */
    public void writeOption(@Nullable final SettingAttribute attribute, @Nullable final String value,
            @NotNull final Element elem) {
        final Element option = new Element(getID());
        option.setAttribute(NAME, attribute.attributeKey);
        option.setAttribute(VALUE, value == null ? attribute.defaultValue : value);
        elem.addContent(option);
    }
}
