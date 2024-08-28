package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import javassist.util.proxy.MethodHandler;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 05/09/2016
 */
class GenericViewModelMethodHandler<T extends ViewModel> implements MethodHandler {

    private final UiWindowProvider<?> windowProvider;
    private final T original;

    public GenericViewModelMethodHandler(UiWindowProvider<?> windowProvider, T original) {
        this.windowProvider = windowProvider;
        this.original = original;
    }

    @Override
    public Object invoke(Object self, Method overridden, Method forwarder, Object[] args) throws Throwable {
        String currentWindowDescriptor = windowProvider.getCurrentWindowDescriptor();
        String thisWindowDescriptor = ((GenericViewModel)original).getWindowDescriptor();
        if (!StringUtils.equals(thisWindowDescriptor, currentWindowDescriptor)) {
            windowProvider.switchWindow(thisWindowDescriptor);
        }
        try {
            overridden.setAccessible(true);
            return overridden.invoke(original, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

}
