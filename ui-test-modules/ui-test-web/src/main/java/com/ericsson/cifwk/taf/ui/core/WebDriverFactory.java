package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserOS;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.spi.UiGridService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

import static com.ericsson.cifwk.taf.utils.InternalFileFinder.findFile;

class WebDriverFactory {

    private static final String MACOS_DRIVER_SUFFIX = "mac";
    private static final String LINUX_DRIVER_SUFFIX = "linux";
    private static final String WINDOWS_DRIVER_SUFFIX = "win";
    private static final String SELENIUM_CHROME_WEBDRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";
    private static final String SELENIUM_IE_WEBDRIVER_SYSTEM_PROPERTY = "webdriver.ie.driver";
    private static final String SELENIUM_FIREFOX_WEBDRIVER_SYSTEM_PROPERTY = "webdriver.gecko.driver";

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final String IEXPLORER_DRIVER_BINARY_NAME = getDriverBinaryName(BrowserType.IEXPLORER);
    private static final String CHROME_DRIVER_BINARY_NAME = getDriverBinaryName(BrowserType.CHROME);
    private static final String GECKO_DRIVER_BINARY_NAME = getDriverBinaryName(BrowserType.FIREFOX);
    private static final String PHANTOMJS_DRIVER_BINARY_NAME = getDriverBinaryName(BrowserType.PHANTOMJS);
    private static final String SELENIUM_DRIVERS_FOLDER = isWindows() ? "selenium-drivers\\" : "selenium-drivers/";
    private static final String BLANK_PAGE_URL = "about:blank";

    private static UiGridService gridService;

    static {
        gridService = new UiTestEnvironmentInfoProvider();
    }

    private WebDriverFactory() {
    }

    public static WebDriver createDefault() {
        return create(BrowserType.FIREFOX);
    }

    public static WebDriver create(BrowserType browserType) {
        return create(browserType, new DesiredCapabilities());
    }

    public static WebDriver create(BrowserType browserType, DesiredCapabilities capabilities) { // NOSONAR
        switch (browserType) {
            case HEADLESS:
                return htmlUnitDriver(capabilities);
            case FIREFOX:
                return firefoxDriver(capabilities);
            case IEXPLORER:
                return iexplorerDriver(capabilities);
            case CHROME:
                return chromeDriver(capabilities);
            case PHANTOMJS:
                return ghostDriver(capabilities);
            default:
                throw new UnsupportedOperationException("Unsupported  browser type: " + browserType);
        }
    }

