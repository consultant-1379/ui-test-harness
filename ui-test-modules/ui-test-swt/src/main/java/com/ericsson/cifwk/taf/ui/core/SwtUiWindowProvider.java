package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.SwtUiNavigator;
import com.ericsson.cifwk.taf.ui.sdk.SwtDesktopWindow;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SwtUiWindowProvider implements UiWindowProvider<DesktopWindow> {

    private SwtUiNavigator swtNavigator;

    public SwtUiWindowProvider(String host, int port) {
        swtNavigator = new SwtUiNavigator(host, port);
    }

    @Override
    public DesktopWindow getWindowByTitle(String title) {
        return createNewWindowMapping(title);
    }

    @Override
    public List<DesktopWindow> get(Map<String, Object> attributes) {
        throw unsupported();
    }

    private SwtDesktopWindow createNewWindowMapping(String title) {
        SwtUiComponentAutowirer autowirer = new SwtUiComponentAutowirer(null);
        ViewModel internalSwtView = swtNavigator.getView(title);
        autowirer.setInternalView(internalSwtView);
        return new SwtDesktopWindow(autowirer, swtNavigator, internalSwtView, title);
    }

    @Override
    public void switchWindow(String windowDescriptor) {
        throw unsupported();
    }

    @Override
    public void switchWindow(DesktopWindow window) {
        throw unsupported();
    }

    @Override
    public void closeWindow(DesktopWindow window) {
        throw unsupported();
    }

    @Override
    public Set<String> getOpenedWindowDescriptors() {
        throw unsupported();
    }

    @Override
    public List<DesktopWindow> getAllOpenWindows() {
        throw unsupported();
    }

    @Override
    public String getCurrentWindowDescriptor() {
        throw unsupported();
    }

    @Override
    public DesktopWindow getCurrentWindow() {
        throw unsupported();
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.DESKTOP_SWT;
    }

    @Override
    public void close() {
        swtNavigator.reset();
    }

    @Override
    public boolean isClosed() {
        throw unsupported();
    }

    @Override
    public UiComponentBasedDelayer getExecutionDelayer() {
        throw unsupported();
    }

    @Override
    public void setCurrentWindowSize(int width, int height) {
        throw unsupported();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D> D getInternalDriver() {
        return (D) swtNavigator;
    }

    private static UnsupportedOperationException unsupported() {
        throw new UnsupportedOperationException("Method is not supported");
    }

}
