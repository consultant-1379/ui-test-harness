package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.core.UiException;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation-agnostic Web browser
 */
public class Browser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Browser.class);

    private final UiWindowProvider<BrowserTab> windowProvider;

    Browser(UiWindowProvider<BrowserTab> windowProvider) {
        this.windowProvider = windowProvider;
    }

    /**
     * Opens a URL in the browser tab
     *
     * @param url to open
     * @return an instance of {@link com.ericsson.cifwk.taf.ui.BrowserTab}
     */
    public BrowserTab open(String url) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("url", url);
        List<BrowserTab> newPages = windowProvider.get(attributes);
        if (newPages.isEmpty()) {
            throw new UiException("Failed to open new window");
        }
        BrowserTab currentWindow = windowProvider.getCurrentWindow();
        UiComponentSize size = currentWindow.getSize();
        LOGGER.info("The current URL is: {}", currentWindow.getCurrentUrl());
        LOGGER.info("The current window dimensions is: {} x {}", size.getWidth(), size.getHeight());

        return newPages.get(0);
    }

    /**
     * Closes all virtual tabs, opened by this browser, and the browser itself
     */
    public void close() {
        LOGGER.info("Closing all browsers " + this.getSeleniumGridSessionId() + " with " + this.getAllOpenTabs() );
        windowProvider.close();
    }

    /**
     * Checks if this browser is closed
     *
     * @return <code>true</code> if this browser is closed
     */
    public boolean isClosed() {
        return windowProvider.isClosed();
    }

    /**
     * Closes tab <code>tab</code>
     */
    public void closeTab(BrowserTab tab) {
        LOGGER.info(" Closing browser tab title " + tab.getTitle()  + " session Id " + this.getSeleniumGridSessionId() );
        windowProvider.closeWindow(tab);
    }

    /**
     * Returns an amount of windows currently opened.
     *
     * @return amount of windows currently opened.
     */
    public int getOpenedWindowsAmount() {
        return windowProvider.getOpenedWindowDescriptors().size();
    }

    /**
     * Returns the tabs that are currently opened.
     *
     * @return the tabs that are currently opened.
     */
    public List<BrowserTab> getAllOpenTabs() {
        return windowProvider.getAllOpenWindows();
    }

    /**
     * Returns the amount of tabs currently opened.
     *
     * @return the amount of tabs currently opened.
     */
    public int getAmountOfOpenTabs() {
        return windowProvider.getAllOpenWindows().size();
    }

    /**
     * Switches the focus from current window to <code>window</code>.
     *
     * @param window Existing window to switch to
     * @throws <code>IllegalStateException</code> if window was not found (possibly, already closed).
     */
    public void switchWindow(BrowserTab window) {
        windowProvider.switchWindow(window);
    }

    /**
     * Returns the currently opened (focused) window
     *
     * @return currently opened (focused) window
     */
    @SuppressWarnings("unchecked")
    public BrowserTab getCurrentWindow() {
        LOGGER.info(String.format("getCurrentWindow All opentabs list is  '%s' total opened tabs in this session '%s' is '%s' ",
                getAllOpenTabs(),getSeleniumGridSessionId(),getAmountOfOpenTabs()));
        try {
            return windowProvider.getCurrentWindow();
        }catch (UndeclaredThrowableException ute){
            LOGGER.info("UndeclaredThrowableException with reason No focused browser" + ute.getCause());
            try {
                Thread.sleep(3000);
                return windowProvider.getCurrentWindow();
            } catch (final InterruptedException e) {
                return windowProvider.getCurrentWindow();
            }
        }
    }

    /**
     * <p>
     * Set window size
     * </p>
     * <p>
     * <b>NOTE:</b> this will work for the whole browser only if all tabs are opened really as tabs - which may vary depending on the
     * browser type. To resize the tab (for example, representing a window opened in Javascript pop-up) use
     * {@link com.ericsson.cifwk.taf.ui.BrowserTab#setSize(int, int)}
     * </p>
     *
     * @param width  browser window screen width
     * @param height browser window screen height
     */
    public void setSize(int width, int height) {
        windowProvider.setCurrentWindowSize(width, height);
    }

    /**
     * <p>
     * Set window size
     * </p>
     * <p>
     * <b>NOTE:</b> this will work for the whole browser only if all tabs are opened really as tabs - which may vary depending on the
     * browser type. To resize the tab (for example, representing a window opened in Javascript pop-up) use
     * {@link com.ericsson.cifwk.taf.ui.BrowserTab#setSize(BrowserSetup.Resolution)}
     * </p>
     *
     * @param resolution pre-defined browser window screen resolution
     * @see com.ericsson.cifwk.taf.ui.BrowserSetup.Resolution
     */
    public void setSize(BrowserSetup.Resolution resolution) {
        windowProvider.setCurrentWindowSize(resolution.width, resolution.height);
    }

    /**
     * @return Selenium Grid current session ID if remote web driver is used, null otherwise
     */
    public String getSeleniumGridSessionId() {
        if (windowProvider instanceof SeleniumGridAware) {
            return ((SeleniumGridAware) windowProvider).getSeleniumGridSessionId();
        }
        return null;
    }

    /**
     * @return Selenium Grid remote web driver capabilities, empty map if driver is not remote
     */
    Map<String, Object> getSeleniumGridCapabilities() {
        if (windowProvider instanceof SeleniumGridAware) {
            return ((SeleniumGridAware) windowProvider).getSeleniumGridCapabilities();
        }
        return Maps.newHashMap();
    }

}
