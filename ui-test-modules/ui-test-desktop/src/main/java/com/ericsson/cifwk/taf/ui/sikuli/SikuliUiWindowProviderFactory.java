package com.ericsson.cifwk.taf.ui.sikuli;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.core.UiWindowType;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderFactory;
import com.google.common.base.Preconditions;

public class SikuliUiWindowProviderFactory implements UiWindowProviderFactory {

    @Override
    public UiWindowProvider<DesktopWindow> newInstance(Object... initParameters) {
        Preconditions.checkArgument(initParameters.length == 2, "");
        String sessionId = (String) initParameters[0];
        String imagesResourceBundle = (String) initParameters[1];
        return new SikuliUiWindowProvider(sessionId, imagesResourceBundle);
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.DESKTOP_SIKULI_GRID;
    }

}
