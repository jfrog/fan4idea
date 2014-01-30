package org.fandev.lang.fan.psi.stubs;

import com.intellij.psi.stubs.NamedStub;
import org.fandev.lang.fan.psi.api.statements.typeDefs.members.FanSlot;

/**
 * @author Dror Bereznitsky
 * @date Jan 10, 2009 6:53:20 PM
 */
public interface FanSlotStub<T extends FanSlot> extends NamedStub<T> {
    String[] getFacetNames();
}