package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiWindowType;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 07/01/2016
 */
public class MockedWindowsProvider implements UiWindowProviderFactory {

    @Override
    public UiWindowProvider<UiWindow> newInstance(Object... initParameters) {
        return new UiWindowProvider<UiWindow>() {
            @Override
            public <D> D getInternalDriver() {
                return null;
            }

            @Override
            public List<UiWindow> get(Map<String, Object> attributes) {
                return null;
            }

            @Override
            public void switchWindow(String windowDescriptor) {

            }

            @Override
            public void switchWindow(UiWindow window) {

            }

            @Override
            public void closeWindow(UiWindow window) {
                System.out.println("Close called");
            }

            @Override
            public Set<String> getOpenedWindowDescriptors() {
                return null;
            }

            @Override
            public List<UiWindow> getAllOpenWindows() {
                return null;
            }

            @Override
            public String getCurrentWindowDescriptor() {
                return null;
            }

            @Override
            public UiWindow getCurrentWindow() {
                return null;
            }

            @Override
            public UiWindowType getType() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public UiComponentBasedDelayer getExecutionDelayer() {
                return null;
            }

            @Override
            public UiWindow getWindowByTitle(String title) {
                return null;
            }

            @Override
            public void setCurrentWindowSize(int width, int height) {

            }

        };
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.BROWSER;
    }
}
