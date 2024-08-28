package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.core.UiComponentCallback;
import com.ericsson.cifwk.taf.ui.core.UiEvent;

import java.util.Collections;
import java.util.Map;

public abstract class AbstractUiEventProcessor implements UiEventProcessor {

    @Override
    public void processEvent(UiEvent event) {
        Map<String, Object> noArgs = Collections.emptyMap();
        processEvent(event, noArgs);
    }

    @Override
    public void processEvent(UiEvent event, Map<String, ?> arguments) {
        processEvent(event, arguments, new UiComponentCallback() {
            @Override
            public void onFinish(Object value) {
                // empty
            }
        });
    }

    @Override
    public void processEvent(UiEvent event, UiComponentCallback callback) {
        processEvent(event, Collections.<String, Object>emptyMap(), callback);
    }
}
