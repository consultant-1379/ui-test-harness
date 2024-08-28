package com.ericsson.cifwk.taf.ui.core.supplier;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.PackageAccessor;
import com.ericsson.cifwk.taf.ui.core.UiComponentMappingDetails;
import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;

import java.util.List;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 09.08.2016
 */
public class CompositeComponentStateManagersSupplier implements Supplier<List<UiComponentStateManager>> {

    private final UiComponentStateManagerFactory stateManagerFactory;

    private final AbstractUiComponent parentComponent;

    private final UiComponentMappingDetails mappingDetails;

    public CompositeComponentStateManagersSupplier(UiComponentStateManagerFactory stateManagerFactory, AbstractUiComponent parentComponent, UiComponentMappingDetails mappingDetails) {
        this.stateManagerFactory = stateManagerFactory;
        this.parentComponent = parentComponent;
        this.mappingDetails = mappingDetails;
    }

    @Override
    public List<UiComponentStateManager> get() {
        UiComponentStateManager parentStateManager = PackageAccessor.getStateManagerFor(parentComponent);
        return stateManagerFactory.createStateManagers(parentStateManager, mappingDetails);
    }

}
