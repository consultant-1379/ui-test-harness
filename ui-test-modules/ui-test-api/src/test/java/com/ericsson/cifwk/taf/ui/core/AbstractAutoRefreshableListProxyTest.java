package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AbstractAutoRefreshableListProxyTest {

    private AbstractAutoRefreshableListProxy unit;

    @Before
    public void setUp() {
        unit = new AbstractAutoRefreshableListProxy() {
            @Override
            protected void doInitializeOnce() {
            }

            @Override
            protected void doPrepareDataBeforeInvocation() {
            }

            @Override
            protected Object doInvoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                return null;
            }
        };
        unit = spy(unit);
    }

    @Test
    public void testInvocation() throws Exception {
        List list = getProxiedList();
        verify(unit, never()).doInitializeOnce();
        verify(unit, never()).doPrepareDataBeforeInvocation();
        verify(unit, never()).doInvoke(anyObject(), any(Method.class), any(Object[].class));

        // Method that doesn't require preprocessing before call
        list.clear();
        verify(unit, times(1)).doInitializeOnce();
        verify(unit, never()).doPrepareDataBeforeInvocation();
        verify(unit, times(1)).doInvoke(anyObject(), any(Method.class), any(Object[].class));

        // Method that requires preprocessing before call
        list.toString();
        verify(unit, times(1)).doInitializeOnce();
        verify(unit, times(1)).doPrepareDataBeforeInvocation();
        verify(unit, times(2)).doInvoke(anyObject(), any(Method.class), any(Object[].class));
    }

    @SuppressWarnings("unchecked")
    private List getProxiedList() {
        return (List<UiComponentStateManager>) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{List.class},
                unit);

    }
}