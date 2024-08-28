package com.ericsson.cifwk.taf.ui;

import org.junit.Assert;
import org.junit.Test;

public class BrowserOSTest {

    @Test
    public void testStringValueRecognition() {
        Assert.assertEquals(BrowserOS.WINDOWS, BrowserOS.forName("Windows"));
        Assert.assertEquals(BrowserOS.LINUX, BrowserOS.forName("Linux"));

        try {
            BrowserOS.forName("no such os");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

}
