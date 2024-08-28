package com.ericsson.cifwk.taf.ui.automation;

/**
 * Basic automation interface with a desktop location as context.
 */
public interface AutomationLocation {

    /**
     * Returns a location relative to current.
     *
     * @param dx delta-x
     * @param dy delta-y
     * @return new location
     */
    AutomationLocation relativeLocation(int dx, int dy);

}
