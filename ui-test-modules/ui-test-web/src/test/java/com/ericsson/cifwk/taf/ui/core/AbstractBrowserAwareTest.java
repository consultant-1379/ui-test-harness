package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTest;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBrowserAwareTest {

    private static final Logger log = LoggerFactory.getLogger(AbstractBrowserAwareTest.class);
    protected static final String BASIC_UI_COMPONENTS_PAGE = "basic_ui_components.htm";

    protected Browser browser;
    protected BrowserTab browserTab;
    protected BrowserType browserType = BrowserType.HEADLESS;
    protected ViewModel genericView;

    @AfterClass
    public static void tearClassDown() {
        log.info("Closing UI windows that are still open");
        UiToolkit.closeAllWindows();
    }

    protected BasicComponentsView openComponentsView() {
        openDefaultBrowserTab();
        return browserTab.getView(BasicComponentsView.class);
    }

    protected ViewModel openGenericView() {
        openDefaultBrowserTab();
        return browserTab.getGenericView();
    }

    void openDefaultBrowserTab() {
        openBrowserTab(BASIC_UI_COMPONENTS_PAGE);
    }

    protected void openBrowserTab(String htmlPage) {
        String testPage = findHtmlPage(htmlPage);
        this.browser = UiToolkit.newBrowser(getBrowserType());
        this.browserTab = browser.open(testPage);
        this.genericView = browserTab.getGenericView();

    }

    protected BrowserType getBrowserType() {
        return browserType;
    }

    protected String findHtmlPage(String fileName) {
        return BrowserTest.findFile(fileName);
    }

    void sleep(int millis) {
        try {
            Thread.sleep(millis); // NOSONAR
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
