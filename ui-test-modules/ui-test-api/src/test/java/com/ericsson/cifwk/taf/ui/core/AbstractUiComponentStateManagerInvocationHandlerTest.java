package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Supplier;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractUiComponentStateManagerInvocationHandlerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldInstantiateOnlyOnceIfComponentFound() throws Throwable {
        String mappingInfo = "mappingInfo";

        Supplier<UiComponentStateManager> instanceSupplier = mock(Supplier.class);
        UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        when(stateManager.isDisplayed()).thenReturn(true);
        when(instanceSupplier.get()).thenReturn(stateManager);
        UiMediator mediator = mock(UiMediator.class);
        AbstractUiComponentStateManagerInvocationHandler unit = getUnit(mappingInfo, instanceSupplier, mediator);

        assertEquals(mappingInfo, unit.invoke(null, UiComponentStateManager.class.getMethod("getMappingInfo"), new Object[]{}));
        assertFalse(unit.isInitialized());

        assertEquals(true, unit.invoke(null, UiComponentStateManager.class.getMethod("isDisplayed"), new Object[]{}));
        assertTrue(unit.isInitialized());
        verify(stateManager, times(1)).isDisplayed();
        verify(instanceSupplier, times(1)).get();

        assertEquals(true, unit.invoke(null, UiComponentStateManager.class.getMethod("isDisplayed"), new Object[]{}));
        verify(stateManager, times(2)).isDisplayed();
        // Already initialized, so no new lookups
        verify(instanceSupplier, times(1)).get();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnMappingInfoIfToStringFails() throws Throwable {
        String mappingInfo = "mappingInfo";
        String asString = "string";

        Supplier<UiComponentStateManager> instanceSupplier = mock(Supplier.class);
        UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        when(stateManager.getAsString()).thenReturn(asString);
        UiComponentNotFoundException exception = new UiComponentNotFoundException(mappingInfo);
        when(instanceSupplier.get())
                .thenThrow(exception)
                .thenReturn(stateManager)
                .thenThrow(exception);
        UiMediator mediator = mock(UiMediator.class);
        AbstractUiComponentStateManagerInvocationHandler unit = getUnit(mappingInfo, instanceSupplier, mediator);

        assertEquals(mappingInfo, unit.invoke(null, UiComponentStateManager.class.getMethod("getAsString"), new Object[]{}));
        assertEquals(mappingInfo, unit.invoke(null, UiComponentStateManager.class.getMethod("getAsString"), new Object[]{}));
        assertEquals(mappingInfo, unit.invoke(null, UiComponentStateManager.class.getMethod("getAsString"), new Object[]{}));
    }

    private AbstractUiComponentStateManagerInvocationHandler getUnit(final String mappingInfo, final Supplier<UiComponentStateManager> instanceSupplier, UiMediator mediator) {
        return new AbstractUiComponentStateManagerInvocationHandler(instanceSupplier, mappingInfo, null) {
            @Override
            protected Object handleInvocationTargetException(InvocationTargetException e, Method method, Object[] args)
                    throws IllegalAccessException, InvocationTargetException {
                return null;
            }
        };
    }

}