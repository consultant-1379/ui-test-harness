package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import org.junit.Assert;
import org.junit.Test;


public class UiWindowProviderRegistryTest {

    @Test
    public void get() {
        UiWindowProviderRegistry unit = new UiWindowProviderRegistry();
        UiWindowProvider<?> provider1 = unit.getBrowserWindowProvider(new BrowserSetup(BrowserType.HEADLESS, null, null));
        UiWindowProvider<?> provider2 = unit.getBrowserWindowProvider(new BrowserSetup(BrowserType.HEADLESS, null, null));
        Assert.assertNotSame(provider1, provider2);
    }

    @Test
    public void registerCreatedProviders() {
        UiWindowProviderRegistry unit = new UiWindowProviderRegistry();
        createAndVerifyNWindowProviders(unit, 2);
    }

    @Test
    public void closeCreatedProviders() {
        UiWindowProviderRegistry unit = new UiWindowProviderRegistry();
        createAndVerifyNWindowProviders(unit, 2);

        unit.closeAllCreatedProviders(UiWindowType.BROWSER);

        Assert.assertEquals(unit.getCreatedProviders(UiWindowType.DESKTOP_SIKULI).size(), 0);
        Assert.assertEquals(unit.getCreatedProviders(UiWindowType.BROWSER).size(), 0);
    }

    @Test
    public void closeCreatedProviders_concurrent() {
        final int threadCount = 5;
        final UiWindowProviderRegistry unit = new UiWindowProviderRegistry();

        createAndVerifyNWindowProviders(unit, 100);

        Runnable windowCloser = new Runnable() {
            @Override
            public void run() {
                unit.closeAllCreatedProviders();
            }
        };

        runWithNThreads(windowCloser, threadCount);

        Assert.assertEquals(unit.getCreatedProviders(UiWindowType.DESKTOP_SIKULI).size(), 0);
        Assert.assertEquals(unit.getCreatedProviders(UiWindowType.BROWSER).size(), 0);
    }

    private void createAndVerifyNWindowProviders(UiWindowProviderRegistry unit, int count) {
        for (int i = 0; i < count; i++) {
            unit.getBrowserWindowProvider(new BrowserSetup(BrowserType.HEADLESS, null, null));
        }
        Assert.assertEquals(unit.getCreatedProviders(UiWindowType.DESKTOP_SIKULI).size(), 0);
        Assert.assertEquals(unit.getCreatedProviders(UiWindowType.BROWSER).size(), count);
    }

    private void runWithNThreads(Runnable function, int threadCount) {
        if (threadCount == 1) {
            function.run();
            return;
        }

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(function);
            threads[i].start();
        }

        try {
            for (int i = 0; i < threadCount; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
