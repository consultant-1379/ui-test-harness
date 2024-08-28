package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Supplier;

import java.lang.reflect.Proxy;

class SikuliUiComponentStateManagerFactory extends GenericUiComponentStateManagerFactory {

    SikuliUiComponentStateManagerFactory(UiMediator mediator) {
        super(mediator);
    }

    /**
     * Overridden to return a proxied instance of <code>UiComponentStateManager</code>,
     * which always returns a fresh instance of component, or throws <code>UiComponentNotFoundException</code>
     * if nothing was found.
     */
    @Override
    public UiComponentStateManager createStateManager(final UiComponentMappingDetails details) {
        return (UiComponentStateManager) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UiComponentStateManager.class},
                new SikuliUiComponentStateManagerInvocationHandler(mediator, new Supplier<UiComponentStateManager>() {
                    @Override
                    public UiComponentStateManager get() {
                        return mediator.retrieve(details).get(0);
                    }
                }, details.toString()));
    }
}
