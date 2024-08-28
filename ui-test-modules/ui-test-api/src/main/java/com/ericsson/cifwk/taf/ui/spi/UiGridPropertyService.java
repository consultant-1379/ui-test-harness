package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.DefaultSettings;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Default implementation of {@link UiGridService} that takes grid information
 * from property files found on the classpath.
 *
 * @see DefaultSettings
 */
public class UiGridPropertyService implements UiGridService {

    @Override
    public boolean isGridDefined() {
        String gridHost = DefaultSettings.getDefaultGridIp();
        int gridPort = DefaultSettings.getDefaultGridPort();
        return !isNullOrEmpty(gridHost) && gridPort > 0;
    }

    @Override
    public String getGridHost() {
        return DefaultSettings.getDefaultGridIp();
    }

    @Override
    public Integer getGridPort() {
        return DefaultSettings.getDefaultGridPort();
    }
}
