package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTabImpl;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.google.common.collect.Sets;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SeleniumUiWindowProviderTest extends AbstractBrowserAwareTest {

    private SeleniumUiWindowProvider unit;

    @Before
    public void setUp() {
        SeleniumUiWindowProvider.waitForAnonymousWindowOpenTimeoutInMillis = 1;
        SeleniumUiWindowProvider.sleepStep = 1;

        BrowserType browserType = BrowserType.HEADLESS;
        WebDriver driver = WebDriverFactory.create(browserType);
        unit = new SeleniumUiWindowProvider(browserType, driver);
    }

    @After
    public void after() {
        try {
            unit.close();
        } catch (Exception e) {
            //ignore
        }
    }

    /**
     * Check if multiple tabs do not intersect, get opened in new tabs, and have distinct descriptors
     */
    @Test
    public void multipleTabsOpened() {
        BrowserTab firstTab = openTab("login.htm");
        assertEquals(firstTab.getWindowDescriptor(), unit.getCurrentWindowDescriptor());
        assertEquals(1, unit.getOpenedWindowDescriptors().size());

        BrowserTab secondTab = openTab("basic_ui_components.htm");
        assertEquals(secondTab.getWindowDescriptor(), unit.getCurrentWindowDescriptor());
        assertEquals(2, unit.getOpenedWindowDescriptors().size());

        Assert.assertThat(firstTab.getWindowDescriptor(), not(secondTab.getWindowDescriptor()));
        Assert.assertThat(firstTab.getCurrentUrl(), CoreMatchers.endsWith("login.htm"));
        Assert.assertThat(secondTab.getCurrentUrl(), CoreMatchers.endsWith("basic_ui_components.htm"));
        assertEquals(secondTab.getWindowDescriptor(), unit.getCurrentWindowDescriptor());
    }

    @Test
    public void closeWindow() {
        BrowserTab firstTab = openTab("login.htm");
        assertEquals(1, unit.getOpenedWindowDescriptors().size());

        BrowserTab secondTab = openTab("basic_ui_components.htm");
        assertEquals(2, unit.getOpenedWindowDescriptors().size());

        BrowserTab thirdTab = openTab("applications.htm");
        assertEquals(3, unit.getOpenedWindowDescriptors().size());
        assertEquals(thirdTab.getWindowDescriptor(), unit.getCurrentWindowDescriptor());

        unit.closeWindow(secondTab);

        assertEquals(2, unit.getOpenedWindowDescriptors().size());
        assertEquals(thirdTab.getWindowDescriptor(), unit.getCurrentWindowDescriptor());

        unit.closeWindow(thirdTab);

        assertEquals(1, unit.getOpenedWindowDescriptors().size());
        assertEquals(firstTab.getWindowDescriptor(), unit.getCurrentWindowDescriptor());
    }

    @Test
    public void synchronizeTabs() {
        WebDriver driverMock = mock(WebDriver.class);
        when(driverMock.switchTo()).thenReturn(mock(WebDriver.TargetLocator.class));

        unit = new SeleniumUiWindowProvider(BrowserType.FIREFOX, driverMock);
        unit = spy(unit);
        doNothing().when(unit).closeWindow(anyString());

        Set<String> openDescriptors = new HashSet<>();
        Map<String, BrowserTab> pageRegistry = new HashMap<>();
        Set<BrowserTab> newTabs = getSyncResult(openDescriptors, pageRegistry);
        assertEquals(0, newTabs.size());

        // 3 pages were added anonymously
        openDescriptors = Sets.newHashSet("a", "b", "c");
        pageRegistry.put("x", new BrowserTabImpl(null, null, null, "x", null));
        pageRegistry.put("y", new BrowserTabImpl(null, null, null, "y", null));

        newTabs = getSyncResult(openDescriptors, pageRegistry);
        assertEquals(3, newTabs.size());
        expectTabDescriptors(newTabs, openDescriptors);

        // Page was deleted anonymously
        openDescriptors = Sets.newHashSet("a", "b");
        newTabs = getSyncResult(openDescriptors, pageRegistry);
        assertEquals(0, newTabs.size());
        //Close Window call below.. is not invoked during Synchronization from now. So ruling this check out!!
        //verify(unit).closeWindow(eq("c"));
    }

    @Test
    public void isPageBlank() {
        unit = new SeleniumUiWindowProvider(BrowserType.FIREFOX, mock(WebDriver.class));
        Assert.assertTrue(unit.isPageBlank(""));
        Assert.assertTrue(unit.isPageBlank("about:blank"));
        Assert.assertFalse(unit.isPageBlank("http://foo.com"));

        unit = new SeleniumUiWindowProvider(BrowserType.CHROME, mock(WebDriver.class));
        Assert.assertTrue(unit.isPageBlank(""));
        Assert.assertTrue(unit.isPageBlank("data:blah"));
        Assert.assertFalse(unit.isPageBlank("http://foo.com"));

        unit = new SeleniumUiWindowProvider(BrowserType.PHANTOMJS, mock(WebDriver.class));
        Assert.assertTrue(unit.isPageBlank(""));
        Assert.assertTrue(unit.isPageBlank("about:blank"));
        Assert.assertFalse(unit.isPageBlank("http://foo.com"));

        WebDriver driver = mock(WebDriver.class);
        when(driver.getTitle()).thenReturn("WebDriver");
        WebElement bodyElement = mock(WebElement.class);
        when(bodyElement.getText()).thenReturn("initial start page for the WebDriver server");
        when(driver.findElement(eq(By.cssSelector("p#main")))).thenReturn(bodyElement);
        unit = new SeleniumUiWindowProvider(BrowserType.IEXPLORER, driver);
        Assert.assertTrue(unit.isPageBlank(""));
        Assert.assertTrue(unit.isPageBlank("about:blank"));
        Assert.assertTrue(unit.isPageBlank("http://foo.com"));
        when(driver.getTitle()).thenReturn("Custom");
        Assert.assertFalse(unit.isPageBlank("http://foo.com"));
    }

    @Test
    public void getSeleniumGridSessionId() {
        assertNull(unit.getSeleniumGridSessionId());

        RemoteWebDriver driver = mock(RemoteWebDriver.class);
        unit = new SeleniumUiWindowProvider(BrowserType.FIREFOX, driver);
        SessionId sessionId = mock(SessionId.class);
        when(driver.getSessionId()).thenReturn(sessionId);
        when(sessionId.toString()).thenReturn("sessionId");

        assertEquals("sessionId", unit.getSeleniumGridSessionId());
    }

    private void expectTabDescriptors(Set<BrowserTab> newTabs, Set<String> openDescriptors) {
        for (BrowserTab tab : newTabs) {
            String windowDescriptor = tab.getWindowDescriptor();
            Assert.assertTrue(openDescriptors.contains(windowDescriptor));
            openDescriptors.remove(windowDescriptor);
        }
        assertEquals(0, openDescriptors.size());
    }

    private Set<BrowserTab> getSyncResult(Set<String> openDescriptors,
                                          Map<String, BrowserTab> pageRegistry) {
        doReturn(openDescriptors).when(unit).getOpenedWindowDescriptors();
        doReturn(pageRegistry).when(unit).getOpenedPagesRegistry();

        return unit.synchronizeTabs();
    }

    private BrowserTab openTab(String url) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("url", findHtmlPage(url));
        List<BrowserTab> newTabs = unit.get(attributes);
        assertEquals(1, newTabs.size());

        return newTabs.get(0);
    }
}
