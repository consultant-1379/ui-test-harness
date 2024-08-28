package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RadioButtonTest extends UiComponentTest {
    private BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
    }

    @Test
    public void testGetMultipleRadioButtons() {
        List<RadioButton> radios = view.getViewComponents(SelectorType.XPATH, "//input[@name='radio1']", RadioButton.class);
        Assert.assertEquals(3, radios.size());
        for (RadioButton radio : radios) {
            if (radio.getId().equals("radio11") || radio.getId().equals("radio13")) {
                Assert.assertFalse(radio.isSelected());
            } else {
                Assert.assertTrue(radio.isSelected());
            }
        }
    }

    @Test
    public void getText() {
        Assert.assertEquals("google", getComponent().getText());
    }

    @Test
    public void getValue() {
        RadioButton radio = getComponent();
        Assert.assertEquals("google", radio.getValue());
    }

    @Override
    protected RadioButton getComponent() {
        return view.getRadioButton("#radio11");
    }

    @Override
    @Test
    public void click() {
        RadioButton radioButton11 = view.getRadioButton("#radio11");
        RadioButton radioButton12 = view.getRadioButton("#radio12");
        RadioButton radioButton13 = view.getRadioButton("#radio13");

        Assert.assertFalse(radioButton11.isSelected());
        Assert.assertTrue(radioButton12.isSelected());
        Assert.assertFalse(radioButton13.isSelected());

        radioButton11.click();

        Assert.assertTrue(radioButton11.isSelected());
        Assert.assertFalse(radioButton12.isSelected());
        Assert.assertFalse(radioButton13.isSelected());

        radioButton12.click();

        Assert.assertFalse(radioButton11.isSelected());
        Assert.assertTrue(radioButton12.isSelected());
        Assert.assertFalse(radioButton13.isSelected());

        radioButton13.click();

        Assert.assertFalse(radioButton11.isSelected());
        Assert.assertFalse(radioButton12.isSelected());
        Assert.assertTrue(radioButton13.isSelected());

    }


}
