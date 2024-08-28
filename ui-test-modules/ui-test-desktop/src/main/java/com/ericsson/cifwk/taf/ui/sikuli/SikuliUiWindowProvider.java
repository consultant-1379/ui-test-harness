package com.ericsson.cifwk.taf.ui.sikuli;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.core.UiTestEnvironmentInfoProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderAdapter;
import io.sterodium.extensions.client.SikuliExtensionClient;

public class SikuliUiWindowProvider extends UiWindowProviderAdapter<DesktopWindow> {

    private final SikuliExtensionClient sikuliClient;

    public SikuliUiWindowProvider(String sessionId, String imagesResourceBundle) {
        UiTestEnvironmentInfoProvider uiTestEnvironmentInfoProvider = new UiTestEnvironmentInfoProvider();
        String gridHost = uiTestEnvironmentInfoProvider.getGridHost();
        int gridPort = uiTestEnvironmentInfoProvider.getGridPort();
        sikuliClient = new SikuliExtensionClient(gridHost, gridPort, sessionId);
        sikuliClient.uploadResourceBundle(imagesResourceBundle);
    }

    @Override
    public DesktopWindow getCurrentWindow() {
        return new SikuliWindow(sikuliClient);
    }

}
