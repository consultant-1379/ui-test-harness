package com.ericsson.cifwk.taf.ui.core;

import java.lang.reflect.Proxy;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;

class SeleniumUiComponentStateManagerProxyFactory implements UiComponentStateManagerProxyFactory {

    @Override
    public UiComponentStateManager createStateManagerProxy(UiMediator mediator, Supplier<UiComponentStateManager> instanceSupplier,
                                                           String mappingInfo) {
        return (UiComponentStateManager) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UiComponentStateManager.class, SeleniumElementAwareComponent.class, StateManagerAware.class},
                new SeleniumUiComponentStateManagerInvocationHandler(instanceSupplier, mappingInfo, mediator));
    }

    @Override
    public UiComponentStateManager createStateManagerProxy(final UiMediator mediator, UiComponentStateManager nonProxiedStateManager) {
        if (Proxy.isProxyClass(nonProxiedStateManager.getClass())) {
            return nonProxiedStateManager;
        }

        return proxyStateManager(mediator, nonProxiedStateManager);
    }

    @VisibleForTesting
    UiComponentStateManager proxyStateManager(final UiMediator mediator, UiComponentStateManager nonProxiedStateManager) {
        final UiComponentMappingDetails selector = nonProxiedStateManager.getComponentDetails();
        return createStateManagerProxy(mediator, new Supplier<UiComponentStateManager>() {
            @Override
            public UiComponentStateManager get() {
                return mediator.retrieve(selector).get(0);
            }
        }, selector.toString());
    }

}
