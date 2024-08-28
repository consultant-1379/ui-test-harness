package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.DesktopWindowImpl;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;

public class DesktopWindowTest {
    @Test
    public void generateDescriptor() {
        DesktopWindow window = new DesktopWindowImpl(null, null, null);
        String descriptor1 = window.getWindowDescriptor();
        Assert.assertNotNull(descriptor1);

        window = new DesktopWindowImpl(null, null, null);
        String descriptor2 = window.getWindowDescriptor();
        Assert.assertNotNull(descriptor2);
        Assert.assertThat(descriptor2, not(descriptor1));
    }
}
