package com.ericsson.cifwk.taf.ui.selenium.remote;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserOS;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import org.junit.AfterClass;
import org.junit.Assert;

public class RemoteBrowserTest {

    // @Test
    public void stressTest() throws Exception {
        final int threadsPerNode = 20;
        final Object[][] browsers = {
                {BrowserType.FIREFOX, BrowserOS.LINUX},
                {BrowserType.CHROME, BrowserOS.LINUX},
                {BrowserType.FIREFOX, BrowserOS.WINDOWS},
                {BrowserType.CHROME, BrowserOS.WINDOWS},
                {BrowserType.IEXPLORER, BrowserOS.WINDOWS}
        };

        int runnerTotalAmount = browsers.length * threadsPerNode;
        Thread[] runners = new Thread[runnerTotalAmount];

        for (int x = 0; x < browsers.length; x++) {
            Object[] browserSetup = browsers[x];
            for (int i = 0; i < threadsPerNode; i++) {
                Thread runner =
                        new Thread(new BrowserTestRunner((BrowserType) browserSetup[0], (BrowserOS) browserSetup[1]));
                runners[(x * threadsPerNode) + i] = runner;
                runner.start();
            }
        }
        for (int i = 0; i < runnerTotalAmount; i++) {
            runners[i].join();
        }
    }

    private class BrowserTestRunner implements Runnable {
        private BrowserSetup browserSetup;

        BrowserTestRunner(BrowserType browserType, BrowserOS browserOS) {
            this(browserType, browserOS, null);
        }

        BrowserTestRunner(BrowserType browserType, BrowserOS browserOS, String browserVersion) {
            this.browserSetup = new BrowserSetup(browserType, browserOS, browserVersion);
        }

        @Override
        public void run() {
            Browser browser = UiToolkit.newBrowser(browserSetup.getBrowserType(), browserSetup.getOs(), browserSetup.getBrowserVersion());
            BrowserTab tab = browser.open("http://www.msn.com");
            Assert.assertNotNull(tab.getTitle());
            UiToolkit.pause(2000);
            browser.close();
        }
    }

    @AfterClass
    public static void cleanUp() {
        UiToolkit.closeAllWindows();
    }
}
