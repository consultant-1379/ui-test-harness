package com.ericsson.cifwk.taf.ui.core;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.cifwk.taf.ui.sdk.AbstractStateManagerBasedViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;

public class ComponentCastingTest {

    private UiMediator mediator = mock(UiMediator.class);
    private UiComponentStateManagerFactory stateManagerFactory;
    private GenericUiComponentAutowirer unit;
    private UiComponentStateManager stateManager;
    private AbstractStateManagerBasedViewModel model;

    @Before
    public void setUp() {
        stateManager = mock(UiComponentStateManager.class);
        stateManagerFactory = mock(UiComponentStateManagerFactory.class);
        unit = new GenericUiComponentAutowirer(stateManagerFactory);
        when(stateManagerFactory.createStateManager(any(UiComponentMappingDetails.class))).thenReturn(stateManager);
        when(stateManagerFactory.getAutowirer()).thenReturn(unit);
        model = getAbstractStateManagerBasedViewModel(
                new GenericUiComponentBasedDelayer(stateManagerFactory, mediator),
                mediator, stateManagerFactory, stateManager);
    }

    @Test
    public void shouldReturnSameInstanceForSameClass() throws Exception {
        UiComponent testBox = model.getTextBox("text");
        assertThat(testBox.as(UiComponent.class), sameInstance(testBox));
    }

    @Test
    public void shouldCreateAnotherComponentRepresentation() throws Exception {
        Button button = model.getButton("button");
        TextBox as = button.as(TextBox.class);
        assertThat(as, notNullValue());
        assertThat(((TextBoxImpl) as).getStateManager(), sameInstance(stateManager));
    }

    private AbstractStateManagerBasedViewModel getAbstractStateManagerBasedViewModel(
            final UiComponentBasedDelayer delayer,
            final UiMediator mediator,
            final UiComponentStateManagerFactory stateManagerFactory,
            final UiComponentStateManager stateManager) {
        return new AbstractStateManagerBasedViewModel() {
            @Override
            protected UiComponentBasedDelayer getDelayer() {
                return delayer;
            }

            @Override
            protected UiMediator getMediator() {
                return mediator;
            }

            protected List<UiComponentStateManager> getComponentStateManagersBySelector(SelectorType selectorType, String selector) {
                return null;
            }

            @Override
            protected UiComponentStateManagerFactory getStateManagerFactory() {
                return stateManagerFactory;
            }

            protected UiComponentStateManager getComponentStateManagerBySelector(SelectorType selectorType, String selector) {
                return stateManager;
            }
        };
    }
}
