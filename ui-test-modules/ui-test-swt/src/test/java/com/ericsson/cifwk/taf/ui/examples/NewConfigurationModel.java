package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: SWT_CUSTOM_VIEW_MODEL3
public class NewConfigurationModel extends SwtViewModel {

    @UiComponentMapping("#0")
    private TextBox nameField;

    @UiComponentMapping("Open new Planned Configuration")
    private CheckBox openPlannedConfig;

    @UiComponentMapping("OK")
    private Button okButton;

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setOpenNewConfig() {
        openPlannedConfig.select();
    }

    public void save() {
        okButton.click();
    }

}
// END SNIPPET: SWT_CUSTOM_VIEW_MODEL3
