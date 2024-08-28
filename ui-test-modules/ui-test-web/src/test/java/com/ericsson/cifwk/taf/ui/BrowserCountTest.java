package com.ericsson.cifwk.taf.ui;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BrowserCountTest {

    @Test
    public void testGetActiveWindowsCount() {
        Browser browser1 = UiToolkit.newBrowser(BrowserType.HEADLESS);
        Browser browser2 = UiToolkit.newBrowser(BrowserType.HEADLESS);
        assertThat(UiToolkit.getActiveBrowserCount(), equalTo(2));
        browser1.close();
        assertThat(UiToolkit.getActiveBrowserCount(), equalTo(1));
        Browser browser3 = UiToolkit.newBrowser(BrowserType.HEADLESS);
        assertThat(UiToolkit.getActiveBrowserCount(), equalTo(2));
        browser2.close();
        browser3.close();
        assertThat(UiToolkit.getActiveBrowserCount(), equalTo(0));
    }

    @After
    public void closeAllWindows() {
        UiToolkit.closeAllWindows();
        assertThat(UiToolkit.getActiveBrowserCount(), equalTo(0));
    }

}
