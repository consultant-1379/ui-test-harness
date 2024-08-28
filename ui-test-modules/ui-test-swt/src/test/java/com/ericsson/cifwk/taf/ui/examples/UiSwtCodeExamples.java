package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.DesktopNavigator;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.SwtUiNavigator;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.MenuItem;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.After;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
public class UiSwtCodeExamples {

    private DesktopNavigator DesktopNavigator = null;

    public void initSwtBavigator() {
        {
            // START SNIPPET: INIT_SWT_NAVIGATOR
            SwtUiNavigator navigator = new SwtUiNavigator("host", 1234); // real port needed
            // END SNIPPET: INIT_SWT_NAVIGATOR
        }
        {
            // START SNIPPET: INIT_SWT_NAVIGATOR_ADVANCED
            int port = 1234;    // real port needed
            String host = "host";
            String startupScript = "/opt/swt/startup_script";
            String configFile = "/opt/swt/config.ini";
            String targetDisplay = String.format("%s:0.0", host);
            long connectionTimeoutInMillis = 30_000L;
            SwtUiNavigator navigator = new SwtUiNavigator(host, port, startupScript, configFile, targetDisplay, connectionTimeoutInMillis);
            // END SNIPPET: INIT_SWT_NAVIGATOR_ADVANCED
        }
    }

    public void getWindowAndViewModels() {
        int port = 1234;
        // START SNIPPET: INIT_SWT_NAVIGATOR_WINDOW
        DesktopNavigator desktopNavigator = UiToolkit.newSwtNavigator("host", port);
        DesktopWindow window = desktopNavigator.getWindowByTitle("OSS Common Explorer - valid configuration");
        // END SNIPPET: INIT_SWT_NAVIGATOR_WINDOW

        // START SNIPPET: INIT_SWT_VIEW_MODEL
        ViewModel genericView = window.getGenericView();
        TextBox nameTextBox = genericView.getTextBox("{label = 'Name:'}");
        nameTextBox.setText("Name");
        Button okButton = genericView.getButton("{text = 'OK'}");
        okButton.click();

        List<Button> allOkButtons = genericView.getViewComponents("{text = 'OK'}", Button.class);
        // END SNIPPET: INIT_SWT_VIEW_MODEL

        // START SNIPPET: INIT_SWT_CUSTOM_VIEW_MODEL
        BsimModel model = window.getView(BsimModel.class).init(genericView);
        model.activateTab("Some Tab");
        SomeTabModel someTabModel = window.getView(SomeTabModel.class);
        someTabModel.addConfiguration();
        NewConfigurationModel newConfigurationModel = window.getView(NewConfigurationModel.class);
        // END SNIPPET: INIT_SWT_CUSTOM_VIEW_MODEL
    }

    public void faqSamples() {
        DesktopWindow DesktopWindow = null;
        final UiComponent openDialogButton = null;
        String filePath = null;

        // START SNIPPET: SWT_FAQ_1
        DesktopWindow.takeScreenshot(filePath);
        // END SNIPPET: SWT_FAQ_1
        // START SNIPPET: SWT_FAQ_2
        DesktopWindow.getMenuItem("File").getMenuItem("Open").click();
        // END SNIPPET: SWT_FAQ_2
        // START SNIPPET: SWT_FAQ_3
        ViewModel genericView = null;
        MenuItem topLevelItem = genericView.getViewComponent("<a proper selector>", MenuItem.class);
        topLevelItem.getMenuItem("Context Menu").getMenuItem("Submenu").click();
        // END SNIPPET: SWT_FAQ_3
        // START SNIPPET: SWT_FAQ_4
        DesktopNavigator.async(new Callable<Void>() {
            @Override
            public Void call() {
                openDialogButton.click();
                return null;
            }
        });
        // END SNIPPET: SWT_FAQ_4
        // START SNIPPET: SWT_FAQ_5
        String userInput = "/tmp/select-file.txt" + "\r\n";
        DesktopWindow.sendKeys(userInput);
        // END SNIPPET: SWT_FAQ_5
    }

    // START SNIPPET: SWT_FAQ_6
    @After
    public void tearDown() {
        DesktopNavigator.close();
    }
    // END SNIPPET: SWT_FAQ_6


}