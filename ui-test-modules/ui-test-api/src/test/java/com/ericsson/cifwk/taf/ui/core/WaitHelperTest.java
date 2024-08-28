package com.ericsson.cifwk.taf.ui.core;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.ericsson.cifwk.taf.ui.spi.ScreenshotProvider.NO_SCREENSHOT_AVAILABLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class WaitHelperTest {

    private WaitHelper unit = new WaitHelper(NO_SCREENSHOT_AVAILABLE);

    private Predicate<UiComponent> componentExistsPredicate = new Predicate<UiComponent>() {
        @Override
        public boolean apply(UiComponent o) {
            return o.exists();
        }
    };

    @Before
    public void setUp() {
        unit = spy(unit);
        doNothing().when(unit).delay();
    }

    @Test
    public void shouldHandleWaitTimeoutForComponent() throws Exception {
        componentExistsPredicate = spy(componentExistsPredicate);
        UiComponent object = mock(UiComponent.class);
        when(object.toString()).thenReturn("div");
        when(object.exists()).thenReturn(false);
        try {
            unit.waitUntil(object, componentExistsPredicate, 100);
            fail(WaitTimedOutException.class + " expected");
        } catch (Exception e) {
            assertEquals("Timed out while waiting for component 'div', was waiting for 100 millis", e.getMessage());
            verify(componentExistsPredicate, atLeastOnce()).apply(object);
        }
    }

    @Test
    public void shouldWaitForComponent() throws Exception {
        componentExistsPredicate = spy(componentExistsPredicate);
        UiComponent object = mock(UiComponent.class);
        when(object.toString()).thenReturn("div");
        doReturn(false).doReturn(false).doReturn(true).when(object).exists();
        unit.waitUntil(object, componentExistsPredicate, 100);
        verify(componentExistsPredicate, atLeastOnce()).apply(object);
    }

    @Test
    public void shouldHandleWaitTimeoutForGenericCondition() throws Exception {
        GenericPredicate predicate = new GenericPredicate() {
            @Override
            public boolean apply() {
                return false;
            }
        };
        predicate = spy(predicate);
        try {
            unit.waitUntil(predicate, 100);
            fail(WaitTimedOutException.class + " expected");
        } catch (Exception e) {
            assertEquals("Timed out while waiting for condition, was waiting for 100 millis", e.getMessage());
            verify(predicate, atLeastOnce()).apply();
        }
    }

    @Test
    public void shouldWaitForGenericCondition() throws Exception {
        final AtomicInteger integerRef = new AtomicInteger();
        GenericPredicate predicate = new GenericPredicate() {
            @Override
            public boolean apply() {
                return integerRef.addAndGet(1) > 100;
            }
        };
        predicate = spy(predicate);
        unit.waitUntil(predicate, 100);
        verify(predicate, atLeastOnce()).apply();
    }
}