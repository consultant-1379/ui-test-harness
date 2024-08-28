package com.ericsson.cifwk.taf.ui.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;

class LazyUiComponentListMethodHandler extends AbstractAutoRefreshableListProxy {

    private final Supplier<List<UiComponentStateManager>> stateManagerListSupplier;
    private final UiComponentFactory componentFactory;
    private final Class<? extends UiComponent> componentClass;
    private final boolean isStaticList;
    private List<UiComponent> instance;

    <T extends UiComponent> LazyUiComponentListMethodHandler(Supplier<List<UiComponentStateManager>> stateManagerListSupplier,
                                                                    UiComponentFactory componentFactory, Class<T> componentClass, boolean isStaticList) {
        this.stateManagerListSupplier = stateManagerListSupplier;
        this.componentFactory = componentFactory;
        this.componentClass = componentClass;
        this.isStaticList = isStaticList;
    }

    @Override
    protected void doInitializeOnce() {
        instance = new ArrayList<>();
        if (isStaticList()) {
            initializeComponentList();
        }
    }

    @Override
    protected void doPrepareDataBeforeInvocation() {
        if (!isStaticList()) {
            instance.clear();
            // If the collection of state managers is proxied by DynamicStateManagerListInvocationHandler,
            // if will be refreshed here, hence the resulting list size will be different, too
            initializeComponentList();
        }
    }

    private boolean isStaticList() {
        return isStaticList;
    }

    @Override
    protected Object doInvoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }

    @VisibleForTesting
    void initializeComponentList() {
        List<UiComponentStateManager> stateManagers = stateManagerListSupplier.get();
        for (UiComponentStateManager manager : stateManagers) {
            UiComponent component = componentFactory.instantiateComponent(manager, componentClass);
            instance.add(component);
        }
    }
}
