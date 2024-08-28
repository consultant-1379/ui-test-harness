package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

/**
 * Specification of the facility to autowire fields annotated with {@link com.ericsson.cifwk.taf.ui.core.UiComponentMapping}
 * annotation.
 */
public interface UiComponentAutowirer {

    /**
     * Autowires view's fields
     *
     * @param view
     */
    void initialize(ViewModel view);

    /**
     * Autowires container's fields
     *
     * @param container
     */
    void initialize(AbstractUiComponent container);

}
