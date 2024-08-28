package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;

import java.util.List;

/**
 * Basic class of SWT view models
 */
public class SwtViewModel extends ViewModelAdapter {

    private ViewModel swtInternalView;

    // Used by reflection in SwtDesktopWindow. Public only for test purposes.
    public SwtViewModel() {
        super();
    }

    public Table getTable(String selector) {
        return getViewComponent(selector, Table.class);
    }

    @Override
    public final <T extends UiComponent> T getViewComponent(SelectorType selectorType, String selector, Class<T> componentClass) {
        return swtInternalView.getViewComponent(selectorType, selector, componentClass);
    }

    @Override
    public final <T extends UiComponent> List<T> getViewComponents(SelectorType selectorType, String selector, Class<T> componentClass) {
        return swtInternalView.getViewComponents(selectorType, selector, componentClass);
    }

    void setInternalView(ViewModel swtInternalView) {
        this.swtInternalView = swtInternalView;
    }

}
