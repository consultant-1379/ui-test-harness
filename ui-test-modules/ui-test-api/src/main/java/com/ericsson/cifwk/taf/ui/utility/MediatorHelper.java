package com.ericsson.cifwk.taf.ui.utility;

import com.ericsson.cifwk.taf.annotations.Attachment;
import com.ericsson.cifwk.taf.ui.spi.ScreenshotProvider;
import com.google.common.base.Optional;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by eniakel on 15/06/2016.
 */
public class MediatorHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediatorHelper.class);

    private static final String SCREENSHOT_FILE_NAME = "screenshot.png";

    private MediatorHelper() {
        // Empty private constructor for static utility methods
    }

    @Attachment(type = "image/png", value = SCREENSHOT_FILE_NAME)
    public static byte[] createScreenshot(ScreenshotProvider screenshotProvider) {
        return createScreenshotIfEnabled(screenshotProvider);
    }

    // Don't remove screenshotName - it's unused in this class but used in AttachmentAspects to form a screenshot name in the Allure report
    @Attachment(type = "image/png", value = "{1}")
    public static byte[] createScreenshot(ScreenshotProvider screenshotProvider, String screenshotNameInTheReport) { // NOSONAR
        return createScreenshotIfEnabled(screenshotProvider);
    }

    private static byte[] createScreenshotIfEnabled(ScreenshotProvider screenshotProvider) {
        if (ScreenshotToggle.isEnabled()) {
            LOGGER.info("Screenshot will be attached to Allure report");
            return takeScreenshotInternally(screenshotProvider);
        } else {
            // empty screenshot won't be attached to the report
            return new byte[]{};
        }
    }

    private static byte[] takeScreenshotInternally(ScreenshotProvider screenshotProvider) {
        try {
            Optional<InputStream> optional = screenshotProvider.takeScreenshot();
            if (optional.isPresent()) {
                InputStream stream = optional.get();
                return IOUtils.toByteArray(stream);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to create screenshot", e);
        }
        return new byte[]{};
    }

}
