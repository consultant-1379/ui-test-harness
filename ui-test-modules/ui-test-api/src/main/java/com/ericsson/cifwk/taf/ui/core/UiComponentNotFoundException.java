package com.ericsson.cifwk.taf.ui.core;

/**
 * Thrown if searched UI element was not found.
 */
public class UiComponentNotFoundException extends UiException {

    private static final long serialVersionUID = 1244303857745442206L;

    private final String mappingInfo;

    public UiComponentNotFoundException(Throwable exception, String mappingInfo) {
        super(exception);
        this.mappingInfo = mappingInfo;
    }

    public UiComponentNotFoundException(String mappingInfo) {
        super(mappingInfo);
        this.mappingInfo = mappingInfo;
    }

    public String getMappingInfo() {
        return mappingInfo;
    }
}
