package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

public class Example {

//    public static void main(String... args) {
//        DesktopNavigator desktopNavigator = UiToolkit.newSwtNavigator(getRemoteHost(), getRemoteHostPort());
//        ViewModel view = desktopNavigator.getWindowByTitle("OSS Common Explorer - valid configuration").getGenericView();
//
//        UiComponent pcaTab = view.getViewComponent("{type = 'org.eclipse.swt.custom.CTabItem', mnemonicText = 'PCA', initActions = ['activate']}");
//        Table table = view.getViewComponent("{type = 'org.eclipse.swt.widgets.Table', index = 0}", Table.class);
//        table.select(table.getRowIndex("PCA", "Name"));
//    }

    public static void main(String[] args) {
        SwtUiNavigator swtNavigator = new SwtUiNavigator("localhost", 8080);

        System.out.println(swtNavigator.getViews());
        ViewModel mainView = swtNavigator.getView("SWT Application");

        // testing selector types
        int selectorCount = 1;
        TextBox textBox = mainView.getTextBox("Delete me");
        textBox.setText(textBox.getText() + selectorCount++);
        textBox = mainView.getTextBox("#sample.text.input");
        textBox.setText(textBox.getText() + selectorCount++);
        textBox = mainView.getTextBox("#0");
        textBox.setText(textBox.getText() + selectorCount++);
        textBox = mainView.getTextBox("customId#firstName");
        textBox.setText(textBox.getText() + selectorCount++);
        textBox = mainView.getTextBox("[customId=firstName]");
        textBox.setText(textBox.getText() + selectorCount++);
        textBox = mainView.getTextBox("Input+");
        textBox.setText(textBox.getText() + selectorCount++);
        textBox = mainView.getTextBox("Please delete me:hover");
        textBox.setText(textBox.getText() + selectorCount++);

        Label label = mainView.getLabel("Input");

        swtNavigator.reset();
        System.out.println("Success!");
    }

    private static String getLocalHost() {
        return "localhost";
    }

    private static int getLocalHostPort() {
        return 8080;
    }

    private static int getRemoteHostPort() {
        return 7777;
    }

    private static String getRemoteHost() {
        return "atvts839.athtem.eei.ericsson.se";
    }
}
