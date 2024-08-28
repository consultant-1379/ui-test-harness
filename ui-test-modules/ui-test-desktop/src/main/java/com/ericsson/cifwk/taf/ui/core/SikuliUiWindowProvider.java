package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.DesktopWindowImpl;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.google.common.collect.Lists;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SikuliUiWindowProvider implements UiWindowProvider<DesktopWindow> {

    private final boolean highlightOn;
    private boolean closed = false;
    private ScreenRegion region;

    public SikuliUiWindowProvider(boolean highlightOn) {
        this.highlightOn = highlightOn;
    }

    public ScreenRegion getScreenRegion() {
        if (this.region == null) {
            this.region = new DesktopScreenRegion();
        }
        return this.region;
    }

    @Override
    public List<DesktopWindow> get(Map<String, Object> attributes) {
        SikuliUiMediator mediator = createMediator();
        DesktopWindow desktopWindow = new DesktopWindowImpl(this, mediator, new SikuliUiComponentStateManagerFactory(mediator));
        return Lists.newArrayList(desktopWindow);
    }

    private SikuliUiMediator createMediator() {
        return new SikuliUiMediator(getScreenRegion(), highlightOn);
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.DESKTOP_SIKULI;
    }

    @Override
    public void switchWindow(String windowDescriptor) {
        throw new UnsupportedOperationException("Sikuli API unsupported switchWindow method");
    }

    @Override
    public void close() {
        closed = true;
    }

    public void closeWindow(String windowDescriptor) {
        throw new UnsupportedOperationException("Sikuli API unsupported closeWindow method");
    }

    @Override
    public Set<String> getOpenedWindowDescriptors() {
        throw new UnsupportedOperationException("Sikuli API unsupported getOpenedWindowDescriptors method");
    }

    @Override
    public void switchWindow(DesktopWindow window) {
        throw new UnsupportedOperationException("Sikuli API unsupported switchWindow method");
    }

    @Override
    public void closeWindow(DesktopWindow window) {
        throw new UnsupportedOperationException("Sikuli API unsupported closeWindow method");
    }

    @Override
    public List<DesktopWindow> getAllOpenWindows() {
        throw new UnsupportedOperationException("Sikuli API unsupported getAllOpenWindows method");
    }

    @Override
    public String getCurrentWindowDescriptor() {
        throw new UnsupportedOperationException("Sikuli API unsupported getCurrentWindowDescriptor method");
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public UiComponentBasedDelayer getExecutionDelayer() {
        SikuliUiMediator mediator = createMediator();
        return new GenericUiComponentBasedDelayer(new SikuliUiComponentStateManagerFactory(mediator), mediator);
    }

    @Override
    public DesktopWindow getWindowByTitle(String title) {
        throw new UnsupportedOperationException("Sikuli API unsupported getWindowByTitle method");
    }

    @Override
    public DesktopWindow getCurrentWindow() {
        SikuliUiMediator mediator = createMediator();
        return new DesktopWindowImpl(this, mediator, new SikuliUiComponentStateManagerFactory(mediator));
    }

    @Override
    public void setCurrentWindowSize(int width, int height) {
        throw new UnsupportedOperationException("Sikuli API unsupported setCurrentWindowSize method");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D> D getInternalDriver() {
        return (D) region;
    }

}
