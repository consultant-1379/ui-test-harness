package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserCookie;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SeleniumUiMediatorTest {

    @Test
    public void retrieveByUiComponentMappingDetails() {
        SeleniumUiMediator unit = new SeleniumUiMediator(null);
        UiComponentMappingDetails details = mock(UiComponentMappingDetails.class);
        List<WebElement> elements = new ArrayList<>();
        final WebElement webElement = mock(WebElement.class);
        elements.add(webElement);
        unit = spy(unit);
        doReturn(elements).when(unit).findByCssSelector(anyString());
        doReturn(elements).when(unit).findByXpathExpression(anyString());

        when(details.getSelectorType()).thenReturn(SelectorType.CSS);
        when(details.getSelector()).thenReturn("a.TorMegamenu-topBar-signOutButton");
        unit.retrieve(details);
        verify(unit).findByCssSelector(anyString());

        when(details.getSelectorType()).thenReturn(SelectorType.XPATH);
        when(details.getSelector()).thenReturn("//a[@title='Launch OSS Common Explorer']");
        unit.retrieve(details);
        verify(unit).findByXpathExpression(anyString());
    }

    @Test
    public void getAllCookies() {
        WebDriver driverMock = mock(WebDriver.class, RETURNS_DEEP_STUBS);
        SeleniumUiMediator unit = new SeleniumUiMediator(driverMock);

        Cookie cookie1 = createCookie("cookie1", "cookie1Value", "cookie1Domain", "cookie1Path", null, false);
        Cookie cookie2 = createCookie("cookie2", "cookie2Value", "cookie2Domain", "cookie2Path", null, true);

        when(driverMock.manage().getCookies()).thenReturn(Sets.newHashSet(cookie1, cookie2));

        Set<BrowserCookie> cookies = unit.getAllCookies();
        Assert.assertEquals(2, cookies.size());
    }

    @Test
    public void processEvent_getAllCookies() {
        WebDriver driverMock = mock(WebDriver.class, RETURNS_DEEP_STUBS);
        SeleniumUiMediator unit = new SeleniumUiMediator(driverMock);

        Cookie cookie1 = createCookie("cookie1", "cookie1Value", "cookie1Domain", "cookie1Path", null, false);
        Cookie cookie2 = createCookie("cookie2", "cookie2Value", "cookie2Domain", "cookie2Path", null, true);

        when(driverMock.manage().getCookies()).thenReturn(Sets.newHashSet(cookie1, cookie2));

        final AtomicReference<Set<BrowserCookie>> container = new AtomicReference<>();
        unit.processEvent(UiEvent.WEB_GET_COOKIES, new UiComponentCallback() {
            @SuppressWarnings("unchecked")
            @Override
            public void onFinish(Object value) {
                container.set((Set<BrowserCookie>) value);
            }
        });
        Set<BrowserCookie> cookies = container.get();
        Assert.assertEquals(2, cookies.size());
    }

    @Test
    public void processEvent_getCookie() {
        WebDriver driverMock = mock(WebDriver.class, RETURNS_DEEP_STUBS);
        SeleniumUiMediator unit = new SeleniumUiMediator(driverMock);

        Cookie cookie1 = createCookie("cookie1", "cookie1Value", "cookie1Domain", "cookie1Path", null, false);

        when(driverMock.manage().getCookieNamed(anyString())).thenReturn(null);
        when(driverMock.manage().getCookieNamed(eq("cookie1"))).thenReturn(cookie1);

        BrowserCookie cookie = getCookie(unit, "cookie2");
        Assert.assertNull(cookie);

        cookie = getCookie(unit, "cookie1");
        Assert.assertEquals("cookie1", cookie.getName());
        Assert.assertEquals("cookie1Value", cookie.getValue());
        Assert.assertEquals("cookie1Domain", cookie.getDomain());
        Assert.assertNull(cookie.getExpiry());
        Assert.assertEquals(false, cookie.isSecure());
    }

    private BrowserCookie getCookie(SeleniumUiMediator unit, String cookieName) {
        final AtomicReference<BrowserCookie> container = new AtomicReference<>();
        Map<String, String> argumentMap = Maps.newHashMap();
        argumentMap.put(UiProperties.COOKIE_NAME, cookieName);
        unit.processEvent(UiEvent.WEB_GET_COOKIES, argumentMap, new UiComponentCallback() {
            @Override
            public void onFinish(Object value) {
                container.set((BrowserCookie) value);
            }
        });
        return container.get();
    }

    private Cookie createCookie(String name, String value, String domain, String path, Date expiry, boolean isSecure) {
        Cookie cookieMock = mock(Cookie.class);
        when(cookieMock.getName()).thenReturn(name);
        when(cookieMock.getValue()).thenReturn(value);
        when(cookieMock.getDomain()).thenReturn(domain);
        when(cookieMock.getPath()).thenReturn(path);
        when(cookieMock.getExpiry()).thenReturn(expiry);
        when(cookieMock.isSecure()).thenReturn(isSecure);

        return cookieMock;
    }

    @Test
    public void testConvertWebElements() {
        WebDriver driverMock = mock(WebDriver.class, Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
        SeleniumUiMediator unit = new SeleniumUiMediator(driverMock);
        WebElement webElementMock= mock(WebElement.class);
        List<WebElement> webElements = Lists.newArrayList();
        webElements.add(mock(WebElement.class));
        webElements.add(mock(WebElement.class));
        List<Object> objectsTypeSame = Lists.newArrayList();
        objectsTypeSame.add(1L);
        objectsTypeSame.add(2L);
        objectsTypeSame.add(3L);
        List<Object> objectsMixedType = Lists.newArrayList();
        objectsMixedType.add(1L);
        objectsMixedType.add(2.5);
        objectsMixedType.add(Boolean.TRUE);
        List<Object> objectsMixedTypeWithList = Lists.newArrayList();
        objectsMixedTypeWithList.add(1L);
        objectsMixedTypeWithList.add(2.5);
        objectsMixedTypeWithList.add(mock(WebElement.class));
        objectsMixedTypeWithList.add(webElements);
        objectsMixedTypeWithList.add(objectsMixedType);

        Assert.assertTrue(unit.convertWebElements(6L) instanceof Long);
        Assert.assertTrue(unit.convertWebElements(3.5) instanceof Double);
        Assert.assertTrue(unit.convertWebElements(Boolean.TRUE) instanceof Boolean);
        Assert.assertTrue(unit.convertWebElements(webElementMock) instanceof UiComponent);

        List<Object> objects = (List<Object>)unit.convertWebElements(webElements);
        Assert.assertTrue(objects.get(0) instanceof UiComponent);
        Assert.assertTrue(objects.get(1) instanceof UiComponent);
        objects = (List<Object>)unit.convertWebElements(objectsTypeSame);
        Assert.assertTrue(objects.get(0) instanceof Long);
        Assert.assertTrue(objects.get(1) instanceof Long);
        Assert.assertTrue(objects.get(2) instanceof Long);
        objects = (List<Object>)unit.convertWebElements(objectsMixedType);
        Assert.assertTrue(objects.get(0) instanceof Long);
        Assert.assertTrue(objects.get(1) instanceof Double);
        Assert.assertTrue(objects.get(2) instanceof Boolean);
        objects = (List<Object>)unit.convertWebElements(objectsMixedTypeWithList);
        Assert.assertTrue(objects.get(0) instanceof Long);
        Assert.assertTrue(objects.get(1) instanceof Double);
        Assert.assertTrue(objects.get(2) instanceof UiComponent);
        Assert.assertTrue(objects.get(3) instanceof List);
        Assert.assertTrue(((List)objects.get(3)).get(0) instanceof UiComponent);
        Assert.assertTrue(((List)objects.get(3)).get(1) instanceof UiComponent);
        Assert.assertTrue(objects.get(4) instanceof List);
        Assert.assertTrue(((List)objects.get(4)).get(0) instanceof Long);
        Assert.assertTrue(((List)objects.get(4)).get(1) instanceof Double);
        Assert.assertTrue(((List)objects.get(4)).get(2) instanceof Boolean);
        }
}
