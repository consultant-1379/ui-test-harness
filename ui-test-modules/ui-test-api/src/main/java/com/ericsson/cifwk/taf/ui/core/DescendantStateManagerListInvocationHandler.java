package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class DescendantStateManagerListInvocationHandler extends AbstractAutoRefreshableListProxy {

    private final Supplier<List<UiComponentStateManager>> listSupplier;
    private List<UiComponentStateManager> list = new ArrayList<>();

    DescendantStateManagerListInvocationHandler(Supplier<List<UiComponentStateManager>> listSupplier) {
        this.listSupplier = listSupplier;
    }

    @Override
    protected void doInitializeOnce() {
        // empty
    }

    @Override
    protected void doPrepareDataBeforeInvocation() {
        list = listSupplier.get();
    }

    @Override
    protected Object doInvoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(list, args);
    }

}
