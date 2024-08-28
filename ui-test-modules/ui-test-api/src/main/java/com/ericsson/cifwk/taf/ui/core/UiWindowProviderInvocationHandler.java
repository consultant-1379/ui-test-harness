package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class UiWindowProviderInvocationHandler implements InvocationHandler {
    static final String IS_CLOSED_METHOD = "isClosed";
    private final UiWindowProvider<?> provider;

    UiWindowProviderInvocationHandler(UiWindowProvider<?> provider) {
        this.provider = provider;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (provider.isClosed() && !method.getName().equals(IS_CLOSED_METHOD)) {
            throw new IllegalStateException("Window provider " + provider + " is closed");
        }
        return method.invoke(provider, args);
    }

}
