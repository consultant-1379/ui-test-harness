package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.google.common.base.Predicate;

/**
 * Specification of a tool that delays the current execution flow, based
 * on some rules that apply to {@link com.ericsson.cifwk.taf.ui.core.UiComponent}s.
 * <p>
 * To delay the execution flow with this delayer, get its instance from appropriate
 * {@link com.ericsson.cifwk.taf.ui.UiWindow} or {@link com.ericsson.cifwk.taf.ui.sdk.ViewModel} subclass.
 */
public interface UiComponentBasedDelayer {

    /**
     * @see com.ericsson.cifwk.taf.ui.ConditionWait#waitUntilComponentIsDisplayed(String selector, long timeoutInMillis)
     */
    UiComponent waitUntilComponentIsDisplayed(String selector, long timeoutInMillis);

    /**
     * @see com.ericsson.cifwk.taf.ui.ConditionWait#waitUntilComponentIsDisplayed(SelectorType selectorType, String selector, long timeoutInMillis)
     */
    UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String selector, long timeoutInMillis);

    /**
     * @see com.ericsson.cifwk.taf.ui.ConditionWait#waitUntilComponentIsDisplayed(UiComponent component, long timeoutInMillis)
     */
    UiComponent waitUntilComponentIsDisplayed(UiComponent component, long timeoutInMillis);

    /**
     * @see com.ericsson.cifwk.taf.ui.ConditionWait#waitUntilComponentIsHidden(UiComponent component, long timeoutInMillis)
     */
    UiComponent waitUntilComponentIsHidden(UiComponent component, long timeoutInMillis);

    /**
     * @see com.ericsson.cifwk.taf.ui.ConditionWait#waitUntil(C component, Predicate predicate, long timeoutInMillis)
     */
    <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate, long timeoutInMillis);

    /**
     * @see com.ericsson.cifwk.taf.ui.ConditionWait#waitUntil(GenericPredicate predicate, long timeoutInMillis)
     */
    void waitUntil(GenericPredicate predicate, long timeoutInMillis);

}
