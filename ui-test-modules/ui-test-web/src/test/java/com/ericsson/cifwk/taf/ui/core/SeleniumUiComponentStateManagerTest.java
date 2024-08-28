package com.ericsson.cifwk.taf.ui.core;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumUiComponentStateManagerTest {
    private SeleniumUiComponentStateManager unit;

    @Before
    public void setUp() {
        unit = new SeleniumUiComponentStateManager(mock(WebDriver.class), mock(UiComponentStateManagerFactory.class), mock(WebElement.class), "mapping", new SeleniumUiMediator(mock(WebDriver.class)));
    }

    @Test
    public void localizeXpathExpression() {
        Assert.assertEquals("./author", unit.localizeXpathExpression("author"));
        Assert.assertEquals("./author", unit.localizeXpathExpression("./author"));
        Assert.assertEquals("./author", unit.localizeXpathExpression("/author"));
        Assert.assertEquals(".//author", unit.localizeXpathExpression("//author"));
    }
}
