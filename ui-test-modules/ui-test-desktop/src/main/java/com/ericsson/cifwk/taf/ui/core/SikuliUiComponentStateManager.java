package com.ericsson.cifwk.taf.ui.core;

import static com.ericsson.cifwk.taf.ui.core.UiEvent.CLICK;
import static com.ericsson.cifwk.taf.ui.core.UiEvent.FOCUS;
import static com.ericsson.cifwk.taf.ui.core.UiEvent.KEY_PRESS;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import com.ericsson.cifwk.taf.ui.spi.AbstractUiEventProcessor;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Optional;

class SikuliUiComponentStateManager extends AbstractUiEventProcessor implements UiComponentStateManager {

    private final SikuliUiMediator mediator;
    private final String mapping;
    private ScreenRegion region;

    SikuliUiComponentStateManager(SikuliUiMediator mediator, ScreenRegion region, String mapping) {
        this.mediator = mediator;
        this.region = region;
        this.mapping = mapping;
    }

    @Override
    public String getProperty(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperty(String name, String value) {
        if (UiProperties.VALUE.equals(name)) {
            processEvent(FOCUS);
            HashMap<String, Object> arguments = new HashMap<>();
            arguments.put("keys", value);
            processEvent(KEY_PRESS, arguments);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<UiComponentStateManager> getDescendantsByExpression(SelectorType selectorType, String expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UiComponentStateManager> getDescendantsByExpression(UiComponentMappingDetails mappingDetails) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processEvent(UiEvent event, Map<String, ?> arguments) {
        if (CLICK == event || FOCUS == event) {
            Mouse mouse = new DesktopMouse();
            mouse.click(region.getCenter());
        } else if (KEY_PRESS == event) {
            String keys = (String) arguments.get("keys");
            int[] modifiers = (int[]) arguments.get("modifiers");
            mediator.sendKeys(keys, modifiers);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void processEvent(UiEvent event, UiComponentCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processEvent(UiEvent event, Map<String, ?> arguments,
                             UiComponentCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisplayed() {
        return region != null;
    }

    @Override
    public boolean exists() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendKeysSafely(CharSequence... keysToSend) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseOverAt(int... coordinates) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasFocus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearSelection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseDownAt(int... coordinates) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseUpAt(int... coordinates) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseMoveAt(int... coordinates) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseOut() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UiComponentSize getSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMappingInfo() {
        return mapping;
    }

    @Override
    public String getComponentName() {
        return null;
    }

    @Override
    public String getAsString() {
        return getClass().getName() + "[" + mapping + "]";
    }

    @Override
    public UiComponentMappingDetails getComponentDetails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UiActions createUiActions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object evaluate(UiComponent uiComponent, String expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<InputStream> takeScreenshot() {
        return mediator.takeScreenshot();
    }
}
