package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserOS;
import com.ericsson.cifwk.taf.ui.BrowserSetup;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.core.extension.GridFileDownloadRequest;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.FileSelector;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import org.junit.Assert;
import org.junit.Ignore;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringEndsWith.endsWith;


/**
 * Needed for documentation purposes.
 *
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 18/04/2017
 */
@Ignore
public class UiWebCodeExamples {

    public void initializeBrowser() {
        // START SNIPPET: INSTANTIATE_BROWSERS
        Browser firefoxBrowser = UiToolkit.newBrowser(BrowserType.FIREFOX);
        Browser headlessBrowser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        // END SNIPPET: INSTANTIATE_BROWSERS

        // START SNIPPET: INSTANTIATE_BROWSERS_WITH_PARAMS
        Browser winBrowser = UiToolkit.newBrowser();
        // Default settings used - see next chapter
        Browser winBrowser2 = UiToolkit.newBrowser(BrowserType.FIREFOX);
        Browser winBrowser3 = UiToolkit.newBrowser(BrowserType.FIREFOX, BrowserOS.WINDOWS);
        Browser linuxBrowser = UiToolkit.newBrowser(BrowserType.CHROME, BrowserOS.LINUX, "25.3");
        // END SNIPPET: INSTANTIATE_BROWSERS_WITH_PARAMS
    }

    public void switchWindows() {
        Browser browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        // START SNIPPET: SWITCH_WINDOWS
        BrowserTab ericssonBrowserTab = browser.open("http://www.ericsson.se");
        // Opens new tab and focuses on it
        BrowserTab irishTimesBrowserTab = browser.open("http://www.irishtimes.com");
        // Switches back to the first tab
        browser.switchWindow(ericssonBrowserTab);
        // END SNIPPET: SWITCH_WINDOWS

        // START SNIPPET: SWITCH_WINDOWS_CHECKING_URLS
        BrowserTab appsTab = browser.open("http://www.ericsson.se/applications.htm");
        Assert.assertThat(appsTab.getCurrentUrl(), endsWith("applications.htm"));
        Assert.assertThat(browser.getCurrentWindow().getCurrentUrl(), endsWith("applications.htm"));

        BrowserTab loginTab = browser.open("http://www.ericsson.se/login.htm");
        Assert.assertThat(loginTab.getCurrentUrl(), endsWith("login.htm"));
        Assert.assertThat(appsTab.getCurrentUrl(), endsWith("applications.htm"));
        Assert.assertThat(browser.getCurrentWindow().getCurrentUrl(), endsWith("login.htm"));
        // END SNIPPET: SWITCH_WINDOWS_CHECKING_URLS
    }

    public void openMultipleWindows() {
        // START SNIPPET: OPEN_MULTIPLE_WINDOWS
        Browser browser = UiToolkit.newBrowser(BrowserType.FIREFOX);
        // open first browser tab
        BrowserTab essBrowserTab = browser.open("http://www.ericsson.se");
        // opened tab count is 1
        assertThat(browser.getOpenedWindowsAmount(), is(1));
        // opens second tab and focuses on it
        BrowserTab irishTimesBrowserTab = browser.open("http://www.irishtimes.com");
        // increases the number of open pages
        assertThat(browser.getOpenedWindowsAmount(), is(2));
        // Switches back to the first tab
        browser.switchWindow(essBrowserTab);
        // END SNIPPET: OPEN_MULTIPLE_WINDOWS

        // START SNIPPET: USING_WINDOWS_COLLECTION
        // Get new window using collection of windows
        List<BrowserTab> allOpenWindows = browser.getAllOpenTabs();
        BrowserTab newTab = null;
        for (BrowserTab tab : allOpenWindows) {
            ViewModel genericView = tab.getGenericView();
            if (genericView.hasComponent("#popUpHeading")) {
                newTab = tab;
            }
        }

        if (newTab == null) {
            throw new RuntimeException("Pop-up not opened");
        }

        MyPopupView popupView = newTab.getView(MyPopupView.class);
        // END SNIPPET: USING_WINDOWS_COLLECTION
    }

