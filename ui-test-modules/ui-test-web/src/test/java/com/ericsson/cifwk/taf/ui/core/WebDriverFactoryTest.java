package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.DefaultSettings;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WebDriverFactoryTest {

    @Rule
    public final TestRule restoreSystemProperties = new RestoreSystemProperties();

    @Mock
    private RemoteWebDriver webDriver;

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfBinaryDoesntExist() {
        WebDriverFactory.getPathToDriverBinary("chrome-linuxfwe-x64");
    }

    @Test
    public void getDriverBinaryName_chrome() {
        setOsName("Windows 2000");
        {
            setOsBits(32);

            Assert.assertEquals("chrome-win-x32.exe", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));

            setOsBits(64);
            Assert.assertEquals("chrome-win-x64.exe", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));
        }

        setOsName("RedHat Linux");
        {
            setOsBits(32);

            Assert.assertEquals("chrome-linux-x32", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));

            setOsBits(64);
            Assert.assertEquals("chrome-linux-x64", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));
        }

        setOsName("HP-UX");
        {
            setOsBits(32);

            Assert.assertEquals("chrome-linux-x32", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));

            setOsBits(64);
            Assert.assertEquals("chrome-linux-x64", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));
        }

        setOsName("Mac OS 4.3");
        {
            setOsBits(32);

            Assert.assertEquals("chrome-mac-x32", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));

            setOsBits(64);
            Assert.assertEquals("chrome-mac-x64", WebDriverFactory.getDriverBinaryName(BrowserType.CHROME));
        }
    }

    @Test
    public void getDriverBinaryName_ie() {
        setOsName("Windows 2000");
        setOsBits(32);

        Assert.assertEquals("ie-win-x32.exe", WebDriverFactory.getDriverBinaryName(BrowserType.IEXPLORER));

        setOsBits(64);
        Assert.assertEquals("ie-win-x64.exe", WebDriverFactory.getDriverBinaryName(BrowserType.IEXPLORER));

        setOsName("Mac OS 4.3");
        Assert.assertNull(WebDriverFactory.getDriverBinaryName(BrowserType.IEXPLORER));
    }

    @Test
    public void getDriverBinaryName_phantomJs() {
        setOsName("Windows 2000");
        setOsBits(32);

        BrowserType browserType = BrowserType.PHANTOMJS;
        Assert.assertEquals("phantomjs.exe", WebDriverFactory.getDriverBinaryName(browserType));

        setOsName("RedHat Linux");
        Assert.assertEquals("phantomjs", WebDriverFactory.getDriverBinaryName(browserType));
    }

    @Test
    public void getGridHubUrl() {
        System.setProperty(DefaultSettings.UI_DEFAULT_GRID_IP_PROPERTY, "gridhost.ericsson.se");
        System.setProperty(DefaultSettings.UI_DEFAULT_GRID_PORT_PROPERTY, "1234");
        Assert.assertEquals("http://gridhost.ericsson.se:1234/wd/hub", WebDriverFactory.getGridHubUrl());
    }

    @Test
    public void configureWebDriver() {
        WebDriverFactory.configureWebDriver(webDriver);
        verify(webDriver).setFileDetector(any(LocalFileDetector.class));
    }

    private void setOsName(String osName) {
        System.setProperty("os.name", osName);
    }

    private void setOsBits(int osBits) {
        System.setProperty("sun.arch.data.model", String.valueOf(osBits));
    }

}
