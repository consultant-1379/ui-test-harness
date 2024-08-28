package com.ericsson.cifwk.taf.ui.core;

/**
 * Thrown if expectation has timed out while waiting for element to appear.
 */
public class WaitTimedOutException extends UiException {

    private static final long serialVersionUID = 972079382865141399L;

    public WaitTimedOutException(String elementName, long timeoutInMillis) {
        super(String.format("Timed out while waiting for component '%s', was waiting for %d millis", elementName, timeoutInMillis));
    }

    public WaitTimedOutException(long timeoutInMillis) {
        super(String.format("Timed out while waiting for condition, was waiting for %d millis", timeoutInMillis));
    }

    public WaitTimedOutException(String message) {
        super(message);
    }
}
