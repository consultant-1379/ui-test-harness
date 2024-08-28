package com.ericsson.cifwk.taf.ui;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import static java.lang.String.format;

/**
 * <p>
 * Browser types, supported by TAF UI.
 * <p>
 * <ul>
 * <li><code>HEADLESS</code> browser is being backed up by Selenium HtmlUnit web driver (doesn't start any actual browsers).
 * <li><code>FIREFOX</code> browser is being backed up by Selenium Firefox web driver. Requires Firefox installation on a client machine.
 * <li><code>IEXPLORER</code> browser is being backed up by Selenium Internet Explorer web driver. Requires IE installation on a client machine.
 * <li><code>CHROME</code> browser is being backed up by Selenium Chrome web driver. Requires Chrome installation on a client machine.
 * <li><code>PHANTOMJS</code> headless browser is being backed up by GhostDriver web driver. Requires PhantomJS with GhostDriver installation on a
 * client machine
 */
public enum BrowserType {

    HEADLESS, FIREFOX, IEXPLORER, CHROME, PHANTOMJS;

    /**
     * Returns all supported browser types, comma-separated
     *
     * @return all supported browser types, comma-separated
     */
    public static String allValues() {
        return Joiner.on(", ").join(values());
    }

    /**
     * Returns a browser type based on string representation of its name
     *
     * @param browserName browser name. Should be equal to the name of one of the enum values.
     * @return browser type
     * @throws <code>IllegalArgumentException</code> if there is not browser with such name
     */
    public static BrowserType forName(String browserName) {
        Preconditions.checkArgument(browserName != null, "Browser name should not be null");
        try {
            return valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            String message = format("TAF UI doesn't support browser '%s', only the following are supported: %s", browserName, allValues());
            throw new IllegalArgumentException(message, e);
        }
    }
}
