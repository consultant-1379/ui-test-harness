package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderFactory;

public class SikuliUiWindowProviderFactory implements UiWindowProviderFactory {

    @Override
    public UiWindowProvider<DesktopWindow> newInstance(Object... initParameters) {
        if (initParameters.length == 0) {
            return new SikuliUiWindowProvider(false);
        } else {
            Boolean highlightOn = (Boolean) initParameters[0];
            return new SikuliUiWindowProvider(Boolean.TRUE.equals(highlightOn));
        }
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.DESKTOP_SIKULI;
    }

}
