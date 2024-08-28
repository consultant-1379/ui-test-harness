package com.ericsson.cifwk.taf.ui.spi;

/**
 * This is a generic UI Test Toolkit API which could be used to implement and register in SPI.
 * Its intention is to provide information about the Selenium Grid host used for testing.
 *
 * Check JDK {@link java.util.ServiceLoader} for details how to write and register your plugin.
 */
public interface UiGridService {

    /**
     * Verify whether host of GRID is found from provided configuration
     *
     * @return
     *      <code>true</code> if grid is defined, otherwise <code>false</code>
     */
    boolean isGridDefined();

    /**
     * @return name or IP of the GRID host
     */
    String getGridHost();

    /**
     * @return port of the GRID
     */
    Integer getGridPort();
}