    public void openInTheSameWindow() {
        // START SNIPPET: OPEN_IN_THE_SAME_WINDOW
        Browser browser = UiToolkit.newBrowser(BrowserType.FIREFOX);
        // open first browser tab
        BrowserTab browserTab = browser.open("http://www.ericsson.se");
        // opened tab count is 1
        assertThat(browser.getOpenedWindowsAmount(), is(1));
        // opens new URL in same tab
        browserTab.open("http://www.irishtimes.com");
        // amount of opened tabs isn't changed
        assertThat(browser.getOpenedWindowsAmount(), is(1));
        // END SNIPPET: OPEN_IN_THE_SAME_WINDOW
    }

    public void viewModelTypes() {
        BrowserTab browserTab = null;
        // START SNIPPET: VIEW_MODEL_TYPES
        // Generic view model
        ViewModel genericViewModel = browserTab.getGenericView();
        // or
        // Custom view model
        MyViewModel customViewModel = browserTab.getView(MyViewModel.class);
        // END SNIPPET: VIEW_MODEL_TYPES
    }

    public void genericViewModelFormSubmission() {
        BrowserTab browserTab = null;
        // START SNIPPET: GENERIC_VIEW_MODEL_FORM_SUBMISSION
        ViewModel genericView = browserTab.getGenericView();
        TextBox nameEntry = genericView.getTextBox("#name");
        TextBox surnameEntry = genericView.getTextBox("#surname");
        UiComponent submitButton = genericView.getViewComponent(SelectorType.XPATH, "/div[@class='container']/span[1]", UiComponent.class);
        nameEntry.setText("Anders");
        surnameEntry.setText("Rasmussen");
        submitButton.click();

        // Need to wait until confirmation page appears
        genericView.waitUntilComponentIsDisplayed("#applicationSuccessful", 5000);
        Link logoutLink = genericView.getViewComponent("div.logout > a.topRightLink", Link.class);
        logoutLink.click();
        // END SNIPPET: GENERIC_VIEW_MODEL_FORM_SUBMISSION
    }

    public void breakdownIntoCustomViews() {
        BrowserTab browserTab = null;
        // START SNIPPET: BREAKDOWN_INTO_CUSTOM_VIEWS
        MainSettingsView settingsView = browserTab.getView(MainSettingsView.class);
        NotificationSettingsView notificationSettingsView = browserTab.getView(NotificationSettingsView.class);
        TroubleTicketView troubleTicketView = browserTab.getView(TroubleTicketView.class);
        // END SNIPPET: BREAKDOWN_INTO_CUSTOM_VIEWS
    }

    public void desiredCaps() {
        // START SNIPPET: DEFINE_PROXY
        // For example you can define proxy
        BrowserSetup.Builder setup = BrowserSetup.build()
                .withType(BrowserType.CHROME)
                .withOS(BrowserOS.LINUX)
                .withSize(BrowserSetup.Resolution.RESOLUTION_1024x768)
                .withCapability(
                        CapabilityType.PROXY,
                        new Proxy().setHttpProxy("www-proxy.ericsson.se:8080")
                );
        Browser browser = UiToolkit.newBrowser(setup);
        // END SNIPPET: DEFINE_PROXY
    }

    public void disableProxy() {
        // START SNIPPET: DISABLE_PROXY
        BrowserSetup.Builder setup = BrowserSetup.build()
                .withCapability(
                        CapabilityType.PROXY,
                        new Proxy().setProxyType(Proxy.ProxyType.DIRECT));
        // END SNIPPET: DISABLE_PROXY
    }

    public void getChildElements() {
        BrowserTab browserTab = null;
        // START SNIPPET: GET_CHILD_ELEMENTS
        ViewModel genericView = browserTab.getGenericView();
        UiComponent list = genericView.getViewComponent(SelectorType.XPATH, "/div[@class='container']/ul[1]", UiComponent.class);
        List<UiComponent> children = list.getChildren();
        for(UiComponent child: children) {
            TextBox textBox = child.as(TextBox.class);
            textBox.clear();
        }
        // END SNIPPET: GET_CHILD_ELEMENTS
    }

    public void uploadFile() {
        // START SNIPPET: UPLOAD_FILE
        Browser browser = UiToolkit.newBrowser();
        BrowserTab tab = browser.open("http://myurl/");
        ViewModel view = tab.getGenericView();
        FileSelector fileSelector = view.getFileSelector("#fileInput");
        fileSelector.select(new File("/tmp/myfile.zip"));
        Button button = view.getButton("#submitBtn");
        button.click();
        // END SNIPPET: UPLOAD_FILE
    }

