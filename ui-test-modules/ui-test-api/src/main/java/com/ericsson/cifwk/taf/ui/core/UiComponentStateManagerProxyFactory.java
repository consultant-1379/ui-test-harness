package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Supplier;

public interface UiComponentStateManagerProxyFactory {

    UiComponentStateManager createStateManagerProxy(UiMediator mediator, Supplier<UiComponentStateManager> instanceSupplier,
                                                    String mappingInfo);

    UiComponentStateManager createStateManagerProxy(UiMediator mediator, UiComponentStateManager nonProxiedStateManager);

}
