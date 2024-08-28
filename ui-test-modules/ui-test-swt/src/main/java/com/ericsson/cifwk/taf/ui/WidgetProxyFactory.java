package com.ericsson.cifwk.taf.ui;

import com.google.common.base.Preconditions;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import org.objenesis.ObjenesisStd;

import java.util.HashMap;
import java.util.Map;

class WidgetProxyFactory {

    private Map<Object, String> widgetIds = new HashMap<>();

    private RemoteInvoker invoker;

    private static final NamingPolicy NAMING_POLICY_FOR_CLASSES_IN_SIGNED_PACKAGES = new DefaultNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }
    };

    WidgetProxyFactory(RemoteInvoker invoker) {
        this.invoker = invoker;
    }

    public void setInvoker(RemoteInvoker invoker) {
        this.invoker = invoker;
    }

    @SuppressWarnings("unchecked")
    <T> T create(Class<T> clazz, String widgetId) {

        // creating proxy class
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setUseFactory(true);
        enhancer.setCallbackType(WidgetMethodInterceptor.class);
        if (clazz.getSigners() != null) {
            enhancer.setNamingPolicy(NAMING_POLICY_FOR_CLASSES_IN_SIGNED_PACKAGES);
        }
        Class<?> proxyClass = enhancer.createClass();

        // instantiating class without constructor call
        ObjenesisStd objenesis = new ObjenesisStd();
        Factory proxy = (Factory) objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[]{new WidgetMethodInterceptor(this, invoker, widgetId)});
        T widget = (T) proxy;

        widgetIds.put(widget, widgetId);
        return widget;
    }

    <T> T castWidgetTo(Object widget, Class<T> clazz) {
        String errorMessage = "Given widget has not been created by instance of current " + WidgetProxyFactory.class.getSimpleName();
        Preconditions.checkArgument(Enhancer.isEnhanced(widget.getClass()), errorMessage);
        String widgetId = widgetIds.get(widget);
        return create(clazz, widgetId);
    }

    void reset() {
        widgetIds.clear();
    }

}
