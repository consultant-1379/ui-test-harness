package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DesktopNavigator {

    private final UiWindowProvider<DesktopWindow> windowProvider;

    DesktopNavigator(UiWindowProvider<DesktopWindow> windowProvider) {
        this.windowProvider = windowProvider;
    }

    public DesktopWindow getCurrentWindow() {
        return windowProvider.getCurrentWindow();
    }

    public DesktopWindow getWindowByTitle(String title) {
        return windowProvider.getWindowByTitle(title);
    }

    public <T> Future<T> async(Callable<T> callable) {
        return Executors.newSingleThreadExecutor().submit(callable);
    }

    public void close() {
        windowProvider.close();
    }

}
