package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.RadioButton;

/**
 * Default radio button representation.
 */
class RadioButtonImpl extends AbstractUiComponent implements RadioButton {

    @Override
    public String getValue() {
        return getProperty(UiProperties.VALUE);
    }

}
