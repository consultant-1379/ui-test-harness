package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

public class TextBoxTest extends UiComponentTest {
    private TextBox textBox;

    @Before
    public void setUp() {
        BasicComponentsView view = openComponentsView();
        this.textBox = view.getTextBox();
    }

    @Test
    public void getText() {
        Assert.assertEquals("Sample text", textBox.getText());
    }

    @Test
    public void setText() {
        final String text = "New text entered";
        textBox.setText(text);
        Assert.assertEquals(text, textBox.getText());
    }

    @Test
    public void getProperty() {
        Assert.assertEquals("textBoxName", textBox.getProperty(UiProperties.NAME));
    }

    @Override
    protected UiComponent getComponent() {
        return textBox;
    }

    @Test
    public void testName() throws Exception {
        assertThat(textBox.getText(), is("Sample text"));
        textBox.clear();
        assertThat(textBox.getText(), isEmptyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTextAsNull() {
        String text = null;
        textBox.setText(text);
    }
}
