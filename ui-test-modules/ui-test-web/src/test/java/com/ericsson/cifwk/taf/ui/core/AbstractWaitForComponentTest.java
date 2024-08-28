package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.ConditionWait;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.google.common.base.Predicate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public abstract class AbstractWaitForComponentTest extends AbstractBrowserAwareTest {

    protected BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
        UiToolkit.setDefaultWaitTimeout(10_000);
    }

    @Test(expected = WaitTimedOutException.class)
    public void delayExecutionWaitingForElementToAppear_timeout() {
        // Element #quickAppearingDiv appears in 1 second after page is loaded
        getTestUnit().waitUntilComponentIsDisplayed(SelectorType.CSS,
                "#quickAppearingDiv", 200);
    }

    @Test(expected = WaitTimedOutException.class)
    public void shouldUseDefaultTimeout() {
        UiToolkit.setDefaultWaitTimeout(10);
        view.waitUntilComponentIsDisplayed(SelectorType.CSS, "#quickAppearingDiv");
    }

    @Test
    public void delayExecutionWaitingForElementToAppear_byCustomSelector() {
        UiComponent component = getTestUnit().waitUntilComponentIsDisplayed(
                SelectorType.CSS, "#quickAppearingDiv", 2000);
        validateQuickAppearingDivComponent(component);
    }

    @Test
    public void delayExecutionWaitingForElementToAppear_byDefaultSelector() {
        UiComponent component = getTestUnit().waitUntilComponentIsDisplayed(
                "#quickAppearingDiv", 2000);
        validateQuickAppearingDivComponent(component);
    }

    @Test
    public void delayExecutionWaitingForElementToAppear_byComponent() {
        UiComponent component = getTestUnit().waitUntilComponentIsDisplayed(
                view.getQuickAppearingDiv(), 2000);
        validateQuickAppearingDivComponent(component);
    }

    @Test(expected = WaitTimedOutException.class)
    public void delayExecutionWaitingForNonExistingElementToAppear() {
        Button button = view.getButton("#nonExistingButton");
        getTestUnit().waitUntilComponentIsDisplayed(button, 1000);
    }

    @Test
    public void delayExecutionWaitingForComponentToDisappear() {
        Label label = view.getHidingDiv();
        assertTrue(label.isDisplayed());
        assertSame(label, getTestUnit().waitUntilComponentIsHidden(label, 3500));
        Assert.assertFalse(label.isDisplayed());
    }

    @Test
    public void delayExecutionWaitingForNonExistingComponentToDisappear() {
        Button button = view.getButton("#nonExistingButton");
        Assert.assertFalse(button.isDisplayed());
        assertSame(button, getTestUnit().waitUntilComponentIsHidden(button, 3500));
        Assert.assertFalse(button.isDisplayed());
    }

    @Test
    public void delayExecutionWaitingForChildAdded() {
        UiComponent container = view.getViewComponent("#timeTriggered");
        int childrenCount = container.getChildren().size();
        Predicate<UiComponent> predicate = UiComponentPredicates.CHILD_ADDED
                .withCurrentChildrenCount(childrenCount);
        assertSame(container, getTestUnit().waitUntil(container, predicate, 4000));
        assertTrue(container.getChildren().size() > childrenCount);
    }

    @Test
    public void delayExecutionWaitingForTextAdded() {
        TextBox textBox = view.getTextBox("#emptyTextBox");
        assertTrue(textBox.getText().isEmpty());
        assertSame(textBox, getTestUnit().waitUntil(textBox,
                UiComponentPredicates.HAS_TEXT, 2000));
        assertFalse(textBox.getText().isEmpty());
    }

    @Test(expected = WaitTimedOutException.class)
    public void delayExecutionWaitingForNonExistingComponentTextAdded() {
        TextBox textBox = view.getTextBox("#nonExistingTextBox");
        assertSame(textBox, getTestUnit().waitUntil(textBox,
                UiComponentPredicates.HAS_TEXT, 1500));
    }

    @Test
    public void delayExecutionWaitingForElementToAppear_manyElements() {
        // Element #quickAppearingDiv appears in 1 second after page is loaded
        Assert.assertNotNull(getTestUnit().waitUntilComponentIsDisplayed(
                SelectorType.CSS, ".commonClass", 2000));
    }

    private void validateQuickAppearingDivComponent(UiComponent element) {
        Assert.assertTrue(element.isDisplayed());
        Assert.assertEquals("quickAppearingDivClass appearingDiv",
                element.getProperty("class"));
    }

    protected abstract ConditionWait getTestUnit();
}
