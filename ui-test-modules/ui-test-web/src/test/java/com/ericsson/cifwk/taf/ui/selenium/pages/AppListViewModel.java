package com.ericsson.cifwk.taf.ui.selenium.pages;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

// START SNIPPET: APPLIST_VIEW_MODEL
public class AppListViewModel extends GenericViewModel {

    @UiComponentMapping("a.TorMegamenu-topBar-signOutButton")
    private Link signOutLink;

    @UiComponentMapping(selectorType = SelectorType.XPATH, selector = "//a[@title='Launch OSS Common Explorer']")
    private Link cexLink;

    public void clickSignOut() {
        signOutLink.click();
    }

    // ................
    // END SNIPPET: APPLIST_VIEW_MODEL

    @Override
    public boolean isCurrentView() {
        return signOutLink.isDisplayed();
    }

    public boolean isCEXLinkAvailable() {
        return cexLink.isDisplayed();
    }

    public void clickCEXLink() {
        cexLink.click();
    }

    public Link getSignOutLink() {
        return signOutLink;
    }

    public Link getCexLink() {
        return cexLink;
    }


}
