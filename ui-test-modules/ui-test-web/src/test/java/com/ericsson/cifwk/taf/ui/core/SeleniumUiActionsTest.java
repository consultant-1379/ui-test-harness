package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.UiAction;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 25/05/2016
 */
@RunWith(MockitoJUnitRunner.class)
public class SeleniumUiActionsTest {

    @Mock
    private Actions actions;

    private SeleniumUiActions unit;

    @Before
    public void setUp() {
        when(actions.build()).thenReturn(mock(Action.class));
        unit = new SeleniumUiActions(actions);
    }

    @Test
    public void shouldPerformSingleAction() {
        unit.click();
        List<UiAction> tafUiActions = unit.getTafUiActions();

        assertThat(tafUiActions, hasSize(1));
        assertEquals(UiAction.Type.CLICK_ACTION, tafUiActions.get(0).getActionType());

        verify(actions, never()).click();
        unit.perform();
        verify(actions).click();
    }

    @Test
    public void shouldPerformMultipleActions() {
        WebElement webElement = mock(WebElement.class);
        Keys keyDown = Keys.F1;
        unit.click().contextClick(getMockedTextBox(webElement)).keyDown(keyDown);
        List<UiAction> tafUiActions = unit.getTafUiActions();

        assertThat(tafUiActions, hasSize(3));
        assertEquals(UiAction.Type.CLICK_ACTION, tafUiActions.get(0).getActionType());
        assertEquals(UiAction.Type.CONTEXT_CLICK_ACTION, tafUiActions.get(1).getActionType());
        assertEquals(UiAction.Type.KEY_DOWN_ACTION, tafUiActions.get(2).getActionType());

        verifyZeroInteractions(actions);
        unit.perform();
        verify(actions).click();
        verify(actions).contextClick(webElement);
        verify(actions).keyDown(keyDown);
    }

    @Test
    public void shouldClickDefault() {
        unit.click().perform();
        verify(actions).click();
    }

    @Test
    public void shouldClickOnElement() {
        WebElement webElement = mock(WebElement.class);
        unit.click(getMockedTextBox(webElement)).perform();
        verify(actions).click(webElement);
    }

    @Test
    public void shouldDoubleClickDefault() {
        unit.doubleClick().perform();
        verify(actions).doubleClick();
    }

    @Test
    public void shouldDoubleClickOnElement() {
        WebElement webElement = mock(WebElement.class);
        unit.doubleClick(getMockedTextBox(webElement)).perform();
        verify(actions).doubleClick(webElement);
    }

    @Test
    public void shouldContextClickDefault() {
        unit.contextClick().perform();
        verify(actions).contextClick();
    }

    @Test
    public void shouldContextClickOnElement() {
        WebElement webElement = mock(WebElement.class);
        unit.contextClick(getMockedTextBox(webElement)).perform();
        verify(actions).contextClick(webElement);
    }

    @Test
    public void shouldMouseDown() {
        unit.mouseDown().perform();
        verify(actions).clickAndHold();
    }

    @Test
    public void shouldMouseDownOnElement() {
        WebElement webElement = mock(WebElement.class);
        unit.mouseDown(getMockedTextBox(webElement)).perform();
        verify(actions).clickAndHold(webElement);
    }

    @Test
    public void shouldMouseUp() {
        unit.mouseUp().perform();
        verify(actions).release();
    }

    @Test
    public void shouldMouseUpOnElement() {
        WebElement webElement = mock(WebElement.class);
        unit.mouseUp(getMockedTextBox(webElement)).perform();
        verify(actions).release(webElement);
    }

    @Test
    public void shouldMouseOverElement() {
        WebElement webElement = mock(WebElement.class);
        unit.mouseOver(getMockedTextBox(webElement)).perform();
        verify(actions).moveToElement(webElement);
    }

    @Test
    public void shouldKeyDown() {
        Keys keyDown = Keys.ALT;
        unit.keyDown(keyDown).perform();
        verify(actions).keyDown(keyDown);
    }

    @Test
    public void shouldKeyDownOnElement() {
        WebElement webElement = mock(WebElement.class);
        Keys keyDown = Keys.ALT;
        unit.keyDown(getMockedTextBox(webElement), keyDown).perform();
        verify(actions).keyDown(webElement, keyDown);
    }

    @Test
    public void shouldKeyUp() {
        Keys keyUp = Keys.ALT;
        unit.keyUp(keyUp).perform();
        verify(actions).keyUp(keyUp);
    }

    @Test
    public void shouldKeyUpOnElement() {
        WebElement webElement = mock(WebElement.class);
        Keys keyDown = Keys.ALT;
        unit.keyUp(getMockedTextBox(webElement), keyDown).perform();
        verify(actions).keyUp(webElement, keyDown);
    }

    @Test
    public void shouldSendKeys() {
        Keys[] keys = new Keys[]{Keys.ALT, Keys.CONTROL, Keys.DELETE};
        unit.sendKeys(keys).perform();
        verify(actions).sendKeys(keys);
    }

    @Test
    public void shouldSendKeysToElement() {
        WebElement webElement = mock(WebElement.class);
        Keys[] keys = new Keys[]{Keys.ALT, Keys.CONTROL, Keys.DELETE};
        unit.sendKeys(getMockedTextBox(webElement), keys).perform();
        verify(actions).sendKeys(webElement, keys);
    }

    /**
     * Make sure UiActions.sendKeys(CharSequence...) is compatible with old version UiActions.sendKeys(CharSequence)
     */
    @Test
    public void shouldSendKeys_backwardCompatible() {
        Keys keys = Keys.ALT;
        unit.sendKeys(keys).perform();
        verify(actions).sendKeys(keys);
    }

    private TextBox getMockedTextBox(WebElement webElement) {
        TextBoxImpl uiComponent = mock(TextBoxImpl.class);
        SeleniumUiComponentStateManager stateManager = mock(SeleniumUiComponentStateManager.class);
        when(uiComponent.getStateManager()).thenReturn(stateManager);
        when(stateManager.getElement()).thenReturn(webElement);
        return uiComponent;
    }
}
