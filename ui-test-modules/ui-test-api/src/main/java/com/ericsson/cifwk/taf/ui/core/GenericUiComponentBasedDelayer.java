package com.ericsson.cifwk.taf.ui.core;

import static com.google.common.base.Preconditions.checkState;

import com.ericsson.cifwk.taf.ui.debug.DebugDelayer;
import com.ericsson.cifwk.taf.ui.spi.ScreenshotProvider;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

class GenericUiComponentBasedDelayer implements UiComponentBasedDelayer, DebugDelayer {

    private final UiComponentStateManagerFactory stateManagerFactory;
    private final UiComponentFactory uiComponentFactory;
    private WaitHelper helper;
    private UiMediator mediator;

    GenericUiComponentBasedDelayer(UiComponentStateManagerFactory stateManagerFactory, ScreenshotProvider screenshotProvider) {
        this.stateManagerFactory = stateManagerFactory;
        this.uiComponentFactory = new UiComponentFactoryImpl(stateManagerFactory.getAutowirer());
        this.helper = new WaitHelper(screenshotProvider);
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(String selector, long timeoutInMillis) {
        return waitUntilComponentIsDisplayed(SelectorType.DEFAULT, selector, timeoutInMillis);
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String selector, long timeoutInMillis) {
        return waitUntilComponentIsDisplayed(initComponent(selectorType, selector), timeoutInMillis);
    }

    private GenericUiComponent initComponent(final SelectorType selectorType, final String selector) {
        Supplier<UiComponentStateManager> smSupplier = new Supplier<UiComponentStateManager>() {
            @Override
            public UiComponentStateManager get() {
                return stateManagerFactory.createStateManager(new UiComponentMappingDetails(selectorType, selector));
            }
        };
        return uiComponentFactory.instantiateComponent(smSupplier, GenericUiComponent.class);
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(UiComponent component, long timeoutInMillis) {
        return waitUntil(component, UiComponentPredicates.DISPLAYED, timeoutInMillis);
    }

    @Override
    public UiComponent waitUntilComponentIsHidden(UiComponent component, long timeoutInMillis) {
        return waitUntil(component, UiComponentPredicates.HIDDEN, timeoutInMillis);
    }

    @Override
    public void waitUntil(GenericPredicate predicate, long timeoutInMillis) {
        helper.waitUntil(predicate, timeoutInMillis);
    }

    @Override
    public <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate, long timeoutInMillis) {
        return helper.waitUntil(component, predicate, timeoutInMillis);
    }

    @Override
    public void setDebugMode(UiMediator mediator, long delay) {
        this.mediator = mediator;
        helper = new WaitHelper(mediator, delay);
    }

    @Override
    public void unsetDebugMode() {
        checkState(mediator != null, "Please set debug mode before calling this.");
        helper = new WaitHelper(mediator);
    }
}
