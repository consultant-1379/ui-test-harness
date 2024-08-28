package com.ericsson.cifwk.taf.ui.selenium;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserOS;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * CIP-5878 Create support for switching proxy
 * <p/>
 * See: <a href="http://jira-oss.lmera.ericsson.se/browse/CIP-5878">jira:CIP-5878</a>
 */
@RunWith(Parameterized.class)
@Ignore("Temporarily disabled due to problems with grid stability")
public class CIP5878_GridTest extends AbstractSeleniumGridITest {

    private final BrowserOS browserOs;
    private final BrowserType browserType;
    private final String connectionErrorMsg;

    private BrowserSetup.Builder woProxy;
    private BrowserSetup.Builder withProxy;


    public CIP5878_GridTest(BrowserOS browserOs, BrowserType browserType, String connectionErrorMsg) {
        this.browserOs = browserOs;
        this.browserType = browserType;
        this.connectionErrorMsg = connectionErrorMsg;
    }

    @Parameterized.Parameters(name = "os={0}, browser={1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {BrowserOS.LINUX, BrowserType.FIREFOX, "Problem loading page"},
                {BrowserOS.LINUX, BrowserType.CHROME, "is not available"}
        });
    }

    @Before
    public void setUp() throws Exception {
        withProxy = BrowserSetup.build()
                .withType(browserType)
                .withOS(browserOs)
                .withCapability(CapabilityType.PROXY,
                        new Proxy().setHttpProxy("localhost:8080"));
        woProxy = BrowserSetup.build()
                .withOS(browserOs);
    }

    private String page(String url) {
        return "http://www.google.com/#q=" + url;
    }

    @Test
    public void shouldAllowProxyForBrowser() throws Exception {
        Browser b1 = null;
        Browser b2 = null;
        try {
            //
            b1 = UiToolkit.newBrowser(withProxy.withType(browserType));
            b2 = UiToolkit.newBrowser(woProxy.withType(browserType));
            BrowserTab t1 = b1.open(page("first.htm"));
            BrowserTab t2 = b2.open(page("first.htm"));
            assertThat(t1.getTitle(), containsString(connectionErrorMsg));
            assertThat(t2.getTitle(), containsString("first"));
        } finally {
            close(b1, b2);
        }
    }

    @Test
    @Ignore
    public void shouldDisAllowProxyForIEXPLORER() throws Exception {
        Browser b1 = null;
        Browser b2 = null;
        try {
            b1 = UiToolkit.newBrowser(withProxy.withType(BrowserType.IEXPLORER));
            fail("Taf UI SDK can't support proxy setting for Internet explorer");
        } catch (IllegalArgumentException expected) {
            assertThat(browserOs, not(is(BrowserOS.WINDOWS)));
        } catch (UnsupportedOperationException expected) {
            assertThat(browserOs, is(BrowserOS.WINDOWS));
        } finally {
            close(b1, b2);
        }
    }

    private void close(Browser... browsers) {
        for (Browser browser : browsers) {
            try {
                browser.close();
            } catch (Exception ignore) {
            }
        }
    }

}
