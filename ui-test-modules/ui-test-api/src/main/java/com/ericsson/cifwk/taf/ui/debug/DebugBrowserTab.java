package com.ericsson.cifwk.taf.ui.debug;

import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;

public interface DebugBrowserTab {
    UiComponentBasedDelayer setDebugMode(int delay);

    void unsetDebugMode();
}
