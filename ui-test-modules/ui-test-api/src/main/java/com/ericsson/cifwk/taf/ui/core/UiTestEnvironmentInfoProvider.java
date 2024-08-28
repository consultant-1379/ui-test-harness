package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiGridService;
import com.ericsson.cifwk.taf.ui.spi.UiGridPropertyService;

import java.util.ServiceLoader;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Iterables.isEmpty;

public class UiTestEnvironmentInfoProvider implements UiGridService {

    UiGridService delegateService;

    public UiTestEnvironmentInfoProvider() {
        ServiceLoader<UiGridService> gridServices = ServiceLoader.load(UiGridService.class);
        if (isEmpty(gridServices)) {
            delegateService = new UiGridPropertyService();
        } else {
            delegateService = getLast(gridServices);
        }
    }

    @Override
    public boolean isGridDefined() {
        return delegateService.isGridDefined();
    }

    @Override
    public String getGridHost() {
        return delegateService.getGridHost();
    }

    @Override
    public Integer getGridPort() {
        return delegateService.getGridPort();
    }
}
