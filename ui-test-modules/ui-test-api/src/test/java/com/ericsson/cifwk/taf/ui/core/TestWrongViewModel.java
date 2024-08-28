package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;

import java.util.List;

class TestWrongViewModel extends GenericViewModel {

    @UiComponentMapping(id = "staticLoginForm")
    private static Label staticLoginFormLabel;

    @SuppressWarnings("unused")
    private String usualField;

    @SuppressWarnings("unused")
    private AbstractUiComponent abstractComponent;

    @UiComponentMapping(id = "foo")
    private String stringMapping;

    @UiComponentMapping(id = "loginForm")
    private Label loginFormLabel;

    @UiComponentMapping("mapping1")
    private UiComponent rawComponent;

    @UiComponentMapping("div.box")
    private List<UiComponent> manyComponents;

    @UiComponentMapping("div.box")
    private List<Label> manyLabels;

    @UiComponentMapping("div.box")
    private List<?> manyValues;

    @UiComponentMapping("div.box")
    private List<Long> manyLongValues;

    @UiComponentMapping("div.box")
    private List<AbstractUiComponent> manyAbstractComponents;

    @UiComponentMapping("div.box")
    private final List<AbstractUiComponent> finalComponentList;

    public TestWrongViewModel(UiComponentStateManagerFactory stateManagerFactory) {
        setComponentStateManagerFactory(stateManagerFactory);
        finalComponentList = null;
    }
}
