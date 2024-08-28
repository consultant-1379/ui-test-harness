package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.AbstractConditionWait;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.spi.UiActions;

import java.util.List;

abstract class AbstractViewModel extends AbstractConditionWait implements ViewModel {

    @Override
    public Button getButton(SelectorType selectorType, String selector) {
        return getViewComponent(selectorType, selector, Button.class);
    }

    @Override
    public TextBox getTextBox(SelectorType selectorType, String selector) {
        return getViewComponent(selectorType, selector, TextBox.class);
    }

    @Override
    public Label getLabel(SelectorType selectorType, String selector) {
        return getViewComponent(selectorType, selector, Label.class);
    }

    @Override
    public Link getLink(SelectorType selectorType, String selector) {
        return getViewComponent(selectorType, selector, Link.class);
    }

    @Override
    public Select getSelect(SelectorType selectorType, String selector) {
        return getViewComponent(selectorType, selector, Select.class);
    }

    @Override
    public RadioButton getRadioButton(SelectorType selectorType, String selector) {
        return getViewComponent(selectorType, selector, RadioButton.class);
    }

    @Override
    public CheckBox getCheckBox(SelectorType selectorType, String selector) {
        return getViewComponent(selectorType, selector, CheckBox.class);
    }

    @Override
    public Button getButton(String selector) {
        return getViewComponent(selector, Button.class);
    }

    @Override
    public TextBox getTextBox(String selector) {
        return getViewComponent(selector, TextBox.class);
    }

    @Override
    public FileSelector getFileSelector(String selector) {
        return getViewComponent(selector, FileSelector.class);
    }

    @Override
    public Label getLabel(String selector) {
        return getViewComponent(selector, Label.class);
    }

    @Override
    public Link getLink(String selector) {
        return getViewComponent(selector, Link.class);
    }

    @Override
    public Select getSelect(String selector) {
        return getViewComponent(selector, Select.class);
    }

    @Override
    public RadioButton getRadioButton(String selector) {
        return getViewComponent(selector, RadioButton.class);
    }

    @Override
    public CheckBox getCheckBox(String selector) {
        return getViewComponent(selector, CheckBox.class);
    }

    @Override
    public final <T extends UiComponent> T getViewComponent(String selector, Class<T> componentClass) {
        return getViewComponent(SelectorType.DEFAULT, selector, componentClass);
    }

    @Override
    public UiComponent getViewComponent(String selector) {
        return getViewComponent(selector, UiComponent.class);
    }

    @Override
    public boolean isCurrentView() {
        throw new UnsupportedOperationException("Method should be overridden in subclass");
    }

    @Override
    public final boolean hasComponent(SelectorType selectorType, String selector) {
        UiComponent component = getViewComponent(selectorType, selector, UiComponent.class);
        return component.exists();
    }

    @Override
    public final boolean hasComponent(String selector) {
        return hasComponent(SelectorType.DEFAULT, selector);
    }

    @Override
    public final <T extends UiComponent> List<T> getViewComponents(String selector, Class<T> componentClass) {
        return getViewComponents(SelectorType.DEFAULT, selector, componentClass);
    }

    @Override
    public final int getComponentCount(SelectorType selectorType, String selector) {
        List<UiComponent> viewComponents = getViewComponents(selectorType, selector, UiComponent.class);
        return viewComponents.size();
    }

    @Override
    public UiActions newActionChain() {
        return getMediator().newActionChain();
    }

}
