package com.ericsson.cifwk.taf.ui.spi;

import com.google.common.base.Optional;

import java.io.InputStream;

/**
 * Created by eendjor on 08/12/2016.
 */
public interface ScreenshotProvider {

    ScreenshotProvider NO_SCREENSHOT_AVAILABLE = new ScreenshotProvider() {
        @Override
        public Optional<InputStream> takeScreenshot() {
            return Optional.absent();
        }
    };

    /**
     * Captures screenshot.
     *
     * @return optional of byte stream of the taken screenshot in PNG format.
     */
    Optional<InputStream> takeScreenshot();

}
