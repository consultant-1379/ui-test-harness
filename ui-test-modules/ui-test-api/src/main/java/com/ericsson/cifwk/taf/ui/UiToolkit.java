package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.execution.TestExecutionEvent;
import com.ericsson.cifwk.taf.ui.BrowserSetup.Resolution;
import com.ericsson.cifwk.taf.ui.core.UiTestEnvironmentInfoProvider;
import com.ericsson.cifwk.taf.ui.core.UiWindowProviderRegistry;
import com.ericsson.cifwk.taf.ui.core.UiWindowType;
import com.ericsson.cifwk.taf.ui.spi.UiGridService;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

import static com.ericsson.cifwk.taf.ui.BrowserSetup.IMAGE_RECOGNITION_CAPABILITY;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * UI Test Toolkit service providing gateway.
 */
public class UiToolkit {

    private static final Logger LOGGER = LoggerFactory.getLogger(UiToolkit.class);

    public static final int MAX_TIMEOUT_MILLIS = 30_000;

    public static final int DEFAULT_TIMEOUT_MILLIS = 15_000;

    private static int defaultWaitTimeoutInMillis = DEFAULT_TIMEOUT_MILLIS;

    @VisibleForTesting
    protected static final InheritableThreadLocal<UiWindowProviderRegistry> registryContainer;
    @VisibleForTesting
    protected static final InheritableThreadLocal<Boolean> resetDone;

    private static TestExecutionEvent testExecutionEvent = TestExecutionEvent.ON_TEST_FINISH;

    private static UiGridService gridService;

    static {
        registryContainer = new InheritableThreadLocal<UiWindowProviderRegistry>() {
            @Override
            protected UiWindowProviderRegistry initialValue() {
                UiWindowProviderRegistry registry = new UiWindowProviderRegistry();
                LOGGER.debug("UI window provider registry initialized. {}", registry.hashCode());
                return registry;
            }
        };

        resetDone = new InheritableThreadLocal<>();
        gridService = new UiTestEnvironmentInfoProvider();
    }

    UiToolkit() {
        // hiding object instantiation
    }

    /**
     * For internal use.
     * Resets registry container with UiWindowProviderRegistry.
     * Allows to separate scope of UI window providers between threads.
     */
    public static void resetRegistryContainer() {
        resetDone.remove();
        resetDone.set(true);

        registryContainer.remove();
        registryContainer.set(new UiWindowProviderRegistry());
    }

    /**
     * For internal use.
     * Check if re-initialization of registry container is possible (no providers created, not happened in current thread).
     * It is not recommended to re-initialize registry container if it has created providers inside.
     * This will cause providers leak and leave them permanently open.
     */
    public static boolean isReInitializationPossible() {
        int createdProviderCount = registryContainer.get().getCreatedProviderCount();
        return !resetDone.get() && createdProviderCount == 0;
    }

    /**
     * Creates a new instance of browser, with default browser type and on
     * default target OS (if it will be remote). Make sure you have TAF
     * properties <code>taf_ui.default_browser</code> and <code>taf_ui.default_OS</code> defined.
     * <p>
     * If a <code>SELENIUM_GRID</code> host is defined in hosts property file, browser will be started on this grid, otherwise it will be
     * started locally. In case of local run only default browser type setting will be used - target OS settings are ignored for local
     * browsers. See {@link #newBrowser(BrowserType, BrowserOS, String)} for more details on grid setup.
     *
     * @return new instance of browser
     * @throws com.ericsson.cifwk.taf.ui.core.UiException if one of the default settings were not set
     */
    public static Browser newBrowser() {
        return newBrowser(new BrowserSetup());
    }

