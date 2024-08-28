package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: SWT_CUSTOM_VIEW_MODEL1
public class BsimModel extends SwtViewModel {

    private ViewModel genericView;

    public BsimModel init(ViewModel genericView) {
        this.genericView = genericView;
        return this;
    }

    public void activateTab(String tabName) {
        String selector = String.format("{wrapperType = 'org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem', " +
                        "type = 'org.eclipse.swt.custom.CTabItem', " +
                        "mnemonicText='%s', " +
                        "initActions = ['activate']}",
                tabName);
        genericView.getViewComponent(selector);
    }
}
// END SNIPPET: SWT_CUSTOM_VIEW_MODEL1
