package org.fandev.debugger;

import com.intellij.ui.classFilter.DebuggerClassFilterProvider;
import com.intellij.ui.classFilter.ClassFilter;
import java.util.List;
import java.util.ArrayList;

/**
 * Date: Sep 3, 2009
 * Time: 12:17:52 AM
 *
 * @author Dror Bereznitsky
 */
public class FanDebuggerClassFilterProvider implements DebuggerClassFilterProvider {
    public List<ClassFilter> getFilters() {
        final ArrayList<ClassFilter> list = new ArrayList<ClassFilter>();
        return list;
    }
}
