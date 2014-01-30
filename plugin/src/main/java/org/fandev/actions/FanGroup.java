package org.fandev.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import org.fandev.lang.fan.FanBundle;
import org.fandev.icons.Icons;
import org.fandev.module.FanModuleType;

/**
 * @author Dror Bereznitsky
 * @date Apr 1, 2009 5:27:33 PM
 */
public class FanGroup extends DefaultActionGroup {
    public FanGroup() {
    super(FanBundle.message("action.group.Fan.text"), true);
    getTemplatePresentation().setDescription(FanBundle.message("action.group.Fan.description"));
    getTemplatePresentation().setIcon(Icons.FAN_24);
  }

  public void update(final AnActionEvent event) {
    super.update(event);
    final Module module = event.getData(DataKeys.MODULE);
    final Presentation presentation = event.getPresentation();
    if (module == null ||
        !(module.getModuleType() instanceof FanModuleType) ||       
        !presentation.isEnabled()) {
      presentation.setEnabled(false);
      presentation.setVisible(false);
    } else {
      presentation.setEnabled(true);
      presentation.setVisible(true);
    }
  }
}
