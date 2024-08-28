package com.ericsson.cifwk.taf.ui.utility;

import static java.lang.Boolean.TRUE;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 06.01.2017
 */
public abstract class ScreenshotToggle {

    private static final ThreadLocal<Boolean> enabled = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };

    private ScreenshotToggle(){}


    public static void enable() {
        enabled.set(true);
    }

    public static void disable() {
        enabled.set(false);
    }

    public static boolean isEnabled() {
        return TRUE.equals(enabled.get());
    }

}
