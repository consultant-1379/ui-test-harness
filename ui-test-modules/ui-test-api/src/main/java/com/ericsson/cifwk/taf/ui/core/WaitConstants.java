package com.ericsson.cifwk.taf.ui.core;

public final class WaitConstants {

    /**
     * Amount of millis to sleep between retry attempts when waiting for a condition to be met
     */
    static final long SLEEP_STEP = 500;
    static final long MINIMUM_SLEEP_STEP = 200;

    private WaitConstants() {
        // NOSONAR
    }

}
