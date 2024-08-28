package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.CheckBox;


/**
 * Default checkbox representation.
 */
class CheckBoxImpl extends AbstractUiComponent implements CheckBox {

    @Override
    public String getValue() {
        return getProperty(UiProperties.VALUE);
    }

    @Override
    public void select() {
        if (!isSelected()) {
            click();
        }
    }

    @Override
    public void deselect() {
        if (isSelected()) {
            click();
        }
    }
}
