package com.ericsson.cifwk.taf.ui.sdk;


import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.AbstractBrowserAwareTest;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class MouseTest extends AbstractBrowserAwareTest {


    @Test
    public void mouseOverAndMove() {
        ViewModel view = openGenericView();
        UiComponent mouseButton = view.getViewComponent("#mouseButtonId");
        mouseButton.mouseOver();
        Assert.assertEquals("MOUSE_OVER", view.getViewComponent("#mouseButtonEventId").getText());
        Assert.assertEquals("MOUSE_MOVE", view.getViewComponent("#mouseMoveDetectorId").getText());
    }

    @Test
    public void mouseOut() {
        ViewModel view = openGenericView();
        UiComponent mouseButton = view.getViewComponent("#mouseButtonId");
        //mouseButton.focus();
        mouseButton.mouseOver();
        mouseButton.mouseOut();
        Assert.assertEquals("MOUSE_OUT", view.getViewComponent("#mouseButtonEventId").getText());
    }

    @Test
    public void mouseDownAndUp() {
        ViewModel view = openGenericView();
        UiComponent mouseButton = view.getViewComponent("#mouseButtonId");
        mouseButton.mouseDown();
        Assert.assertEquals("MOUSE_DOWN", view.getViewComponent("#mouseButtonEventId").getText());
        mouseButton.mouseUp();
        UiToolkit.pause(500);
        Assert.assertEquals("MOUSE_UP", view.getViewComponent("#mouseButtonEventId").getText());
    }

    @Test
    public void mouseContextClick() {
        browserType = BrowserType.HEADLESS;
        ViewModel view = openGenericView();
        UiComponent mouseButton = view.getViewComponent("#mouseButtonId");
        Assert.assertFalse(view.getViewComponent("#contextMenu").isDisplayed());

        mouseButton.contextClick();

        UiComponent contextMenu = view.waitUntilComponentIsDisplayed("#contextMenu");
        Assert.assertEquals("Context Menu", contextMenu.getText());
    }

    @Test
    @Ignore("Doesn't work - see https://code.google.com/p/selenium/issues/detail?id=3604")
    public void dragAndDrop_html5() {
        ViewModel view = openGenericView();
        UiComponent dragAndDropState = view.getViewComponent("#dragAndDropStateId");
        Assert.assertEquals("", dragAndDropState.getText());

        UiComponent draggableItem = view.getViewComponent("#draggable");
        UiComponent dragTarget = view.getViewComponent("#dragTarget");

        browserTab.dragAndDropTo(draggableItem, dragTarget);

        Assert.assertEquals("DROPPED_TO_TARGET", dragAndDropState.getText());
    }

    @Test
    @Ignore("Looks like there's possible issues with phantomjs & actions, will look into it more 24/06/15")
    public void dragAndDrop_oldStyle() {
        ViewModel view = openGenericView();
        UiComponent dragAndDropState = view.getViewComponent("#dragAndDropStateId");
        Assert.assertEquals("", dragAndDropState.getText());

        UiComponent draggableItem = view.getViewComponent("#draggableOld");
        UiComponent dragTarget = view.getViewComponent("#dragTargetOld");

        browserTab.dragAndDropTo(draggableItem, dragTarget);

        Assert.assertEquals("DROPPED_TO_TARGET", dragAndDropState.getText());
    }

}
