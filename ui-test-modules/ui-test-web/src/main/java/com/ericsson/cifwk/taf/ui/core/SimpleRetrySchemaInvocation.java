package com.ericsson.cifwk.taf.ui.core;

import com.google.common.base.Throwables;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 26/04/2016
 */
public class SimpleRetrySchemaInvocation {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRetrySchemaInvocation.class);

    public final <T> T perform(Callable<T> callable) {
        return perform(callable, null);
    }

    public final <T> T perform(Callable<T> callable, String operationDescription) {
        boolean operationDescriptionDefined = operationDescription != null;
        String attemptMessage = null;
        RetrySchema retrySchema = new RetrySchema();
        int attempt = 1;
        while (true) {
            if (LOGGER.isTraceEnabled() && operationDescriptionDefined) {
                attemptMessage = String.format("Attempt %d to %s", attempt++, operationDescription);
            }
            try {
                return callable.call();
            } catch (Exception e) {
                if (LOGGER.isTraceEnabled() && attemptMessage != null) {
                    LOGGER.trace(String.format("%s - FAILED (%s)!", attemptMessage, e.getMessage()));
                }
                if (shouldPropagateException(e, retrySchema)) {
                    throw Throwables.propagate(e);
                } else {
                    retrySchema.sleep();
                }
            }
        }
    }

    protected boolean shouldPropagateException(Exception e, RetrySchema retrySchema) {
        Set<Class<? extends WebDriverException>> temporaryExceptionList = SeleniumUiMediator.getTemporaryExceptionList();
        return !temporaryExceptionList.contains(e.getClass()) || retrySchema.shouldGiveUp();
    }

}
