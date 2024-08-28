package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.ui.utility.MediatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ericsson.cifwk.taf.ui.core.WaitConstants.*;

public class ScreenshotDelayStrategy implements DelayStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotDelayStrategy.class);

    private final long delay;
    private final UiMediator mediator;
    private final String reason;

    public ScreenshotDelayStrategy(UiMediator mediator, long delay, String reason) {
        this.mediator = mediator;

        if (delay < MINIMUM_SLEEP_STEP) {
            LOGGER.warn("Attempted to set delay between screenshots below the minimum delay " + MINIMUM_SLEEP_STEP + " ms.");
            this.delay = MINIMUM_SLEEP_STEP;
        } else {
            this.delay = delay;
        }

        if ("".equals(reason)) {
            this.reason = ": " + reason;
        } else {
            this.reason = "";
        }
    }

    @Override
    public void delay() {
        try {
            Thread.sleep(delay);
            MediatorHelper.createScreenshot(mediator, "ScreenshotDelay" + reason);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
