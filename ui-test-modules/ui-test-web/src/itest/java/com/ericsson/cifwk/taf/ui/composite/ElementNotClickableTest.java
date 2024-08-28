package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTestUtils;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriverException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@Ignore
public class ElementNotClickableTest {

    private CustomViewModel view;

    private BrowserTab tab;

    private Browser browser;

    @Before
    public void setUp() {
        String testPage = BrowserTestUtils.findHtmlPage("element_not_clickable.htm");
        browser = UiToolkit.newBrowser(BrowserType.PHANTOMJS);
        tab = browser.open(testPage);
        view = tab.getView(CustomViewModel.class);
    }

    @After
    public void tearDown() {
        browser.close();
    }

    @Test
    public void elementNotClickable() {
        view.clickTop();

        try {
            view.clickBottom();
            tab.takeScreenshot("elementNotClickable");
            fail("Click was unexpectedly successful.");
        } catch (WebDriverException e) {
            String message = e.getMessage();
            assertThat(message).contains("Element");
            assertThat(message).contains("is not clickable at point");
            assertThat(message).contains("Other element would receive the click");
        }
    }

    @Test
    public void elementNotClickableRetry() {
        view.clickButton(); // this causes the top div to hide after 3 seconds
        view.clickBottom(); // this should only succeeed after top div is hidden
        assertThat(view.getBottomText()).contains("I was clicked");
    }

    public static class CustomViewModel extends GenericViewModel {
        @UiComponentMapping("#top")
        private UiComponent top;

        @UiComponentMapping("#bottom")
        private UiComponent bottom;

        @UiComponentMapping("#button")
        private Button button;

        public String getTopText() {
            return top.getText();
        }

        public String getBottomText() {
            return bottom.getText();
        }

        public void clickTop() {
            top.click();
        }

        public void clickBottom() {
            bottom.click();
        }

        public void clickButton() {
            button.click();
        }
    }

}
