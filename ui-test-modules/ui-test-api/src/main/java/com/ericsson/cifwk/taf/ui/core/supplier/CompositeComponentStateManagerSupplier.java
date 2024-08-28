package com.ericsson.cifwk.taf.ui.core.supplier;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.PackageAccessor;
import com.ericsson.cifwk.taf.ui.core.UiComponentMappingDetails;
import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 09.08.2016
 */
public class CompositeComponentStateManagerSupplier implements Supplier<UiComponentStateManager> {

    private final UiComponentStateManagerFactory stateManagerFactory;

    private final AbstractUiComponent parentComponent;

    private final UiComponentMappingDetails mappingDetails;

    public CompositeComponentStateManagerSupplier(UiComponentStateManagerFactory stateManagerFactory, AbstractUiComponent parentComponent, UiComponentMappingDetails mappingDetails) {
        this.stateManagerFactory = stateManagerFactory;
        this.parentComponent = parentComponent;
        this.mappingDetails = mappingDetails;
    }

    @Override
    public UiComponentStateManager get() {
        UiComponentStateManager parentStateManager = PackageAccessor.getStateManagerFor(parentComponent);
        return stateManagerFactory.createStateManager(parentStateManager, mappingDetails);
    }

}
