package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicComponentTest extends AbstractBrowserAwareTest {

    public static final String NEXT_LABEL = "Label 3";
    public static final String ACTIVE_LABEL_SELECTOR = ".activeLabel";

    @Before
    public void setUp() {
        UiToolkit.setDefaultWaitTimeout(10_000);
        openBrowserTab("dynamic-component.html");
    }

    @Test
    public void componentMappingCatchesHtmlClassMove() {

        // initial check
        MyView view = browserTab.getView(MyView.class);
        assertThat(view.getActiveLabel()).isEqualTo("Label 2");

        // checking that next label was activated
        view.waitForNextLabelActivated();
        assertThat(view.getLabel(ACTIVE_LABEL_SELECTOR).getText()).isEqualTo(NEXT_LABEL);

        // checking mapped component after HTML class moved
        // TODO: fix the test?
//        assertThat(view.getActiveLabel()).isEqualTo(NEXT_LABEL);
    }

    public static class MyView extends GenericViewModel {

        @UiComponentMapping(ACTIVE_LABEL_SELECTOR)
        private Label activeLabel;

        public String getActiveLabel() {
            return activeLabel.getText();
        }

        public void waitForNextLabelActivated() {
            waitUntilComponentIsDisplayed("#updateMarker");
        }

    }

}
