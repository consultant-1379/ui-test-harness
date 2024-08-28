package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.google.common.base.Predicate;

/**
 * Created by Mihails Volkovs <mihails.volkovs@ericsson.com>
 * 2015.09.28.
 */
public class UiWindowAdapter implements UiWindow {
    @Override
    public boolean isClosed() {
        throw unsupported();
    }

    @Override
    public ViewModel getGenericView() {
        throw unsupported();
    }

    @Override
    public <T extends ViewModel> T getView(Class<T> clazz) {
        throw unsupported();
    }

    @Override
    public String getWindowDescriptor() {
        throw unsupported();
    }

    @Override
    public MessageBox getMessageBox() {
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

    @Override
    public void maximize() {
        throw unsupported();
    }

    @Override
    public UiComponentSize getSize() {
        throw unsupported();
    }

    @Override
    public Object evaluate(String expression) {
        throw unsupported();
    }

    @Override
    public void dragAndDropTo(UiComponent object, UiComponent target) {
        throw unsupported();
    }

    @Override
    public String copy() {
        throw unsupported();
    }

    @Override
    public void sendKey(int key, int... modifiers) {
        throw unsupported();
    }

    @Override
    public void sendKeys(String keys, int... modifiers) {
        throw unsupported();
    }

    @Override
    public void takeScreenshot(String name) {
        throw unsupported();
    }

    @Override
    public String getTitle() {
        throw unsupported();
    }

    private static UnsupportedOperationException unsupported() {
        throw new UnsupportedOperationException("Method is not supported");
    }

}
