package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.AbstractUiWindow;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.SwtUiNavigator;
import com.ericsson.cifwk.taf.ui.core.SwtUiComponentAutowirer;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentAutowirer;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.core.UiException;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SwtDesktopWindow extends AbstractUiWindow implements DesktopWindow {

    private ViewModel swtInternalView;
    private SwtUiNavigator swtNavigator;
    private UiComponentAutowirer componentAutowirer;
    private String windowTitle;

    public SwtDesktopWindow(SwtUiComponentAutowirer componentAutowirer, SwtUiNavigator swtNavigator,
                            ViewModel swtInternalView, String windowTitle) {
        this.componentAutowirer = componentAutowirer;
        this.swtNavigator = swtNavigator;
        this.swtInternalView = swtInternalView;
        this.windowTitle = windowTitle;
    }

    @Override
    public ViewModel getGenericView() {
        return swtInternalView;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T getView(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            if (!(instance instanceof SwtViewModel)) {
                throw new IllegalArgumentException("View model class must extend " + SwtViewModel.class.getName());
            }
            SwtViewModel genericViewModel = (SwtViewModel) instance;
            genericViewModel.setInternalView(swtInternalView);
            componentAutowirer.initialize(genericViewModel);

            return (T) genericViewModel;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public String getTitle() {
        return windowTitle;
    }

    @Override
    protected UiComponentBasedDelayer getDelayer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getWindowDescriptor() {
        return windowTitle;
    }

    @Override
    public MessageBox getMessageBox() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void maximize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UiComponentSize getSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object evaluate(String expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dragAndDropTo(UiComponent object, UiComponent target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendKey(int key, int... modifiers) {
        UiComponent shell = swtNavigator.getShell(windowTitle);
        shell.sendKeys(Character.toString((char) key));
    }

    @Override
    public void sendKeys(String keys, int... modifiers) {
        UiComponent shell = swtNavigator.getShell(windowTitle);
        shell.sendKeys(keys);
    }

    @Override
    public void takeScreenshot(String path) {
        byte[] screenshotBytes = swtNavigator.getScreenshot(windowTitle);
        try(FileOutputStream outputStream = new FileOutputStream(path, false)) {
            ByteStreams.copy(new ByteArrayInputStream(screenshotBytes), outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new UiException("Failed to create the screenshot for window '" + windowTitle + "'", e);
        }
    }

    @Override
    public MenuItem getMenuItem(String menuName) {
        return swtInternalView.getViewComponent(menuName, MenuItem.class);
    }

    @Override
    protected UiMediator getMediator() {
        throw new UnsupportedOperationException();
    }
}
