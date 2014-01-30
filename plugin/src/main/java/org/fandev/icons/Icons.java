package org.fandev.icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 3:56:47 PM
 */
public interface Icons {
    Icon POD = IconLoader.getIcon("/icons/structure/pod.png");
    Icon CLASS = IconLoader.getIcon("/icons/structure/class.png");
    Icon ABSTRACT_CLASS = IconLoader.getIcon("/icons/structure/abstract-class.png");
    Icon MIXIN = IconLoader.getIcon("/icons/structure/mixin.png");
    Icon ENUM = IconLoader.getIcon("/icons/structure/enum.png");
    Icon METHOD = IconLoader.getIcon("/icons/structure/method.png");
    Icon FIELD = IconLoader.getIcon("/icons/structure/field.png");
    Icon FAN_16 = IconLoader.getIcon("/icons/fileTypes/fan.png");
    Icon FAN_24 = IconLoader.getIcon("/icons/fan_24.png");
    Icon FAN_MODULE_OPEN = IconLoader.getIcon("/icons/modules/fan_module_opened.png");
    Icon FAN_MODULE_CLOSE = IconLoader.getIcon("/icons/modules/fan_module_closed.png");
}
