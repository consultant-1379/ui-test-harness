package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderFactory;

import static com.google.common.base.Preconditions.checkArgument;

public class SwtUiWindowProviderFactory implements UiWindowProviderFactory {

    @Override
    public UiWindowProvider<DesktopWindow> newInstance(Object... initParameters) {
        checkArgument(initParameters.length == 2, "Host and port must be provided");
        String host = (String) initParameters[0];
        int port = (Integer) initParameters[1];
        return new SwtUiWindowProvider(host, port);
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.DESKTOP_SWT;
    }

}
