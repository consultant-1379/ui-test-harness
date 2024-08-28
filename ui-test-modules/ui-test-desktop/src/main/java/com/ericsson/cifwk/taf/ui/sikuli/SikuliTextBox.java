package com.ericsson.cifwk.taf.ui.sikuli;

import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import io.sterodium.extensions.client.SikuliExtensionClient;
import org.sikuli.api.ImageTarget;

import java.awt.event.KeyEvent;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.10.01.
 */
public class SikuliTextBox extends SikuliUiComponent implements TextBox {

    public SikuliTextBox(ImageTarget imageTarget, SikuliExtensionClient sikuliClient) {
        super(imageTarget, sikuliClient);
    }

    @Override
    public void setText(String text) {
        clear();
        getKeyboard().type(text);
    }

    @Override
    public void clear() {
        click();
        sendKey(KeyEvent.VK_A, KeyEvent.VK_CONTROL);
        sendKey(KeyEvent.VK_BACK_SPACE);
    }

}
