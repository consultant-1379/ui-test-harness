package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class GenericUiComponentStateManagerFactory implements UiComponentStateManagerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericUiComponentStateManagerFactory.class);

    final UiMediator mediator;

    GenericUiComponentStateManagerFactory(UiMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public UiComponentStateManager createStateManager(final UiComponentMappingDetails mappingDetails) {
        try{
            return mediator.retrieve(mappingDetails).get(0);
        } catch (UiComponentNotFoundException ignored){
            LOGGER.trace("No components bound to collection. That is OK.", ignored);
            return null;
        }
    }

    @Override
    public UiComponentStateManager createStateManager(UiComponentStateManager parentStateManager, UiComponentMappingDetails mappingDetails) {
        List<UiComponentStateManager> descendantStateManagers = createStateManagers(parentStateManager, mappingDetails);
        return descendantStateManagers.isEmpty() ? null : descendantStateManagers.get(0);
    }

    @Override
    public List<UiComponentStateManager> createStateManagers(UiComponentStateManager parentStateManager, UiComponentMappingDetails mappingDetails) {
        return parentStateManager.getDescendantsByExpression(mappingDetails);
    }

    @Override
    public List<UiComponentStateManager> createStateManagers(final UiComponentMappingDetails mappingDetails) {
        try{
            return mediator.retrieve(mappingDetails);
        } catch (UiComponentNotFoundException ignored){
            LOGGER.trace("No components bound to collection. That is OK.", ignored);
            return new ArrayList<>();
        }

    }

    @Override
    public UiComponentAutowirer getAutowirer() {
        return new GenericUiComponentAutowirer(this);
    }

}
