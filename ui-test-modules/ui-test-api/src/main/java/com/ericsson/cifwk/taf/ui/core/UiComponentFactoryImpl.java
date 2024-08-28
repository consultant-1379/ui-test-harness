package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class UiComponentFactoryImpl implements UiComponentFactory {

    private final UiComponentAutowirer autowirer;

    public UiComponentFactoryImpl(UiComponentAutowirer autowirer) {
        this.autowirer = autowirer;
    }

    @Override
    public <T extends UiComponent> T instantiateComponent(final UiComponentStateManager stateManager, Class<T> componentClass) {
        return instantiateComponent(new Supplier<UiComponentStateManager>() {
            @Override
            public UiComponentStateManager get() {
                return stateManager;
            }
        }, componentClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends UiComponent> T instantiateComponent(Supplier<UiComponentStateManager> stateManagerSupplier,
                                                          Class<T> componentClass) {
        ProxyFactory factory = new ProxyFactory();
        try {
            factory.setSuperclass(getTargetClass(componentClass));
            factory.setFilter(new MethodFilter() {
                public boolean isHandled(Method m) {
                    // ignore basic Object methods, excluding toString() which is overridden in AbstractUiComponent
                    return !ProxyObjectCommons.shouldSkipPreProcessingFor(m.getName());
                }
            });
            Class<T> clazz = factory.createClass();
            MethodHandler handler = new LazyUiComponentMethodHandler(stateManagerSupplier, autowirer);
            T instance = clazz.newInstance();
            ((ProxyObject) instance).setHandler(handler);
            return instance;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }

    }

    @VisibleForTesting
    @SuppressWarnings("unchecked")
    <T extends UiComponent> Class<T> getTargetClass(Class<T> componentClass) throws ClassNotFoundException {
        Class<T> targetClass;
        if (componentClass.isInterface()) {
            if (componentClass.equals(UiComponent.class)) {
                targetClass = (Class<T>) GenericUiComponent.class;
            } else {
                Package defaultImplPackage = UiComponent.class.getPackage();
                String defaultImplPackageName = defaultImplPackage.getName();
                try {
                    targetClass = (Class<T>) Class.forName(String.format("%s.%sImpl", defaultImplPackageName, componentClass.getSimpleName()));
                } catch (ClassNotFoundException ignored) { // NOSONAR
                    targetClass = (Class<T>) Class.forName(componentClass.getName() + "Impl");
                }
            }
        } else {
            targetClass = componentClass;
        }
        return targetClass;
    }

    @Override
    public <T extends UiComponent> List<T> instantiateComponentList(final List<UiComponentStateManager> stateManagerList,
                                                                    Class<T> componentClass, boolean isStaticList) {
        return instantiateComponentList(new Supplier<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> get() {
                return stateManagerList;
            }
        }, componentClass, isStaticList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends UiComponent> List<T> instantiateComponentList(Supplier<List<UiComponentStateManager>> stateManagerListSupplier,
                                                                    Class<T> componentClass, boolean isStaticList) {
        // Couldn't use Javassist with List because of problems in Java 8 - see https://github.com/jboss-javassist/javassist/issues/45
        return (List<T>) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{List.class},
                new LazyUiComponentListMethodHandler(stateManagerListSupplier, this, componentClass, isStaticList));
    }
}
