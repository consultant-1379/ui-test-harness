package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.core.UiWindowType;

public interface UiWindowProviderFactory {

    UiWindowProvider newInstance(Object... initParameters);

    UiWindowType getType();

}
