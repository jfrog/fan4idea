package org.fandev.lang.fan;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.debugger.DebuggerManager;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessListener;
import com.intellij.debugger.engine.SuspendContext;
import com.intellij.util.Function;
import com.intellij.xdebugger.DefaultDebugProcessHandler;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RemoteConnection;
import com.intellij.execution.ExecutionException;
import org.jetbrains.annotations.NotNull;
import org.fandev.debugger.FanPositionManager;

/**
 * Date: Sep 2, 2009
 * Time: 11:52:56 PM
 *
 * @author Dror Bereznitsky
 */
public class FanLoader implements ApplicationComponent {
    static {
        System.setProperty("fan.debug", "true");
    }

    @NotNull
    public String getComponentName() {
        return "fan.support.loader";
    }

    public void initComponent() {
        ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter() {
            public void projectOpened(final Project project) {
                DebuggerManager.getInstance(project).registerPositionManagerFactory(new Function<DebugProcess, PositionManager>() {
                    public PositionManager fun(final DebugProcess debugProcess) {
                        return new FanPositionManager(debugProcess);
                    }
                });               
            }
        });
    }

    public void disposeComponent() {
        
    }
}
