package com.ericsson.cifwk.taf.ui.spi;

import java.util.List;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMappingDetails;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;

/**
 * Component for abstract element's (implementation of {@link com.ericsson.cifwk.taf.ui.core.UiComponent}) interaction
 * with the actual representation.
 */
public interface UiComponentStateManager extends UiEventProcessor, ScreenshotProvider {

    String getProperty(String name);

    void setProperty(String name, String value);

    List<UiComponentStateManager> getDescendantsByExpression(SelectorType selectorType, String expression);

    List<UiComponentStateManager> getDescendantsByExpression(UiComponentMappingDetails mappingDetails);

    boolean isDisplayed();

    boolean exists();

    void clearSelection();

    void sendKeys(CharSequence... keysToSend);

    void sendKeysSafely(CharSequence... keysToSend);

    void mouseOverAt(int... coordinates);

    boolean isSelected();

    boolean isEnabled();

    boolean hasFocus();

    void mouseDownAt(int... coordinates);

    void mouseUpAt(int... coordinates);

    void mouseMoveAt(int... coordinates);

    void mouseOut();

    UiComponentSize getSize();

    String getMappingInfo();

    String getComponentName();

    String getAsString();

    UiComponentMappingDetails getComponentDetails();

    UiActions createUiActions();

    Object evaluate(UiComponent uiComponent, String expression);

}
