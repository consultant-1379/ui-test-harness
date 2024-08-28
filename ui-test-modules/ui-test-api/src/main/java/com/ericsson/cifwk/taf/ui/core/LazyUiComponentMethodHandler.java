package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import javassist.util.proxy.MethodHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class LazyUiComponentMethodHandler<T extends AbstractUiComponent> implements MethodHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LazyUiComponentMethodHandler.class);

    private volatile boolean initialized;
    private final Supplier<UiComponentStateManager> stateManagerSupplier;
    private final UiComponentAutowirer autowirer;

    LazyUiComponentMethodHandler(Supplier<UiComponentStateManager> stateManagerSupplier, UiComponentAutowirer autowirer) {
        this.stateManagerSupplier = stateManagerSupplier;
        this.autowirer = autowirer;
    }

    @Override
    public Object invoke(Object self, Method overridden, Method forwarder, Object[] args) throws Throwable {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Calling %s@%s.%s(%s) (initialized=%s)",
                    self.getClass().getSuperclass().getName(), Integer.toHexString(hashCode()), overridden.getName(),
                    args == null ? "" : Arrays.asList(args), initialized));
        }
        if (shouldInitializeFor(overridden)) {
            initializeComponent((T) self);
        }
        try {
            return doInvoke(self, forwarder, args);
        } catch (InvocationTargetException e) { // NOSONAR
            throw Throwables.propagate(e.getCause());
        }
    }

    private boolean shouldInitializeFor(Method overridden) {
        return !initialized && !ProxyObjectCommons.shouldSkipPreProcessingFor(overridden);
    }

    @VisibleForTesting
    void initializeComponent(T component) {
        // Setting component's state manager
        UiComponentStateManager stateManager = stateManagerSupplier.get();

        component.setStateManager(stateManager);
        component.setAutowirer(autowirer);

        // Initializing autowireable fields if there are any
        autowirer.initialize(component);

        initialized = true;
    }

    @VisibleForTesting
    Object doInvoke(Object self, Method forwarder, Object[] args) throws IllegalAccessException, InvocationTargetException {
        return forwarder.invoke(self, args);
    }

    boolean isInitialized() {
        return initialized;
    }
}
