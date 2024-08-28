package com.ericsson.cifwk.taf.ui;

import org.junit.Assert;
import org.junit.Test;

public class TafUiSwtNavigatorTest {
    @Test
    public void test() {
        DesktopNavigator swtNavigator = UiToolkit.newSwtNavigator(getLocalHost(), getLocalHostPort());
        DesktopWindow window = swtNavigator.getWindowByTitle("SWT Window");
        Assert.assertNotNull(window);
//        String windowDescriptor = window.getWindowDescriptor();
//        System.out.println(windowDescriptor);
    }

    private static String getLocalHost() {
        return "localhost";
    }

    private static int getLocalHostPort() {
        return 8080;
    }
}
