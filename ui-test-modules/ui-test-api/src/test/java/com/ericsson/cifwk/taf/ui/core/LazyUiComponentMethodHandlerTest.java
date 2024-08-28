package com.ericsson.cifwk.taf.ui.core;

import com.google.common.base.Supplier;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LazyUiComponentMethodHandlerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotInitForBasicObjectMethod() throws Throwable {
        Supplier componentSupplier = mock(Supplier.class);
        UiComponentAutowirer autowirer = mock(UiComponentAutowirer.class);
        LazyUiComponentMethodHandler unit = new LazyUiComponentMethodHandler(componentSupplier, autowirer);
        unit = spy(unit);
        doReturn(null).when(unit).doInvoke(any(Object.class), any(Method.class), any(Object[].class));

        TextBoxImpl textBox = new TextBoxImpl();

        unit.invoke(textBox, Object.class.getMethod("hashCode"), null, new Object[]{});
        verify(unit, never()).initializeComponent(any(AbstractUiComponent.class));
        assertFalse(unit.isInitialized());

        unit.invoke(textBox, TextBoxImpl.class.getMethod("getText"), null, new Object[]{});
        verify(unit).initializeComponent(any(AbstractUiComponent.class));
        assertTrue(unit.isInitialized());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldInitOnlyOnce() throws Throwable {
        Supplier componentSupplier = mock(Supplier.class);
        UiComponentAutowirer autowirer = mock(UiComponentAutowirer.class);
        LazyUiComponentMethodHandler unit = new LazyUiComponentMethodHandler(componentSupplier, autowirer);
        unit = spy(unit);
        doReturn(null).when(unit).doInvoke(any(Object.class), any(Method.class), any(Object[].class));

        TextBoxImpl textBox = new TextBoxImpl();

        unit.invoke(textBox, TextBoxImpl.class.getMethod("getText"), null, new Object[]{});
        verify(unit, times(1)).initializeComponent(any(AbstractUiComponent.class));
        assertTrue(unit.isInitialized());

        unit.invoke(textBox, TextBoxImpl.class.getMethod("getText"), null, new Object[]{});
        verify(unit, times(1)).initializeComponent(any(AbstractUiComponent.class));
    }

}