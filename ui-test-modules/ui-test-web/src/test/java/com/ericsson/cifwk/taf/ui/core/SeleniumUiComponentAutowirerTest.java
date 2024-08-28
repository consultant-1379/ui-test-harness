package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SeleniumUiComponentAutowirerTest extends AbstractBrowserAwareTest {
    @Test
    public void testNotFoundIsNotNull() {
        BasicComponentsView view = openComponentsView();
        UiComponent notFound = view.getNotFound();
        Assert.assertNotNull(notFound);
        try {
            notFound.getText();
            Assert.fail("Exception expected");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
    }

    @Test
    public void testDisappearingElementMapping() {
        BasicComponentsView view = openComponentsView();
        Label hidingDiv = view.getHidingDiv();
        Assert.assertEquals("Text hiding in 3 seconds...", hidingDiv.getText());
        // Wait for DIV to disappear
        UiToolkit.pause(4000);
        try {
            hidingDiv.getText();
            Assert.fail("UiComponentNotFound exception expected");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
    }

    @Test
    public void testAppearingElementMapping() {
        BasicComponentsView view = openComponentsView();
        Label appearingDiv = view.getAppearingDiv();
        appearingDiv.getProperty("id");
        Assert.assertTrue("appearingDiv", appearingDiv.exists());
        Assert.assertEquals("appearingDiv", appearingDiv.getProperty("id"));
        Assert.assertEquals("commonClass appearingDiv", appearingDiv.getProperty("class"));
    }

    @Test
    public void testMultipleElementsMapping() {
        BasicComponentsView view = openComponentsView();
        List<UiComponent> multipleElementsWithSameClass = view.getMultipleElementsWithSameClass();
        Assert.assertEquals(8, multipleElementsWithSameClass.size());
        // Wait for 4 seconds for another DIV to appear
        //UI.pause(4000);
        //Assert.assertEquals(7, multipleElementsWithSameClass.size());
    }

    @Test
    public void testMultipleElementsMappingWhenNothingFound() {
        BasicComponentsView view = openComponentsView();
        List<UiComponent> nothingFound = view.getNotFoundMany();
        Assert.assertEquals(0, nothingFound.size());
    }

}
