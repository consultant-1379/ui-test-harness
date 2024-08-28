package com.ericsson.cifwk.taf.ui.core;

import java.util.List;

import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.MenuItem;
import com.ericsson.cifwk.taf.ui.sdk.UiComponentContainer;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

/**
 * Common interface of TAF UI components that represent graphical controls
 */
public interface UiComponent extends UiComponentContainer {

    /**
     * Returns the ID of this component
     *
     * @return ID of this component
     */
    String getId();

    /**
     * Returns the component name. It is different from the name represented by "name" attribute (if there's one).
     * In case of Web component this is the name of the tag (without the brackets).
     *
     * @return component name in lowercase
     */
    String getComponentName();

    /**
     * @return <code>true</code> if component is displayed on the page (not hidden), <code>false</code> otherwise
     */
    boolean isDisplayed();

    /**
     * @return <code>true</code> if component is present on the page (may be hidden), <code>false</code> otherwise
     */
    boolean exists();

    /**
     * @return <code>true</code> if component is selected. Element can represent dropdown, listbox, checkbox or radiobutton.
     */
    boolean isSelected();

    /**
     * @return <code>true</code> if component is enabled
     */
    boolean isEnabled();

    /**
     * @return <code>true</code> if component has focus
     */
    boolean hasFocus();

    /**
     * Sends a mouse click event to the component
     */
    void click();

    /**
     * Sends a mouse right-click event to the component
     */
    void contextClick();

    /**
     * @return textual representation of the component's content. In case of <code>&lt;select&gt;</code> it will be the title of the selected option. In case of
     * text input - field value. In case of label - inner text.
     */
    String getText();

    /**
     * Focuses the current tab on this component
     */
    void focus();

    /**
     * Retrieves a property/attribute of this component.
     *
     * @param propertyName name of the property to fetch. Take a look at {@link com.ericsson.cifwk.taf.ui.core.UiProperties} for some predefined universal values.
     *                     You can also use native notations of implementing framework if you are sure it is supported.
     * @return a property/attribute of this component.
     */
    String getProperty(String propertyName);

    /**
     * Sends the defined sequence of keystrokes to the underlying UI element.
     */
    void sendKeys(CharSequence... keysToSend);

    /**
     * Simulates the mouse cursor placement on the component
     *
     * @param coordinates if defined, there should be exactly 2 coordinates: X and Y (relative to the component, offset from the top-left corner).
     *                    Negative values are accepted.
     */
    void mouseOver(int... coordinates);

    /**
     * Simulates the mouse button pressing (without releasing it yet) on the component
     *
     * @param coordinates if defined, there should be exactly 2 coordinates: X and Y (relative to the component, offset from the top-left corner).
     *                    Negative values are accepted.
     */
    void mouseDown(int... coordinates);

    /**
     * Simulates the release of the mouse button, pressed on the component
     *
     * @param coordinates if defined, there should be exactly 2 coordinates: X and Y (relative to the component, offset from the top-left corner).
     *                    Negative values are accepted.
     */
    void mouseUp(int... coordinates);

    /**
     * Simulates the mouse moving over the component
     *
     * @param coordinates if defined, there should be exactly 2 coordinates: X and Y (relative to the component, offset from the top-left corner).
     *                    Negative values are accepted.
     */
    void mouseMove(int... coordinates);

    /**
     * Simulates the mouse cursor removal from the component. Doesn't work in Firefox (known issue).
     */
    void mouseOut();

    /**
     * Returns the component size in pixels. In headless mode returns the size of the window (known issue).
     *
     * @return the component size in pixels. In headless mode returns the size of the window (known issue).
     */
    UiComponentSize getSize();

    /**
     * Returns context menu item for current component and given menu name.
     */
    MenuItem getMenuItem(String menuName);

    /**
     * Returns the list of the child components of this component. If there are no child elements, the returned list will be empty.
     *
     * @return the list of the child components of this component. If there are no child elements, the returned list will be empty.
     */
    List<UiComponent> getChildren();

    /**
     * Returns the first descendant component found which matches the selector.
     * If no matching descendant is found then the Optional will be absent.
     *
     * @param selector default select expression (CSS for Web).
     * @param type The type of {@link UiComponent} to return
     * @param <T> generic type which extends {@link UiComponent}
     * @return An Optional which contains the {@link UiComponent}
     */
    <T extends UiComponent> Optional<T> getFirstDescendantBySelector(String selector, Class<T> type);

    /**
     * Returns the first descendant component found which matches the selector
     * If no matching descendant is found then the Optional will be absent.
     *
     * @param selectorType the selector type that is used
     * @param selector CSS or XPath expression. Please note that XPath absolute locators will be forcibly "localized".
     * @param type The type of {@link UiComponent} to return
     * @param <T> generic type which extends {@link UiComponent}
     * @return An Optional which contains the {@link UiComponent}
     */
    <T extends UiComponent> Optional<T> getFirstDescendantBySelector(SelectorType selectorType, String selector, Class<T> type);

