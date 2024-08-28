package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.google.common.base.Predicate;

import java.util.List;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.28.
 */
public class ViewModelAdapter implements ViewModel {
    @Override
    public Button getButton(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public TextBox getTextBox(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public Label getLabel(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public Link getLink(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public Select getSelect(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public RadioButton getRadioButton(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public CheckBox getCheckBox(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public Button getButton(String selector) {
        throw unsupported();
    }

    @Override
    public TextBox getTextBox(String selector) {
        throw unsupported();
    }

    @Override
    public FileSelector getFileSelector(String selector) {
        throw unsupported();
    }

    @Override
    public Label getLabel(String selector) {
        throw unsupported();
    }

    @Override
    public Link getLink(String selector) {
        throw unsupported();
    }

    @Override
    public Select getSelect(String selector) {
        throw unsupported();
    }

    @Override
    public RadioButton getRadioButton(String selector) {
        throw unsupported();
    }

    @Override
    public CheckBox getCheckBox(String selector) {
        throw unsupported();
    }

    @Override
    public UiComponent getViewComponent(String selector) {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> T getViewComponent(String selector, Class<T> componentClass) {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> T getViewComponent(SelectorType selectorType, String selector, Class<T> componentClass) {
        throw unsupported();
    }

    @Override
    public boolean isCurrentView() {
        throw unsupported();
    }

    @Override
    public boolean hasComponent(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public boolean hasComponent(String selector) {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> List<T> getViewComponents(SelectorType selectorType, String selector, Class<T> componentClass) {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> List<T> getViewComponents(String selector, Class<T> componentClass) {
        throw unsupported();
    }

    @Override
    public int getComponentCount(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public UiActions newActionChain() {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(String selector) {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(UiComponent component) {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsHidden(UiComponent component) {
        throw unsupported();
    }

    @Override
    public <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate) {
        throw unsupported();
    }

    @Override
    public void waitUntil(GenericPredicate predicate) {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(String selector, long timeoutInMillis) {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(SelectorType selectorType, String selector, long timeoutInMillis) {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsDisplayed(UiComponent component, long timeoutInMillis) {
        throw unsupported();
    }

    @Override
    public UiComponent waitUntilComponentIsHidden(UiComponent component, long timeoutInMillis) {
        throw unsupported();
    }

    @Override
    public <C extends UiComponent> C waitUntil(C component, Predicate<C> predicate, long timeoutInMillis) {
        throw unsupported();
    }

    @Override
    public void waitUntil(GenericPredicate predicate, long timeoutInMillis) {
        throw unsupported();
    }

    private static UnsupportedOperationException unsupported() {
        throw new UnsupportedOperationException("Method is not supported");
    }

}
