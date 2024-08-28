package com.ericsson.cifwk.taf.ui.automation.impl;

import com.ericsson.cifwk.taf.ui.automation.AutomationLocation;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenLocation;

class DefaultAutomationLocation implements AutomationLocation {

    protected final ScreenLocation location;

    DefaultAutomationLocation(ScreenLocation location) {
        this.location = location;
    }

    public ScreenLocation getLocation() {
        return location;
    }

    @Override
    public AutomationLocation relativeLocation(int dx, int dy) {
        ScreenLocation childLocation = Relative.to(this.location).right(dx).below(dy).getScreenLocation();
        return new DefaultAutomationLocation(childLocation);
    }

}
