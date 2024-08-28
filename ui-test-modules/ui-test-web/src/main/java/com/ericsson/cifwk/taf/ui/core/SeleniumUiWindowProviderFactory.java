package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.SeleniumGridAware;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProviderFactory;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Proxy;

public class SeleniumUiWindowProviderFactory implements UiWindowProviderFactory {

    @Override
    public UiWindowProvider<BrowserTab> newInstance(Object... initParameters) {
        if (ArrayUtils.isEmpty(initParameters)) {
            throw new IllegalArgumentException("Cannot instantiate SeleniumUiWindowProvider - need information about the browser to create");
        }

        BrowserSetup browserSetup = (BrowserSetup) initParameters[0];
        SeleniumUiWindowProvider provider = new SeleniumUiWindowProvider(browserSetup);

        return (UiWindowProvider<BrowserTab>) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UiWindowProvider.class, SeleniumGridAware.class},
                new UiWindowProviderInvocationHandler(provider));
    }

    @Override
    public UiWindowType getType() {
        return UiWindowType.BROWSER;
    }

}