    /**
     * Creates a new instance of browser, to be run on default target OS (if it
     * will be remote). In the latter case make sure you have TAF property <code>taf_ui.default_OS</code> defined.
     * <p>
     * If a <code>SELENIUM_GRID</code> host is defined in hosts property file, browser will be started on this grid, otherwise it will be
     * started locally. In case of local run the target OS setting will be ignored. See {@link #newBrowser(BrowserType, BrowserOS, String)}
     * for more details on grid setup.
     *
     * @param browserType type of the browser to initialize
     * @return new instance of <code>browserType</code> browser
     * @throws com.ericsson.cifwk.taf.ui.core.UiException if the default OS setting is not set, or
     *                                                    if there is no such browser and OS combination available in the grid.
     */
    public static Browser newBrowser(BrowserType browserType) {
        return newBrowser(BrowserSetup.build().withType(browserType));
    }

    /**
     * Creates a new instance of browser. <br />
     * see: {@link #newBrowser(BrowserType)}
     *
     * @param browserType type of the browser to initialize
     * @param resolution  pre-defined browser window screen resolution
     * @return instance of the local or remote browser, depending on Selenium
     * @see Resolution
     * @see UiToolkit#newBrowser(BrowserType browserType)
     */
    public static Browser newBrowser(BrowserType browserType, Resolution resolution) {
        return newBrowser(BrowserSetup.build().withType(browserType).withSize(resolution));
    }

    /**
     * Creates a new instance of browser. <br />
     * see: {@link #newBrowser(BrowserType)}
     *
     * @param browserType type of the browser to initialize
     * @param width       browser window screen width
     * @param height      browser window screen height
     * @return instance of the local or remote browser, depending on Selenium
     * @see UiToolkit#newBrowser(BrowserType browserType)
     * @see UiToolkit#newBrowser(BrowserType browserType, Resolution resolution)
     */
    public static Browser newBrowser(BrowserType browserType, int width, int height) {
        return newBrowser(BrowserSetup.build().withType(browserType).withSize(width, height));
    }

    /**
     * <p>
     * Creates a new instance of browser that works on the remote server, if a <code>SELENIUM_GRID</code> host is defined in hosts property
     * file. Please see {@link #newBrowser(BrowserType, BrowserOS, String)} for more details on grid setup.
     * <p>
     * If grid configuration is not found, browser is started locally, and <code>os</code> parameter is ignored.
     *
     * @param browserType type of the browser to open.
     * @param os          operational system that the browser needs to be launched on.
     *                    Ignored if grid configuration is absent.
     * @return instance of the local or remote browser, depending on Selenium
     * Grid configuration presence.
     * @throws com.ericsson.cifwk.taf.ui.core.UiException if there is no such browser and OS
     *                                                    combination available in the grid.
     */
    public static Browser newBrowser(BrowserType browserType, BrowserOS os) {
        return newBrowser(BrowserSetup.build().withType(browserType).withOS(os));
    }

    /**
     * Creates a new instance of browser. <br />
     *
     * @param browserType type of the browser to open.
     * @param os          operational system that the browser needs to be launched on.
     *                    Ignored if grid configuration is absent.
     * @param resolution  pre-defined browser window screen resolution
     * @return instance of the local or remote browser, depending on Selenium
     * @see Resolution
     * @see UiToolkit#newBrowser(BrowserType browserType, BrowserOS os)
     */
    public static Browser newBrowser(BrowserType browserType, BrowserOS os, Resolution resolution) {
        return newBrowser(BrowserSetup.build().withType(browserType).withOS(os).withSize(resolution));
    }

    /**
     * Creates a new instance of browser. <br />
     *
     * @param browserType type of the browser to open.
     * @param os          operational system that the browser needs to be launched on.
     *                    Ignored if grid configuration is absent.
     * @param width       browser window screen width
     * @param height      browser window screen height
     * @return instance of the local or remote browser, depending on Selenium
     * @see UiToolkit#newBrowser(BrowserType browserType, BrowserOS os)
     * @see UiToolkit#newBrowser(BrowserType browserType, BrowserOS os, Resolution resolution)
     */
    public static Browser newBrowser(BrowserType browserType, BrowserOS os, int width, int height) {
        return newBrowser(BrowserSetup.build().withType(browserType).withOS(os).withSize(width, height));
    }

