package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.ui.utility.MediatorHelper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.indexOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * A proxy invocation handler that handles situations when element is stale or
 * not found. If it's stale, it's being looked up again; if not found,
 * a <code>UiComponentNotFoundException</code> is thrown.
 */
class SeleniumUiComponentStateManagerInvocationHandler extends AbstractUiComponentStateManagerInvocationHandler implements SeleniumElementAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUiComponentStateManagerInvocationHandler.class);

    private static final String TIMEOUT_MESSAGE = "Was waiting for component %s to become %s, but it's still the same: %s";

    SeleniumUiComponentStateManagerInvocationHandler(Supplier<UiComponentStateManager> instanceSupplier, String mappingInfo, UiMediator mediator) {
        super(instanceSupplier, mappingInfo, mediator);
    }

    @Override
    public WebElement getElement() {
        return (instance == null) ? null : ((SeleniumUiComponentStateManager) instance).getElement();
    }

    protected synchronized Object handleInvocationTargetException(InvocationTargetException wrapper, final Method method,
                                                                  final Object[] args) throws IllegalAccessException, InvocationTargetException {
        Throwable targetException = wrapper.getTargetException();
        LOGGER.debug(String.format("Got '%s' exception while trying to call %s(%s) for element with mapping %s",
                targetException.getClass(), method.getName(), args == null ? "" : Arrays.asList(args), mappingInfo));
        if (isExceptionKnown(targetException)) {
            return handleKnownException(method, args);
        } else if (targetException instanceof NoSuchElementException) {
            createScreenshot(targetException);
            throw new UiComponentNotFoundException(targetException, mappingInfo);
        } else {
            createScreenshot(targetException);
            return propagate(targetException);
        }
    }

    private boolean isExceptionKnown(Throwable exception) {
        return exception instanceof StaleElementReferenceException ||
                exception instanceof ElementNotVisibleException ||
                isWebDriverExceptionKnown(exception);
    }

    private boolean isWebDriverExceptionKnown(Throwable exception) {
        String message = exception.getMessage();
        // Only case so far - "Element ... is not clickable at point ... Other element would receive the click: ..."
        return exception instanceof WebDriverException &&
                message.contains("Element") &&
                message.contains("is not clickable at point") &&
                message.contains("Other element would receive the click");
    }

    private static Object propagate(Throwable targetException) {
        if (targetException instanceof RuntimeException) {
            throw (RuntimeException) targetException;
        } else {
            throw new RuntimeException(targetException); // NOSONAR
        }
    }

    private Object handleKnownException(Method method, Object[] args) throws IllegalAccessException {
        RetrySchema retrySchema = new RetrySchema();
        int attempt = 1;
        while (true) {
            String attemptMessage = null;
            if (LOGGER.isTraceEnabled()) {
                attemptMessage = String.format("Attempt %d to retry call '%s(%s)' for element with mapping %s",
                        attempt++, method.getName(), args == null ? "" : Arrays.asList(args), mappingInfo);
            }
            try {
                instance = instanceSupplier.get();
                Object result = invoke(method, args);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(attemptMessage + " - SUCCESS!");
                }
                return result;
            } catch (InvocationTargetException invocationException) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(attemptMessage + " - FAILED!");
                }
                handleKnownException(retrySchema, invocationException);
            } catch (Exception e) {
                createScreenshot(e);
                throw e;
            }
        }
    }

    private void handleKnownException(RetrySchema retrySchema, InvocationTargetException invocationException) {
        Throwable targetException = invocationException.getTargetException();

        Set<Class<? extends WebDriverException>> temporaryExceptionList = SeleniumUiMediator.getTemporaryExceptionList();
        // this check is required, so unexpected errors are propagated without waiting for timeout
        if (temporaryExceptionList.contains(targetException.getClass())) {
            if (retrySchema.shouldGiveUp()) {
                createScreenshot(targetException);
                if (targetException instanceof StaleElementReferenceException) {
                    LOGGER.error(String.format(TIMEOUT_MESSAGE, instance.getMappingInfo(), "not stale", retrySchema));
                    throw (StaleElementReferenceException) targetException;
                } else if (targetException instanceof ElementNotVisibleException) {
                    LOGGER.error(String.format(TIMEOUT_MESSAGE, instance.getMappingInfo(), "visible", retrySchema));
                    throw new UiComponentNotVisibleException(targetException, mappingInfo);
                } else if (isWebDriverExceptionKnown(targetException)) {
                    LOGGER.error(String.format(TIMEOUT_MESSAGE, instance.getMappingInfo(), "clickable", retrySchema));
                    throw new WebDriverException("Element " + mappingInfo + " is not clickable.\n" + targetException.getMessage(), targetException);
                }
            } else {
                retrySchema.sleep();
            }
        } else {
            throw new RuntimeException(targetException); // NOSONAR
        }
    }

    @VisibleForTesting
    byte[] createScreenshot(Throwable problemCause) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Making screenshot for element mapped as '%s'", this.mappingInfo));
        }
        return MediatorHelper.createScreenshot(this.mediator, getScreenshotName(this.mappingInfo, problemCause));
    }

    @VisibleForTesting
    String getScreenshotName(String mappingInfo, Throwable problemCause) {
        String elementName = getElementName(mappingInfo);
        // For better readability
        if (elementName.length() > 33) {
            String contentToShorten = substring(elementName, 15, elementName.length()-15);
            elementName = replace(elementName, contentToShorten, "...", 1);
        }
        return (problemCause == null) ? elementName : elementName.concat(String.format("(%s)", problemCause.getClass().getSimpleName()));
    }

    @VisibleForTesting
    String getElementName(String mappingInfo) {
        int start = indexOf(mappingInfo, "=") + 1;
        int end = lastIndexOf(mappingInfo, "]");
        if (contains(mappingInfo, "MappingBySelector")) {
            end = lastIndexOf(mappingInfo, ",");
        }
        String elementName = substring(mappingInfo, start, end);
        return isBlank(elementName) ? "<unresolved element>" : elementName;
    }
}
