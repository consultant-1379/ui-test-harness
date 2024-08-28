package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.Option;

/**
 * Default select (dropdown or listbox) element representation.
 */
class OptionImpl extends AbstractUiComponent implements Option {

    @Override
    public String getTitle() {
        return getText();
    }

    @Override
    public String getValue() {
        return getProperty(UiProperties.VALUE);
    }
}
