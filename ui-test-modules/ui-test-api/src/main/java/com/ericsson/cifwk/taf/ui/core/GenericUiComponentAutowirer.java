package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.core.supplier.CompositeComponentStateManagerSupplier;
import com.ericsson.cifwk.taf.ui.core.supplier.CompositeComponentStateManagersSupplier;
import com.ericsson.cifwk.taf.ui.core.supplier.ViewModelStateManagerSupplier;
import com.ericsson.cifwk.taf.ui.core.supplier.ViewModelStateManagersSupplier;
import com.ericsson.cifwk.taf.ui.sdk.UiComponentContainer;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import javassist.util.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic implementation of annotation-driven autowiring of the elements in
 * {@link com.ericsson.cifwk.taf.ui.sdk.ViewModel} instances.
 */
class GenericUiComponentAutowirer implements UiComponentAutowirer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericUiComponentAutowirer.class);

    private final UiComponentStateManagerFactory stateManagerFactory;

    private final UiComponentFactory componentFactory;

    GenericUiComponentAutowirer(UiComponentStateManagerFactory stateManagerFactory) {
        this.stateManagerFactory = stateManagerFactory;
        this.componentFactory = new UiComponentFactoryImpl(this);
    }

    @Override
    public void initialize(ViewModel view) {
        Field[] fields = getFields(view);
        for (Field field : fields) {
            if (canMapValue(field)) {
                initFieldInViewModel(field, view);
            }
        }
    }

    @Override
    public void initialize(AbstractUiComponent container) {
        Field[] fields = getFields(container);
        for (Field field : fields) {
            if (canMapValue(field)) {
                initFieldInCompositeComponent(field, container);
            }
        }
    }

    @VisibleForTesting
    protected Field[] getFields(UiComponentContainer container) {
        Class<? extends UiComponentContainer> containerClass = container.getClass();
        boolean isProxy = ProxyFactory.isProxyClass(containerClass);
        if (isProxy) {
            containerClass = containerClass.getSuperclass().asSubclass(UiComponentContainer.class);
        }
        return getAllFields(containerClass);
    }

    @VisibleForTesting
    Field[] getAllFields(Class klass) {
        List<Field> fields = new ArrayList<>();
        Class superClass = klass.getSuperclass();
        fields.addAll(Arrays.asList(klass.getDeclaredFields()));
        if (superClass != null && UiComponentContainer.class.isAssignableFrom(superClass)) {
            fields.addAll(Arrays.asList(getAllFields(superClass)));
        }
        return fields.toArray(new Field[] {});
    }

    private void initFieldInViewModel(Field field, ViewModel view) {
        UiComponentMappingDetails mappingDetails = getMappingDetails(field);

        Object fieldValue;
        if (canFieldAcceptMultiple(field)) {
            Supplier<List<UiComponentStateManager>> smSupplier = new ViewModelStateManagersSupplier(stateManagerFactory, mappingDetails);
            fieldValue = instantiateComponents(field, smSupplier, mappingDetails.isStaticList());
        } else {
            Supplier<UiComponentStateManager> smSupplier = new ViewModelStateManagerSupplier(stateManagerFactory, mappingDetails);
            fieldValue = instantiateComponent(field, smSupplier);
        }

        setFieldValue(view, field, fieldValue);
    }

    private void initFieldInCompositeComponent(Field field, AbstractUiComponent container) {
        UiComponentMappingDetails mappingDetails = getMappingDetails(field);

        Object fieldValue;
        if (canFieldAcceptMultiple(field)) {
            Supplier<List<UiComponentStateManager>> smSupplier = new CompositeComponentStateManagersSupplier(stateManagerFactory, container, mappingDetails);
            fieldValue = instantiateComponents(field, smSupplier, mappingDetails.isStaticList());
        } else {
            Supplier<UiComponentStateManager> smSupplier = new CompositeComponentStateManagerSupplier(stateManagerFactory, container, mappingDetails);
            fieldValue = instantiateComponent(field, smSupplier);
        }

        setFieldValue(container, field, fieldValue);
    }

    private static UiComponentMappingDetails getMappingDetails(Field field) {
        UiComponentMapping annotation = field.getAnnotation(UiComponentMapping.class);
        final UiComponentMappingDetails mappingDetails = UiComponentMappingDetails.from(annotation);

        boolean isGlobalScope = field.getAnnotation(GlobalScope.class) != null;
        mappingDetails.setGlobalScope(isGlobalScope);

        boolean isStatic = field.getAnnotation(StaticList.class) != null;
        mappingDetails.setStaticList(isStatic);

        return mappingDetails;
    }

    @SuppressWarnings("unchecked")
    Class<? extends UiComponent> getFieldClass(Field field) {
        Class<?> type = field.getType();
        if (type.equals(UiComponent.class)) {
            return GenericUiComponent.class;
        }

        return (Class<? extends UiComponent>) type;
    }

    @VisibleForTesting
    protected void setFieldValue(Object instance, Field field, Object fieldValue) {
        field.setAccessible(true);
        try {
            field.set(instance, fieldValue);
        } catch (Exception e) {
            throw new RuntimeException(e); // NOSONAR
        }
    }

    @VisibleForTesting
    protected List<UiComponent> instantiateComponents(Field field, Supplier<List<UiComponentStateManager>> stateManagerListSupplier, boolean isStaticList) {
        Class<UiComponent> collectionComponentType = getCollectionUiComponentType(field);
        return componentFactory.instantiateComponentList(stateManagerListSupplier, collectionComponentType, isStaticList);
    }

    @VisibleForTesting
    protected UiComponent instantiateComponent(Field field, Supplier<UiComponentStateManager> smSupplier) {
        Class<? extends UiComponent> fieldClass = getFieldClass(field);
        return componentFactory.instantiateComponent(smSupplier, fieldClass);
    }

    boolean canMapValue(Field field) {
        if (!isMappingDefined(field)) {
            return false;
        }
        field.setAccessible(true);
        try {
            if (!isFieldTypeOk(field) || !areFieldModifiersOk(field)) {
                return false;
            }
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e); // NOSONAR
        }

        return true;
    }

    boolean isFieldTypeOk(Field field) {
        Class<?> fieldClass = field.getType();
        if (isUiComponentTypeOk(fieldClass)) {
            return true;
        }

        // A collection of UiComponents is OK, too
        return canFieldAcceptMultiple(field);
    }

    boolean canFieldAcceptMultiple(Field field) {
        Class<?> fieldClass = field.getType();
        if (List.class.isAssignableFrom(fieldClass)) {
            Class<UiComponent> collectionComponentType = getCollectionUiComponentType(field);
            if (collectionComponentType == null || !isUiComponentTypeOk(collectionComponentType)) {
                String error = String.format("Problem with the field '%s': "
                                + "TAF UI can map only the List of UiComponents or non-abstract subclasses of UiComponent - no other types are allowed",
                        field.toGenericString());
                LOGGER.error(error);
                return false;
            }
            return true;
        }

        return false;
    }

    @VisibleForTesting
    boolean isUiComponentTypeOk(Class<?> componentType) {
        return UiComponent.class.isAssignableFrom(componentType) &&
                (componentType.isInterface() || !Modifier.isAbstract(componentType.getModifiers()));
    }

    @SuppressWarnings("unchecked")
    Class<UiComponent> getCollectionUiComponentType(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        if (genericType == null) {
            return null;
        }

        Type[] actualTypeArguments = genericType.getActualTypeArguments();
        try {
            Type actualTypeArgument = actualTypeArguments[0];
            Class<?> collectionContentType = (Class<?>) actualTypeArgument;
            if (UiComponent.class.isAssignableFrom(collectionContentType)) {
                return (Class<UiComponent>) collectionContentType;
            }
        } catch (ClassCastException ignored) {
            LOGGER.debug("Unexpected collection item type", ignored);
            return null;
        }
        return null;
    }

    boolean isMappingDefined(Field field) {
        return field.getAnnotation(UiComponentMapping.class) != null;
    }

    boolean areFieldModifiersOk(Field field) {
        return !(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()));
    }
}
