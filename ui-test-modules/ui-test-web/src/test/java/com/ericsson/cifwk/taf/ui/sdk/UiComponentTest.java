package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.AbstractBrowserAwareTest;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import org.junit.Assert;
import org.junit.Test;

/**
 * A base class for testing TAF UI SDK components
 */
public abstract class UiComponentTest extends AbstractBrowserAwareTest {

    protected abstract UiComponent getComponent();

    @Test
    public void click() {
        UiComponent component = getComponent();
        component.click();
    }

    @Test
    public void getSize() {
        UiComponent component = getComponent();
        UiComponentSize size = component.getSize();
        Assert.assertTrue(size.getWidth() > 0);
        Assert.assertTrue(size.getHeight() > 0);
    }
}