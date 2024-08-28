package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CheckBoxTest extends UiComponentTest {
    private BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
    }

    @Test
    public void testGetMultipleSelection() {
        List<CheckBox> checkBoxes = view.getViewComponents(SelectorType.XPATH, "//input[@name='checkbox1']", CheckBox.class);
        Assert.assertEquals(3, checkBoxes.size());
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.getId().equals("checkbox11") || checkBox.getId().equals("checkbox13")) {
                Assert.assertFalse(checkBox.isSelected());
            } else {
                Assert.assertTrue(checkBox.isSelected());
            }
        }
    }

    @Test
    public void getText() {
        Assert.assertEquals("orange", getComponent().getText());
    }

    @Test
    public void getValue() {
        CheckBox checkBox = getComponent();
        Assert.assertEquals("orange", checkBox.getValue());
    }

    @Override
    protected CheckBox getComponent() {
        return view.getCheckBox("#checkbox11");
    }

    @Override
    @Test
    public void click() {
        CheckBox checkBox11 = view.getCheckBox("#checkbox11");
        CheckBox checkBox12 = view.getCheckBox("#checkbox12");
        CheckBox checkBox13 = view.getCheckBox("#checkbox13");

        Assert.assertFalse(checkBox11.isSelected());
        Assert.assertTrue(checkBox12.isSelected());
        Assert.assertFalse(checkBox13.isSelected());

        checkBox11.click();

        Assert.assertTrue(checkBox11.isSelected());
        Assert.assertTrue(checkBox12.isSelected());
        Assert.assertFalse(checkBox13.isSelected());

        checkBox12.click();

        Assert.assertTrue(checkBox11.isSelected());
        Assert.assertFalse(checkBox12.isSelected());
        Assert.assertFalse(checkBox13.isSelected());

        checkBox13.click();

        Assert.assertTrue(checkBox11.isSelected());
        Assert.assertFalse(checkBox12.isSelected());
        Assert.assertTrue(checkBox13.isSelected());
    }

    @Test
    public void select() {
        CheckBox uncheckedBox = view.getCheckBox("#checkbox11");
        CheckBox checkedBox = view.getCheckBox("#checkbox12");

        Assert.assertFalse(uncheckedBox.isSelected());
        Assert.assertTrue(checkedBox.isSelected());

        uncheckedBox.select();
        checkedBox.select();

        Assert.assertTrue(uncheckedBox.isSelected());
        Assert.assertTrue(checkedBox.isSelected());
    }

    @Test
    public void deselect() {
        CheckBox uncheckedBox = view.getCheckBox("#checkbox11");
        CheckBox checkedBox = view.getCheckBox("#checkbox12");

        Assert.assertFalse(uncheckedBox.isSelected());
        Assert.assertTrue(checkedBox.isSelected());

        uncheckedBox.deselect();
        checkedBox.deselect();

        Assert.assertFalse(uncheckedBox.isSelected());
        Assert.assertFalse(checkedBox.isSelected());
    }

}
