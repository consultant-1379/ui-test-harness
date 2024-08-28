package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.utils.InternalFileFinder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class BrowserTest {

    private BrowserType browserType;
    private Browser browser;

    public BrowserTest(BrowserType browserType) {
        this.browserType = browserType;
    }

    @Before
    public void tearDown() throws Exception {
        browser = UiToolkit.newBrowser(browserType);
    }

    @After
    public void _tearDown() throws Exception {
        browser.close();
        UiToolkit.closeAllWindows();
    }

    @Parameterized.Parameters(name = "browser={0}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {BrowserType.HEADLESS}
        });
    }

    protected String findHtmlPage(String fileName) {
        return findFile(fileName);
    }

    @Test
    public void shouldOpenMultipleWindows() throws Exception {
        String firstPage = findHtmlPage("first.htm");
        BrowserTab first = browser.open(firstPage);
        assertThat(first.getTitle(), is("first"));
        assertThat(browser.getOpenedWindowsAmount(), is(1));
        //
        String secondPage = findHtmlPage("second.htm");
        BrowserTab second = browser.open(secondPage);
        assertThat(second.getTitle(), is("second"));
        assertThat(browser.getOpenedWindowsAmount(), is(2));
    }

    @Test
    public void first() throws Exception {
        String firstPage = findHtmlPage("first.htm");
        BrowserTab tab = browser.open(firstPage);
        assertThat(tab.getTitle(), is("first"));
        assertThat(browser.getOpenedWindowsAmount(), is(1));
        //
        String secondPage = findHtmlPage("second.htm");
        tab.open(secondPage);
        assertThat(tab.getTitle(), is("second"));
        assertThat(browser.getOpenedWindowsAmount(), is(1));
    }

    public static String findFile(String fileName) {
        String foundFile = InternalFileFinder.findFile(fileName);
        if (foundFile == null) {
            Assert.fail("'" + fileName + "' not found!");
        }
        return "file:///" + foundFile;
    }

}

