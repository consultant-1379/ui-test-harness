package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.ConditionWait;

public class BrowserTabWaitForComponentTest extends AbstractWaitForComponentTest { // NOSONAR

    @Override
    protected ConditionWait getTestUnit() {
        return browserTab;
    }
}
