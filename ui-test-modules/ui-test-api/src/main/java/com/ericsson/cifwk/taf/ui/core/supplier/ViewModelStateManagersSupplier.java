package com.ericsson.cifwk.taf.ui.core.supplier;

import com.ericsson.cifwk.taf.ui.core.UiComponentMappingDetails;
import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;

import java.util.List;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 09.08.2016
 */
public class ViewModelStateManagersSupplier implements Supplier<List<UiComponentStateManager>> {

    private final UiComponentStateManagerFactory stateManagerFactory;

    private final UiComponentMappingDetails mappingDetails;

    public ViewModelStateManagersSupplier(UiComponentStateManagerFactory stateManagerFactory, UiComponentMappingDetails mappingDetails) {
        this.stateManagerFactory = stateManagerFactory;
        this.mappingDetails = mappingDetails;
    }

    @Override
    public List<UiComponentStateManager> get() {
        return stateManagerFactory.createStateManagers(mappingDetails);
    }

}
