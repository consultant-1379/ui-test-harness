package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Supplier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class SikuliUiComponentStateManagerInvocationHandler extends AbstractUiComponentStateManagerInvocationHandler {

    SikuliUiComponentStateManagerInvocationHandler(UiMediator uiMediator, Supplier<UiComponentStateManager> instanceSupplier, String mappingInfo) {
        super(instanceSupplier, mappingInfo, uiMediator);
    }

    @Override
    protected Object handleInvocationTargetException(InvocationTargetException e, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        throw e;
    }
}