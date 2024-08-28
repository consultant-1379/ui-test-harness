package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

/**
 * Implementation-agnostic textbox representation.
 */
public interface TextBox extends UiComponent {

    void setText(String text);

    void clear();
}
