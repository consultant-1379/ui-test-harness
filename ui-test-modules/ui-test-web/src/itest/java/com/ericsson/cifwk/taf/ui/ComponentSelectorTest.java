package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.WaitTimedOutException;
import com.ericsson.cifwk.taf.ui.models.SampleViewModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ComponentSelectorTest {

    Browser browser;

    @Before
    public void setUp() throws Exception {
        browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
    }

    @Test(expected = WaitTimedOutException.class)
    public void testBySelectorId() {
        String firstPage = BrowserTestUtils.findHtmlPage("third.htm");
        BrowserTab first = browser.open(firstPage);
        SampleViewModel view = first.getView(SampleViewModel.class);
        assertNotNull(first.waitUntilComponentIsDisplayed(view.getNoticeOkButton(), 1000));
    }

    @After
    public void tearDown() throws Exception {
        browser.close();
        UiToolkit.closeAllWindows();
    }
}
