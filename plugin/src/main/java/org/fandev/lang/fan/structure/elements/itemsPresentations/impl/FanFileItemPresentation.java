package org.fandev.lang.fan.structure.elements.itemsPresentations.impl;

import org.fandev.lang.fan.structure.elements.itemsPresentations.FanItemPresentation;
import org.fandev.lang.fan.structure.FanElementPresentation;
import org.fandev.lang.fan.psi.impl.FanFileImpl;

/**
 *
 * @author Dror Bereznitsky
 * @date Jan 7, 2009 4:51:47 PM
 */
public class FanFileItemPresentation extends FanItemPresentation {

    public FanFileItemPresentation(final FanFileImpl myElement) {
        super(myElement);
    }

    public String getPresentableText() {
        return FanElementPresentation.getFilePresentableText((FanFileImpl) myElement);
    }
}
