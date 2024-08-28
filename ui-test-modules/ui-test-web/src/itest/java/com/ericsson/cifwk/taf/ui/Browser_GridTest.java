package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.selenium.AbstractSeleniumGridITest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
@Ignore("Temporarily disabled due to problems with grid stability")
public class Browser_GridTest extends AbstractSeleniumGridITest {

    private BrowserType browserType;
    private Browser browser;

    public Browser_GridTest(BrowserType browserType) {
        this.browserType = browserType;
    }

    @Parameterized.Parameters(name = "browser={0}")
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {BrowserType.FIREFOX},
                {BrowserType.CHROME}

        });
    }

    protected String findHtmlPage(String html) {
        return "https://www.google.com/#q=" + html;
    }

    @Before
    public void setUp() throws Exception {
        browser = UiToolkit.newBrowser(browserType);
    }

    @After
    public void tearDown() throws Exception {
        browser.close();
        UiToolkit.closeAllWindows();
    }

    @Test
    public void takesScreenshot() throws Exception {
        String firstPage = findHtmlPage("first.htm");
        BrowserTab first = browser.open(firstPage);
        first.takeScreenshot("shot-" + browserType.toString() + ".png");
    }

    @Test
    public void shouldOpenMultipleWindows() throws Exception {
        String firstPage = findHtmlPage("first.htm");
        BrowserTab first = browser.open(firstPage);
        Thread.sleep(5000);
        assertThat(first.getTitle(), containsString("first"));
        assertThat(browser.getOpenedWindowsAmount(), is(1));
        //
        String secondPage = findHtmlPage("second.htm");
        BrowserTab second = browser.open(secondPage);
        Thread.sleep(5000);
        assertThat(second.getTitle(), containsString("second"));
        assertThat(browser.getOpenedWindowsAmount(), is(2));
    }

    @Test
    public void shouldOpenURLInSameTab() throws Exception {
        String firstPage = findHtmlPage("first.htm");
        BrowserTab tab = browser.open(firstPage);
        Thread.sleep(5000);
        assertThat(tab.getTitle(), containsString("first"));
        assertThat(browser.getOpenedWindowsAmount(), is(1));
        //
        String secondPage = findHtmlPage("second.htm");
        tab.open(secondPage);
        Thread.sleep(5000);
        assertThat(tab.getTitle(), containsString("second"));
        assertThat(browser.getOpenedWindowsAmount(), is(1));
    }
}


