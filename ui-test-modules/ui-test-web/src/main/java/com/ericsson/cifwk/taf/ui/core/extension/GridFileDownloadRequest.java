package com.ericsson.cifwk.taf.ui.core.extension;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.core.UiTestEnvironmentInfoProvider;
import com.ericsson.cifwk.taf.ui.spi.UiGridService;
import com.google.common.annotations.VisibleForTesting;
import io.sterodium.extensions.client.download.FileDownloadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 06/10/2015
 *         <p/>
 */
public class GridFileDownloadRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GridFileDownloadRequest.class);

    private FileDownloadRequest fileDownloadRequest;
    private UiGridService gridService;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private int delay = 12;
    private int retryAttempts = 100;

    public GridFileDownloadRequest(Browser browser) {
        gridService = new UiTestEnvironmentInfoProvider();
        if (gridService.isGridDefined()) {
            String gridHost = gridService.getGridHost();
            int gridPort = gridService.getGridPort();
            fileDownloadRequest = new FileDownloadRequest(gridHost, gridPort, browser.getSeleniumGridSessionId());
        }
    }

    public File download(String pathToFile) {
        if(!isFileDownloadRequestCreated()){
            return null;
        }
        boolean shouldRetry = true;
        int retries = 0;
        File result = null;
        do {
            try {
                LOGGER.info("Attempt No: " + (retries + 1));
                result = requestDownload(pathToFile);
                if (!isFileEmpty(result) || isLastAttempt(retries)) {
                    shouldRetry = false;
                } else {
                    retries++;
                    delay(timeUnit, delay);
                }
            } catch (RuntimeException e) {
                LOGGER.info("File does not exist at {} attempt", retries + 1);
                if (isLastAttempt(retries)) {
                    LOGGER.error("File does not exist");
                    throw e;
                }
                delay(timeUnit, delay);
                retries++;
            }
        } while (shouldRetry);
        return result;
    }

    @VisibleForTesting
    protected boolean isFileDownloadRequestCreated() {
        return fileDownloadRequest != null;
    }

    @VisibleForTesting
    protected File requestDownload(String pathToFile) {
       return fileDownloadRequest.download(pathToFile);
    }

    private boolean isLastAttempt(int attempt) {
        return attempt + 1  >= retryAttempts;
    }

    private static boolean isFileEmpty(File file) {
        return file.length() <= 0;
    }

    private static void delay(TimeUnit timeUnit, int count) {
        try {
            timeUnit.sleep(count);
        } catch (InterruptedException e) {
            LOGGER.error("failed to wait");
            Thread.currentThread().interrupt();
        }
    }
}
