package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.Link;

/**
 * Default link representation.
 */
class LinkImpl extends AbstractUiComponent implements Link {

    @Override
    public String getUrl() {
        return getProperty(UiProperties.URL);
    }
}
