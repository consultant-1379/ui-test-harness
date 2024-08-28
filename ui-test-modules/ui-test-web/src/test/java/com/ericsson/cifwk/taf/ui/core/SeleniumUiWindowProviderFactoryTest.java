package com.ericsson.cifwk.taf.ui.core;


import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.SeleniumGridAware;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class SeleniumUiWindowProviderFactoryTest {

    private UiWindowProvider<?> provider;

    @Before
    public void setUp() {
        SeleniumUiWindowProviderFactory unit = new SeleniumUiWindowProviderFactory();
        provider = unit.newInstance(new BrowserSetup(BrowserType.HEADLESS, null, null));
    }

    @Test
    public void proxyControlsClosedState() {
        Assert.assertFalse(provider.isClosed());
        provider.getCurrentWindowDescriptor();
        provider.close();
        assertTrue(provider.isClosed());
        try {
            provider.getCurrentWindowDescriptor();
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // OK
        }
    }

    @Test
    // Proxy correctness depends on isClosed() method
    public void isClosedMethodExists() throws Exception {
        Method method = SeleniumUiWindowProvider.class.getMethod(UiWindowProviderInvocationHandler.IS_CLOSED_METHOD);
        Assert.assertNotNull(method);
    }

    @Test
    public void proxyIsSeleniumGridAware() {
        assertTrue(provider instanceof SeleniumGridAware);
    }

}

