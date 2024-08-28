package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.spi.ScreenshotProvider;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.ui.utility.MediatorHelper;
import com.ericsson.cifwk.taf.ui.utility.ScreenshotToggle;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitHelper.class);

    private final DelayStrategy delayStrategy;

    private final ScreenshotProvider screenshotProvider;

    public WaitHelper(ScreenshotProvider screenshotProvider) {
        this.screenshotProvider = screenshotProvider;
        delayStrategy = new SimpleDelayStrategy();
    }

    public WaitHelper(UiMediator mediator, long delay) {
        this.delayStrategy = new ScreenshotDelayStrategy(mediator, delay, "BrowserTabDebugMode");
        this.screenshotProvider = mediator;
    }

    public void waitUntil(GenericPredicate predicate) {
        waitUntil(predicate, UiToolkit.getDefaultWaitTimeout());
    }

    public <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate) {
        return waitUntil(component, predicate, UiToolkit.getDefaultWaitTimeout());
    }

    public void waitUntil(GenericPredicate predicate, long timeoutInMillis) {
        waitUntilInternal(null, predicate, timeoutInMillis);
    }

    public <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate, long timeoutInMillis) {
        return waitUntilInternal(component, predicate, timeoutInMillis);
    }

    @VisibleForTesting
    private <C> C waitUntilInternal(C component, Predicate<C> predicate, long timeoutInMillis) {
        long actualTimeoutInMillis = UiToolkit.getValidTimeout(timeoutInMillis);
        long currentTimeMillis = System.currentTimeMillis();
        int attempt = 0;
        while (true) {
            attempt++;
            if (LOGGER.isTraceEnabled() && attempt > 1) {
                LOGGER.trace(String.format("Attempt #%d waiting for a condition on element mapped as '%s'", attempt, component));
            }
            try {
                ScreenshotToggle.disable();
                Boolean token = predicate.apply(component);
                LOGGER.trace("KEY Statement: isDisplayed()? " + token);
                if (token) {
                    break;
                }
            } catch (UiComponentNotFoundException e) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(String.format("Not found element mapped as '%s' while waiting for a condition on it", e.getMappingInfo()));
                }
            } finally {
                ScreenshotToggle.enable();
            }

            delay();

            if (System.currentTimeMillis() - currentTimeMillis > actualTimeoutInMillis) {
                MediatorHelper.createScreenshot(screenshotProvider, "Timeout");
                if (component == null) {
                    throw new WaitTimedOutException(actualTimeoutInMillis);
                } else {
                    throw new WaitTimedOutException(component.toString(), actualTimeoutInMillis);
                }
            }
        }
        return component;
    }

    @VisibleForTesting
    void delay() {
        delayStrategy.delay();
    }
}
