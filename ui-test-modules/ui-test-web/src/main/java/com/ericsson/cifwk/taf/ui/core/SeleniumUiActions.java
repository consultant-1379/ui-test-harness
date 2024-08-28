package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.UiAction;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by ejambuc on 23/10/2014.
 */
public class SeleniumUiActions implements UiActions {

    private List<UiAction> tafUiActions;

    private Actions compositeActions;

    public SeleniumUiActions(WebDriver driver, List<UiAction> actions) {
        this.tafUiActions = actions;
        this.compositeActions = new Actions(driver);
    }

    public SeleniumUiActions(WebDriver driver) {
        this(driver, Lists.<UiAction>newArrayList());
    }

    @VisibleForTesting
    SeleniumUiActions(Actions compositeActions) {
        this.tafUiActions = Lists.newArrayList();
        this.compositeActions = compositeActions;
    }

    @Override
    public UiActions click(UiComponent clickable) {
        return action(new UiAction(clickable, UiAction.Type.CLICK_ACTION));
    }

    @Override
    public UiActions click() {
        return action(new UiAction(UiAction.Type.CLICK_ACTION));
    }

    @Override
    public UiActions contextClick(UiComponent clickable) {
        return action(new UiAction(clickable, UiAction.Type.CONTEXT_CLICK_ACTION));
    }

    @Override
    public UiActions contextClick() {
        return action(new UiAction(UiAction.Type.CONTEXT_CLICK_ACTION));
    }

    @Override
    public UiActions mouseDown(UiComponent clickable) {
        return action(new UiAction(clickable, UiAction.Type.MOUSE_DOWN_ACTION));
    }

    @Override
    public UiActions mouseDown() {
        return action(new UiAction(UiAction.Type.MOUSE_DOWN_ACTION));
    }

    @Override
    public UiActions mouseUp() {
        return action(new UiAction(UiAction.Type.MOUSE_UP_ACTION));
    }

    @Override
    public UiActions mouseUp(UiComponent clickable) {
        return action(new UiAction(clickable, UiAction.Type.MOUSE_UP_ACTION));
    }

    @Override
    public UiActions mouseOver(UiComponent clickable) {
        return action(new UiAction(clickable, UiAction.Type.MOUSE_OVER_ACTION));
    }

    @Override
    public UiActions keyDown(CharSequence key) {
        return action(new UiAction(UiAction.Type.KEY_DOWN_ACTION, key));
    }

    @Override
    public UiActions keyDown(UiComponent component, CharSequence key) {
        return action(new UiAction(component, UiAction.Type.KEY_DOWN_ACTION, key));
    }

    @Override
    public UiActions keyUp(CharSequence key) {
        return action(new UiAction(UiAction.Type.KEY_UP_ACTION, key));
    }

    @Override
    public UiActions keyUp(UiComponent component, CharSequence key) {
        return action(new UiAction(component, UiAction.Type.KEY_UP_ACTION, key));
    }

    @Override
    public UiActions doubleClick() {
        return action(new UiAction(UiAction.Type.DOUBLE_CLICK_ACTION));
    }

    @Override
    public UiActions doubleClick(UiComponent clickable) {
        return action(new UiAction(clickable, UiAction.Type.DOUBLE_CLICK_ACTION));
    }

    @Override
    public UiActions sendKeys(CharSequence... keys) {
        return action(new UiAction(UiAction.Type.SEND_KEYS_ACTION, keys));
    }

    @Override
    public UiActions sendKeys(UiComponent component, CharSequence... keys) {
        return action(new UiAction(component, UiAction.Type.SEND_KEYS_ACTION, keys));
    }

    private UiActions action(UiAction uiAction) {
        tafUiActions.add(uiAction);
        return this;
    }

    @Override
    public void perform() {
        for (UiAction action : tafUiActions) {
            populateAction(action);
        }
        Action compositeAction = compositeActions.build();
        compositeAction.perform();
    }

    private void populateAction(UiAction action) {
        CharSequence[] keyStrokes = action.getKeyStrokes();
        UiAction.Type actionType = action.getActionType();
        UiComponent component = action.getComponent();
        WebElement element = (component != null) ? getWebElement(component) : null;

        switch (actionType) {
            case CLICK_ACTION:
                click(element);
                break;
            case CONTEXT_CLICK_ACTION:
                contextClick(element);
                break;
            case DOUBLE_CLICK_ACTION:
                doubleClick(element);
                break;
            case SEND_KEYS_ACTION:
                sendKeys(keyStrokes, element);
                break;
            case KEY_UP_ACTION:
                keyUp(keyStrokes, actionType, element);
                break;
            case KEY_DOWN_ACTION:
                keyDown(keyStrokes, actionType, element);
                break;
            case MOUSE_DOWN_ACTION:
                mouseDown(element);
                break;
            case MOUSE_UP_ACTION:
                mouseUp(element);
                break;
            case MOUSE_OVER_ACTION:
                mouseOver(element);
                break;
            default:
                throw new IllegalArgumentException("Unsupported Ui Operation.");
        }
    }

    private void mouseOver(WebElement element) {
        checkArgument(element != null, "Please define the component to mouse over");
        compositeActions.moveToElement(element);
    }

    private void mouseUp(WebElement element) {
        if (element == null) {
            compositeActions.release();
        } else {
            compositeActions.release(element);
        }
    }

    private void mouseDown(WebElement element) {
        if (element == null) {
            compositeActions.clickAndHold();
        } else {
            compositeActions.clickAndHold(element);
        }
    }

    private void keyDown(CharSequence[] keyStrokes, UiAction.Type actionType, WebElement element) {
        checkArgument(keyStrokes.length > 0,
                String.format("Please define the keys to use for this operation (%s)", actionType));
        Keys keyDown = (Keys) keyStrokes[0];
        if (element == null) {
            compositeActions.keyDown(keyDown);
        } else {
            compositeActions.keyDown(element, keyDown);
        }
    }

    private void keyUp(CharSequence[] keyStrokes, UiAction.Type actionType, WebElement element) {
        checkArgument(keyStrokes.length > 0,
                String.format("Please define the keys to use for this operation (%s)", actionType));
        Keys keyUp = (Keys) keyStrokes[0];
        if (element == null) {
            compositeActions.keyUp(keyUp);
        } else {
            compositeActions.keyUp(element, keyUp);
        }
    }

    private void sendKeys(CharSequence[] keyStrokes, WebElement element) {
        if (element == null) {
            compositeActions.sendKeys(keyStrokes);
        } else {
            compositeActions.sendKeys(element, keyStrokes);
        }
    }

    private void doubleClick(WebElement element) {
        if (element == null) {
            compositeActions.doubleClick();
        } else {
            compositeActions.doubleClick(element);
        }
    }

    private void contextClick(WebElement element) {
        if (element == null) {
            compositeActions.contextClick();
        } else {
            compositeActions.contextClick(element);
        }
    }

    private void click(WebElement element) {
        if (element == null) {
            compositeActions.click();
        } else {
            compositeActions.click(element);
        }
    }

    private WebElement getWebElement(UiComponent component) {
        AbstractUiComponent abstractUiComponent = (AbstractUiComponent) component;
        SeleniumElementAwareComponent manager = (SeleniumElementAwareComponent) abstractUiComponent.getStateManager();
        return manager.getElement();
    }

    @VisibleForTesting
    List<UiAction> getTafUiActions() {
        return tafUiActions;
    }

    @VisibleForTesting
    Actions getCompositeActions() {
        return compositeActions;
    }
}
