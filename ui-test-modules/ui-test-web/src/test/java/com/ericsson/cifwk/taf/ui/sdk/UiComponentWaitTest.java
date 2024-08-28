package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.AbstractBrowserAwareTest;
import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.WaitTimedOutException;
import org.junit.Before;
import org.junit.Test;

public class UiComponentWaitTest extends AbstractBrowserAwareTest {

    private TextBox emptyTextBox;
    private UiComponent observer;

    @Before
    public void setUp() {
        BasicComponentsView view = openComponentsView();
        emptyTextBox = view.getTextBox("#emptyTextBox");
        observer = view.getTextBox("#enabledInFutureButton");
        UiToolkit.setDefaultWaitTimeout(10_000);
    }

    @Test(timeout = 3000)
    public void shouldWaitForComponentStateChange() {
        observer.waitUntil(getFilledTextBoxPredicate());
    }

    @Test(expected = WaitTimedOutException.class)
    public void shouldUseDefaultTimeoutInWaits() {
        UiToolkit.setDefaultWaitTimeout(10);
        observer.waitUntil(getFilledTextBoxPredicate());
    }

    private GenericPredicate getFilledTextBoxPredicate() {
        return new GenericPredicate() {
            @Override
            public boolean apply() {
                return emptyTextBox.getText().length() > 0;
            }
        };
    }

}
