package com.ericsson.cifwk.taf.ui;

import org.junit.Assert;
import org.junit.Test;

public class BrowserTypeTest {
    @Test
    public void allValues() {
        Assert.assertEquals("HEADLESS, FIREFOX, IEXPLORER, CHROME, PHANTOMJS", BrowserType.allValues());
    }

    @Test
    public void testStringValueRecognition() {
        Assert.assertEquals(BrowserType.FIREFOX, BrowserType.forName("FIREFOX"));
        try {
            BrowserType.forName("no such browser");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }
    }
}