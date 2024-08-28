package com.ericsson.cifwk.taf.ui.sdk;

/**
 * An abstraction for message box (alerts, confirmations, etc.) - dialog box that blocks the parent UI window.
 */
public interface MessageBox {
    /**
     * Clicks the confirmation button
     */
    void clickOk();

    /**
     * Clicks the cancellation button. If message box doesn't have this button (alert/info/warning), nothing happens.
     */
    void clickCancel();

    /**
     * Returns the text content of the message box
     *
     * @return text content of the message box
     */
    String getText();

    /**
     * Sends the sequence of keystrokes to the message box
     *
     * @param keysToSend sequence of keystrokes
     */
    void sendKeys(String keysToSend);
}
