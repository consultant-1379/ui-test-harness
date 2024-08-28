package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UiComponentFactoryProxyTest {

    @Test
    public void lazyLoading_gettingWebElement() throws Exception {
        UiComponentAutowirer autowirer = mock(UiComponentAutowirer.class);
        UiComponentFactoryImpl unit = new UiComponentFactoryImpl(autowirer);
        Supplier supplier = mock(Supplier.class);
        UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        when(supplier.get()).thenReturn(stateManager);
        GenericUiComponent uiComponent = unit.instantiateComponent(supplier, GenericUiComponent.class);

        verify(supplier, never()).get();
        verify(autowirer, never()).initialize(uiComponent);

        // Should init on this call
        UiComponentStateManager componentStateManager = uiComponent.getStateManager();

        verify(supplier, times(1)).get();
        verify(autowirer, times(1)).initialize(uiComponent);

        Assert.assertSame(stateManager, componentStateManager);
    }

}