package com.ericsson.cifwk.taf.ui.debug;

import com.ericsson.cifwk.taf.ui.spi.UiMediator;

public interface DebugDelayer {
    void setDebugMode(UiMediator mediator, long delay);

    void unsetDebugMode();
}
