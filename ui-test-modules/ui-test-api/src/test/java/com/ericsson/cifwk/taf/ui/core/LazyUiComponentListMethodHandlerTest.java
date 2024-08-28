package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LazyUiComponentListMethodHandlerTest {

    @Test
    public void shouldInitializeOnlyOnceForNonDynamicCollection() throws Throwable {

        LazyUiComponentListMethodHandler unit = getUnit(true);
        unit = spy(unit);
        doNothing().when(unit).initializeComponentList();

        unit.invoke(null, List.class.getMethod("size"), new Object[]{});
        verify(unit, times(1)).initializeComponentList();
        Assert.assertTrue(unit.isInitialized());

        unit.invoke(null, List.class.getMethod("size"), new Object[]{});
        verify(unit, times(1)).initializeComponentList();
    }

    @Test
    public void shouldReInitializeForDynamicCollection() throws Throwable {
        verifyDynamicCollectionInit();
    }

    @Test
    public void shouldReInitializeByDefault() throws Throwable {
        verifyDynamicCollectionInit();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void happyPath() throws Throwable {

        Supplier<List<UiComponentStateManager>> listSupplier = (Supplier<List<UiComponentStateManager>>) mock(Supplier.class);
        when(listSupplier.get()).thenReturn(asList(mock(UiComponentStateManager.class)))
                .thenReturn(asList(mock(UiComponentStateManager.class), mock(UiComponentStateManager.class)));
        UiComponentFactory uiComponentFactory = mock(UiComponentFactory.class);
        when(uiComponentFactory.instantiateComponent(any(UiComponentStateManager.class), eq(GenericUiComponent.class)))
                .thenReturn(mock(GenericUiComponent.class));
        LazyUiComponentListMethodHandler unit =
                new LazyUiComponentListMethodHandler(listSupplier, uiComponentFactory, GenericUiComponent.class, false);

        Assert.assertEquals(1, unit.invoke(null, List.class.getMethod("size"), new Object[]{}));
        Assert.assertEquals(2, unit.invoke(null, List.class.getMethod("size"), new Object[]{}));
    }

    @SuppressWarnings("unchecked")
    private LazyUiComponentListMethodHandler getUnit(boolean isStaticList) {
        Supplier<List<UiComponentStateManager>> listSupplier = (Supplier<List<UiComponentStateManager>>) mock(Supplier.class);
        return new LazyUiComponentListMethodHandler(listSupplier, mock(UiComponentFactory.class), GenericUiComponent.class, isStaticList);
    }

    private void verifyDynamicCollectionInit() throws Throwable {
        LazyUiComponentListMethodHandler unit = getUnit(false);
        unit = spy(unit);
        doNothing().when(unit).initializeComponentList();

        unit.invoke(null, List.class.getMethod("size"), new Object[]{});
        verify(unit, times(1)).initializeComponentList();
        Assert.assertTrue(unit.isInitialized());

        // Make sure no re-init happens on appropriate method calls
        unit.invoke(null, List.class.getMethod("clear"), new Object[]{});
        verify(unit, times(1)).initializeComponentList();

        unit.invoke(null, List.class.getMethod("size"), new Object[]{});
        verify(unit, times(2)).initializeComponentList();
    }

}