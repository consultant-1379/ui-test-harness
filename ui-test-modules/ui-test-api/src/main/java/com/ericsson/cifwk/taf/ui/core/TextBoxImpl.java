package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.google.common.base.Preconditions;

class TextBoxImpl extends AbstractUiComponent implements TextBox {

    @Override
    public void setText(String text) {
        Preconditions.checkArgument(text != null, "Text passed to element cannot be null");
        stateManager.setProperty(UiProperties.VALUE, text);
    }

    @Override
    public String getText() {
        return getProperty(UiProperties.VALUE);
    }

    @Override
    public void clear() {
        processEvent(UiEvent.CLEAR);
    }
}
