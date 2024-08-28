package com.ericsson.cifwk.taf.ui;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import static java.lang.String.format;

/**
 * Browser operational system platforms supported by TAF UI
 */
public enum BrowserOS {

    WINDOWS, LINUX, ANY;

    /**
     * Returns all supported OS types, comma-separated
     *
     * @return all supported OS types, comma-separated
     */
    public static String allValues() {
        return Joiner.on(", ").join(values());
    }

    public static BrowserOS forName(String osName) {
        Preconditions.checkArgument(osName != null);
        try {
            return valueOf(osName.toUpperCase());
        } catch (IllegalArgumentException e) {
            String message = format("TAF UI doesn't support OS '%s', only the following are supported: %s", osName, allValues());
            throw new IllegalArgumentException(message, e);
        }
    }
}
