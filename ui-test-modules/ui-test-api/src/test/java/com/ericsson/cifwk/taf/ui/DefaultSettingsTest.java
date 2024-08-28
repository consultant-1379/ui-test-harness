package com.ericsson.cifwk.taf.ui;

import static java.lang.System.setProperty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.ericsson.cifwk.taf.ui.DefaultSettings.RETRY_SCHEMA;
import static com.ericsson.cifwk.taf.ui.DefaultSettings.TIMEOUT;
import static com.ericsson.cifwk.taf.ui.DefaultSettings.UI_RETRY_SCHEMA_PROPERTY;
import static com.ericsson.cifwk.taf.ui.DefaultSettings.UI_RETRY_SCHEMA_TIMEOUT_PROPERTY;
import static com.ericsson.cifwk.taf.ui.DefaultSettings.getLongProperty;
import static com.ericsson.cifwk.taf.ui.DefaultSettings.getProperty;
import static com.ericsson.cifwk.taf.ui.spi.TestPropertiesKeyMapper.KEY;
import static com.ericsson.cifwk.taf.ui.spi.TestPropertiesKeyMapper.LONG_KEY;
import static com.ericsson.cifwk.taf.ui.spi.TestPropertiesKeyMapper.TAF_KEY;
import static com.ericsson.cifwk.taf.ui.spi.TestPropertiesKeyMapper.TAF_LONG_KEY;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.rules.TestRule;

import com.ericsson.cifwk.taf.ui.spi.TestPropertiesKeyMapper;

public class DefaultSettingsTest {

    @Rule
    public final TestRule restoreSystemProperties = new RestoreSystemProperties();

    @Test
    public void testGetDefaultBrowserType() throws Exception {
        Assert.assertEquals(BrowserType.CHROME, DefaultSettings.getDefaultBrowserType());
        setProperty(DefaultSettings.UI_DEFAULT_BROWSER_PROPERTY, "FIREFOX");
        Assert.assertEquals(BrowserType.FIREFOX, DefaultSettings.getDefaultBrowserType());
    }

    @Test
    public void testGetDefaultGridIp() throws Exception {
        Assert.assertNull(DefaultSettings.getDefaultGridIp());
        setProperty(DefaultSettings.UI_DEFAULT_GRID_IP_PROPERTY, "127.0.0.1");
        Assert.assertEquals("127.0.0.1", DefaultSettings.getDefaultGridIp());
    }

    @Test
    public void testGetDefaultGridPort() throws Exception {
        Assert.assertEquals(DefaultSettings.getDefaultGridPort(), 0);
        setProperty(DefaultSettings.UI_DEFAULT_GRID_PORT_PROPERTY, "5555");
        Assert.assertEquals(5555, DefaultSettings.getDefaultGridPort());
    }

    @Test
    public void testGetDefaultBrowserOS() throws Exception {
        Assert.assertEquals(BrowserOS.WINDOWS, DefaultSettings.getDefaultBrowserOS());
        setProperty(DefaultSettings.UI_DEFAULT_OS_PROPERTY, "LINUX");
        Assert.assertEquals(BrowserOS.LINUX, DefaultSettings.getDefaultBrowserOS());
    }

    @Test
    public void getImplicitWaitTimeout() {
        Assert.assertEquals(TIMEOUT, DefaultSettings.getImplicitWaitTimeout());
        setProperty(UI_RETRY_SCHEMA_TIMEOUT_PROPERTY, "1234");
        Assert.assertEquals(1234L, DefaultSettings.getImplicitWaitTimeout());
    }

    @Test
    public void getImplicitWaitRetrySchema() {
        assertThat(DefaultSettings.getImplicitWaitRetrySchema(), equalTo(RETRY_SCHEMA));
        setProperty(UI_RETRY_SCHEMA_PROPERTY, "1,2,3,4");
        assertThat(DefaultSettings.getImplicitWaitRetrySchema(), equalTo(new long[]{1, 2, 3, 4}));
    }

    @Test
    public void propertyMapped() {
        assertThat(getProperty(KEY)).isNull();

        // search in alternative properties
        setProperty(TAF_KEY, "value1");
        assertThat(getProperty(KEY)).isEqualTo("value1");

        // canonical property has priority
        setProperty(KEY, "value2");
        assertThat(getProperty(KEY)).isEqualTo("value2");
    }

    @Test
    public void longPropertyMapped() {
        assertThat(getLongProperty(LONG_KEY, 0L)).isEqualTo(0L);

        // search in alternative properties
        setProperty(TAF_LONG_KEY, "1");
        assertThat(getLongProperty(LONG_KEY, 0L)).isEqualTo(1L);

        // canonical property has priority
        setProperty(LONG_KEY, "2");
        assertThat(getLongProperty(LONG_KEY, 0L)).isEqualTo(2L);
    }

    @Test
    public void retrySchemaPropertyMapped() {
        assertThat(DefaultSettings.getImplicitWaitRetrySchema()).isEqualTo(RETRY_SCHEMA);
        setProperty(TestPropertiesKeyMapper.TAF_UI_RETRY_SCHEMA_PROPERTY, "1,2,3");
        assertThat(DefaultSettings.getImplicitWaitRetrySchema()).isEqualTo(new Long[]{1L, 2L, 3L});
    }

}
