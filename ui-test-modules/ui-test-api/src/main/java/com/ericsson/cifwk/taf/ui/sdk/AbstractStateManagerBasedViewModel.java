package com.ericsson.cifwk.taf.ui.sdk;

import java.util.List;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentFactory;
import com.ericsson.cifwk.taf.ui.core.UiComponentFactoryImpl;
import com.ericsson.cifwk.taf.ui.core.UiComponentMappingDetails;
import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;

public abstract class AbstractStateManagerBasedViewModel extends AbstractViewModel {

    protected abstract UiComponentStateManagerFactory getStateManagerFactory();

    @Override
    public final <T extends UiComponent> T getViewComponent(SelectorType selectorType, String selector, Class<T> componentClass) {
        final UiComponentStateManager stateManager = getComponentStateManagerBySelector(selectorType, selector);
        return getUiComponentFactory().instantiateComponent(stateManager, componentClass);
    }

    @Override
    public final <T extends UiComponent> List<T> getViewComponents(SelectorType selectorType, String selector, Class<T> componentClass) {
        List<UiComponentStateManager> stateManagers = getComponentStateManagersBySelector(selectorType, selector);
        return getUiComponentFactory().instantiateComponentList(stateManagers, componentClass, false);
    }

    private UiComponentFactory getUiComponentFactory() {
        UiComponentStateManagerFactory stateManagerFactory = getStateManagerFactory();
        return new UiComponentFactoryImpl(stateManagerFactory.getAutowirer());
    }

    protected UiComponentStateManager getComponentStateManagerBySelector(SelectorType selectorType, String selector) {
        return getStateManagerFactory().createStateManager(new UiComponentMappingDetails(selectorType, selector));
    }

    protected List<UiComponentStateManager> getComponentStateManagersBySelector(SelectorType selectorType, String selector) {
        return getStateManagerFactory().createStateManagers(new UiComponentMappingDetails(selectorType, selector));
    }

}
