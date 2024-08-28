package com.ericsson.cifwk.taf.ui.core;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetrySchemaTest {

    private RetrySchema retrySchema;

    @Before
    public void setUp() {
        retrySchema = spy(new RetrySchema(1000, new long[]{0, 100, 200, 500}));
        doNothing().when(retrySchema).sleep(anyLong());
    }

    @Test
    public void getNextPause() {
        assertEquals(0, retrySchema.getNextPause());
        assertEquals(100, retrySchema.getNextPause());
        assertEquals(200, retrySchema.getNextPause());
        assertEquals(500, retrySchema.getNextPause());
        assertEquals(500, retrySchema.getNextPause());
        assertEquals(500, retrySchema.getNextPause());
    }

    @Test
    public void sleep() {

        // zero millis don't trigger sleep
        retrySchema.sleep();
        verify(retrySchema, never()).sleep(anyLong());

        // non zero millis trigger sleep
        retrySchema.sleep();
        verify(retrySchema).sleep(anyLong());
    }

    @Test
    public void testToString() throws InterruptedException {
        assertTrue(retrySchema.toString().contains("<="));
        Thread.sleep(1100); // NOSONAR
        assertTrue(retrySchema.toString().contains(">"));
    }

    @Test
    public void shouldRetry() throws InterruptedException {
        assertTrue(retrySchema.shouldRetry());
        Thread.sleep(1100); // NOSONAR
        assertFalse(retrySchema.shouldRetry());
    }

    @Test
    public void shouldGiveUp() {

        when(retrySchema.shouldRetry()).thenReturn(true);
        assertEquals(false, retrySchema.shouldGiveUp());

        when(retrySchema.shouldRetry()).thenReturn(false);
        assertEquals(true, retrySchema.shouldGiveUp());
    }

    @Test
    public void publicConstructor() {
        RetrySchema retrySchema = new RetrySchema();
        HashSet<Long> pauses = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            pauses.add(retrySchema.getNextPause());
        }
        assertEquals(6, pauses.size());
    }
}
