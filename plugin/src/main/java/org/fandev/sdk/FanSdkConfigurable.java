package org.fandev.sdk;

import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

/**
 * @author Dror Bereznitsky
 * @date Jan 18, 2009 5:56:55 PM
 */
public class FanSdkConfigurable implements AdditionalDataConfigurable {
    private Sdk mySdk;

    public void setSdk(final Sdk sdk) {
        this.mySdk = sdk;
    }

    public JComponent createComponent() {
        return new JPanel();
    }

    public boolean isModified() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void apply() throws ConfigurationException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void disposeUIResources() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
