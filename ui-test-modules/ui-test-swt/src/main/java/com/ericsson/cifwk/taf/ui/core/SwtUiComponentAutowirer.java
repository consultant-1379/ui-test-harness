package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.google.common.base.Throwables;

import java.lang.reflect.Field;
import java.util.List;

public class SwtUiComponentAutowirer extends GenericUiComponentAutowirer {

    private ViewModel internalView;

    SwtUiComponentAutowirer(UiComponentStateManagerFactory stateManagerFactory) {
        super(stateManagerFactory);
    }

    void setInternalView(ViewModel internalView) {
        this.internalView = internalView;
    }

    @Override
    public void initialize(ViewModel view) {
        Class<? extends ViewModel> viewClass = view.getClass();
        Field[] fields = viewClass.getDeclaredFields();
        for (Field field : fields) {
            if (canMapValue(field)) {
                UiComponentMapping annotation = field.getAnnotation(UiComponentMapping.class);
                UiComponentMappingDetails mappingDetails = UiComponentMappingDetails.from(annotation);
                if (canFieldAcceptMultiple(field)) {
                    setFieldValueAsList(view, field, mappingDetails);
                } else {
                    setFieldValue(view, field, mappingDetails);
                }
            }
        }
    }

    void setFieldValue(ViewModel view, Field field, UiComponentMappingDetails mappingDetails) {
        field.setAccessible(true);
        Class<? extends UiComponent> fieldClass = getFieldClass(field);
        try {
            SelectorType selectorType = mappingDetails.getSelectorType();
            String selector = mappingDetails.getSelector();
            UiComponent viewComponent = internalView.getViewComponent(selectorType, selector, fieldClass);
            field.set(view, viewComponent);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    void setFieldValueAsList(ViewModel view, Field field, UiComponentMappingDetails mappingDetails) {
        field.setAccessible(true);
        try {
            Class<UiComponent> collectionComponentType = getCollectionUiComponentType(field);
            SelectorType selectorType = mappingDetails.getSelectorType();
            String selector = mappingDetails.getSelector();
            List<UiComponent> list = internalView.getViewComponents(selectorType, selector, collectionComponentType);
            field.set(view, list);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

}
