package com.ericsson.cifwk.taf.ui.core;

import com.google.common.collect.Sets;

import java.lang.reflect.Method;
import java.util.Set;

public class ProxyObjectCommons {

    /**
     * List of UiComponent object methods that should not be proxied
     */
    private static final Set<String> SKIP_PREPROCESSING_FOR_METHODS =
            Sets.newHashSet("notify", "notifyAll", "wait", "equals", "hashCode", "finalize", "getClass", "iterator",
                    "spliterator", "forEach");

    private ProxyObjectCommons() {
    }

    static boolean shouldSkipPreProcessingFor(Method method) {
        return shouldSkipPreProcessingFor(method.getName());
    }

    public static boolean shouldSkipPreProcessingFor(String methodName) {
        return SKIP_PREPROCESSING_FOR_METHODS.contains(methodName);
    }
}
