package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class UiWindowProviderRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(UiWindowProviderRegistry.class);

    private final Map<UiWindowType, UiWindowProviderFactory> providerFactories;

    private final Map<UiWindowType, Set<UiWindowProvider>> createdProviders;

    public UiWindowProviderRegistry() {
        this(ServiceLoader.load(UiWindowProviderFactory.class));
    }

    private UiWindowProviderRegistry(Iterable<UiWindowProviderFactory> providers) {
        this.providerFactories = new EnumMap<>(UiWindowType.class);
        this.createdProviders = Collections.synchronizedMap(new HashMap<UiWindowType, Set<UiWindowProvider>>());
        for (UiWindowProviderFactory providerFactory : providers) {
            this.providerFactories.put(providerFactory.getType(), providerFactory);
        }
    }

    private synchronized void registerWindowProvider(UiWindowType type, UiWindowProvider newInstance) {
        Set<UiWindowProvider> createdProvidersList = createdProviders.get(type);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding new window provider of type " + type + ", currently " + ((createdProvidersList == null) ? 0 : createdProvidersList.size())
                    + " are registered");
        }
        if (createdProvidersList == null) {
            createdProvidersList = Collections.synchronizedSet(new HashSet<UiWindowProvider>());
            createdProviders.put(type, createdProvidersList);
        }
        createdProvidersList.add(newInstance);
    }

    @SuppressWarnings("unchecked")
    public UiWindowProvider<BrowserTab> getBrowserWindowProvider(BrowserSetup browserSetup) {
        UiWindowProviderFactory providerFactory = getWindowProviderFactory(UiWindowType.BROWSER);
        UiWindowProvider newInstance = providerFactory.newInstance(browserSetup);
        registerWindowProvider(UiWindowType.BROWSER, newInstance);

        return newInstance;
    }

    @SuppressWarnings("unchecked")
    public UiWindowProvider<DesktopWindow> getDesktopWindowProvider(boolean highlightOn) {
        UiWindowProviderFactory providerFactory = getWindowProviderFactory(UiWindowType.DESKTOP_SIKULI);
        UiWindowProvider newInstance = providerFactory.newInstance(highlightOn);
        registerWindowProvider(UiWindowType.DESKTOP_SIKULI, newInstance);

        return newInstance;
    }

    private UiWindowProviderFactory getWindowProviderFactory(UiWindowType type) {
        UiWindowProviderFactory providerFactory = providerFactories.get(type);
        if (providerFactory == null) {
            throw new IllegalArgumentException("No Window provider for UI type: " + type);
        }
        return providerFactory;
    }

    public synchronized void closeAllCreatedProviders() {
        Set<UiWindowType> windowTypes = createdProviders.keySet();
        for (UiWindowType windowType : windowTypes) {
            if (LOGGER.isInfoEnabled()) {
                int providerCount = getCreatedProviders(windowType).size();
                if (providerCount > 0) {
                    LOGGER.info("{} providers are opened for window type {} - closing them", providerCount, windowType);
                }
            }
            closeAllCreatedProviders(windowType);
        }
    }

    public synchronized int getCreatedProviderCount() {
        Set<UiWindowType> windowTypes = createdProviders.keySet();
        int count = 0;
        for (UiWindowType windowType : windowTypes) {
            int providerCount = getCreatedProviders(windowType).size();
            count += providerCount;
        }
        return count;
    }

    public synchronized int getActiveBrowserCount() {
        int activeBrowsers = 0;
        Set<UiWindowProvider> browserProviders = getCreatedProviders(UiWindowType.BROWSER);
        for (UiWindowProvider provider : browserProviders) {
            if (!provider.isClosed()) {
                ++activeBrowsers;
            }
        }
        return activeBrowsers;
    }

    synchronized void closeAllCreatedProviders(UiWindowType type) {
        Set<UiWindowProvider> allProviders = getCreatedProviders(type);
        for (UiWindowProvider provider : allProviders) {
            if (!provider.isClosed()) {
                provider.close();
            }
        }
        allProviders.clear();
    }

    synchronized Set<UiWindowProvider> getCreatedProviders(UiWindowType type) {
        Set<UiWindowProvider> result = createdProviders.get(type);
        return (result == null) ? new HashSet<UiWindowProvider>() : result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UiWindowProviderRegistry that = (UiWindowProviderRegistry) o;
        return factoriesEquals(that) && providersEquals(that);
    }

    private boolean factoriesEquals(UiWindowProviderRegistry that) {
        if (this.providerFactories == null) {
            return that.providerFactories == null;
        } else {
            return this.providerFactories.equals(that.providerFactories);
        }
    }

    private boolean providersEquals(UiWindowProviderRegistry that) {
        if (createdProviders == null) {
            return that.createdProviders == null;
        } else {
            return this.createdProviders.equals(that.createdProviders);
        }
    }

    @Override
    public int hashCode() {
        int result = providerFactories != null ? providerFactories.hashCode() : 0;
        result = 31 * result + (createdProviders != null ? createdProviders.hashCode() : 0);
        return result;
    }
}
