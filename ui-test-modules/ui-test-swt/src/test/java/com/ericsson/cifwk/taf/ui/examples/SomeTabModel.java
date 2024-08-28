package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Table;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: SWT_CUSTOM_VIEW_MODEL2
public class SomeTabModel extends SwtViewModel {

    @UiComponentMapping("{index = 0}")
    private Table configurationTable;

    @UiComponentMapping("New Planned Configuration:hover")
    private Button newConfigurationButton;

    public boolean configurationExists(String configurationName) {
        return configurationTable.getRowIndex(configurationName, "Name") >= 0;
    }

    public void selectConfiguration(String configurationName) {
        configurationTable.select(configurationTable.getRowIndex(configurationName, "Name"));
    }

    public void addConfiguration() {
        newConfigurationButton.click();
    }

}
// END SNIPPET: SWT_CUSTOM_VIEW_MODEL2
