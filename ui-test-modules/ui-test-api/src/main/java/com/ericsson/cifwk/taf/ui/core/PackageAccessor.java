package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;

/**
 * This class provides access to protected classes in this package to other internal packages
 * (as Java modularization limitation workaround).
 *
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 09.08.2016
 */
public class PackageAccessor {

    private PackageAccessor(){}

    public static UiComponentStateManager getStateManagerFor(AbstractUiComponent component) {
        return component.getStateManager();
    }
}
