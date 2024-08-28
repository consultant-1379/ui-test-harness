package com.ericsson.cifwk.taf.ui.selenium;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.EmbeddedJetty;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * CIP-5795 As a TAF UI user I would like to change browser proxy settings & browser resolution <br />
 * <p/>
 * See: <a href="http://jira-oss.lmera.ericsson.se/browse/CIP-5795">jira:CIP-5795</a>
 */
@RunWith(Parameterized.class)
public class CIP5877 {

    private EmbeddedJetty jetty;
    private BrowserType browserType;
    private Browser browser;


    public CIP5877(BrowserType browserType) {
        this.browserType = browserType;
    }

    @Parameterized.Parameters(name = "browser={0}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {BrowserType.HEADLESS},
                {BrowserType.CHROME},
                {BrowserType.IEXPLORER},
                {BrowserType.FIREFOX}
        });
    }

    @Before
    public void setUp() throws Exception {
        jetty = EmbeddedJetty.build()
                .withResourceBase("./src/itest/resources/html_pages")
                .start();
        browser = UiToolkit.newBrowser(browserType, BrowserSetup.Resolution.RESOLUTION_800x600);
    }

    @After
    public void tearDown() throws Exception {
        browser.close();
        UiToolkit.closeAllWindows();
        jetty.stop();
    }

    protected BrowserTab open(String html) {
        return browser.open("http://localhost:" + jetty.getPort() + "/" + html);
    }

    @Test
    public void shouldCreateBrowser_with_ScreenResolution() throws Exception {
        BrowserTab tab = open("first.htm");
        assertThat(tab.getTitle(), is("first"));
        UiComponentSize size = tab.getSize();
        assertThat(size.getWidth(), is(800));
        assertThat(size.getHeight(), is(600));
    }

    @Test
    public void shouldChangeWindowsSize() throws Exception {
        browser.setSize(720, 480);
        BrowserTab tab = open("first.htm");
        assertThat(tab.getTitle(), is("first"));
        UiComponentSize size = tab.getSize();
        assertThat(size.getWidth(), is(720));
        assertThat(size.getHeight(), is(480));
    }


}
