package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.WaitTimedOutException;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.selenium.AbstractEmbeddedJettyITest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class Browser_TakeScreenshotTest extends AbstractEmbeddedJettyITest {

    Browser browser;

    private GenericViewModel genericView;

    private String selector;

    private SelectorType selectorType;

    public Browser_TakeScreenshotTest(String selector, SelectorType selectorType) {
        this.selector = selector;
        this.selectorType = selectorType;
    }

    @Before
    public void setUp() throws Exception {
        browser = UiToolkit.newBrowser(BrowserType.PHANTOMJS);
    }

    @After
    public void tearDown() throws Exception {
        browser.close();
        UiToolkit.closeAllWindows();
    }

    @Parameterized.Parameters(name = "selector={0},selectorType={1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {"#textBox", SelectorType.CSS},
                {"/html/body/div/div/div/form/div/div[1]/button", SelectorType.XPATH},
                {"//*[contains(@class, 'elTablelib-Table-pretable')]//th[contains(@class, 'elTablelib-CheckboxHeaderCell')]", SelectorType.XPATH}});
    }

    protected String findHtmlPage(String fileName) {
        return BrowserTestUtils.findHtmlPage(fileName);
    }

    @Test
    public void shouldGetButtonComponent() {
        String firstPage = findHtmlPage("third.htm");
        BrowserTab first = browser.open(firstPage);
        genericView = first.getView(GenericViewModel.class);
        Button submit = genericView.getButton("#submit");
        first.waitUntilComponentIsDisplayed(submit, 1000);
        assertEquals(submit.getText(), "Submit");
    }

    @Test(expected = WaitTimedOutException.class)
    public void shouldTakeScreenshotBecauseOfMissingTextBoxComponent() {
        String firstPage = findHtmlPage("third.htm");
        BrowserTab first = browser.open(firstPage);
        genericView = first.getView(GenericViewModel.class);
        TextBox textBox = genericView.getTextBox(selectorType, selector);
        assertNotNull(first.waitUntilComponentIsDisplayed(textBox, 1000));
    }
}
