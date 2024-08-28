package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.DefaultSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Class is responsible for sleeping proper time period
 * (according to the schema provided).
 * It encapsulates stopwatch to help client understand if sleep call is required.
 * <p>
 * If sleeping time is zero, sleep method is ignored.
 * If sleep method called more than items in provided schema, tha latest item is kept actual.
 */
public class RetrySchema {

    private long[] sleepMillis;

    private int sleepIndex = 0;

    private long totalTimeout;

    private final Stopwatch stopwatch;

    public RetrySchema() {
        this(DefaultSettings.getImplicitWaitTimeout(), DefaultSettings.getImplicitWaitRetrySchema());
    }

    @VisibleForTesting
    protected RetrySchema(long totalTimeoutMillis, long[] sleepMillis) {
        checkArgument(sleepMillis.length > 0, "Provided retry schema is empty");
        this.sleepMillis = sleepMillis;
        this.totalTimeout = totalTimeoutMillis;
        this.stopwatch = Stopwatch.createStarted();
    }

    public boolean shouldRetry() {
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        return elapsed <= totalTimeout;
    }

    public boolean shouldGiveUp() {
        return !shouldRetry();
    }

    public void sleep() {
        long nextPause = getNextPause();
        if (nextPause == 0) {
            return;
        }
        sleep(nextPause);
    }

    @VisibleForTesting
    long getNextPause() {
        long result = sleepMillis[sleepIndex];
        if (sleepIndex < sleepMillis.length - 1) {
            sleepIndex++;
        }
        return result;
    }

    @VisibleForTesting
    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        return String.format("Elapsed time (%s millis) %s total timeout time (%s millis).", elapsed, shouldRetry() ? "<=" : ">", totalTimeout);
    }
}