    /**
     * <p>
     * Creates a new instance of browser that works on the remote server, if a <code>SELENIUM_GRID</code> host is defined in hosts property
     * file. Browser will be launched on appropriate Selenium Grid cluster. You have to define HTTP connection details for Grid hub (master
     * node) in hosts property file. You have to set the <code>SELENIUM_GRID</code> host type for this. Browser requests will be sent to the
     * first grid found in hosts property file.
     * <p>
     * <p>
     * Host declaration example:
     * <p>
     * <p>
     * <pre>
     * host.UiTestGrid.type=SELENIUM_GRID
     * host.UiTestGrid.ip=atvts874.athtem.eei.ericsson.se
     * host.UiTestGrid.port.ssh=22
     * host.UiTestGrid.port.http=4444
     * host.UiTestGrid.user.root.pass=shroot
     * host.UiTestGrid.user.root.type=ADMIN
     * </pre>
     * <p>
     * <p>
     * Please note that the browser will be opened only in case if appropriate node is present in the grid: with the browser, its version
     * and OS matching the ones provided as parameters to this method. So, if there is only Linux Firefox 17 available, requests to Firefox
     * 17 on Windows and Firefox 25 on Linux will fail.
     * <p>
     * <p>
     * If grid configuration is not found, browser is started locally, <code>os</code> and <code>browserVersion</code> parameters are
     * ignored.
     *
     * @param browserType    type of the browser to open.
     * @param os             operational system that the browser needs to be launched on.
     *                       Ignored if grid configuration is absent.
     * @param browserVersion browser version. Ignored if grid configuration is absent.
     * @return instance of the local or remote browser, depending on Selenium
     * Grid configuration presence. In case of the remote one the
     * browser requests will be sent to the first grid found in hosts
     * property file.
     * @throws com.ericsson.cifwk.taf.ui.core.UiException if there is no such OS, browser and its
     *                                                    version combination available in the grid (in case of remote
     *                                                    browser).
     */
    public static Browser newBrowser(BrowserType browserType, BrowserOS os, String browserVersion) {
        return newBrowser(BrowserSetup.build().withType(browserType).withOS(os).withVersion(browserVersion));
    }

    /**
     * Creates a new instance of browser. <br />
     *
     * @param browserType    type of the browser to open.
     * @param os             operational system that the browser needs to be launched on.
     *                       Ignored if grid configuration is absent.
     * @param browserVersion browser version. Ignored if grid configuration is absent.
     * @param resolution     pre-defined browser window screen resolution
     * @return instance of the local or remote browser, depending on Selenium
     * @see Resolution
     * @see UiToolkit#newBrowser(BrowserType browserType, BrowserOS os, String browserVersion)
     */
    public static Browser newBrowser(BrowserType browserType, BrowserOS os, String browserVersion, Resolution resolution) {
        return newBrowser(BrowserSetup.build().withType(browserType).withOS(os).withVersion(browserVersion).withSize(resolution));
    }

    /**
     * Creates a new instance of browser. <br />
     *
     * @param browserType    type of the browser to open.
     * @param os             operational system that the browser needs to be launched on.
     *                       Ignored if grid configuration is absent.
     * @param browserVersion browser version. Ignored if grid configuration is absent.
     * @param width          browser window screen width
     * @param height         browser window screen height
     * @return instance of the local or remote browser, depending on Selenium
     * @see UiToolkit#newBrowser(BrowserType browserType, BrowserOS os, String browserVersion)
     * @see UiToolkit#newBrowser(BrowserType browserType, BrowserOS os, String browserVersion, Resolution resolution)
     */
    public static Browser newBrowser(BrowserType browserType, BrowserOS os, String browserVersion, int width, int height) {
        return newBrowser(BrowserSetup.build().withType(browserType).withOS(os).withVersion(browserVersion).withSize(width, height));
    }

