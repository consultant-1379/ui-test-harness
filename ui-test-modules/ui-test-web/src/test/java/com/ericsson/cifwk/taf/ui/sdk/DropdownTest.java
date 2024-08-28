package com.ericsson.cifwk.taf.ui.sdk;


import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.core.UiProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DropdownTest extends UiComponentTest {
    private Select dropdown;
    private BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
        this.dropdown = view.getSelect();
    }

    @Test
    public void selectOptionByValue() {
        dropdown.selectByValue("option2 value");
        Assert.assertEquals("option2 title", dropdown.getText());

        try {
            dropdown.selectByValue("no such value");
            Assert.fail("UiComponentNotFoundException expected");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
        Assert.assertEquals("option2 title", dropdown.getText());
    }

    @Test
    public void selectOptionByTitle() {
        dropdown.selectByTitle("option2 title");
        Assert.assertEquals("option2 title", dropdown.getText());

        try {
            dropdown.selectByTitle("no such value");
            Assert.fail("UiComponentNotFoundException expected");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
        Assert.assertEquals("option2 title", dropdown.getText());
    }

    @Test
    public void getText() {
        // Selected by default
        Assert.assertEquals("option3 title", dropdown.getText());
        dropdown.selectByValue("option2 value");
        Assert.assertEquals("option2 title", dropdown.getText());
    }

    @Test
    public void getProperty() {
        Assert.assertEquals("selectName", dropdown.getProperty(UiProperties.NAME));
    }

    @Test
    public void getValue() {
        Assert.assertEquals("option3 value", dropdown.getValue());
        dropdown.selectByTitle("option2 title");
        Assert.assertEquals("option2 value", dropdown.getValue());
    }

    @Test
    public void getAllOptions() {
        List<UiComponent> options = view.getViewComponents("#selectId > option", UiComponent.class);
        Assert.assertEquals(3, options.size());
    }

    @Override
    protected UiComponent getComponent() {
        return dropdown;
    }

}