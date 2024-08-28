package com.ericsson.cifwk.taf.ui.selenium;

import com.ericsson.cifwk.taf.ui.EmbeddedJetty;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import org.junit.After;
import org.junit.Before;

public class AbstractEmbeddedJettyITest extends AbstractSeleniumITest {

    private EmbeddedJetty jetty;

    @Before
    public void setUp() throws Exception {
        jetty = EmbeddedJetty.build()
                .withResourceBase("./target/itest-classes/html_pages")
                .start();
    }

    @After
    public void tearDown() throws Exception {
        UiToolkit.closeAllWindows();
        jetty.stop();
    }

    @Override
    protected String findHtmlPage(String html) {
        return "http://localhost:" + jetty.getPort() + "/" + html;
    }

}
