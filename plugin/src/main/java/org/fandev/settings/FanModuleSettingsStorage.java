package org.fandev.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleServiceManager;
import com.intellij.openapi.roots.impl.storage.ClasspathStorage;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

/**
 * @author Dror Bereznitsky
 * @date Jan 28, 2009 12:02:46 AM
 */
@State(
        name = "FanModuleSettingsStorage",
        storages = {
                @Storage(
                        id = ClasspathStorage.DEFAULT_STORAGE,
                        file = "$MODULE_FILE$"
                )
        }
)
public class FanModuleSettingsStorage extends SettingsExternalizer implements PersistentStateComponent<Element> {
    private final EnumMap<SettingAttribute, String> attributeMap =
            new EnumMap<SettingAttribute, String>(SettingAttribute.class);

    public Element getState() {
        final Element element = new Element(getID());
        for (final SettingAttribute key : attributeMap.keySet()) {
            writeOption(key, attributeMap.get(key), element);
        }
        return element;
    }

    public void loadState(final Element state) {
        attributeMap.clear();
        attributeMap.putAll(buildOptionsByElement(state));
    }

    @NotNull
    public static FanModuleSettingsStorage getInstance(final Module module) {
        return ModuleServiceManager.getService(module, FanModuleSettingsStorage.class);
    }

    public String getID() {
        return "FanModuleEntry";
    }

    public String getAttributeValue(final SettingAttribute key) {
        final String value = attributeMap.get(key);
        return value != null ? value : "";
    }

    public FanModuleSettingsStorage setAttributeValue(final SettingAttribute key, final String value) {
        attributeMap.put(key, value);
        return this;
    }
}
