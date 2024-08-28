package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import org.junit.Assert;


public class UiWindowProviderRegistryTest {

    @org.junit.Test
    public void get() {
        UiWindowProviderRegistry unit = new UiWindowProviderRegistry();
        UiWindowProvider provider1 = unit.getDesktopWindowProvider(false);
        UiWindowProvider provider2 = unit.getDesktopWindowProvider(false);
        Assert.assertNotSame(provider1, provider2);
    }
}
