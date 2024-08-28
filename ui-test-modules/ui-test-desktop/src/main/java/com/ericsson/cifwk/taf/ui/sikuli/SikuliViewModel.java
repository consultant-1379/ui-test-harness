package com.ericsson.cifwk.taf.ui.sikuli;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModelAdapter;
import io.sterodium.extensions.client.SikuliExtensionClient;
import org.sikuli.api.ImageTarget;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.28.
 */
class SikuliViewModel extends ViewModelAdapter {

    private SikuliExtensionClient sikuliClient;

    public SikuliViewModel(SikuliExtensionClient sikuliClient) {
        this.sikuliClient = sikuliClient;
    }

    @Override
    public UiComponent getViewComponent(String selector) {
        ImageTarget imageTarget = this.sikuliClient.getTargetFactory().createImageTarget(selector);
        return new SikuliUiComponent(imageTarget, sikuliClient);
    }

    @Override
    public TextBox getTextBox(String selector) {
        ImageTarget imageTarget = this.sikuliClient.getTargetFactory().createImageTarget(selector);
        return new SikuliTextBox(imageTarget, sikuliClient);
    }

    @Override
    public Button getButton(String selector) {
        return getViewComponent(selector).as(Button.class);
    }

}
