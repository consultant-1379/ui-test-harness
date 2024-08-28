package com.ericsson.cifwk.taf.ui.core;

/**
 * UI component selector
 *
 * @deprecated
 */
@Deprecated
public class UiComponentSelector {
    private final SelectorType selectorType;
    private final String selectorExpression;

    public UiComponentSelector(SelectorType selectorType, String selectorExpression) {
        this.selectorType = selectorType;
        this.selectorExpression = selectorExpression;
    }

    public SelectorType getSelectorType() {
        return selectorType;
    }

    public String getSelectorExpression() {
        return selectorExpression;
    }

    @Override
    public String toString() {
        return "UiComponentSelector [selectorType=" + selectorType + ", selectorExpression=" + selectorExpression + "]";
    }
}
