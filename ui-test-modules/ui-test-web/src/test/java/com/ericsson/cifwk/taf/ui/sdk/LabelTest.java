package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LabelTest extends UiComponentTest {
    private Label label;

    @Before
    public void setUp() {
        BasicComponentsView view = openComponentsView();
        this.label = view.getLabel();
    }

    @Test
    public void getText() {
        // Strips the tags
        Assert.assertEquals("Div bold content", label.getText());
    }

    @Override
    protected UiComponent getComponent() {
        return label;
    }
}
