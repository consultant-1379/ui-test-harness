package com.ericsson.cifwk.taf.ui.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

/**
 * POJO that keeps the settings of the UI component mapping.
 */
public class UiComponentMappingDetails {

    private String id;
    private String name;
    private String selector;
    private SelectorType selectorType;
    private boolean isGlobalScope;
    private boolean isStaticList;

    public UiComponentMappingDetails() {
        // default constructor for POJO
    }

    public UiComponentMappingDetails(SelectorType selectorType, String selector) {
        this.selectorType = selectorType;
        this.selector = selector;
    }

    @VisibleForTesting
    protected UiComponentMappingDetails(String id, String name, String selector, SelectorType selectorType) {
        this.id = id;
        this.name = name;
        this.selector = selector;
        this.selectorType = selectorType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public SelectorType getSelectorType() {
        return selectorType;
    }

    public void setSelectorType(SelectorType selectorType) {
        this.selectorType = selectorType;
    }

    public boolean isGlobalScope() {
        return isGlobalScope;
    }

    public void setGlobalScope(boolean globalScope) {
        isGlobalScope = globalScope;
    }

    public boolean isStaticList() {
        return isStaticList;
    }

    public void setStaticList(boolean staticList) {
        isStaticList = staticList;
    }

    public static UiComponentMappingDetails from(UiComponentMapping mappingAnnotation) {
        UiComponentMappingDetails result = new UiComponentMappingDetails();
        result.setId(!Strings.isNullOrEmpty(mappingAnnotation.id()) ? mappingAnnotation.id() : null);
        result.setName(!Strings.isNullOrEmpty(mappingAnnotation.name()) ? mappingAnnotation.name() : null);
        result.setSelector(!Strings.isNullOrEmpty(mappingAnnotation.value()) ? mappingAnnotation.value() : null);
        if (!Strings.isNullOrEmpty(mappingAnnotation.selector())) {
            result.setSelector(mappingAnnotation.selector());
        }
        result.setSelectorType(mappingAnnotation.selectorType());

        return result;
    }

    @Override
    public String toString() {
        String isGlobal = isGlobalScope ? ", isGlobalScope=true" : "";
        String isStatic = isStaticList ? ", isStaticList=true" : "";
        return "UiComponentMappingDetails [id=" + id + ", name=" + name
                + ", selector=" + selector + ", selectorType=" + selectorType + isGlobal + isStatic + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((selector == null) ? 0 : selector.hashCode());
        result = prime * result + ((selectorType == null) ? 0 : selectorType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UiComponentMappingDetails other = (UiComponentMappingDetails) obj;
        if (selector == null) {
            if (other.selector != null)
                return false;
        } else if (!selector.equals(other.selector))
            return false;
        if (selectorType != other.selectorType)
            return false;
        return true;
    }
}
