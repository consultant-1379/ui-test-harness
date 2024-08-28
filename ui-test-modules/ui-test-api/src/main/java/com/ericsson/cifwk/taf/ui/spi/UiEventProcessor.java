package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.core.UiComponentCallback;
import com.ericsson.cifwk.taf.ui.core.UiEvent;

import java.util.Map;

public interface UiEventProcessor {

    void processEvent(UiEvent event);

    void processEvent(UiEvent event, UiComponentCallback callback);

    void processEvent(UiEvent event, Map<String, ?> arguments);

    void processEvent(UiEvent event, Map<String, ?> arguments, UiComponentCallback callback);
}
