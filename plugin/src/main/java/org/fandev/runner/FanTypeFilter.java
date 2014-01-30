package org.fandev.runner;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import org.fandev.index.FanIndex;
import org.fandev.lang.fan.psi.FanFile;
import org.fandev.lang.fan.psi.api.statements.typeDefs.FanTypeDefinition;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanMethod;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Date: Sep 23, 2009
 * Time: 11:54:45 PM
 *
 * @author Dror Bereznitsky
 */
public class FanTypeFilter implements Filter {
    private Project project;
    private FanIndex index;
    // pattern for matching Fan identifiers: pod::type.method
    private final Pattern identifier = Pattern.compile("^.*(\\b[a-zA-Z0-9_]+::[a-zA-Z0-9_\\.]+).*\n");
    // pattern for mathcing line number inside a Fan file, for example: (HelloWorld.fan:9)
    private final Pattern lineInFile = Pattern.compile("^.*(\\(.*\\.fan:)(\\d+)(\\)).*\n");

    public FanTypeFilter(final Project project) {
        this.project = project;
        this.index = project.getComponent(FanIndex.class);
    }

    public Result applyFilter(final String line, final int entireLength) {
        Matcher m = identifier.matcher(line);
        if (m.matches()) {
            final String id = m.group(1);
            final int start = line.indexOf(id);

            String typeName = id.substring(id.indexOf("::") + 2);
            String methodName = null;
            final int dotIdx = typeName.indexOf(".");
            if (dotIdx > -1) {
                methodName = typeName.substring(dotIdx + 1);
                typeName = typeName.substring(0, dotIdx);
            }

            final FanFile file = index.getFanFileByTypeName(typeName);
            if (file != null) {
                int offset;

                m = lineInFile.matcher(line);
                if (m.matches()) {
                    // We have the line number in the stack trace
                    offset = Integer.valueOf(m.group(2)).intValue() - 1;
                    return new Result(
                            entireLength - line.length() + start,
                            entireLength - line.length() + start + id.length(),
                            new OpenFileHyperlinkInfo(new OpenFileDescriptor(project, file.getVirtualFile(), offset, 0)));
                } else {
                    // No line number, get the type or method offset inside the file
                    final FanTypeDefinition typeDef = file.getTypeByName(typeName);
                    offset = typeDef.getTextOffset();

                    if (methodName != null) {
                        final FanMethod fanMethod = typeDef.getMethodByName(methodName);
                        if (fanMethod != null) {
                            offset = fanMethod.getTextOffset();
                        }
                    }
                    return new Result(
                            entireLength - line.length() + start,
                            entireLength - line.length() + start + id.length(),
                            new OpenFileHyperlinkInfo(new OpenFileDescriptor(project, file.getVirtualFile(), offset)));
                }

            }
        }
        return null;
    }
}
