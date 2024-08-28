package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import org.openqa.selenium.Alert;

class SeleniumMessageBox implements MessageBox {
    private final Alert alert;

    SeleniumMessageBox(Alert alert) {
        this.alert = alert;
    }

    @Override
    public void clickOk() {
        alert.accept();
    }

    @Override
    public void clickCancel() {
        alert.dismiss();
    }

    @Override
    public String getText() {
        return alert.getText();
    }

    @Override
    public void sendKeys(String keysToSend) {
        alert.sendKeys(keysToSend);
    }

}
