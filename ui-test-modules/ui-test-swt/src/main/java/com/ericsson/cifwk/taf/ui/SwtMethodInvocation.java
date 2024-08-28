package com.ericsson.cifwk.taf.ui;

class SwtMethodInvocation {

    private final String method;

    private final Object[] arguments;

    private final String[] argumentClasses;

    public SwtMethodInvocation(String method, String[] argumentClasses, Object[] arguments) {
        this.method = method;
        this.argumentClasses = argumentClasses;
        this.arguments = arguments;
    }

    public String getMethod() {
        return method;
    }

    public String[] getArgumentClasses() {
        return argumentClasses;
    }

    public Object[] getArguments() {
        return arguments;
    }

}
