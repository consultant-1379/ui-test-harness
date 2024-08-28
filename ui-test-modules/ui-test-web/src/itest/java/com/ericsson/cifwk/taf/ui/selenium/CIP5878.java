package com.ericsson.cifwk.taf.ui.selenium;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.EmbeddedJetty;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * CIP-5878 Create support for switching proxy
 * <p/>
 * See: <a href="http://jira-oss.lmera.ericsson.se/browse/CIP-5878">jira:CIP-5878</a>
 */
public class CIP5878 {

    private EmbeddedJetty jetty;
    private BrowserSetup.Builder incorrectProxy;
    private BrowserSetup.Builder directProxy;

    @Before
    public void setUp() throws Exception {
        jetty = EmbeddedJetty.build()
                .withResourceBase("./src/itest/resources/html_pages")
                .start();
        incorrectProxy = BrowserSetup.build()
                .withCapability(CapabilityType.PROXY,
                        new Proxy().setHttpProxy("localhost:8080"));
        directProxy = BrowserSetup.build()
                .withCapability(CapabilityType.PROXY,
                        new Proxy().setProxyType(Proxy.ProxyType.DIRECT));
    }

    @After
    public void tearDown() throws Exception {
        UiToolkit.closeAllWindows();
    }

    private String page(String html) {
        return "http://localhost:" + jetty.getPort() + "/" + html;
    }

    @Test
    public void shouldAllowProxyForFirefox() throws Exception {
        Browser b1 = null;
        Browser b2 = null;
        try {
            b1 = UiToolkit.newBrowser(incorrectProxy.withType(BrowserType.FIREFOX));
            b2 = UiToolkit.newBrowser(directProxy.withType(BrowserType.FIREFOX));
            BrowserTab t1 = b1.open(page("first.htm"));
            BrowserTab t2 = b2.open(page("first.htm"));
            assertThat(t1.getTitle(), containsString("Problem loading page"));
            assertThat(t2.getTitle(), containsString("first"));
        } finally {
            close(b1, b2);
        }
    }

    @Test
    public void shouldAllowProxyForChrome() throws Exception {
        Browser b1 = null;
        Browser b2 = null;
        try {
            b1 = UiToolkit.newBrowser(incorrectProxy.withType(BrowserType.CHROME));
            b2 = UiToolkit.newBrowser(directProxy.withType(BrowserType.CHROME));
            BrowserTab t1 = b1.open(page("first.htm"));
            BrowserTab t2 = b2.open(page("first.htm"));
            assertThat(t1.getTitle(), containsString("is not available"));
            assertThat(t2.getTitle(), containsString("first"));
        } finally {
            close(b1, b2);
        }
    }

    @Test
    public void shouldAllowProxyForHEADLESS() throws Exception {
        Browser b1 = null;
        Browser b2 = null;
        try {
            b1 = UiToolkit.newBrowser(incorrectProxy.withType(BrowserType.HEADLESS));
            b2 = UiToolkit.newBrowser(directProxy.withType(BrowserType.HEADLESS));
            BrowserTab t1 = b1.open(page("first.htm"));
            BrowserTab t2 = b2.open(page("first.htm"));
            assertThat(t1.getTitle(), Matchers.isEmptyString());
            assertThat(t2.getTitle(), containsString("first"));
        } finally {
            close(b1, b2);
        }
    }

    @Test
    public void shouldDisAllowProxyForIEXPLORER() throws Exception {
        Browser b1 = null;
        Browser b2 = null;
        try {
            b1 = UiToolkit.newBrowser(incorrectProxy.withType(BrowserType.IEXPLORER));
            fail("Taf UI SDK can't support proxy setting for Internet explorer");
        } catch (UnsupportedOperationException expected) {
            // Expected
        } finally {
            close(b1, b2);
        }
    }

    private void close(Browser... browsers) {
        for (Browser browser : browsers) {
            try {
                if (browser != null) {
                    browser.close();
                }
            } catch (Exception ignore) {
                // Ignore
            }
        }
    }

}
