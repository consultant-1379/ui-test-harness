package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.ui.utility.MediatorHelper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static java.lang.String.format;

/**
 * A base for UiComponentStateManager proxy invocation handler.
 */
abstract class AbstractUiComponentStateManagerInvocationHandler implements InvocationHandler, StateManagerAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUiComponentStateManagerInvocationHandler.class);

    private static final String GET_STATE_MANAGER_METHOD = "getStateManager";
    private static final String GET_MAPPING_INFO_METHOD = "getMappingInfo";
    private static final String GET_AS_STRING_METHOD = "getAsString";
    private static final String EXISTS_METHOD = "exists";
    private static final String IS_DISPLAYED_METHOD = "isDisplayed";
    private static final Set<String> TRANSPARENT_METHODS = Sets.newHashSet(EXISTS_METHOD, IS_DISPLAYED_METHOD);

    final Supplier<UiComponentStateManager> instanceSupplier;
    protected final String mappingInfo;
    final UiMediator mediator;

    protected UiComponentStateManager instance;

    static {
        try {
            UiComponentStateManager.class.getMethod(GET_MAPPING_INFO_METHOD);
        } catch (NoSuchMethodException e) {
            String message = format("Method %s  is undefined for %s", GET_MAPPING_INFO_METHOD, UiComponentStateManager.class.getName());
            throw new IllegalStateException(message, e);
        } catch (SecurityException e) {
            throw new RuntimeException(e); // NOSONAR
        }
    }

    AbstractUiComponentStateManagerInvocationHandler(Supplier<UiComponentStateManager> instanceSupplier, String mappingInfo, UiMediator mediator) {
        this.instanceSupplier = instanceSupplier;
        this.mappingInfo = mappingInfo;
        this.mediator = mediator;
    }

    @Override
    public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!isInitialized()) {

            // Should be able to get the component mapping even if it doesn't exist
            String methodName = method.getName();
            if (GET_MAPPING_INFO_METHOD.equals(methodName) || GET_AS_STRING_METHOD.equals(methodName)) {
                return mappingInfo;
            }

            RetrySchema retrySchema = new RetrySchema();
            while (true) {
                try {
                    // This will throw UiComponentNotFoundException if element is not on the page
                    instance = instanceSupplier.get();
                    break;
                } catch (UiComponentNotFoundException e) {
                    if (shouldPropagate(methodName, retrySchema)) {
                        MediatorHelper.createScreenshot(mediator);
                        throw Throwables.propagate(e);
                    } else {
                        retrySchema.sleep();
                    }
                }
            }
        }

        if (GET_STATE_MANAGER_METHOD.equals(method.getName())) {
            return getStateManager();
        }
        Object result;
        try {
            result = invoke(method, args);
        } catch (InvocationTargetException e) {
            return handleInvocationTargetException(e, method, args);
        }
        return result;
    }

    @VisibleForTesting
    protected Object invoke(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(format("Calling %s(%s) for element with mapping %s (initialized=%s)", method.getName(),
                    args == null ? "" : Arrays.asList(args), mappingInfo, isInitialized()));
        }
        return method.invoke(instance, args);
    }

    private static boolean shouldPropagate(String methodName, RetrySchema retrySchema) {
        return TRANSPARENT_METHODS.contains(methodName) || retrySchema.shouldGiveUp();
    }

    @VisibleForTesting
    boolean isInitialized() {
        return instance != null;
    }

    @Override
    public UiComponentStateManager getStateManager() {
        return instance;
    }

    protected abstract Object handleInvocationTargetException(InvocationTargetException e, Method method, Object[] args)
            throws IllegalAccessException, InvocationTargetException;
}
