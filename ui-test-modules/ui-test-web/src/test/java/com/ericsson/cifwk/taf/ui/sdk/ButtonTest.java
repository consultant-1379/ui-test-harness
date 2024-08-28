package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ButtonTest extends UiComponentTest {
    private Button button;
    private BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
        this.button = view.getButton();
    }

    @Test
    public void getText() {
        Assert.assertEquals("ButtonCaption", button.getText());
        Assert.assertEquals("Show", view.getButton("#timestampGenerator").getText());
    }

    @Override
    protected UiComponent getComponent() {
        return button;
    }
}
