package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.MenuItem;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.google.common.base.Objects;

import java.util.UUID;

public class DesktopWindowImpl extends AbstractUiWindow implements DesktopWindow {

    private final String descriptor;

    private final UiMediator mediator;

    private boolean closed;

    private final UiComponentStateManagerFactory stateManagerFactory;

    private final UiWindowProvider<DesktopWindow> windowProvider;

    public DesktopWindowImpl(UiWindowProvider<DesktopWindow> windowProvider, UiMediator mediator, UiComponentStateManagerFactory stateManagerFactory) {
        this.mediator = mediator;
        this.stateManagerFactory = stateManagerFactory;
        this.descriptor = UUID.randomUUID().toString();
        this.closed = false;
        this.windowProvider = windowProvider;
    }

    @Override
    public void maximize() {
        mediator.maximize();
    }

    @Override
    public String getWindowDescriptor() {
        return descriptor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(descriptor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        DesktopWindowImpl other = (DesktopWindowImpl) obj;
        if (descriptor == null) {
            if (other.descriptor != null)
                return false;
        } else if (!descriptor.equals(other.descriptor))
            return false;
        return true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public ViewModel getGenericView() {
        GenericViewModel viewModel = new GenericViewModel();
        viewModel.setComponentStateManagerFactory(stateManagerFactory);
        viewModel.setDelayer(getDelayer());
        viewModel.setMediator(mediator);
        return viewModel;
    }

    @Override
    public MessageBox getMessageBox() {
        return mediator.getMessageBox();
    }

    @Override
    public <T extends ViewModel> T getView(Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected UiComponentBasedDelayer getDelayer() {
        return windowProvider.getExecutionDelayer();
    }

    @Override
    public UiComponentSize getSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object evaluate(String expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dragAndDropTo(UiComponent object, UiComponent target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendKey(int key, int... modifiers) {
        mediator.sendKey(key, modifiers);
    }

    @Override
    public void sendKeys(String keys, int... modifiers) {
        mediator.sendKeys(keys, modifiers);
    }

    @Override
    public void takeScreenshot(String fileName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuItem getMenuItem(String menuName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected UiMediator getMediator() {
        return mediator;
    }
}
