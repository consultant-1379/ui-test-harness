package com.ericsson.cifwk.taf.ui;

import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class BrowserSetupTest {

    @Test
    public void shouldConstructBrowserSetupWithDefaultValues() throws Exception {
        BrowserSetup browserSetup = new BrowserSetup();
        assertThat(browserSetup.getBrowserType(), nullValue());
        assertThat(browserSetup.getOs(), nullValue());
        assertThat(browserSetup.getBrowserVersion(), nullValue());
        assertThat(browserSetup.getWidth(), is(0));
        assertThat(browserSetup.getHeight(), is(0));
        assertThat(browserSetup.getCapabilities().isEmpty(), is(true));
    }

    @Test
    public void shouldBuilderConstructBrowserSetupsameAsDefaultBrowserSetup() throws Exception {
        BrowserSetup expected = new BrowserSetup();
        BrowserSetup actual = BrowserSetup.build().setup();
        assertThat(actual.getBrowserType(), equalTo(expected.getBrowserType()));
        assertThat(actual.getOs(), equalTo(expected.getOs()));
        assertThat(actual.getBrowserVersion(), equalTo(expected.getBrowserVersion()));
        assertThat(actual.getWidth(), is(expected.getWidth()));
        assertThat(actual.getHeight(), is(expected.getHeight()));
        assertThat(actual.getCapabilities().size(), is(expected.getCapabilities().size()));
        Iterator<String> a = actual.getCapabilities().keySet().iterator();
        Iterator<String> e = expected.getCapabilities().keySet().iterator();
        while (a.hasNext()) {
            String actualKey = a.next();
            String expectedKey = e.next();
            assertThat(actualKey, equalTo(expectedKey));
            assertThat(actual.getCapabilities().get(actualKey), equalTo(expected.getCapabilities().get(expectedKey)));
        }
    }

    @Test
    public void testBrowserSetupBuilder() throws Exception {
        BrowserSetup actual = BrowserSetup.build()
                .withType(BrowserType.CHROME)
                .withOS(BrowserOS.WINDOWS)
                .withVersion("VERSION")
                .withCapability("Capability", "TEST")
                .withSize(BrowserSetup.Resolution.RESOLUTION_1024x768)
                .setup();
        assertThat(actual.getBrowserType(), equalTo(BrowserType.CHROME));
        assertThat(actual.getOs(), equalTo(BrowserOS.WINDOWS));
        assertThat(actual.getBrowserVersion(), equalTo("VERSION"));
        assertThat(actual.getWidth(), is(BrowserSetup.Resolution.RESOLUTION_1024x768.width));
        assertThat(actual.getHeight(), is(BrowserSetup.Resolution.RESOLUTION_1024x768.height));
        assertThat(actual.getCapabilities().size(), is(1));
        assertThat((String) actual.getCapabilities().get("Capability"), equalTo("TEST"));
    }
}
