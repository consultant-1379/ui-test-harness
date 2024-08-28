package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;

import java.util.List;

public interface UiComponentFactory {

    <T extends UiComponent> T instantiateComponent(UiComponentStateManager stateManager, Class<T> componentClass);

    <T extends UiComponent> T instantiateComponent(Supplier<UiComponentStateManager> stateManagerSupplier,
                                                   Class<T> componentClass);

    <T extends UiComponent> List<T> instantiateComponentList(Supplier<List<UiComponentStateManager>> stateManagerListSupplier,
                                                             Class<T> componentClass, boolean isStaticList);

    <T extends UiComponent> List<T> instantiateComponentList(List<UiComponentStateManager> stateManagerList,
                                                             Class<T> componentClass, boolean isStaticList);
}
