package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.SikuliUiWindowProvider;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DesktopNavigatorTest {

    @Test
    public void elementPresenceAndHighlight() {
        boolean highlightCurrentSelection = true;
        SikuliUiWindowProvider sikuliUiWindowProvider = new SikuliUiWindowProvider(highlightCurrentSelection);

        DesktopNavigator desktopClient = new DesktopNavigator(sikuliUiWindowProvider);
        DesktopWindow currentWindow = desktopClient.getCurrentWindow();
        ViewModel genericView = currentWindow.getGenericView();

        UiComponent viewComponent = genericView.getViewComponent("automation/freemind.png");
        Assert.assertTrue(viewComponent.exists());
    }

    @Test
    public void waitingForElement() {
        // Highlighting the current selections is OFF
        SikuliUiWindowProvider sikuliUiWindowProvider = new SikuliUiWindowProvider(false);

        DesktopNavigator desktopClient = new DesktopNavigator(sikuliUiWindowProvider);
        DesktopWindow currentWindow = desktopClient.getCurrentWindow();

        currentWindow.waitUntilComponentIsDisplayed("automation/freemind.png", 5000);
    }
}
