package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.UiWindow;
import com.ericsson.cifwk.taf.ui.core.UiWindowType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.28.
 */
public class UiWindowProviderAdapter<T extends UiWindow> implements UiWindowProvider<T> {

    @Override
    public List<T> get(Map<String, Object> attributes) {
        throw unsupported();
    }

    @Override
    public void switchWindow(String windowDescriptor) {
        throw unsupported();
    }

    @Override
    public void switchWindow(T window) {
        throw unsupported();
    }

    @Override
    public void closeWindow(T window) {
        throw unsupported();
    }

    @Override
    public Set<String> getOpenedWindowDescriptors() {
        throw unsupported();
    }

    @Override
    public List<T> getAllOpenWindows() {
        throw unsupported();
    }

    @Override
    public String getCurrentWindowDescriptor() {
        throw unsupported();
    }

    @Override
    public T getCurrentWindow() {
        throw unsupported();
    }

    @Override
    public UiWindowType getType() {
        throw unsupported();
    }

    @Override
    public void close() {
        throw unsupported();
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
    public T getWindowByTitle(String title) {
        throw unsupported();
    }

    @Override
    public void setCurrentWindowSize(int width, int height) {
        throw unsupported();
    }

    @Override
    public <D> D getInternalDriver() {
        throw unsupported();
    }

    private static UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException("Method is not supported");
    }

}
