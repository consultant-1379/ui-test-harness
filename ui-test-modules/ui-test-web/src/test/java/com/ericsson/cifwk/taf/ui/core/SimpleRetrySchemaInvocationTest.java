package com.ericsson.cifwk.taf.ui.core;

import org.junit.Test;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SimpleRetrySchemaInvocationTest {

    private SimpleRetrySchemaInvocation unit = new SimpleRetrySchemaInvocation();

    @Test(expected = RuntimeException.class)
    public void performWithPropagatedException() throws Exception {
        Callable callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException());
        try {
            unit.perform(callable);
        } finally {
            verify(callable).call();
        }
    }

    @Test(expected = ElementNotVisibleException.class)
    public void performWithTolerableException() throws Exception {
        Callable callable = mock(Callable.class);
        when(callable.call()).thenThrow(new ElementNotVisibleException(""));
        try {
            unit.perform(callable);
        } finally {
            verify(callable, atLeast(2)).call();
        }
    }

    @Test
    public void performWithTolerableException_happyPath() throws Exception {
        Callable callable = mock(Callable.class);
        when(callable.call()).thenThrow(new ElementNotVisibleException("")).thenReturn(null);
        assertNull(unit.perform(callable));
        verify(callable, times(2)).call();
    }

    @Test
    public void testShouldPropagateException() throws Exception {
        RetrySchema retrySchemaThatGivesUp = getRetrySchemaMock(true);
        RetrySchema retrySchemaThatHoldsOn = getRetrySchemaMock(false);

        assertTrue(unit.shouldPropagateException(new StaleElementReferenceException(""), retrySchemaThatGivesUp));
        assertTrue(unit.shouldPropagateException(new RuntimeException(), retrySchemaThatHoldsOn));
        // Wrong exception type
        assertTrue(unit.shouldPropagateException(new RuntimeException(), retrySchemaThatGivesUp));
        // Exception is OK but retry schema holds on
        assertFalse(unit.shouldPropagateException(new StaleElementReferenceException(""), retrySchemaThatHoldsOn));
    }

    private RetrySchema getRetrySchemaMock(boolean giveUp) {
        RetrySchema retrySchema = mock(RetrySchema.class);
        when(retrySchema.shouldGiveUp()).thenReturn(giveUp);
        return retrySchema;
    }
}