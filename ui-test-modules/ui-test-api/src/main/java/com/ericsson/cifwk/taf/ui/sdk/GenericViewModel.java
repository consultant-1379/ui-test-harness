package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Preconditions;

/**
 * Generic implementation of {@link ViewModel}
 */
public class GenericViewModel extends AbstractStateManagerBasedViewModel {

    private UiComponentStateManagerFactory stateManagerFactory;
    private UiComponentBasedDelayer delayer;
    private UiMediator mediator;
    private String windowDescriptor;

    public GenericViewModel() {
        super();
    }

    public void setWindowDescriptor(String windowDescriptor) {
        this.windowDescriptor = windowDescriptor;
    }

    public String getWindowDescriptor() {
        return windowDescriptor;
    }

    public final void setDelayer(UiComponentBasedDelayer delayer) {
        Preconditions.checkArgument(delayer != null);
        this.delayer = delayer;
    }

    public final void setComponentStateManagerFactory(UiComponentStateManagerFactory stateManagerFactory) {
        Preconditions.checkArgument(stateManagerFactory != null);
        this.stateManagerFactory = stateManagerFactory;
    }

    @Override
    protected UiComponentStateManagerFactory getStateManagerFactory() {
        return stateManagerFactory;
    }

    public final void setMediator(UiMediator mediator) {
        Preconditions.checkArgument(mediator != null);
        this.mediator = mediator;
    }

    @Override
    protected UiComponentBasedDelayer getDelayer() {
        return delayer;
    }

    @Override
    protected UiMediator getMediator() {
        return mediator;
    }
}
