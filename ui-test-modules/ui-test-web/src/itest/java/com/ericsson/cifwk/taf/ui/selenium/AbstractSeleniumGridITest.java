package com.ericsson.cifwk.taf.ui.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractSeleniumGridITest extends AbstractSeleniumITest {

    static final String[][] SELENIUM_GRID_PROPERTIES = new String[][]{
            {"host.UiTestGrid.type", "SELENIUM_GRID"},
            {"host.UiTestGrid.ip", "141.137.235.242"},
            {"host.UiTestGrid.port.ssh", "22"},
            {"host.UiTestGrid.port.http", "4444"},
            {"host.UiTestGrid.user.root.pass", "shroot"},
            {"host.UiTestGrid.user.root.type", "ADMIN"}
    };

    @BeforeClass
    public static void setUpGridConfiguration() throws Exception {
        for (String[] property : SELENIUM_GRID_PROPERTIES) {
            System.setProperty(property[0], property[1]);
        }
        System.setProperty("taf_ui.default_OS", "LINUX");
    }

    @AfterClass
    public static void tearDownGridConfiguration() throws Exception {
        for (String[] property : SELENIUM_GRID_PROPERTIES) {
            System.getProperties().remove(property[0]);
        }
        System.getProperties().remove("taf_ui.default_OS");
    }
}