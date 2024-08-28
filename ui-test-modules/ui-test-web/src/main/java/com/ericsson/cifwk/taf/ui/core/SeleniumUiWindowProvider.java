package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTabImpl;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.InternalDriverAware;
import com.ericsson.cifwk.taf.ui.SeleniumGridAware;
import com.ericsson.cifwk.taf.ui.debug.DebugBrowserTab;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang.StringUtils.isBlank;

class SeleniumUiWindowProvider implements UiWindowProvider<BrowserTab>, SeleniumGridAware, InternalDriverAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUiWindowProvider.class);

    private static final String MSG_SELENIUM_GRID_CONFIGURATION = "Selenium grid configuration detected - all browsers will be launched {}";
    private static final Set<String> SKIP_TAB_FOCUS_ON_METHODS =
            Sets.newHashSet("notify", "notifyAll", "wait", "equals", "hashCode", "toString", "getWindowDescriptor",
                    "getMessageBox", "finalize", "getClass", "getDriver", "markAsClosed");
    @VisibleForTesting
    static int sleepStep = 500;
    @VisibleForTesting
    static int waitForAnonymousWindowOpenTimeoutInMillis = 10_000;

    private final BrowserType browserType;
    private final Map<String, BrowserTab> openedPagesRegistry = new ConcurrentHashMap<>();
    private boolean closed = false;

    private final WebDriver driver;

    SeleniumUiWindowProvider(BrowserSetup providedSetup) {
        BrowserSetup fullBrowserSetup = new BrowserSetupHelper().getFullBrowserSetup(providedSetup);
        this.browserType = fullBrowserSetup.getBrowserType();

        LOGGER.info("BrowserType used is: {}", browserType);
        LOGGER.info("BrowserType Version is: {}", fullBrowserSetup.getBrowserVersion());
        LOGGER.info("Operating System used is: {}", fullBrowserSetup.getOs());


        if (WebDriverFactory.isGridAvailable()) {
            LOGGER.info(MSG_SELENIUM_GRID_CONFIGURATION, "remotely");
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities(fullBrowserSetup.getCapabilities());
            this.driver = WebDriverFactory.createRemote(browserType, fullBrowserSetup.getOs(), fullBrowserSetup.getBrowserVersion(), desiredCapabilities);
        } else {
            LOGGER.info("Selenium grid configuration was not detected - all browsers will be launched on local machine, OS and browser version settings will be ignored");
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities(fullBrowserSetup.getCapabilities());
            this.driver = WebDriverFactory.create(fullBrowserSetup.getBrowserType(), desiredCapabilities);
        }
        if (fullBrowserSetup.getWidth() > 0) {
            this.driver.manage().window().setSize(new Dimension(fullBrowserSetup.getWidth(), fullBrowserSetup.getHeight()));
        }
    }

    // For test purposes
    SeleniumUiWindowProvider(BrowserType browserType, WebDriver driver) {
        this.browserType = browserType;
        this.driver = driver;
    }

    @Override
    public List<BrowserTab> get(Map<String, Object> attributes) {
        Set<String> currentWindowDescriptors = getOpenedWindowDescriptors();
        String url = (String) attributes.get("url");
        if (!attributes.containsKey("_self")) {
            openNewWindowIfNecessary(currentWindowDescriptors);
        }
        driver.get(url);
        Set<BrowserTab> newTabs = synchronizeTabs();
        return Lists.newArrayList(newTabs);
    }

    /**
     * Synchronizes the registered tabs with actually opened, creates instances
     * of missing <code>BrowserTab</code>s, and also returns in a set.
     *
     * @return set of tabs that were not registered
     */
    @SuppressWarnings("unchecked")
    Set<BrowserTab> synchronizeTabs() {
        LOGGER.info("Registered Browsers:: " + getOpenedPagesRegistry().keySet());
        LOGGER.info("Opened Browsers:: " + getOpenedWindowDescriptors());

        Set<String> currentWindowDescriptors = getOpenedWindowDescriptors();
        Set<String> registeredDescriptors = getOpenedPagesRegistry().keySet();
        Set<BrowserTab> newTabs = new HashSet<>();

        if (CollectionUtils.disjunction(registeredDescriptors,
                currentWindowDescriptors).isEmpty()) {
            return newTabs;
        }
        Collection<String> newTabDescriptors;
        newTabDescriptors = CollectionUtils.disjunction(registeredDescriptors, currentWindowDescriptors);
        LOGGER.info("Disjunction of Registered & Current browsers list:: " + newTabDescriptors);
        if (!newTabDescriptors.isEmpty()) {
            BrowserTab lastNewTab = null;
            for (String newTabDescriptor : newTabDescriptors) {
                // Check to protect from possible window closure in the meantime
                if (!registeredDescriptors.contains(newTabDescriptor)) {
                    BrowserTab newTab = createNewTab(newTabDescriptor);
                    addWindowToRegistry(newTab);
                    newTabs.add(newTab);
                    lastNewTab = newTab;
                } else {
                    //Tab was anonymously closed
                    LOGGER.info("Window {} is already closed and so removing the same from the registered list!!", newTabDescriptor );
                    getOpenedPagesRegistry().remove(newTabDescriptor);
                }
                if (lastNewTab != null && !(lastNewTab.isClosed())) {
                    switchWindow(lastNewTab.getWindowDescriptor(), true);
                }
            }
        }
        return newTabs;
    }

    Map<String, BrowserTab> getOpenedPagesRegistry() {
        return openedPagesRegistry;
    }

    private BrowserTab createNewTab(String newTabDescriptor) {
        SeleniumUiMediator mediator = createMediator();
        SeleniumUiComponentStateManagerFactory stateManagerFactory = new SeleniumUiComponentStateManagerFactory(mediator);
        final BrowserTabImpl target = new BrowserTabImpl(stateManagerFactory.getAutowirer(), stateManagerFactory, this,
                newTabDescriptor, mediator);

        return (BrowserTab) java.lang.reflect.Proxy.newProxyInstance(BrowserTab.class.getClassLoader(),
                new Class[]{BrowserTab.class, DebugBrowserTab.class, InternalDriverAware.class}, new BrowserTabInvocationHandler(target));
    }

    private SeleniumUiMediator createMediator() {
        return new SeleniumUiMediator(driver);
    }

    private void addWindowToRegistry(BrowserTab tab) {
        getOpenedPagesRegistry().put(tab.getWindowDescriptor(), tab);
    }

    private void openNewWindowIfNecessary(Set<String> currentWindowHandles) {
        // Check if anything is already opened in current window
        if (currentWindowHandles.size() != 1) {
            openNewWindowAndFocusOnIt(currentWindowHandles);
        } else {
            String windowDescriptor = currentWindowHandles.iterator().next();
            switchWindow(windowDescriptor);
            String currentUrl = driver.getCurrentUrl();
            if (!isPageBlank(currentUrl)) {
                openNewWindowAndFocusOnIt(currentWindowHandles);
            }
        }
    }

    @VisibleForTesting
    boolean isPageBlank(String currentUrl) {
        if (isBlank(currentUrl) || StringUtils.equals("about:blank", currentUrl)) {
            return true;
        }
        if (browserType == BrowserType.CHROME) {
            return StringUtils.startsWith(currentUrl, "data:");
        }
        if (browserType == BrowserType.IEXPLORER && "WebDriver" .equals(driver.getTitle())) {
            // IE driver opens a custom page on init, so it's not
            // straightforward to check if it is a "start page"
            try {
                String text = driver.findElement(By.cssSelector("p#main")).getText();
                return StringUtils.contains(text, "initial start page for the WebDriver server");
            } catch (NoSuchElementException e) {
                LOGGER.debug("Element not found", e);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void openNewWindowAndFocusOnIt(Set<String> currentWindowHandles) {
        openNewWindow();
        Collection<String> diff = (Collection<String>) CollectionUtils.disjunction(getOpenedWindowDescriptors(), currentWindowHandles);
        Set<String> addedHandles = Sets.newHashSet(diff);
        if (!addedHandles.isEmpty()) {
            for (String addedHandle : addedHandles) {
                driver.switchTo().window(addedHandle);
                if (isPageBlank(driver.getCurrentUrl())) {
                    break;
                }
            }
        }

    }

    private void openNewWindow() {
        Dimension size = driver.manage().window().getSize();
        String script = "var result=window.open('','_blank', 'width=%s,height=%s');if(result==null) return false;else return true;";
        Object element = ((JavascriptExecutor) driver).executeScript(String.format(script, size.getWidth(), size.getHeight()));
        boolean newWindowOpened = element instanceof Boolean && (Boolean) element;
        if (!newWindowOpened) {
            throw new UiException("Unable to open tab");
        }
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.BROWSER;
    }

    @Override
    public void close() {
        for (BrowserTab page : getOpenedPagesRegistry().values()) {
            page.markAsClosed();
        }
        getOpenedPagesRegistry().clear();
        try {
            driver.quit();
        } catch (Exception e) {
            LOGGER.info("Exception while trying to close Selenium driver", e);
        }
        closed = true;
    }

    @Override
    public void closeWindow(BrowserTab window) {
        synchronized (getOpenedPagesRegistry()) {
            String windowDescriptor = window.getWindowDescriptor();
            LOGGER.info("Window being closed:: " + windowDescriptor );
            closeWindow(windowDescriptor);
            window.markAsClosed();
            getOpenedPagesRegistry().remove(windowDescriptor);
        }
    }

    void closeWindow(String windowDescriptor) {
        String currentWindowHandle = getCurrentWindowDescriptor();
        LOGGER.info("Current Window Descriptor being:: " + getCurrentWindowDescriptor());
        boolean currentWindow = StringUtils.equals(windowDescriptor, currentWindowHandle);
        if (!currentWindow) {
            switchWindow(windowDescriptor);
        }
        int openWindowsAmount = getOpenedWindowDescriptors().size();
        driver.close();
        LOGGER.info(String.format("Window Descriptor '%s' has just been closed!!", windowDescriptor));
        LOGGER.info("Total open windows count in window provider " + openWindowsAmount);
        if (openWindowsAmount == 1) {
            LOGGER.warn("The last window was closed in the window provider - provider is shutting down." +
                    " Please initiate another one to continue ");
            closed = true;
            return;
        }
        if (!currentWindow) {
            switchWindow(currentWindowHandle);
        } else {
            // Current window was closed - so we need to switch to another
            // window,
            // because Selenium will not do that automatically for us - it's
            // still focused
            // on the old window which actually doesn't exist anymore.
            // We'll switch to the first one in set.
            final String windowToFocusOn = getOpenedWindowDescriptors().iterator().next();
            LOGGER.info("The window was closed in the window provider - Switch Focus to another window "
                    + windowToFocusOn);
            switchWindow(windowToFocusOn);
        }
        openedPagesRegistry.remove(windowDescriptor);
    }

    @Override
    public Set<String> getOpenedWindowDescriptors() {
        return driver.getWindowHandles();
    }

    @Override
    public String getCurrentWindowDescriptor() {
        return driver.getWindowHandle();
    }

    @Override
    public void switchWindow(BrowserTab window) {
        switchWindow(window.getWindowDescriptor(), false);
    }

    @Override
    public void switchWindow(String windowDescriptor) {
        switchWindow(windowDescriptor, false);
    }

    private void switchWindow(String windowDescriptor, boolean waitForPageToLoad) {
        try {
            LOGGER.info("Switching to Window:: " + windowDescriptor);
            driver.switchTo().window(windowDescriptor);
            // If window was opened via "window.open()" it doesn't have page loaded immediately, but Selenium returns its
            // handle right away - as opposed to when you use driver.get().
            // So if we return control immediately here, this will result in test errors - as the tests will operate
            // with window which is not fully loaded yet.
            if (waitForPageToLoad && isPageBlank(driver.getCurrentUrl())) {
                long startTime = System.currentTimeMillis();
                boolean loaded = false;
                do {
                    boolean notEmpty = !isPageBlank(driver.getCurrentUrl());
                    loaded = notEmpty || loaded;
                    pause();
                } while (System.currentTimeMillis() - startTime < waitForAnonymousWindowOpenTimeoutInMillis);
                if (!loaded) {
                    LOGGER.warn(String.format("Waited for %d millis for browser tab %s to load, but in vain - it's still '%s'",
                            waitForAnonymousWindowOpenTimeoutInMillis, windowDescriptor, driver.getCurrentUrl()));
                }
            }
        } catch (NoSuchWindowException e) {
            String error = String.format("Cannot find window with descriptor '%s'", windowDescriptor);
            LOGGER.error(error, e);
            throw new IllegalStateException(error);
        }
    }

    private static void pause() {
        try {
            Thread.sleep(sleepStep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public List<BrowserTab> getAllOpenWindows() {
        synchronizeTabs();
        return Lists.newArrayList(getOpenedPagesRegistry().values());
    }

    @Override
    public UiComponentBasedDelayer getExecutionDelayer() {
        SeleniumUiMediator mediator = createMediator();
        SeleniumUiComponentStateManagerFactory stateManagerFactory = new SeleniumUiComponentStateManagerFactory(mediator);
        return new GenericUiComponentBasedDelayer(stateManagerFactory, mediator);
    }

    @Override
    public BrowserTab getWindowByTitle(String title) {
        List<BrowserTab> allOpenWindows = getAllOpenWindows();
        for (BrowserTab window : allOpenWindows) {
            if (StringUtils.equals(window.getTitle(), title)) {
                return window;
            }
        }
        return null;
    }

    @Override
    public BrowserTab getCurrentWindow() {
        synchronizeTabs();
        String currentWindowDescriptor = getCurrentWindowDescriptor();
        return openedPagesRegistry.get(currentWindowDescriptor);
    }

    @Override
    public void setCurrentWindowSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @Override
    public String getSeleniumGridSessionId() {
        if (driver instanceof RemoteWebDriver) {
            RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
            return remoteWebDriver.getSessionId().toString();
        }
        return null;
    }

    @Override
    public Map<String, Object> getSeleniumGridCapabilities() {
        if (driver instanceof RemoteWebDriver) {
            RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;
            Capabilities capabilities = remoteWebDriver.getCapabilities();
            if (capabilities instanceof DesiredCapabilities) {
                DesiredCapabilities desiredCapabilities = (DesiredCapabilities) capabilities;

                // making capabilities map modifiable
                return getCapabilitiesField(desiredCapabilities);
            }
            return new HashMap<>(capabilities.asMap());
        }
        return Maps.newHashMap();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getCapabilitiesField(DesiredCapabilities desiredCapabilities) {
        try {
            Field capabilitiesField = DesiredCapabilities.class.getDeclaredField("capabilities");
            capabilitiesField.setAccessible(true);
            return (Map<String, Object>) capabilitiesField.get(desiredCapabilities);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D> D getInternalDriver() {
        return (D) driver;
    }

    private static class BrowserTabInvocationHandler implements InvocationHandler {

        private BrowserTabImpl target;

        BrowserTabInvocationHandler(BrowserTabImpl target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                if (!SKIP_TAB_FOCUS_ON_METHODS.contains(method.getName())) {
                    target.activate();
                }
            } catch (Exception ignore) {
                // activation is optional - ignoring
                LOGGER.debug("Exception during tab activation", ignore);
            }
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                if (e.getCause() != null) {
                    throw e.getCause();
                } else {
                    throw e;
                }
            }
        }
    }

}
