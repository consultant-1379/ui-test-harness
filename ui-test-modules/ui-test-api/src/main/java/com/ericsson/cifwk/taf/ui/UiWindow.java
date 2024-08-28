package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.google.common.base.Predicate;

/**
 * Abstraction of UI window
 */
public interface UiWindow {
    /**
     * Checks if the window is closed
     *
     * @return <code>true</code> if the window is closed
     */
    boolean isClosed();

    /**
     * Returns the generic view instance. Use it instead of {@link UiWindow#getView(Class)} if you
     * don't need to create mappings, but would rather prefer to look up the components dynamically.
     *
     * @return generic view instance
     */
    ViewModel getGenericView();

    /**
     * Returns an instance of {@link ViewModel} subclass, mapped to
     * the contents of the currently opened page.
     *
     * @param clazz a concrete subclass of {@link ViewModel}
     * @return an instance of {@link ViewModel} subclass, with autowired
     * attributes mapped to the contents of the currently opened page.
     */
    <T extends ViewModel> T getView(Class<T> clazz);

    /**
     * Returns the current window descriptor
     *
     * @return current window descriptor
     */
    String getWindowDescriptor();

    /**
     * Returns the currently opened dialog message box (alert/info/confirmation)
     *
     * @return the currently opened dialog message box (alert/info/confirmation). <code>null</code> is returned if no dialog box is opened.
     */
    MessageBox getMessageBox();

    /**
     * Suspends the current execution flow until the component, specified by <code>selector</code>
     * (and default selector type) is displayed - or operation times out.
     *
     * @param selector        selector to use for element search
     * @param timeoutInMillis max time to search, in milliseconds
     * @return found component. If many components are found, the first is returned.
     * @throws <code>WaitTimedOutException</code> if operation times out
     */
    UiComponent waitUntilComponentIsDisplayed(String selector, long timeoutInMillis);

    /**
     * Suspends the current execution flow until the component, specified by <code>selector</code>
     * and <code>selectorType</code> selector type, is displayed - or operation times out.
     *
     * @param selector        selector to use for element search
     * @param timeoutInMillis max time to search, in milliseconds
     * @return found component. If many components are found, the first is returned.
     * @throws <code>WaitTimedOutException</code> if operation times out
     */
    UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String selector, long timeoutInMillis);

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * is displayed or operation times out.
     *
     * @param component       that needs to appear
     * @param timeoutInMillis max time to search, in milliseconds
     * @return found component. If many components are found, the first is returned.
     * @throws <code>WaitTimedOutException</code> if operation times out
     */
    UiComponent waitUntilComponentIsDisplayed(UiComponent component, long timeoutInMillis);

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * disappears or operation times out.
     *
     * @param component       that needs to disappear
     * @param timeoutInMillis max time to wait, in milliseconds
     * @return given component.
     * @throws <code>WaitTimedOutException</code> if operation times out
     */
    UiComponent waitUntilComponentIsHidden(UiComponent component, long timeoutInMillis);

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * satisfies given predicate or operation times out. Returns given component.
     *
     * @param component       that needs to satisfy given predicate
     * @param predicate       that needs to be satisfied by given component.
     *                        Please see <link>UiComponentPredicates</link> for examples.
     * @param timeoutInMillis max time to wait, in milliseconds
     * @return given component.
     * @throws <code>WaitTimedOutException</code> if operation times out
     */
    <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate, long timeoutInMillis);

    /**
     * Suspends the current execution flow until given predicate or operation times out.
     *
     * @param predicate       that needs to be satisfied to return
     * @param timeoutInMillis max time to wait, in milliseconds
     * @throws <code>WaitTimedOutException</code> if operation times out
     */
    void waitUntil(GenericPredicate predicate, long timeoutInMillis);

    /**
     * Maximizes this window
     */
    void maximize();

    /**
     * Returns the size of this window
     *
     * @return size of this window
     */
    UiComponentSize getSize();

    /**
     * Requests the underlying framework to evaluate the <code>expression</code> and returns the evaluation result
     *
     * @param expression expression that underlying framework understands
     * @return evaluation result
     */
    Object evaluate(String expression);

    /**
     * Drags <code>object</code> and drops it on <code>target</code>
     *
     * @param object object to drag
     * @param target target to drop the source object to
     */
    void dragAndDropTo(UiComponent object, UiComponent target);

    /**
     * Returns a string implementation of clipboard content.
     *
     * @return string implementation of clipboard content
     */
    String copy();

    /**
     * Sends a keystroke to an underlying window
     *
     * @param key
     * @param modifiers
     */
    void sendKey(int key, int... modifiers);

    /**
     * Sends a keystrokes combination to an underlying window
     *
     * @param keys
     * @param modifiers
     */
    void sendKeys(String keys, int... modifiers);

    /**
     * Takes screenshot of the current UI content.
     *
     * @param name name of the screenshot to appear in the Allure report
     */
    void takeScreenshot(String name);

    /**
     * Returns the window title
     *
     * @return window title
     */
    String getTitle();

}