    /**
     * Returns the list of the descendant components of this component (child elements and deeper) that match the selector.
     * If there are no descendant elements, the returned list will be empty.
     *
     * @param selector default selector expression (CSS for Web).
     * @return the list of the descendant components of this component (child elements and deeper).
     */
    List<UiComponent> getDescendantsBySelector(String selector);

    /**
     * Returns the list of the descendant components of this component (child elements and deeper) that match the selector.
     * If there are no descendant elements, the returned list will be empty
     * @param selector default select expression (CSS for Web).
     * @param type The type of {@link UiComponent} to return
     * @param <T> generic type which extends {@link UiComponent}
     * @return the list of the descendant components of this component (child elements and deeper).
     */
    <T extends UiComponent> List<T> getDescendantsBySelector(String selector, Class<T> type);

    /**
     * <p>
     * Returns the list of the descendant components of this component (child elements and deeper) that match the selector. If there are no descendant elements,
     * the returned list will be empty.
     * </p>
     * <p>
     * Please note that XPath absolute locators will be forcibly "localized".
     * </p>
     *
     * @param selectorType the selector type that is used
     * @param selector     CSS or XPath expression. Please note that XPath absolute locators will be forcibly "localized".
     * @return the list of the descendant components of this component (child elements and deeper).
     */
    List<UiComponent> getDescendantsBySelector(SelectorType selectorType, String selector);

    /**
     * <p>
     * Returns the list of the descendant components of this component (child elements and deeper) that match the selector. If there are no descendant elements,
     * the returned list will be empty.
     * </p>
     * <p>
     * Please note that XPath absolute locators will be forcibly "localized".
     * </p>
     *
     * @param selectorType the {@link SelectorType} this is used
     * @param selector expression which will select an element. Please note that XPath absolute locators will be forcibly "localized".
     * @param type The type of {@link UiComponent} to return
     * @param <T> generic type which extends {@link UiComponent}
     * @return the list of the descendant components of this component (child elements and deeper).
     */
    <T extends UiComponent> List<T> getDescendantsBySelector(SelectorType selectorType, String selector, Class<T> type);

    /**
     * Returns the most effective selector that can be used to look up this component.
     *
     * @return the most effective selector that can be used to look up this component.
     *
     * @deprecated use {@link UiComponent#getComponentDetails() instead.
     */
    @Deprecated
    UiComponentSelector getComponentSelector();

    /**
     * Returns the details that can be used to look up this component.
     *
     * @return the details that can be used to look up this component.
     *
     */
    UiComponentMappingDetails getComponentDetails();

    /**
     * Convert of this component to the component of another type
     *
     * @param clazz The component class that you want to convert in
     * @return Component of the required type
     */
    <T extends UiComponent> T as(Class<T> clazz);

    void setStateManager(UiComponentStateManager stateManager);

    /**
     * Suspends the current thread execution until the condition outlined in <code>condition</code> is met
     * or expectation time exceeds <code>timeoutInMillis</code>.
     *
     * @param condition       predicate that checks for some condition
     * @param timeoutInMillis max milliseconds to wait for condition to be met.
     * @throws com.ericsson.cifwk.taf.ui.core.WaitTimedOutException if the condition is not met in time.
     */
    void waitUntil(GenericPredicate condition, long timeoutInMillis);

    /**
     * Suspends the current thread execution until the condition outlined in <code>condition</code> is met or
     * or expectation time exceeds default timeout (see {@link UiToolkit#setDefaultWaitTimeout(int)}).
     * Use with caution, as this method will block your execution in case of condition that is always false.
     *
     * @param condition predicate that checks for some condition
     */
    void waitUntil(GenericPredicate condition);

    /**
     * Suspends the current thread execution until the condition outlined in <code>condition</code> is met
     * or expectation time exceeds <code>timeoutInMillis</code>. The maximum timeout value that can be set is 30000 milliseconds.
     *
     * @param condition       predicate that checks for some condition
     * @param timeoutInMillis max milliseconds to wait for condition to be met.
     * @throws com.ericsson.cifwk.taf.ui.core.WaitTimedOutException if the condition is not met in time.
     */
    <C extends UiComponent> void waitUntil(C component, Predicate<C> condition, long timeoutInMillis);

    /**
     * Suspends the current thread execution until the condition outlined in <code>condition</code> is met or
     * or expectation time exceeds default timeout (see {@link UiToolkit#setDefaultWaitTimeout(int)}).
     * Use with caution, as this method will block your execution in case of condition that is always false.
     *
     * @param condition predicate that checks for some condition
     */
    <C extends UiComponent> void waitUntil(C component, Predicate<C> condition);

    /**
     * Creates an instance of {@link UiActions}.
     *
     * @return instance of {@link UiActions}.
     */
    UiActions createUiActions();

    /**
     * Execute javascript on the BrowserTab that the UiComponent is located on.
     *
     * If you wish to interact specifically with the UiComponent then the UiComponent is defined as var element in javascript
     * e.g. .evaluate("return element.innerHTML;");
     *
     * @param expression the javascript code that you wish to execute
     * @return will return an object if the javascript has a return statement otherwise will be NULL
     */
    Object evaluate(String expression);

}
