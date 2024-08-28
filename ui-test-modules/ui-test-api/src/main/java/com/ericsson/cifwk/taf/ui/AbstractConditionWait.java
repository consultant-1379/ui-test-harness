package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.ui.utility.MediatorHelper;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;

public abstract class AbstractConditionWait implements ConditionWait {

    @Override
    public UiComponent waitUntilComponentIsDisplayed(String selector) {
        return waitUntilComponentIsDisplayed(selector, UiToolkit.getDefaultWaitTimeout());
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String selector) {
        return waitUntilComponentIsDisplayed(selectorType, selector, UiToolkit.getDefaultWaitTimeout());
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(UiComponent component) {
        return waitUntilComponentIsDisplayed(component, UiToolkit.getDefaultWaitTimeout());
    }

    @Override
    public UiComponent waitUntilComponentIsHidden(UiComponent component) {
        return waitUntilComponentIsHidden(component, UiToolkit.getDefaultWaitTimeout());
    }

    @Override
    public <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate) {
        return waitUntil(component, predicate, UiToolkit.getDefaultWaitTimeout());
    }

    @Override
    public void waitUntil(GenericPredicate predicate) {
        waitUntil(predicate, UiToolkit.getDefaultWaitTimeout());
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(final String selector, final long timeoutInMillis) {
        return attemptToGetComponent(new Supplier<UiComponent>() {
            @Override
            public UiComponent get() {
                return getDelayer().waitUntilComponentIsDisplayed(selector, timeoutInMillis);
            }
        });
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(final SelectorType selectorType, final String selector,
                                                     final long timeoutInMillis) {
        return attemptToGetComponent(new Supplier<UiComponent>() {
            @Override
            public UiComponent get() {
                return getDelayer().waitUntilComponentIsDisplayed(selectorType, selector, timeoutInMillis);
            }
        });
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(final UiComponent component, final long timeoutInMillis) {
        return attemptToGetComponent(new Supplier<UiComponent>() {
            @Override
            public UiComponent get() {
                return getDelayer().waitUntilComponentIsDisplayed(component, timeoutInMillis);
            }
        });
    }

    @Override
    public UiComponent waitUntilComponentIsHidden(final UiComponent component, final long timeoutInMillis) {
        return attemptToGetComponent(new Supplier<UiComponent>() {
            @Override
            public UiComponent get() {
                return getDelayer().waitUntilComponentIsHidden(component, timeoutInMillis);
            }
        });
    }

    byte[] createScreenshot() {
        return MediatorHelper.createScreenshot(getMediator());
    }

    byte[] createScreenshot(String screenshotNameInTheReport ) {
        return MediatorHelper.createScreenshot(getMediator(), screenshotNameInTheReport);
    }

    private UiComponent attemptToGetComponent(Supplier<UiComponent> componentSupplier) {
        UiComponent uiComponent;
        try {
            uiComponent = componentSupplier.get();
        } catch (UiComponentNotFoundException e) {
            createScreenshot();
            throw Throwables.propagate(e);
        }
        return uiComponent;
    }

    @Override
    public <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate, long timeoutInMillis) {
        return getDelayer().waitUntil(component, predicate, timeoutInMillis);
    }

    @Override
    public void waitUntil(GenericPredicate predicate, long timeoutInMillis) {
        getDelayer().waitUntil(predicate, timeoutInMillis);
    }

    protected abstract UiComponentBasedDelayer getDelayer();

    protected abstract UiMediator getMediator();

}
