package com.ericsson.cifwk.taf.ui.core;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProxyObjectCommonsTest {

    @Test
    public void testShouldSkipPreProcessingForMethodName() throws Exception {
        assertTrue(ProxyObjectCommons.shouldSkipPreProcessingFor("getClass"));
        assertFalse(ProxyObjectCommons.shouldSkipPreProcessingFor("toString"));
    }

}