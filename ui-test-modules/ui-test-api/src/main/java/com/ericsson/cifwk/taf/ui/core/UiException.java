package com.ericsson.cifwk.taf.ui.core;

/**
 * Base TAF UI exception
 */
public class UiException extends RuntimeException {
    private static final long serialVersionUID = 8381580495969213334L;

    public UiException() {
        super();
    }

    public UiException(String message) {
        super(message);
    }

    public UiException(Throwable exception) {
        super(exception);
    }

    public UiException(String message, Throwable exception) {
        super(message, exception);
    }

}