    public void downloadFile() {
        // START SNIPPET: DOWNLOAD_FILE_PROFILE
        //define firefox profile which automatically downloads pfd files
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.download.folderList", 2); //download happens to default directory
        firefoxProfile.setPreference("browser.download.dir", "/tmp/selenium-downloads"); //default directory
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf"); //do not show dialog for save

        //settings disable pdf reader in browser, so pdfs are downloaded automatically
        firefoxProfile.setPreference("pdfjs.disabled", true);
        firefoxProfile.setPreference("plugin.scan.plid.all", false);
        firefoxProfile.setPreference("plugin.scan.Acrobat", "99.0");
        // END SNIPPET: DOWNLOAD_FILE_PROFILE

        // START SNIPPET: DOWNLOAD_FILE_BROWSER_SESSION
        BrowserSetup.Builder builder = new BrowserSetup.Builder()
                .withType(BrowserType.FIREFOX)
                .withOS(BrowserOS.ANY)
                .withCapability(FirefoxDriver.PROFILE, firefoxProfile);
        // END SNIPPET: DOWNLOAD_FILE_BROWSER_SESSION

        // START SNIPPET: DOWNLOAD_FILE_REQUEST
        Browser browser = null;
        String pathToFile = null;
        GridFileDownloadRequest remoteFileDownloadRequest = new GridFileDownloadRequest(browser);
        File yourFile = remoteFileDownloadRequest.download(pathToFile); //e.g. /tmp/selenium-downloads/mypdf.pdf
        // END SNIPPET: DOWNLOAD_FILE_REQUEST
    }

    public void defineResolution() {
        // START SNIPPET: REDEFINE_RESOLUTION
        // You can create an instance of appropriate Web browser specifying the required resolution:
        int width = 800;
        int height = 600;
        Browser browser = UiToolkit.newBrowser(BrowserType.FIREFOX, width, height);
        // or use predefined resolutions
        Browser anotherBrowser = UiToolkit.newBrowser(BrowserType.FIREFOX, BrowserSetup.Resolution.RESOLUTION_800x600);

        // You can change the browser screen size
        browser.setSize(1024,768);
        // or use predefined resolutions
        browser.setSize(BrowserSetup.Resolution.RESOLUTION_1024x768);
        // END SNIPPET: REDEFINE_RESOLUTION
    }

    public void actionChains() {
        BrowserTab browserTab = null;
        {
            // START SNIPPET: ACTION_CHAINS1
            UiActions actionChain = browserTab.newActionChain();
            // END SNIPPET: ACTION_CHAINS1
        }
        UiComponent row = null;
        UiComponent row2 = null;
        {
            // START SNIPPET: ACTION_CHAINS2
            UiActions actionChain = browserTab.newActionChain()
                    .click(row)
                    .keyDown(Keys.CONTROL)
                    .click(row2)
                    .keyUp(Keys.CONTROL);
            actionChain.perform();
            // END SNIPPET: ACTION_CHAINS2
        }
        UiComponent button = null;
        {
            UiActions actions = browserTab.newActionChain();

            actions.keyDown(Keys.CONTROL);
            actions.click(button);
            actions.perform();
        }
    }

    private class MainSettingsView extends GenericViewModel { }

    private class NotificationSettingsView extends GenericViewModel { }

    private class TroubleTicketView extends GenericViewModel { }

    private class MyPopupView extends GenericViewModel { }

    private class MyViewModel extends GenericViewModel {
        // START SNIPPET: COMPONENT_MAPPING_BY_ID1
        @UiComponentMapping("#myId")
        // END SNIPPET: COMPONENT_MAPPING_BY_ID1
        private TextBox box1;
        // START SNIPPET: COMPONENT_MAPPING_BY_ID2
        @UiComponentMapping(id="myId")
        // END SNIPPET: COMPONENT_MAPPING_BY_ID2
        private TextBox box2;
        // START SNIPPET: COMPONENT_MAPPING_BY_ID3
        @UiComponentMapping(selectorType=SelectorType.CSS, selector="#myId")
        // END SNIPPET: COMPONENT_MAPPING_BY_ID3
        private TextBox box3;

        // START SNIPPET: MAP_AS_UI_COMPONENT
        @UiComponentMapping("td.tableHeadingLeft")
        private UiComponent headingTd;

        public String getHeadingText() {
            return headingTd.getText();
        }
        // END SNIPPET: MAP_AS_UI_COMPONENT

    }
}
