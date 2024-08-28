package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LinkTest extends UiComponentTest {
    private Link link;

    @Before
    public void setUp() {
        BasicComponentsView view = openComponentsView();
        this.link = view.getLink();
    }

    @Test
    public void getText() {
        Assert.assertEquals("Sample link", link.getText());
    }

    @Test
    public void getUrl() {
        Assert.assertEquals("http://www.ericsson.se/", link.getUrl());
    }

    @Override
    protected UiComponent getComponent() {
        return link;
    }

    @Override
    // Need to disable this test here, otherwise WebDriver will start connecting to real host
    public void click() {
    }

}
