package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.DesktopNavigator;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 20/04/2017
 */
public class UiDesktopCodeExamples {

    public void initDesktopNavigator() {
        // START SNIPPET: INIT_DESKTOP_NAVIGATOR -->
        DesktopNavigator desktopNavigator = UiToolkit.newDesktopNavigator(true);
        // END SNIPPET: INIT_DESKTOP_NAVIGATOR -->
    }

    public void useGenericView() {
        // START SNIPPET: USE_GENERIC_VIEW -->
        final String SURNAME_ENTRY_FIELD_IMG = "/img/surname_entry.png";
        // .................
        DesktopNavigator desktopNavigator = UiToolkit.newDesktopNavigator();
        DesktopWindow window = desktopNavigator.getCurrentWindow();
        ViewModel view = window.getGenericView();
        TextBox usernameTextBox = view.getTextBox("/img/textbox.png");
        usernameTextBox.setText("mac1202");
        if (view.hasComponent(SURNAME_ENTRY_FIELD_IMG)) {
            view.getTextBox(SURNAME_ENTRY_FIELD_IMG).setText("Lindgren");
        }
        // END SNIPPET: USE_GENERIC_VIEW -->
    }

    public void usingSikuliBrowser() {
        // START SNIPPET: INIT_SIKULI_BROWSER -->
        BrowserSetup.Builder capability = BrowserSetup.build().withType(BrowserType.FIREFOX);
        Browser browser = UiToolkit.newBrowserWithImageRecognition(capability);
        // END SNIPPET: INIT_SIKULI_BROWSER -->
        // START SNIPPET: OPEN_SIKULI_BROWSER_WINDOW -->
        BrowserTab currentWindow = browser.getCurrentWindow();
        currentWindow.open("http://www.ericsson.com/vapp/1/node1/vnc");
        // END SNIPPET: OPEN_SIKULI_BROWSER_WINDOW -->
        // START SNIPPET: OPEN_SIKULI_BROWSER_DESKTOP_NAV -->
        String imageBundle = "images-resource-in-test-classpath/subfolders-are-allowed";
        DesktopNavigator sikuliNavigator = UiToolkit.newDesktopNavigator(browser, imageBundle);
        // END SNIPPET: OPEN_SIKULI_BROWSER_DESKTOP_NAV -->
    }

    public void commonApi() {
        // START SNIPPET: DESKTOP_NAV_COMMON_API -->
        DesktopNavigator desktopNavigator = null;
        DesktopWindow currentWindow = desktopNavigator.getCurrentWindow();
        ViewModel screen = currentWindow.getGenericView();
        // searching for UI component and clicking
        UiComponent portfolioLink = screen.getViewComponent("home-page/neural-network-selector.png");
        portfolioLink.click();

        // cleaning inputs and typing
        TextBox aiEditor = screen.getTextBox("home-page/ai-chat-input.png");
        aiEditor.clear();
        aiEditor.setText("Hello AI!");

        // key shortcuts supported
        aiEditor.sendKeys(toCharSequence(java.awt.event.KeyEvent.VK_A,
                java.awt.event.KeyEvent.VK_CONTROL));
        aiEditor.sendKeys(toCharSequence(java.awt.event.KeyEvent.VK_BACK_SPACE));
        aiEditor.sendKeys(toCharSequence(java.awt.event.KeyEvent.VK_ENTER));
        // END SNIPPET: DESKTOP_NAV_COMMON_API -->
    }

    private CharSequence toCharSequence(int... keys) {
        return null;
    }

    private class SampleViewModel extends GenericViewModel {

        public void getComponent() {
            ViewModel viewModel = null;
            // START SNIPPET: DESKTOP_VM_MAPPING_FULL -->
            viewModel.getButton(SelectorType.FILE_PATH, "/images/button.png");
            // END SNIPPET: DESKTOP_VM_MAPPING_FULL -->
            // START SNIPPET: DESKTOP_VM_MAPPING_DEFAULT -->
            viewModel.getButton("/images/button.png");
            // END SNIPPET: DESKTOP_VM_MAPPING_DEFAULT -->
        }
    }

}