package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.selenium.AbstractEmbeddedJettyITest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class Browser_EmbeddedJettyTest extends AbstractEmbeddedJettyITest {

    EmbeddedJetty jetty;
    BrowserType browserType;
    Browser browser;

    public Browser_EmbeddedJettyTest(BrowserType browserType) {
        this.browserType = browserType;
    }

    @Parameterized.Parameters(name = "browser={0}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {BrowserType.PHANTOMJS}
        });
    }

    @Before
    public void setUp() throws Exception {
        jetty = EmbeddedJetty.build()
                .withResourceBase("./target/itest-classes/html_pages")
                .start();
        browser = UiToolkit.newBrowser(browserType);
    }

    @After
    public void tearDown() throws Exception {
        browser.close();
        UiToolkit.closeAllWindows();
        jetty.stop();
    }

    protected String findHtmlPage(String html) {
        return "http://localhost:" + jetty.getPort() + "/" + html;
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
        //
    }
}


