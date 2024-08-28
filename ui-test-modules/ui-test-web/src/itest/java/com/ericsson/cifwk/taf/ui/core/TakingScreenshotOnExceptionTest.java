package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.selenium.AbstractBrowserAwareITest;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ericsson.cifwk.taf.ui.core.UiComponentPredicates.DISPLAYED;
import static org.junit.Assert.fail;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 22/12/2016
 */
public class TakingScreenshotOnExceptionTest extends AbstractBrowserAwareITest {

    private static final int WAIT_TIMEOUT = 3_000;

    private TestAppender appender;

    @Before
    public void setUp() throws Exception {

        // opening the page
        UiToolkit.setDefaultWaitTimeout(WAIT_TIMEOUT);
        openBrowserTab("implicit-checks.html");

        // configuring log appender
        appender = new TestAppender();
        Logger.getRootLogger().addAppender(appender);
    }

    @After
    public void tearDown() {
        Logger.getRootLogger().removeAppender(appender);
        UiToolkit.setDefaultWaitTimeout(UiToolkit.DEFAULT_TIMEOUT_MILLIS);
        UiToolkit.closeAllWindows();
    }

    @Test
    public void shouldCreateScreenshotOnNotClickableComponent() {
        Button button = genericView.getButton("#invisibleButton");

        try {
            button.click();
            fail();
        } catch(UiComponentNotVisibleException e) {
            assertOneScreenshotTaken();
        }
    }

    @Test
    public void shouldCreateScreenshotOnUnsatisfiedGenericPredicate() {
        try {
            genericView.waitUntil(new AlwaysFalsePredicate());
            fail();
        } catch(WaitTimedOutException e) {
            assertOneScreenshotTaken();
        }
    }

    @Test
    public void shouldCreateScreenshotOnUnsatisfiedPredicate() {
        Button button = genericView.getButton("#nonExistingButton");

        try {
            genericView.waitUntil(button, DISPLAYED);
            fail();
        } catch(WaitTimedOutException e) {
            assertOneScreenshotTaken();
        }
    }

    @Test
    public void shouldCreateScreenshotOnComponentNotFound() {
        Button button = genericView.getButton("#nonExistingButton");

        try {
            button.click();
            fail();
        } catch(UiComponentNotFoundException e) {
            assertOneScreenshotTaken();
        }
    }

    @Test
    public void shouldCreateScreenshotOnDisappearedElement() {

        // hiding button
        Button button = genericView.getButton("#hidingButton");
        button.click();

        try {
            button.click();
            fail();
        } catch(UiComponentNotFoundException e) {
            assertOneScreenshotTaken();
        }
    }

    protected BrowserType getBrowserType() {
        return BrowserType.PHANTOMJS;
    }

    private void assertOneScreenshotTaken() {

        // checking that screenshot was taken
        List<String> logs = appender.getLogs();
        String screenshotMarker = "Taking screenshot...";
        Assertions.assertThat(logs).contains(screenshotMarker);

        // checking that screenshot was taken just once
        Assertions.assertThat(logs).containsOnlyOnce(screenshotMarker);

        // checking that screenshot was attached to Allure report
        Assertions.assertThat(logs).containsOnlyOnce("Screenshot will be attached to Allure report");
    }

    private static class TestAppender extends AppenderSkeleton {

        private final List<String> logs = new ArrayList<>();

        @Override
        public boolean requiresLayout() {
            return false;
        }

        @Override
        protected void append(final LoggingEvent loggingEvent) {
            logs.add(loggingEvent.getRenderedMessage());
        }

        @Override
        public void close() {
            logs.clear();
        }

        public List<String> getLogs() {
            return logs;
        }
    }

    private static class AlwaysFalsePredicate extends GenericPredicate {
        @Override
        public boolean apply() {
            return false;
        }
    }
}
