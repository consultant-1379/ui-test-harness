package com.ericsson.cifwk.taf.ui;

import com.google.gson.Gson;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

class WidgetMethodInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(WidgetMethodInterceptor.class);

    private WidgetProxyFactory proxyFactory;

    private RemoteInvoker invoker;

    private Gson gson = new Gson();

    private String widgetId;

    WidgetMethodInterceptor(WidgetProxyFactory proxyFactory, RemoteInvoker invoker, String widgetId) {
        this.proxyFactory = proxyFactory;
        this.invoker = invoker;
        this.widgetId = widgetId;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return methodProxy.invokeSuper(proxy, arguments);
        }

        try {
            String response = invoker.invoke(widgetId, method, arguments);
            SwtMethodResponse methodResponse = gson.fromJson(response, SwtMethodResponse.class);
            response = methodResponse.getValue();
            return parseResponse(method, response, methodResponse);
        } catch (Exception e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Unmapped response: ", e);
            }
            return null;
        }

    }

    private Object parseResponse(Method method, String response, SwtMethodResponse methodResponse) throws ClassNotFoundException {

        // trying to parse to type from signature
        Class<?> returnType = method.getReturnType();
        Object result = parse(response, returnType);
        if (result != null) {
            return result;
        }

        // trying to parse to type from response
        returnType = Class.forName(methodResponse.getType());
        result = parse(response, returnType);
        if (result != null) {
            return result;
        }

        String newWidgetId = methodResponse.getValue();
        // Failed to find widget
        if (newWidgetId == null) {
            return null;
        }

        return proxyFactory.create(returnType, newWidgetId);
    }

    private static Object parse(String value, Class<?> targetType) {
        try {
            return parseInternally(value, targetType);
        } catch (Exception e) {
            LOG.debug(String.format("Exception parsing string '%s' to type %s", value, targetType), e);
            return null;
        }
    }

    private static Object parseInternally(String value, Class<?> targetType) {
        if (isInteger(targetType)) {
            return Integer.valueOf(value);
        } else if (isLong(targetType)) {
            return Long.valueOf(value);
        } else if (isBoolean(targetType)) {
            return Boolean.valueOf(value);
        } else if (isString(targetType)) {
            return value;
        } else {
            return null;
        }
    }

    private static boolean isString(Class<?> targetType) {
        return String.class.equals(targetType);
    }

    private static boolean isBoolean(Class<?> targetType) {
        return Boolean.class.equals(targetType) || boolean.class.equals(targetType);
    }

    private static boolean isLong(Class<?> targetType) {
        return Long.class.equals(targetType) || long.class.equals(targetType);
    }

    private static boolean isInteger(Class<?> targetType) {
        return Integer.class.equals(targetType) || int.class.equals(targetType);
    }

}
