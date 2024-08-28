package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.ConditionWait;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.spi.UiActions;

import java.util.List;

/**
 * A combination of Model and View that represents a part of the UI window (browser or desktop)
 */
public interface ViewModel extends UiComponentContainer, ConditionWait {

    /**
     * Returns an instance of TAF UI button abstraction.
     *
     * @param selectorType the type of <code>selector</code>
     * @param selector     selector that describes the searched element
     * @return instance of TAF UI button abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Button getButton(SelectorType selectorType, String selector);

    /**
     * Returns an instance of TAF UI text box abstraction.
     *
     * @param selectorType the type of <code>selector</code>
     * @param selector     selector that describes the searched element
     * @return instance of TAF UI text box abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    TextBox getTextBox(SelectorType selectorType, String selector);

    /**
     * Returns an instance of TAF UI label abstraction.
     *
     * @param selectorType the type of <code>selector</code>
     * @param selector     selector that describes the searched element
     * @return instance of TAF UI label abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Label getLabel(SelectorType selectorType, String selector);

    /**
     * Returns an instance of TAF UI link abstraction.
     *
     * @param selectorType the type of <code>selector</code>
     * @param selector     selector that describes the searched element
     * @return instance of TAF UI link abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Link getLink(SelectorType selectorType, String selector);

    /**
     * Returns an instance of TAF UI dropdown abstraction.
     *
     * @param selectorType the type of <code>selector</code>
     * @param selector     selector that describes the searched element
     * @return instance of TAF UI dropdown abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Select getSelect(SelectorType selectorType, String selector);

    /**
     * Returns an instance of TAF UI radio button abstraction.
     *
     * @param selectorType the type of <code>selector</code>
     * @param selector     selector that describes the searched element
     * @return instance of TAF UI radio button abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    RadioButton getRadioButton(SelectorType selectorType, String selector);

    /**
     * Returns an instance of TAF UI check box abstraction.
     *
     * @param selectorType the type of <code>selector</code>
     * @param selector     selector that describes the searched element
     * @return instance of TAF UI check box abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    CheckBox getCheckBox(SelectorType selectorType, String selector);

    /**
     * Returns an instance of TAF UI button abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI button abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Button getButton(String selector);

    /**
     * Returns an instance of TAF UI text box abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI text box abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    TextBox getTextBox(String selector);

    /**
     * Returns an instance of TAF UI file selector abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI file selector abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    FileSelector getFileSelector(String selector);

    /**
     * Returns an instance of TAF UI label abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI label abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Label getLabel(String selector);

    /**
     * Returns an instance of TAF UI link abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI link abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Link getLink(String selector);

    /**
     * Returns an instance of TAF UI dropdown abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI dropdown abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    Select getSelect(String selector);

    /**
     * Returns an instance of TAF UI radio button abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI radio button abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    RadioButton getRadioButton(String selector);

    /**
     * Returns an instance of TAF UI check box abstraction.
     *
     * @param selector selector that describes the searched element. Default selector type applies
     *                 (CSS for Web).
     * @return instance of TAF UI check box abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    CheckBox getCheckBox(String selector);

    /**
     * Returns an instance of {@link UiComponent}, based on default selector type.
     *
     * @param selector selector that describes the searched element. Default selector type
     *                 applies (CSS for Web).
     * @return instance of {@link UiComponent}, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    UiComponent getViewComponent(String selector);

    /**
     * Returns an instance of TAF UI component abstraction.
     *
     * @param selector       selector that describes the searched element. Default selector type
     *                       applies (CSS for Web).
     * @param componentClass class of the component to initialize. Make sure this class suits all UI elements, and
     *                       provides the most of the functions. If your search criteria will find text boxes, it doesn't make sense to map them
     *                       as {@link Label} - it will greatly reduce the functionality. Please note that you can use not
     *                       only the concrete types, but also interface {@link UiComponent} - which is a good idea if your search criteria
     *                       covers elements of many types.
     * @return instance of TAF UI component abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    <T extends UiComponent> T getViewComponent(String selector, Class<T> componentClass);

    /**
     * Returns an instance of TAF UI component abstraction.
     *
     * @param selectorType   the type of <code>selector</code>.
     * @param selector       selector that describes the searched element. Default selector type
     *                       applies (CSS for Web).
     * @param componentClass class of the component to initialize.
     * @return instance of TAF UI component abstraction, even if the target element was not found.
     * In case if element was not found, its <code>exists()</code> method will
     * return <code>false</code>.
     */
    <T extends UiComponent> T getViewComponent(SelectorType selectorType, String selector, Class<T> componentClass);

    /**
     * Returns <code>true</code> if this view is the current one
     *
     * @return <code>true</code> if this view is the current one, <code>false</code> otherwise
     */
    boolean isCurrentView();

    /**
     * Checks if the component, defined by selector, exists.
     *
     * @param selectorType type of <code>selector</code>
     * @param selector     selector that describes the searched element.
     * @return <code>true</code> if component exists (even if it's not visible),
     * <code>false</code> otherwise.
     */
    boolean hasComponent(SelectorType selectorType, String selector);

    /**
     * Checks if the component, defined by selector, exists.
     *
     * @param selector selector that describes the searched element. Default selector type
     *                 applies (CSS for Web).
     * @return <code>true</code> if component exists (even if it's not visible),
     * <code>false</code> otherwise.
     */
    boolean hasComponent(String selector);

    /**
     * Get a list of multiple UI components, providing a selector and a target type of the components.
     *
     * @param selectorType   type of <code>selector</code>
     * @param selector       selector that describes the searched elements.
     * @param componentClass type of the components. Make sure this class suits all UI elements, and provides
     *                       the most of the functions. If your search criteria will find text boxes, it doesn't make sense to map them
     *                       as {@link Label} - it will greatly reduce the functionality. Please note that you can use not
     *                       only the concrete types, but also interface {@link UiComponent} - which is a good idea if your search criteria
     *                       covers elements of many types.
     * @return <code>true</code> if component exists (even if it's not visible),
     * <code>false</code> otherwise.
     */
    <T extends UiComponent> List<T> getViewComponents(SelectorType selectorType, String selector, Class<T> componentClass);

    /**
     * Get a list of multiple UI components, providing a selector and a target type of the components.
     *
     * @param selector       selector that describes the searched elements.
     *                       Default selector type applies (CSS for Web).
     * @param componentClass type of the components. Make sure this class suits all UI elements, and provides
     *                       the most of the functions. If your search criteria will find text boxes, it doesn't make sense to map them
     *                       as {@link Label} - it will greatly reduce the functionality. Please note that you can use not
     *                       only the concrete types, but also interface {@link UiComponent} - which is a good idea if your search criteria
     *                       covers elements of many types.
     * @return <code>true</code> if component exists (even if it's not visible),
     * <code>false</code> otherwise.
     */
    <T extends UiComponent> List<T> getViewComponents(String selector, Class<T> componentClass);

    /**
     * Get the amount of components that match the <code>selector</code> of <code>selectorType</code>
     *
     * @param selectorType
     * @param selector
     * @return amount of components that match the <code>selector</code> of <code>selectorType</code>
     */
    int getComponentCount(SelectorType selectorType, String selector);

    /**
     * Creates an instance of {@link UiActions} bound to this view.
     */
    UiActions newActionChain();

}
