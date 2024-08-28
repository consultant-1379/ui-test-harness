package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.ConditionWait;

public class ViewModelWaitForComponentTest extends AbstractWaitForComponentTest { // NOSONAR

    @Override
    protected ConditionWait getTestUnit() {
        return view;
    }
}
