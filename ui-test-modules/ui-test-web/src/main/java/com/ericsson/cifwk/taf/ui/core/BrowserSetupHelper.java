package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserOS;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.DefaultSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BrowserSetupHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserSetupHelper.class);

    private static final String ERROR_MSG_DEFAULT_VALUE_NOT_FOUND =
            "Tried to retrieve the default %s (%s), but failed. Please set the default TAF UI properties properly or define the values explicitly when initializing the browser";

    /**
     * Merges the provided setup with the default if the provided one is not full (for example, OS or browser type information is missing).
     *
     * @return full setup, combined from the provided one (takes precedence) and the default one
     */
    BrowserSetup getFullBrowserSetup(BrowserSetup providedSetup) {
        BrowserType browserType = providedSetup.getBrowserType();
        BrowserOS os = providedSetup.getOs();
        String browserVersion = providedSetup.getBrowserVersion();

        if (browserType == null) {
            browserType = DefaultSettings.getDefaultBrowserType();
            if (browserType == null) {
                throw new UiException(String.format(ERROR_MSG_DEFAULT_VALUE_NOT_FOUND, "browser type", DefaultSettings.UI_DEFAULT_BROWSER_PROPERTY));
            }
            LOGGER.debug("Browser type was undefined - using the default one (" + browserType + ")");
        }

        if (isGridAvailable() && os == null) {
            os = DefaultSettings.getDefaultBrowserOS();
            if (os == null) {
                throw new UiException(String.format(ERROR_MSG_DEFAULT_VALUE_NOT_FOUND, "browser operational system", DefaultSettings.UI_DEFAULT_OS_PROPERTY));
            }
            LOGGER.debug("Browser OS was undefined - using the default one (" + os + ")");
        }

        return BrowserSetup.build()
                .withType(browserType)
                .withOS(os)
                .withVersion(browserVersion)
                .withSize(providedSetup.getWidth(), providedSetup.getHeight())
                .withCapability(providedSetup.getCapabilities())
                .setup();
    }

    boolean isGridAvailable() {
        return WebDriverFactory.isGridAvailable();
    }
}