    public static Browser newBrowser(BrowserSetup.Builder browserSetup) {
        return newBrowser(browserSetup.setup());
    }

    private static Browser newBrowser(BrowserSetup browserSetup) {
        return new Browser(getWindowProvider(browserSetup));
    }

    /**
     * Creates Sikuli-grid aware browser with default settings (target browser type and OS).
     *
     * @return Sikuli-grid aware browser with default settings
     */
    public static Browser newBrowserWithImageRecognition() {
        return newBrowserWithImageRecognition(BrowserSetup.build());
    }

    public static Browser newBrowserWithImageRecognition(BrowserSetup.Builder capabilitiesBuilder) {
        BrowserSetup browserSetup = capabilitiesBuilder.withImageRecognitionCapability().setup();
        Browser browser = new Browser(getWindowProvider(browserSetup));

        // Selenium should do it, but it doesn't.. so manually feeding up the same!!
        browser.getSeleniumGridCapabilities().put(IMAGE_RECOGNITION_CAPABILITY, true);

        return browser;
    }

    private static UiWindowProvider<BrowserTab> getWindowProvider(BrowserSetup browserSetup) {
        UiWindowProviderRegistry registry = registryContainer.get();
        return registry.getBrowserWindowProvider(browserSetup);
    }

    /**
     * Returns an instance of desktop navigator.
     *
     * @param highlightOn if <code>true</code>, the activated UI components will be
     *                    highlighted (good for presentation purposes, useless
     *                    otherwise).
     * @return an instance of desktop navigator
     */
    public static DesktopNavigator newDesktopNavigator(boolean highlightOn) {
        UiWindowProviderRegistry registry = registryContainer.get();
        return new DesktopNavigator(registry.getDesktopWindowProvider(highlightOn));
    }

    /**
     * Returns an instance of desktop navigator, without active component
     * highlight.
     *
     * @return an instance of desktop navigator
     */
    public static DesktopNavigator newDesktopNavigator() {
        return newDesktopNavigator(false);
    }

    @SuppressWarnings("unchecked")
    public static DesktopNavigator newSwtNavigator(String host, int port) {
        ServiceLoader<UiWindowProviderFactory> serviceLoader = ServiceLoader.load(UiWindowProviderFactory.class);
        Iterable<UiWindowProviderFactory> swtFactories = Iterables.filter(serviceLoader, new Predicate<UiWindowProviderFactory>() {
            @Override
            public boolean apply(UiWindowProviderFactory factory) {
                return UiWindowType.DESKTOP_SWT == factory.getType();
            }
        });
        UiWindowProviderFactory factory = Iterables.getOnlyElement(swtFactories);
        return new DesktopNavigator((UiWindowProvider<DesktopWindow>) factory.newInstance(host, port));
    }

    @SuppressWarnings("unchecked")
    public static DesktopNavigator newDesktopNavigator(Browser browserWithImageRecognition, String imagesResourceBundle) {
        // local Sikuli execution
        if (!gridService.isGridDefined()) {
            LOGGER.warn("Selenium grid is not available. Image recognition will happen locally using current screen.");
            return newDesktopNavigator();
        }

        // checking arguments
        String errorMessage = "Provided browser should have image recognition capability (please use UI.newBrowserWithImageRecognition() method).";
        boolean isBrowserWithImageRecognition = browserWithImageRecognition.getSeleniumGridCapabilities().containsKey(IMAGE_RECOGNITION_CAPABILITY);
        // checkArgument(isBrowserWithImageRecognition, errorMessage);

        ServiceLoader<UiWindowProviderFactory> serviceLoader = ServiceLoader.load(UiWindowProviderFactory.class);
        Iterable<UiWindowProviderFactory> factories = Iterables.filter(serviceLoader, new Predicate<UiWindowProviderFactory>() {
            @Override
            public boolean apply(UiWindowProviderFactory factory) {
                return UiWindowType.DESKTOP_SIKULI_GRID == factory.getType();
            }
        });
        UiWindowProviderFactory factory = Iterables.getOnlyElement(factories);
        String seleniumGridSessionId = browserWithImageRecognition.getSeleniumGridSessionId();
        UiWindowProvider uiWindowProvider = factory.newInstance(seleniumGridSessionId, imagesResourceBundle);
        return new DesktopNavigator((UiWindowProvider<DesktopWindow>) uiWindowProvider);
    }

