package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: BUTTON_DRIVEN_COMPONENT
public class ButtonDrivenComponent extends AbstractUiComponent {

    // .........

    @UiComponentMapping("#textBoxFilledAfterPageLoaded")
    private TextBox textBoxFilledAfterPageLoaded;

    @UiComponentMapping("#actionButton")
    private Button actionButton;

    private void doAction() {
        waitUntil(new GenericPredicate() {
            @Override
            public boolean apply() {
                return textBoxFilledAfterPageLoaded.getText().length() > 0;
            }
        });
        actionButton.click();
    }
    // ........
    // END SNIPPET: BUTTON_DRIVEN_COMPONENT
}