package com.ericsson.cifwk.taf.ui.core.supplier;

import com.ericsson.cifwk.taf.ui.core.UiComponentMappingDetails;
import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 09.08.2016
 */
public class ViewModelStateManagerSupplier implements Supplier<UiComponentStateManager> {

    private final UiComponentStateManagerFactory stateManagerFactory;

    private final UiComponentMappingDetails mappingDetails;

    public ViewModelStateManagerSupplier(UiComponentStateManagerFactory stateManagerFactory, UiComponentMappingDetails mappingDetails) {
        this.stateManagerFactory = stateManagerFactory;
        this.mappingDetails = mappingDetails;
    }

    @Override
    public UiComponentStateManager get() {
        return stateManagerFactory.createStateManager(mappingDetails);
    }

}
