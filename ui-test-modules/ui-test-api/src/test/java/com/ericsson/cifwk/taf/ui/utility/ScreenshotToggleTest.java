package com.ericsson.cifwk.taf.ui.utility;

import org.junit.After;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mihails Volkovs mihails.volkovs@ericsson.com
 *         Date: 06.01.2017
 */
public class ScreenshotToggleTest {

    @After
    public void tearDown() {
        ScreenshotToggle.enable();
    }

    @Test
    public void defaultValue() {
        assertThat(ScreenshotToggle.isEnabled()).isEqualTo(true);
    }

    @Test
    public void disableEnable() {
        assertThat(ScreenshotToggle.isEnabled()).isEqualTo(true);
        ScreenshotToggle.disable();
        assertThat(ScreenshotToggle.isEnabled()).isEqualTo(false);
        ScreenshotToggle.enable();
        assertThat(ScreenshotToggle.isEnabled()).isEqualTo(true);
    }

}