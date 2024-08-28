package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GenericViewModelTest extends AbstractBrowserAwareTest {

    private ViewModel genericView;

    @Before
    public void setUp() {
        this.genericView = openGenericView();
    }

    @Test
    public void getViewComponents_multiple() {
        Assert.assertEquals(8, genericView.getViewComponents(".commonClass", GenericUiComponent.class).size());
        Assert.assertEquals(0, genericView.getViewComponents(".noSuchClass", GenericUiComponent.class).size());
        Assert.assertEquals(3, genericView.getViewComponents(SelectorType.XPATH, "//a", Link.class).size());
        Assert.assertEquals(0, genericView.getViewComponents(SelectorType.XPATH, "//h1", Label.class).size());
    }

    @Test
    public void getComponentCount() {
        Assert.assertEquals(8, genericView.getComponentCount(SelectorType.CSS, ".commonClass"));
        Assert.assertEquals(0, genericView.getComponentCount(SelectorType.CSS, ".noSuchClass"));
        Assert.assertEquals(3, genericView.getComponentCount(SelectorType.XPATH, "//a"));
        Assert.assertEquals(0, genericView.getComponentCount(SelectorType.XPATH, "//h1"));
    }

    @Test
    public void getViewComponents_singleComponent() {
        Assert.assertNotNull(genericView.getViewComponent(".noSuchClass", GenericUiComponent.class));
    }

    @Test
    public void hasComponent() {
        // No component
        Assert.assertFalse(genericView.hasComponent(".noSuchClass"));
        // One component
        Assert.assertTrue(genericView.hasComponent("#linkId"));
        // Many components
        Assert.assertTrue(genericView.hasComponent(SelectorType.XPATH, "//a"));
        Assert.assertTrue(genericView.hasComponent(".commonClass"));
    }

    @Test
    public void getConcreteComponent() {
        Button button = genericView.getButton("#noSuchButton");
        Assert.assertFalse(button.isDisplayed());

        TextBox textBox = genericView.getTextBox("#textBoxId");
        Assert.assertTrue(textBox.isDisplayed());
        Assert.assertEquals("Sample text", textBox.getText());
        textBox.setText("myEnteredText");
        Assert.assertEquals("myEnteredText", textBox.getText());
    }
}
