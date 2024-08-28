package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.selenium.AbstractEmbeddedJettyITest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.containsString;

@RunWith(Parameterized.class)
public class BrowserTabITest extends AbstractEmbeddedJettyITest {

    private BrowserType browserType;
    private Browser browser;
    private BrowserTab browserTab;

    public BrowserTabITest(BrowserType browserType) {
        this.browserType = browserType;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        browser = UiToolkit.newBrowser(browserType);
        browserTab = browser.open(findHtmlPage("basic_ui_components.htm"));
    }

    @Parameterized.Parameters(name = "browser={0}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {BrowserType.PHANTOMJS}
//                 {BrowserType.FIREFOX}
//                , {BrowserType.CHROME}
                //,{BrowserType.IEXPLORER} don't work with 'file://*' url
                // For IE testing use embedded browser and "'http://*' url's
        });
    }

    @Test
    public void shouldSetDifferentSizeForDifferentTabs() {
        String page1 = findHtmlPage("first.htm");
        String page2 = findHtmlPage("second.htm");

        browserTab.evaluate("win1 = window.open('" + page1 + "', '', 'width=100, height=100');");
//        UI.pause(1000);
        BrowserTab page1Tab = browser.getCurrentWindow();
        Assert.assertThat(page1Tab.getCurrentUrl(), containsString("first"));

        browserTab.evaluate("win2 = window.open('" + page2 + "', '', 'width=150, height=150');");
        BrowserTab page2Tab = browser.getCurrentWindow();
        Assert.assertThat(page2Tab.getCurrentUrl(), containsString("second"));

        page1Tab.setSize(200, 200);
        page2Tab.setSize(250, 250);

        Assert.assertEquals(200, page1Tab.getSize().getHeight());
        Assert.assertEquals(200, page1Tab.getSize().getWidth());

        Assert.assertEquals(250, page2Tab.getSize().getHeight());
        Assert.assertEquals(250, page2Tab.getSize().getWidth());
    }
}