    /**
     * @param millis millis
     * @see com.ericsson.cifwk.taf.ui.sdk.GenericViewModel#waitUntilComponentIsDisplayed
     * @see com.ericsson.cifwk.taf.ui.core.UiComponent#waitUntil
     * @deprecated This is bad practice and should be replaced with waiting for a component on the page.
     */
    @Deprecated
    public static void pause(long millis) {
        try {
            LOGGER.warn("UI.pause is bad practice and should only be used as a last resort. Waiting for a component is the preferred solution");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns the total number of browsers opened
     *
     * @return total number of browsers opened
     */
    public static int getActiveBrowserCount() {
        UiWindowProviderRegistry registry = registryContainer.get();
        return registry.getActiveBrowserCount();
    }

    public static void closeAllWindows() {
        LOGGER.info("Closing all windows");
        UiWindowProviderRegistry registry = registryContainer.get();
        registry.closeAllCreatedProviders();
    }

    /**
     * Close UI windows when appropriate Event Type is set
     */
    public static void closeWindow(TestExecutionEvent eventType) {
        testExecutionEvent = eventType;
    }

    public static boolean isCloseOnTest() {
        return TestExecutionEvent.ON_TEST_FINISH.equals(testExecutionEvent);
    }

    public static boolean isCloseOnSuite() {
        return TestExecutionEvent.ON_SUITE_FINISH.equals(testExecutionEvent);
    }

    public static boolean isCloseOnExecution() {
        return TestExecutionEvent.ON_EXECUTION_FINISH.equals(testExecutionEvent);
    }

    /**
     * Used to start the initialization block to instantiate the registryContainer with parent class of test runner
     */
    public static void init() {
        registryContainer.get();

        resetDone.remove();
        resetDone.set(false);
    }

    /**
     * Default timeout for wait...() methods in <code>BrowserTab</code>, <code>ViewModel</code> and <code>UiComponent</code>.
     * The recommended maximum timeout is 30000 milliseconds.
     * If this method is not called, the default timeout is 15000 milliseconds.
     *
     * @param timeoutInMillis new timeout in millis
     */
    public static void setDefaultWaitTimeout(int timeoutInMillis) {
        defaultWaitTimeoutInMillis = (int) getValidTimeout(timeoutInMillis);
    }

    /**
     * Used to validate the timeout value. The recommended maximum timeout is 30000 milliseconds.
     *
     * @param timeoutInMillis timeout in millis
     */
    public static long getValidTimeout(long timeoutInMillis) {
        Preconditions.checkArgument(timeoutInMillis > 0);
        if (timeoutInMillis > MAX_TIMEOUT_MILLIS) {
            LOGGER.warn("The recommended maximum timeout value is {}", MAX_TIMEOUT_MILLIS);
        }
        return timeoutInMillis;
    }

    /**
     * Returns the default timeout for wait...() methods in <code>BrowserTab</code>, <code>ViewModel</code> and <code>UiComponent</code>.
     *
     * @return default timeout in millis. If not altered by @{link #setDefaultWaitTimeout}, it's 15000 milliseconds.
     */
    public static int getDefaultWaitTimeout() {
        return defaultWaitTimeoutInMillis;
    }
}
