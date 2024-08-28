package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

/**
 * Implementation-agnostic select (dropdown or listbox) element representation.
 */
public interface Option extends UiComponent {
    String getTitle();

    String getValue();
}
