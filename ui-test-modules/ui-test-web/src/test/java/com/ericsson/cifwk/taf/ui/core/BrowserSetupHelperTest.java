package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.configuration.TafConfiguration;
import com.ericsson.cifwk.taf.configuration.TafConfigurationProvider;
import com.ericsson.cifwk.taf.ui.BrowserOS;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.DefaultSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class BrowserSetupHelperTest {

    private TafConfiguration config;
    private BrowserSetup expected;

    @Before
    public void setUp() throws Exception {
        config = TafConfigurationProvider.provide().getRuntimeConfiguration();

        config.setProperty(DefaultSettings.UI_DEFAULT_BROWSER_PROPERTY, "CHROME");
        config.setProperty(DefaultSettings.UI_DEFAULT_OS_PROPERTY, "LINUX");

        expected = BrowserSetup.build()
                .withType(BrowserType.FIREFOX)
                .withOS(BrowserOS.WINDOWS)
                .withVersion("VERSION")
                .withCapability("Capability", "TEST")
                .withSize(BrowserSetup.Resolution.RESOLUTION_1024x768)
                .setup();
    }

    @After
    public void tearDown() throws Exception {
        config.clear();
    }

    @Test
    public void shouldReturnSameForCompleteSetup() throws Exception {
        BrowserSetupHelper setupHelper = spy(new BrowserSetupHelper());
        when(setupHelper.isGridAvailable()).thenReturn(false);
        //
        BrowserSetup actual = setupHelper.getFullBrowserSetup(expected);

        assertThat(actual.getBrowserType(), is(BrowserType.FIREFOX));
        assertThat(actual.getOs(), is(BrowserOS.WINDOWS));
        assertThat(actual.getBrowserVersion(), equalTo(expected.getBrowserVersion()));
        assertThat(actual.getWidth(), is(expected.getWidth()));
        assertThat(actual.getHeight(), is(expected.getHeight()));
        assertThat(actual.getCapabilities().size(), is(expected.getCapabilities().size()));
        assertThat((String) actual.getCapabilities().get("Capability"), equalTo("TEST"));
    }

    @Test
    public void shouldPopulateBrowserTypeIfNotGridAviable() throws Exception {
        BrowserSetupHelper setupHelper = spy(new BrowserSetupHelper());
        when(setupHelper.isGridAvailable()).thenReturn(false);
        expected.setBrowserType(null);
        expected.setOs(null);
        //
        BrowserSetup actual = setupHelper.getFullBrowserSetup(expected);
        assertThat(actual.getBrowserType(), is(BrowserType.CHROME));
        assertThat(actual.getOs(), nullValue());
    }

    @Test
    public void shouldPopulateOsIfGridAviable() throws Exception {
        BrowserSetupHelper setupHelper = spy(new BrowserSetupHelper());
        when(setupHelper.isGridAvailable()).thenReturn(true);
        expected.setBrowserType(null);
        expected.setOs(null);
        //
        BrowserSetup actual = setupHelper.getFullBrowserSetup(expected);

        assertThat(actual.getBrowserType(), is(BrowserType.CHROME));
        assertThat(actual.getOs(), is(BrowserOS.LINUX));
        assertThat(actual.getBrowserVersion(), equalTo(expected.getBrowserVersion()));
    }
}