    private static WebDriver ghostDriver(DesiredCapabilities capabilities) {
        String PHANTOMJS_DRIVER_BINARY_PATH =
                getPathToDriverBinary(SELENIUM_DRIVERS_FOLDER + PHANTOMJS_DRIVER_BINARY_NAME);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,PHANTOMJS_DRIVER_BINARY_PATH);
        setDriverSystemPropertyIfAbsent(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOMJS_DRIVER_BINARY_NAME);
        return new PhantomJSDriver(DesiredCapabilities.phantomjs().merge(capabilities));
    }

    private static WebDriver htmlUnitDriver(DesiredCapabilities capabilities) {
        capabilities.setBrowserName("htmlunit");
        capabilities.setVersion(org.openqa.selenium.remote.BrowserType.FIREFOX);
        return new HtmlUnitDriver(capabilities) {
            protected WebClient modifyWebClient(final WebClient client) {
                WebClientOptions clientOptions = client.getOptions();
                clientOptions.setUseInsecureSSL(true);
                clientOptions.setJavaScriptEnabled(true);
                clientOptions.setRedirectEnabled(true);
                clientOptions.setThrowExceptionOnScriptError(false);
                client.waitForBackgroundJavaScript(5000);

                return client;
            }
        };
    }

    /**
     * Date: 03/SEP/2021
     * CIS-150557: TAF to enable local testing in testwares. Currently Selenium v3.141.59 is being used!! <br />
     * For local testing in FIREFOX.. GeckoDriver V0.29.0 is used which supports FF>60.0.
     * For more info: https://firefox-source-docs.mozilla.org/testing/geckodriver/Support.html
     */
    private static WebDriver firefoxDriver(DesiredCapabilities capabilities) {
        FirefoxProfile profile = (FirefoxProfile) capabilities.getCapability(FirefoxDriver.PROFILE);
        if (profile == null) {
            profile = new FirefoxProfile();
        }
        profile.setPreference("browser.startup.homepage", BLANK_PAGE_URL);
        profile.setPreference("startup.homepage_welcome_url", BLANK_PAGE_URL);
        profile.setPreference("startup.homepage_welcome_url.additional", BLANK_PAGE_URL);
        setDriverSystemPropertyIfAbsent(SELENIUM_FIREFOX_WEBDRIVER_SYSTEM_PROPERTY, GECKO_DRIVER_BINARY_NAME);
        capabilities.setCapability("marionette",true);
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        FirefoxOptions options = new FirefoxOptions();
        options.merge(capabilities);
        return new FirefoxDriver(options);
    }

    private static WebDriver iexplorerDriver(DesiredCapabilities capabilities) {
        if (!isWindows()) {
            throw new UnsupportedOperationException("Internet explorer driver works only on Windows");
        }
        if (capabilities.getCapability(CapabilityType.PROXY) != null) {
            throw new UnsupportedOperationException("Proxy isn't supported for Internet explorer, because it will change the system settings");
        }
        setDriverSystemPropertyIfAbsent(SELENIUM_IE_WEBDRIVER_SYSTEM_PROPERTY, IEXPLORER_DRIVER_BINARY_NAME);
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer().merge(capabilities);
        desiredCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        desiredCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.merge(desiredCapabilities);
        return new InternetExplorerDriver(options);
    }

    private static WebDriver chromeDriver(DesiredCapabilities capabilities) {
        setDriverSystemPropertyIfAbsent(SELENIUM_CHROME_WEBDRIVER_SYSTEM_PROPERTY, CHROME_DRIVER_BINARY_NAME);
        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);
        return new ChromeDriver(options);
    }

    private static void setDriverSystemPropertyIfAbsent(String seleniumSysPropName, String driverBinaryName) {
        if (StringUtils.isBlank(System.getProperty(seleniumSysPropName))) {
            String fullPathToIeDriverBinary = getPathToDriverBinary(SELENIUM_DRIVERS_FOLDER + driverBinaryName);
            System.setProperty(seleniumSysPropName, fullPathToIeDriverBinary);
        }
    }

    @VisibleForTesting
    static String getPathToDriverBinary(String driverBinaryName) {
        String pathToDriver = findFile(driverBinaryName);
        if (pathToDriver == null) {
            throw new IllegalStateException("Cannot find the full path to '" + driverBinaryName + "'");
        }
        return pathToDriver;
    }

    @VisibleForTesting
    protected static boolean isWindows() {
        String driverOsPart = getDriverOsPart();
        return driverOsPart.equals(WINDOWS_DRIVER_SUFFIX);
    }

    static String getDriverBinaryName(BrowserType browserType) {
        boolean isWindows = isWindows();
        int osBits = getOsBits();
        switch (browserType) {
            case CHROME:
                return String.format("chrome-%s-x%s%s", getDriverOsPart(), osBits, isWindows ? ".exe" : "");
            case FIREFOX:
                return String.format("geckodriver-%s-x%s%s", getDriverOsPart(), osBits, isWindows ? ".exe" : "");
            case PHANTOMJS:
                return String.format("phantomjs%s", isWindows ? ".exe" : "");
            case IEXPLORER:
                if (!isWindows) {
                    LOGGER.warn("Internet explorer driver would work only on Windows");
                    return null;
                }
                return String.format("ie-win-x%s.exe", osBits);
            default:
                throw new UnsupportedOperationException("Browser " + browserType + " doesn't have an additional Selenium binary configured");
        }
    }

    private static int getOsBits() {
        int osBits = 64;
        try {
            osBits = Integer.parseInt(System.getProperty("sun.arch.data.model"));
        } catch (NumberFormatException ignore) {
            // Ignore
        }
        return osBits;
    }

    private static String getDriverOsPart() {
        if (isWinOs()) {
            return WINDOWS_DRIVER_SUFFIX;
        }
        if (isUnix()) {
            return LINUX_DRIVER_SUFFIX; // We'll use Linux drivers on UNIX, too
        }
        if (isMacOs()) {
            return MACOS_DRIVER_SUFFIX;
        }
        throw new UnsupportedOperationException(String.format("OS '%s' is not supported", System.getProperty("os.name")));
    }

    private static boolean isWinOs() {
        return doesOsNameContain("win");
    }

    private static boolean isMacOs() {
        return doesOsNameContain("mac");
    }

    private static boolean isUnix() {
        return doesOsNameContain("ux") || doesOsNameContain("unix") || doesOsNameContain("aix") || doesOsNameContain("sunos");
    }

    private static boolean doesOsNameContain(String needle) {
        String osName = System.getProperty("os.name");
        return StringUtils.containsIgnoreCase(osName, needle);
    }

    static boolean isGridAvailable() {
        return gridService.isGridDefined();
    }

    static WebDriver createRemote(BrowserType browserType, BrowserOS os, String browserVersion, DesiredCapabilities desiredCapabilities) {
        int retry = 1;
        while(true) {
            checkRemoteDriverState(browserType, os, desiredCapabilities);
            DesiredCapabilities caps = getDesiredCapabilities(browserType, os, browserVersion);
            LOGGER.info("Browser Type::OS::BrowserVersion=" + browserType + "::" + os + "::" + browserVersion);
            caps.merge(desiredCapabilities);
            String gridHubUrl = getGridHubUrl();
            LOGGER.info("Grid URL being:" + gridHubUrl);
            try {
                switch (browserType) {
                    case FIREFOX:
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.addPreference("browser.download.manager.showWhenStarting",false);
                        firefoxOptions.addPreference("browser.download.viewableInternally.enabledTypes", "application/json;application/xml;text/xml");
                        firefoxOptions.addPreference("browser.helperApps.alwaysAsk.force", false);
                        firefoxOptions.merge(caps);
                        RemoteWebDriver firefoxWebDriver = new RemoteWebDriver(new URL(gridHubUrl), firefoxOptions);
                        configureWebDriver(firefoxWebDriver);
                        return firefoxWebDriver;
                    case CHROME:
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.merge(caps);
                        RemoteWebDriver chromeWebDriver = new RemoteWebDriver(new URL(gridHubUrl), chromeOptions);
                        configureWebDriver(chromeWebDriver);
                        return chromeWebDriver;
                    case IEXPLORER:
                        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                        internetExplorerOptions.merge(caps);
                        RemoteWebDriver internetExplorerWebDriver = new RemoteWebDriver(new URL(gridHubUrl), internetExplorerOptions);
                        configureWebDriver(internetExplorerWebDriver);
                        return internetExplorerWebDriver;
                    default:
                        RemoteWebDriver webDriver = new RemoteWebDriver(new URL(gridHubUrl), caps);
                        configureWebDriver(webDriver);
                        return webDriver;
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid URL defined for Selenium grid location", e); // NOSONAR
            } catch (WebDriverException e) {
                if (retry > 3) {
                    if (StringUtils.contains(e.getMessage(), "Error forwarding the new session cannot find")) {
                        if (!StringUtils.isBlank(browserVersion)) {
                            throw new UiException(String.format("Grid at '%s' doesn't have a '%s' version of %s browser set up on %s OS with %s. " +
                                    "An appropriate node needs to be added to grid", gridHubUrl, browserVersion, browserType, os, desiredCapabilities));
                        }
                        else {
                            throw new UiException(String.format("Grid at '%s' doesn't have a %s browser set up on %s OS with capabilities %s. " +
                                    "An appropriate node needs to be added to grid", gridHubUrl, browserType, os, desiredCapabilities));
                        }
                    }
                    throw new RuntimeException("Failed to create browser instance on grid " + gridHubUrl , e);// NOSONAR
                }
                else {
                    LOGGER.info("Failed to create browser instance using grid [{}], retrying in 5 seconds",gridHubUrl);
                    sleep(5);
                    retry++;
                }
            }
        }
    }

    private static void checkRemoteDriverState(BrowserType browserType, BrowserOS os, DesiredCapabilities desiredCapabilities) {
        if (!isGridAvailable()) {
            throw new UiException("Grid configuration not found");
        }

        if (BrowserType.IEXPLORER == browserType) {
            if (BrowserOS.WINDOWS != os) {
                throw new IllegalArgumentException("Internet explorer driver works only on Windows");
            }
            if (desiredCapabilities.getCapability(CapabilityType.PROXY) != null) {
                throw new UnsupportedOperationException("Proxy isn't supported for Internet explorer, because it will change the global System settings");
            }
        }
    }

    static void configureWebDriver(RemoteWebDriver webDriver) {
        webDriver.setFileDetector(new LocalFileDetector());
    }

    static String getGridHubUrl() {
        if (!gridService.isGridDefined()) {
            throw new IllegalStateException("Selenium grid host configuration not found - cannot instantiate remote browser");
        }
        return String.format("http://%s:%s/wd/hub", gridService.getGridHost(), gridService.getGridPort());
    }

    private static DesiredCapabilities getDesiredCapabilities(BrowserType browserType, BrowserOS os, String browserVersion) {
        DesiredCapabilities caps = getDefaultCapabilities(browserType);
        updatePlatform(os, caps);

        if (!StringUtils.isBlank(browserVersion)) {
            caps.setVersion(browserVersion);
        }

        caps.setJavascriptEnabled(true);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        caps.setCapability(CapabilityType.HAS_NATIVE_EVENTS, false);
        return caps;
    }

    private static void updatePlatform(BrowserOS os, DesiredCapabilities caps) {
        switch (os) {
            case LINUX:
                caps.setPlatform(Platform.LINUX);
                break;
            case WINDOWS:
                caps.setPlatform(Platform.WINDOWS);
                break;
            case ANY:
                caps.setPlatform(Platform.ANY);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static DesiredCapabilities getDefaultCapabilities(BrowserType browserType) {
        DesiredCapabilities caps;
        switch (browserType) {
            case HEADLESS:
                caps = DesiredCapabilities.htmlUnit();
                break;
            case FIREFOX:
                caps = new DesiredCapabilities();
                break;
            case IEXPLORER:
                caps = new DesiredCapabilities();
                caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                caps.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
                break;
            case CHROME:
                caps = new DesiredCapabilities();
                break;
            case PHANTOMJS:
                caps = DesiredCapabilities.phantomjs();
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return caps;
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting to create browser instance", e);
        }
    }
}
