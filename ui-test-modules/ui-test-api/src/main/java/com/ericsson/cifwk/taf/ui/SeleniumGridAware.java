package com.ericsson.cifwk.taf.ui;

import java.util.Map;

/**
 * Marks class as being aware of Selenium Grid.
 * <p>
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.29.
 */
public interface SeleniumGridAware {

    /**
     * @return Selenium Grid current session ID if remote web driver is used, null otherwise
     */
    String getSeleniumGridSessionId();

    /**
     * @return Selenium Grid remote web driver capabilities, empty map if driver is not remote
     */
    Map<String, Object> getSeleniumGridCapabilities();

}
