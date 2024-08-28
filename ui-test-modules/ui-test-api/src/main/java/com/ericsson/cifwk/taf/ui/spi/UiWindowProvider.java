package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.InternalDriverAware;
import com.ericsson.cifwk.taf.ui.UiWindow;
import com.ericsson.cifwk.taf.ui.core.UiWindowType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UiWindowProvider<T extends UiWindow> extends InternalDriverAware {

    List<T> get(Map<String, Object> attributes);

    void switchWindow(String windowDescriptor);

    void switchWindow(T window);

    void closeWindow(T window);

    Set<String> getOpenedWindowDescriptors();

    List<T> getAllOpenWindows();

    String getCurrentWindowDescriptor();

    T getCurrentWindow();

    UiWindowType getType();

    void close();

    boolean isClosed();

    UiComponentBasedDelayer getExecutionDelayer();

    T getWindowByTitle(String title);

    void setCurrentWindowSize(int width, int height);
}
