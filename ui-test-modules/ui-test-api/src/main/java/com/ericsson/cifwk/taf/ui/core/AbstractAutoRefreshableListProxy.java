package com.ericsson.cifwk.taf.ui.core;

import com.google.common.collect.Sets;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

abstract class AbstractAutoRefreshableListProxy implements InvocationHandler {

    private static final Set<String> directMethods =
            Sets.newHashSet("finalize", "notify", "notifyAll", "wait", "getClass", "clear");

    private boolean initialized = false;

    @Override
    public final synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!initialized) {
            doInitializeOnce();
            initialized = true;
        }
        if (!shouldBypassProxyForMethod(method)) {
            doPrepareDataBeforeInvocation();
        }
        return doInvoke(proxy, method, args);
    }

    protected abstract void doInitializeOnce();

    protected abstract void doPrepareDataBeforeInvocation();

    protected abstract Object doInvoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;

    boolean shouldBypassProxyForMethod(String methodName) {
        return directMethods.contains(methodName);
    }

    boolean shouldBypassProxyForMethod(Method method) {
        return shouldBypassProxyForMethod(method.getName());
    }

    boolean isInitialized() {
        return initialized;
    }

}
