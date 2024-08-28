package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UiComponentFactoryImplTest {

    private UiComponentFactoryImpl unit;
    private UiComponentAutowirer autowirer;

    @Before
    public void setUp() {
        autowirer = mock(UiComponentAutowirer.class);
        unit = new UiComponentFactoryImpl(autowirer);
    }

    @Test
    public void instantiateComponent() {
        UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        TextBox textBox = unit.instantiateComponent(stateManager, TextBox.class);
        assertNotNull(textBox);
    }

    @Test
    public void getTargetClass() throws Exception {
        assertEquals(GenericUiComponent.class, unit.getTargetClass(UiComponent.class));
        assertEquals(TextBoxImpl.class, unit.getTargetClass(TextBox.class));
        assertEquals(ButtonImpl.class, unit.getTargetClass(Button.class));
//        assertEquals(RadioButtonImpl.class, unit.getTargetClass(RadioButton.class));
    }

    @Test
    public void lazyLoading() throws Exception {
        Supplier supplier = mock(Supplier.class);
        GenericUiComponent uiComponent = unit.instantiateComponent(supplier, GenericUiComponent.class);

        verify(supplier, never()).get();
        verify(autowirer, never()).initialize(uiComponent);

        uiComponent.exists();

        verify(supplier, times(1)).get();
        verify(autowirer, times(1)).initialize(uiComponent);
    }

}