package com.ericsson.cifwk.taf.ui.core;

/**
 * Thrown if searched UI element is not visible.
 */
public class UiComponentNotVisibleException extends UiException {

    private static final long serialVersionUID = 1244303857745442206L;

    private final String mappingInfo;

    public UiComponentNotVisibleException(Throwable exception, String mappingInfo) {
        super(exception);
        this.mappingInfo = mappingInfo;
    }

    public String getMappingInfo() {
        return mappingInfo;
    }

    @Override
    public String getMessage() {
        return mappingInfo;
    }
}
