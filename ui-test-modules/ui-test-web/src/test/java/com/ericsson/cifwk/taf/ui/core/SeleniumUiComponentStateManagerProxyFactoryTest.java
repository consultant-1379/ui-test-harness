package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SeleniumUiComponentStateManagerProxyFactoryTest {

    @Spy
    private SeleniumUiComponentStateManagerProxyFactory unit;

    @Before
    public void setUp() {
        doReturn(null).when(unit).proxyStateManager(any(UiMediator.class), any(UiComponentStateManager.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCreateStateManagerProxy() throws Exception {
        UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        unit.createStateManagerProxy(null, stateManager);
        verify(unit).proxyStateManager(any(UiMediator.class), eq(stateManager));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotCreateStateManagerProxyIfAlreadyProxied() throws Exception {
        UiComponentStateManager stateManagerProxy = (UiComponentStateManager)
                Proxy.newProxyInstance(this.getClass().getClassLoader(),
                        new Class[]{UiComponentStateManager.class}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                return null;
                            }
                        });
        unit.createStateManagerProxy(null, stateManagerProxy);
        verify(unit, never()).proxyStateManager(any(UiMediator.class), any(UiComponentStateManager.class));
    }

}