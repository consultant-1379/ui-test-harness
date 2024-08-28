package com.ericsson.cifwk.taf.ui;

import com.google.gson.Gson;

public class SwtSelector {

    private static final Gson gson = new Gson();

    private String type;

    private int index = 0;

    private boolean nativeWidget = false;

    private String text;

    private String mnemonicText;

    private String tooltip;

    private String label;

    private String wrapperType;

    private String[] initActions;

    private String container;

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    public String getContainer() {
        return container;
    }

    void setContainer(String container) {
        this.container = container;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    public boolean isNativeWidget() {
        return nativeWidget;
    }

    public void setNativeWidget(boolean nativeWidget) {
        this.nativeWidget = nativeWidget;
    }

    public String getMnemonicText() {
        return mnemonicText;
    }

    public void setMnemonicText(String mnemonicText) {
        this.mnemonicText = mnemonicText;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWrapperType() {
        return wrapperType;
    }

    public void setWrapperType(String wrapperType) {
        this.wrapperType = wrapperType;
    }

    public String[] getInitActions() {
        return initActions;
    }

    public void setInitActions(String[] initActions) {
        this.initActions = initActions;
    }

}
