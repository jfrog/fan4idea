package org.fandev.sdk;

import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.options.ConfigurationException;

/**
 * @author Dror Bereznitsky
 * @date Jan 18, 2009 5:54:42 PM
 */
public class FanSdkAdditionalData implements SdkAdditionalData {
    public Object clone() throws CloneNotSupportedException {
        try {
            final FanSdkAdditionalData copy = (FanSdkAdditionalData) super.clone();
            return copy;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void checkValid(final SdkModel sdkModel) throws ConfigurationException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
