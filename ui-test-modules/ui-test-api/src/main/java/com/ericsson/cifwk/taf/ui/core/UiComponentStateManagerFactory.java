package com.ericsson.cifwk.taf.ui.core;

import java.util.List;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;

/**
 * Factory that produces the instances of {@link com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager}
 */
public interface UiComponentStateManagerFactory {

    /**
     * Creates a component state manager, based on selector type.
     *
     * @return component state manager for UI component that matches the selector.
     * If multiple components were found, the state manager for the first one will be returned.
     */
    UiComponentStateManager createStateManager(UiComponentMappingDetails selector);

    UiComponentStateManager createStateManager(UiComponentStateManager parentStateManager, UiComponentMappingDetails mappingDetails);

    /**
     * Creates a component state manager, based on selector type.
     *
     * @return component state managers for UI components that match the selector.
     */
    List<UiComponentStateManager> createStateManagers(UiComponentMappingDetails selector);

    List<UiComponentStateManager> createStateManagers(UiComponentStateManager parentStateManager, UiComponentMappingDetails mappingDetails);

    UiComponentAutowirer getAutowirer();
}
