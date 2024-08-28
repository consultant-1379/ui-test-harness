package com.ericsson.cifwk.taf.ui.spi;

import org.junit.Test;

import java.util.ServiceLoader;

import static org.assertj.core.api.Assertions.assertThat;

public class UiGridPropertyServiceTest {

    @Test
    public void noAnyGridServiceSpiProvidersDetected() {
        ServiceLoader<UiGridService> gridServices = ServiceLoader.load(UiGridService.class);

        assertThat(gridServices).as("SPI Grid services providers").isEmpty();
    }
}
