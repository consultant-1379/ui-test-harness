package com.ericsson.cifwk.taf.ui.selenium;


import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.AbstractBrowserAwareTest;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.selenium.pages.LoginViewModel;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;

public class ComponentRealtimeStateTest extends AbstractBrowserAwareTest {
    private JavascriptTestOperator jsOperator;
    private LoginTestOperator loginOperator;

    @Test
    public void elementIsAlwaysUpToDate() {
        String fullPath = findHtmlPage("basic_ui_components.htm");
        jsOperator = new JavascriptTestOperator(fullPath);

        String timestamp = jsOperator.getTimestampFieldValue();
        Assert.assertEquals("", timestamp);

        jsOperator.clickRefreshTimestampButton();
        Assert.assertThat(timestamp, not(jsOperator.getTimestampFieldValue()));
        timestamp = jsOperator.getTimestampFieldValue();
        UiToolkit.pause(100);

        jsOperator.clickRefreshTimestampButton();
        Assert.assertThat(timestamp, not(jsOperator.getTimestampFieldValue()));
        jsOperator.getTimestampFieldValue();
        UiToolkit.pause(100);
    }

    @Test
    public void staleElementReloadTest() {
        String fullPath = findHtmlPage("login.htm");
        loginOperator = new LoginTestOperator(fullPath);

        LoginViewModel loginViewModel = loginOperator.getLoginViewModel();
        Label errorLabel = loginViewModel.getErrorMessagesLabel();

        Assert.assertEquals(true, StringUtils.isBlank(errorLabel.getText()));
        loginOperator.login("ssouser", "ssouser01");
        try {
            errorLabel.getText();
            Assert.fail("Expected UiComponentNotFound exception");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
        loginOperator.logout();
        Assert.assertEquals(true, StringUtils.isBlank(errorLabel.getText()));
    }
}

