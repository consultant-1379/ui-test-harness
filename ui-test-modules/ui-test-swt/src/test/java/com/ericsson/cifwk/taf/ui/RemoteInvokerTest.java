package com.ericsson.cifwk.taf.ui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemoteInvokerTest {

    private RemoteInvoker invoker;

    @Before
    public void setUp() {
        invoker = new RemoteInvoker(null);
    }

    @Test
    public void getMethodInvocation() {
        SwtMethodInvocation methodInvocation = invoker.getMethodInvocation("methodName", new Class[]{String.class, String[].class}, new Object[]{
                "first arg", new String[]{"var arg 1", "var arg 2"}});
        assertEquals("methodName", methodInvocation.getMethod());
        assertEquals(String.class.getName(), methodInvocation.getArgumentClasses()[0]);
        assertEquals(String[].class.getName(), methodInvocation.getArgumentClasses()[1]);
        assertEquals("first arg", methodInvocation.getArguments()[0]);
        assertEquals("[\"var arg 1\",\"var arg 2\"]", methodInvocation.getArguments()[1]);
    }

}
