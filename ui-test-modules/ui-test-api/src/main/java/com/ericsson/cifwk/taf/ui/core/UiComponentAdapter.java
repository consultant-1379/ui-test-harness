package com.ericsson.cifwk.taf.ui.core;

import java.util.List;

import com.ericsson.cifwk.taf.ui.sdk.MenuItem;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.28.
 */
public class UiComponentAdapter implements UiComponent {
    @Override
    public String getId() {
        throw unsupported();
    }

    @Override
    public String getComponentName() {
        throw unsupported();
    }

    @Override
    public boolean isDisplayed() {
        throw unsupported();
    }

    @Override
    public boolean exists() {
        throw unsupported();
    }

    @Override
    public boolean isSelected() {
        throw unsupported();
    }

    @Override
    public boolean isEnabled() {
        throw unsupported();
    }

    @Override
    public void click() {
        throw unsupported();
    }

    @Override
    public void contextClick() {
        throw unsupported();
    }

    @Override
    public String getText() {
        throw unsupported();
    }

    @Override
    public void focus() {
        throw unsupported();
    }

    @Override
    public boolean hasFocus() {
        throw unsupported();
    }

    @Override
    public String getProperty(String propertyName) {
        throw unsupported();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        throw unsupported();
    }

    @Override
    public void mouseOver(int... coordinates) {
        throw unsupported();
    }

    @Override
    public void mouseDown(int... coordinates) {
        throw unsupported();
    }

    @Override
    public void mouseUp(int... coordinates) {
        throw unsupported();
    }

    @Override
    public void mouseMove(int... coordinates) {
        throw unsupported();
    }

    @Override
    public void mouseOut() {
        throw unsupported();
    }

    @Override
    public UiComponentSize getSize() {
        throw unsupported();
    }

    @Override
    public MenuItem getMenuItem(String menuName) {
        throw unsupported();
    }

    @Override
    public List<UiComponent> getChildren() {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> Optional<T> getFirstDescendantBySelector(final String selector, final Class<T> type) {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> Optional<T> getFirstDescendantBySelector(final SelectorType selectorType, final String selector, final Class<T>
            type) {
        throw unsupported();
    }

    @Override
    public List<UiComponent> getDescendantsBySelector(String selector) {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> List<T> getDescendantsBySelector(final String selector, final Class<T> type) {
        throw unsupported();
    }

    @Override
    public List<UiComponent> getDescendantsBySelector(SelectorType selectorType, String selector) {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> List<T> getDescendantsBySelector(final SelectorType selectorType, final String selector, final Class<T> type) {
        throw unsupported();
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public UiComponentSelector getComponentSelector() {
        throw unsupported();
    }

    @Override
    public UiComponentMappingDetails getComponentDetails() {
        throw unsupported();
    }

    @Override
    public <T extends UiComponent> T as(Class<T> clazz) {
        throw unsupported();
    }

    @Override
    public void setStateManager(UiComponentStateManager stateManager) {
        throw unsupported();
    }

    @Override
    public void waitUntil(GenericPredicate condition, long timeoutInMillis) {
        throw unsupported();
    }

    @Override
    public void waitUntil(GenericPredicate condition) {
        throw unsupported();
    }

    @Override
    public <C extends UiComponent> void waitUntil(C component, Predicate<C> condition, long timeoutInMillis) {
        throw unsupported();
    }

    @Override
    public <C extends UiComponent> void waitUntil(C component, Predicate<C> condition) {
        throw unsupported();
    }

    @Override
    public UiActions createUiActions() {
        throw unsupported();
    }

    @Override
    public Object evaluate(String expression) {
        throw unsupported();
    }

    private static UnsupportedOperationException unsupported() {
        throw new UnsupportedOperationException("Method is not supported");
    }
}
