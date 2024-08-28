package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

class DynamicStateManagerListInvocationHandler extends AbstractAutoRefreshableListProxy {

    private final UiComponentStateManagerProxyFactory stateManagerProxyFactory;
    private final UiMediator mediator;
    private final Supplier<List<UiComponentStateManager>> instanceSupplier;
    private List<UiComponentStateManager> instance = Lists.newArrayList();

    DynamicStateManagerListInvocationHandler(UiComponentStateManagerProxyFactory stateManagerProxyFactory,
                                                    UiMediator mediator,
                                                    Supplier<List<UiComponentStateManager>> instanceSupplier) {
        this.stateManagerProxyFactory = stateManagerProxyFactory;
        this.mediator = mediator;
        this.instanceSupplier = instanceSupplier;
    }

    @Override
    protected void doInitializeOnce() {
        // empty
    }

    @Override
    protected void doPrepareDataBeforeInvocation() {
        SimpleRetrySchemaInvocation retrySchemaInvocation = new SimpleRetrySchemaInvocation();
        instance = retrySchemaInvocation.perform(new Callable<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> call() throws Exception {
                return reInitList();
            }
        }, "reinstantiate dynamic UI component list");
    }

    private List<UiComponentStateManager> reInitList() {
        List<UiComponentStateManager> results = Lists.newArrayList();
        List<UiComponentStateManager> stateManagers = instanceSupplier.get();
        for (UiComponentStateManager stateManager : stateManagers) {
            UiComponentStateManager stateManagerProxy = stateManagerProxyFactory.createStateManagerProxy(mediator, stateManager);
            results.add(stateManagerProxy);
        }
        return results;
    }

    @Override
    protected Object doInvoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }
}
