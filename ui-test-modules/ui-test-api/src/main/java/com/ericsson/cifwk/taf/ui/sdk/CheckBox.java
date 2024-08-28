package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

/**
 * Implementation-agnostic checkbox representation.
 */
public interface CheckBox extends UiComponent {

    /**
     * Returns a value that this checkbox carries
     *
     * @return checkbox internal value
     */
    String getValue();

    /**
     * Selects this checkbox
     */
    void select();

    /**
     * Deselects this checkbox
     */
    void deselect();
}
