package com.ericsson.cifwk.taf.ui.sikuli;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.UiWindowAdapter;
import com.ericsson.cifwk.taf.ui.sdk.MenuItem;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import io.sterodium.extensions.client.SikuliExtensionClient;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.28.
 */
class SikuliWindow extends UiWindowAdapter implements DesktopWindow {

    private SikuliExtensionClient sikuliClient;

    public SikuliWindow(SikuliExtensionClient sikuliClient) {
        this.sikuliClient = sikuliClient;
    }

    @Override
    public MenuItem getMenuItem(String menuName) {
        throw new UnsupportedOperationException("Method is not supported");
    }

    @Override
    public ViewModel getGenericView() {
        return new SikuliViewModel(sikuliClient);
    }
}
