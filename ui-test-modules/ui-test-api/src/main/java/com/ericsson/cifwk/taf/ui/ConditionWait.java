package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.WaitTimedOutException;
import com.google.common.base.Predicate;

/**
 * Specification of methods for condition expectation.
 */
public interface ConditionWait {

    /**
     * Suspends the current execution flow until the component, specified by <code>componentSelector</code>
     * (and default componentSelector type) is displayed - or operation times out with default timeout
     * (see {@link UiToolkit#setDefaultWaitTimeout(int)}).
     *
     * @return found component. If many components are found, the first is returned.
     * @throws WaitTimedOutException if operation times out. If default timeout
     *                               is <code>-1</code>, it's never thrown.
     */
    // START SNIPPET: WAIT_UNTIL_METHOD1
    UiComponent waitUntilComponentIsDisplayed(String componentSelector);
    // END SNIPPET: WAIT_UNTIL_METHOD1

    /**
     * Suspends the current execution flow until the component, specified by <code>componentSelector</code>
     * and <code>selectorType</code> componentSelector type, is displayed - or operation times out with default timeout
     * (see {@link UiToolkit#setDefaultWaitTimeout(int)}).
     *
     * @return found component. If many components are found, the first is returned.
     * @throws WaitTimedOutException if operation times out. If default timeout
     *                               is <code>-1</code>, it's never thrown.
     */
    // START SNIPPET: WAIT_UNTIL_METHOD2
    UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String componentSelector);
    // END SNIPPET: WAIT_UNTIL_METHOD2

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * is displayed or operation times out with default timeout
     * (see {@link UiToolkit#setDefaultWaitTimeout(int)}).
     *
     * @return given component.
     * @throws WaitTimedOutException if operation times out. If default timeout
     *                               is <code>-1</code>, it's never thrown.
     */
    // START SNIPPET: WAIT_UNTIL_METHOD3
    UiComponent waitUntilComponentIsDisplayed(UiComponent component);
    // END SNIPPET: WAIT_UNTIL_METHOD3

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * disappears or operation times out with default timeout
     * (see {@link UiToolkit#setDefaultWaitTimeout(int)}).
     *
     * @param component that needs to disappear
     * @return given component.
     * @throws WaitTimedOutException if operation times out
     */
    // START SNIPPET: WAIT_UNTIL_METHOD4
    UiComponent waitUntilComponentIsHidden(UiComponent component);
    // END SNIPPET: WAIT_UNTIL_METHOD4

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * satisfies given predicate or operation times out with default timeout
     * (see {@link UiToolkit#setDefaultWaitTimeout(int)}). Returns given component.
     *
     * @param component that needs to satisfy given predicate
     * @param predicate that needs to be satisfied by given component
     * @return given component.
     * @throws WaitTimedOutException if operation times out
     */
    // START SNIPPET: WAIT_UNTIL_PREDICATE_METHOD1
    <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate);
    // END SNIPPET: WAIT_UNTIL_PREDICATE_METHOD1

    /**
     * Suspends the current execution flow until given predicate or operation times out with default timeout
     * (see {@link UiToolkit#setDefaultWaitTimeout(int)}).
     *
     * @param predicate that needs to be satisfied to return
     * @throws WaitTimedOutException if operation times out
     */
    // START SNIPPET: WAIT_UNTIL_PREDICATE_METHOD2
    void waitUntil(GenericPredicate predicate);
    // END SNIPPET: WAIT_UNTIL_PREDICATE_METHOD2

    /**
     * Suspends the current execution flow until the component, specified by <code>componentSelector</code>
     * (and default componentSelector type) is displayed - or operation times out. The maximum timeout value that can be set is 30000 milliseconds.
     *
     * @return found component. If many components are found, the first is returned.
     * @throws WaitTimedOutException if operation times out.
     */
    // START SNIPPET: WAIT_UNTIL_MILLIS_METHOD1
    UiComponent waitUntilComponentIsDisplayed(String componentSelector, long timeoutInMillis);
    // END SNIPPET: WAIT_UNTIL_MILLIS_METHOD1

    /**
     * Suspends the current execution flow until the component, specified by <code>componentSelector</code>
     * and <code>selectorType</code> componentSelector type, is displayed - or operation times out.
     * The maximum timeout value that can be set is 30000 milliseconds.
     *
     * @return found component. If many components are found, the first is returned.
     * @throws WaitTimedOutException if operation times out.
     */
    // START SNIPPET: WAIT_UNTIL_MILLIS_METHOD2
    UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String componentSelector, long timeoutInMillis);
    // END SNIPPET: WAIT_UNTIL_MILLIS_METHOD2

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * is displayed or operation times out. The maximum timeout value that can be set is 30000 milliseconds.
     *
     * @return given component.
     * @throws WaitTimedOutException if operation times out.
     */
    // START SNIPPET: WAIT_UNTIL_MILLIS_METHOD3
    UiComponent waitUntilComponentIsDisplayed(UiComponent component, long timeoutInMillis);
    // END SNIPPET: WAIT_UNTIL_MILLIS_METHOD3

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * disappears or operation times out. The maximum timeout value that can be set is 30000 milliseconds.
     *
     * @return given component.
     * @throws WaitTimedOutException if operation times out
     */
    // START SNIPPET: WAIT_UNTIL_MILLIS_METHOD4
    UiComponent waitUntilComponentIsHidden(UiComponent component, long timeoutInMillis);
    // END SNIPPET: WAIT_UNTIL_MILLIS_METHOD4

    /**
     * Suspends the current execution flow until the component <code>component</code>
     * satisfies given predicate or operation times out. Returns given component.
     * The maximum timeout value that can be set is 30000 milliseconds.
     *
     * @return given component.
     * @throws WaitTimedOutException if operation times out
     */
    <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate, long timeoutInMillis);

    /**
     * Suspends the current execution flow until given predicate or operation times out.
     * The maximum timeout value that can be set is 30000 milliseconds.
     *
     * @throws WaitTimedOutException if operation times out
     */
    void waitUntil(GenericPredicate predicate, long timeoutInMillis);
}
