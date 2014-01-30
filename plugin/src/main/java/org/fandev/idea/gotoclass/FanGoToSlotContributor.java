/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fandev.idea.gotoclass;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;
import org.fandev.lang.fan.psi.stubs.index.FanSlotNameIndex;

import java.util.Collection;

/**
 * @author freds
 * @date Jan 27, 2009
 */
public class FanGoToSlotContributor implements ChooseByNameContributor {
    public String[] getNames(final Project project, final boolean includeNonProjectItems) {
        final Collection<String> slotNames = StubIndex.getInstance().getAllKeys(FanSlotNameIndex.KEY, project);
        return slotNames.toArray(new String[0]);
    }

    @SuppressWarnings({"SuspiciousToArrayCall"})
    public NavigationItem[] getItemsByName(final String name, final String pattern, final Project project,
           final boolean includeNonProjectItems) {
        final GlobalSearchScope scope = includeNonProjectItems ? null : GlobalSearchScope.projectScope(project);
        final Collection<FanSlot> slots =
                StubIndex.getInstance().get(FanSlotNameIndex.KEY, name, project, scope);
        return slots.toArray(new NavigationItem[0]);
    }
}