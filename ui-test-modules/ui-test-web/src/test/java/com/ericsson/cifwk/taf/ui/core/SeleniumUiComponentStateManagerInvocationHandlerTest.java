package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.DefaultSettings;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Supplier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeleniumUiComponentStateManagerInvocationHandlerTest {

    @Rule
    public final TestRule restoreSystemProperties = new RestoreSystemProperties();

    @Mock
    UiComponentStateManager stateManager;

    @Mock
    Supplier<UiComponentStateManager> stateManagerSupplier;

    @InjectMocks
    SeleniumUiComponentStateManagerInvocationHandler unit;

    private InvocationTargetException staleException = new InvocationTargetException(new StaleElementReferenceException("foo"));

    private InvocationTargetException notVisibleException = new InvocationTargetException(new ElementNotVisibleException("bar"));

    @Before
    public void setUp() {
        unit = spy(unit);
        doNothing().when(stateManager).clearSelection();
        when(stateManagerSupplier.get()).thenReturn(stateManager);

        System.setProperty(DefaultSettings.UI_RETRY_SCHEMA_TIMEOUT_PROPERTY, "600");
    }

    @Test
    public void handleMultipleStaleEltExceptionsInRow() throws Exception {
        assertAccidentalExceptions(staleException);
    }

    @Test(expected = StaleElementReferenceException.class)
    public void handleMultipleStaleEltExceptionsInRow_timeout() throws Exception {
        assertConstantExceptions(staleException);
    }

    @Test
    public void handleMultipleElementNotVisibleExceptionsInRow() throws Exception {
        assertAccidentalExceptions(this.notVisibleException);
    }

    @Test(expected = UiComponentNotVisibleException.class)
    public void handleMultipleElementNotVisibleExceptionsInRow_timeout() throws Exception {
        assertConstantExceptions(this.notVisibleException);
    }

    @Test
    public void getElementName() throws Throwable {
        verifyElementName("MappingBySelector[selector=#textBox, selectorType=CSS]", "#textBox");
        verifyElementName("MappingById[id=loginNoticeOk]",  "loginNoticeOk");
        verifyElementName("MappingBySelector[selector=//*[text()='qwerty'], selectorType=XPATH]", "//*[text()='qwerty']");
    }

    @Test
    public void getScreenshotName() throws Throwable {
        verifyScreenshotName("MappingBySelector[selector=#textBox, selectorType=CSS]", new UiComponentNotFoundException(null, null),
                "#textBox(UiComponentNotFoundException)");
        verifyScreenshotName("MappingById[id=loginNoticeOk]", new RuntimeException(),
                "loginNoticeOk(RuntimeException)");
        verifyScreenshotName("MappingBySelector[selector=//*[text()='qwerty'], selectorType=XPATH]", new RuntimeException(),
                "//*[text()='qwerty'](RuntimeException)");
        verifyScreenshotName("MappingBySelector[selector=.ebNotification_color_green .ebNotification-label]", null,
                ".ebNotification...ification-label");
        verifyScreenshotName("MappingBySelector[selector=.ebNotification_color_green .ebNotification-label]", new ElementNotVisibleException(null),
                ".ebNotification...ification-label(ElementNotVisibleException)");
    }

    @Test
    public void shouldNotCreateScreenShotsOnUninitialized() throws Throwable {
        when(stateManager.isDisplayed()).thenReturn(true);
        when(stateManagerSupplier.get()).thenThrow(new UiComponentNotFoundException("mappingInfo"));
        try {
            unit.invoke(null, UiComponentStateManager.class.getMethod("getComponentName"), new Object[]{});
            fail("Exception expected");
        } catch (Throwable throwable) {
            verify(unit, never()).createScreenshot(any(Throwable.class));
        }
    }

    @Test
    public void shouldCreateScreenShotsOnFailureOnStale() throws Throwable {
        verifyOnlyOneScreenShotIsDone(new StaleElementReferenceException("mapping"));
    }

    @Test
    public void shouldCreateScreenShotsOnFailureOnNotVisible() throws Throwable {
        verifyOnlyOneScreenShotIsDone(new ElementNotVisibleException("mapping"));
    }

    @Test
    public void shouldCreateScreenShotsOnNoSuchElement() throws Throwable {
        verifyOnlyOneScreenShotIsDone(new NoSuchElementException("mapping"));
    }

    @Test
    public void shouldCreateScreenShotsOnUnhandledException() throws Throwable {
        verifyOnlyOneScreenShotIsDone(new RuntimeException("mapping"));
    }

    private void verifyOnlyOneScreenShotIsDone(Throwable exception) throws Throwable {
        when(stateManager.isSelected()).thenReturn(true).thenThrow(exception);
        when(stateManagerSupplier.get()).thenReturn(stateManager);
        Method method = UiComponentStateManager.class.getMethod("isSelected");
        unit.invoke(null, method, new Object[]{});
        try {
            unit.invoke(null, method, new Object[]{});
            fail("Exception expected");
        } catch (Throwable throwable) {
            verify(unit, times(1)).createScreenshot(any(Throwable.class));
        }
    }

    private void verifyScreenshotName(String mappingInfo, Exception exception, String expectedName) {
        assertEquals(expectedName, unit.getScreenshotName(mappingInfo, exception));
    }

    private void verifyElementName(String mappingInfo, String expectedName) {
        assertEquals(expectedName, unit.getElementName(mappingInfo));
    }

    private void assertAccidentalExceptions(InvocationTargetException knownException) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Throw exception 2 times, then work well
        doThrow(knownException).doThrow(knownException).doReturn(stateManager).when(unit).invoke(any(Method.class), any(Object[].class));
        unit.handleInvocationTargetException(knownException, UiComponentStateManager.class.getMethod("clearSelection"), new Object[]{});
        verify(unit, times(3)).invoke(any(Method.class), any(Object[].class));
    }

    private void assertConstantExceptions(InvocationTargetException knownException) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Throw exceptions all the time
        doThrow(knownException).when(unit).invoke(any(Method.class), any(Object[].class));
        unit.handleInvocationTargetException(knownException, UiComponentStateManager.class.getMethod("clearSelection"), new Object[]{});
    }

}
