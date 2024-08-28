package com.ericsson.cifwk.taf.ui;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Method;

class RemoteInvoker {

    private Gson gson = new Gson();

    private RestClient restClient;

    RemoteInvoker(RestClient restClient) {
        this.restClient = restClient;
    }

    String invoke(String widgetId, Method method, Object[] arguments) throws IOException {
        SwtMethodInvocation swtMethodInvocation = getMethodInvocation(method.getName(), method.getParameterTypes(), arguments);
        return restClient.invoke(widgetId, swtMethodInvocation);
    }

    SwtMethodInvocation getMethodInvocation(String methodName, Class<?>[] argumentTypes, Object[] arguments) {
        Preconditions.checkArgument(argumentTypes.length == arguments.length);

        // collecting arguments classes names
        String[] argumentClasses = new String[argumentTypes.length];
        for (int i = 0; i < arguments.length; i++) {
            argumentClasses[i] = argumentTypes[i].getName();
            if (argumentTypes[i].isArray()) {
                arguments[i] = gson.toJson(arguments[i]);
            }
        }

        return new SwtMethodInvocation(methodName, argumentClasses, arguments);
    }
}
