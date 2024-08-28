package com.ericsson.cifwk.taf.ui.selenium;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

class JavascriptTestOperator {
    private final Browser browser;
    private final BrowserTab browserTab;
    private JavascriptStagingViewModel jsView;

    JavascriptTestOperator(String url) {
        this.browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        this.browserTab = browser.open(url);
        jsView = browserTab.getView(JavascriptStagingViewModel.class);
    }

    public void closeResources() {
        browser.close();
    }

    public boolean doesHidingLabelAppear() {
        return jsView.getDisappearingLabel() != null;
    }

    String getTimestampFieldValue() {
        TextBox timestampBox = jsView.getTimestampBox();
        return timestampBox.getText();
    }

    void clickRefreshTimestampButton() {
        Button button = jsView.getRefreshTimestampButton();
        button.click();
    }
}
