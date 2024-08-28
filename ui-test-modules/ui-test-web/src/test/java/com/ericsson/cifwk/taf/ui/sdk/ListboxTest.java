package com.ericsson.cifwk.taf.ui.sdk;


import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.core.UiProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.endsWith;

public class ListboxTest extends UiComponentTest {
    private Select listbox;
    private BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
        this.listbox = view.getSelect("#listboxId");
    }

    @Test
    public void selectOptionByValue() {
        Assert.assertEquals(1, listbox.getSelectedOptions().size());

        listbox.selectByValue("m_option2 value");
        Assert.assertEquals(2, listbox.getSelectedOptions().size());

        try {
            listbox.selectByValue("no such value");
            Assert.fail("UiComponentNotFoundException expected");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
        Assert.assertEquals(2, listbox.getSelectedOptions().size());
    }

    @Test
    public void selectOptionByTitle() {
        Assert.assertEquals(1, listbox.getSelectedOptions().size());

        listbox.selectByTitle("m_option2 title");
        Assert.assertEquals(2, listbox.getSelectedOptions().size());

        try {
            listbox.selectByTitle("no such value");
            Assert.fail("UiComponentNotFoundException expected");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
        Assert.assertEquals(2, listbox.getSelectedOptions().size());
    }

    @Test
    public void getText() {
        // Selected by default
        Assert.assertEquals("m_option3 title", listbox.getText());
        listbox.selectByValue("m_option2 value");
        try {
            listbox.getText();
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // OK
        }
    }

    @Test
    public void getProperty() {
        Assert.assertEquals("listboxName", listbox.getProperty(UiProperties.NAME));
    }

    @Test
    public void getValue() {
        Assert.assertEquals("m_option3 value", listbox.getValue());
        listbox.selectByValue("m_option2 value");
        try {
            listbox.getValue();
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // OK
        }
    }

    @Test
    public void getSelectedOptions() {
        selectOptionByValue();
        List<Option> selectedOptions = listbox.getSelectedOptions();
        Assert.assertEquals(2, selectedOptions.size());
        for (Option option : selectedOptions) {
            Assert.assertThat(option.getTitle(), endsWith("title"));
            Assert.assertThat(option.getValue(), endsWith("value"));
            Assert.assertTrue(option.isSelected());
        }
    }

    @Test
    public void getAllOptionsViaCssSelector() {
        List<UiComponent> options = view.getViewComponents("#selectId > option", UiComponent.class);
        Assert.assertEquals(3, options.size());
    }

    @Test
    public void getAllOptionsDeselectAllAndClickOnOption() {
        List<Option> options = listbox.getAllOptions();
        Assert.assertEquals(3, options.size());

        listbox.clearSelection();
        // Check getSelectedOptions()
        Assert.assertEquals(0, listbox.getSelectedOptions().size());
        // and check 1-by-1
        for (Option option : options) {
            Assert.assertFalse(option.isSelected());
        }

        Option option = listbox.getAllOptions().get(0);
        option.click();
        Assert.assertEquals(1, listbox.getSelectedOptions().size());
    }

    @Override
    protected UiComponent getComponent() {
        return listbox;
    }

}