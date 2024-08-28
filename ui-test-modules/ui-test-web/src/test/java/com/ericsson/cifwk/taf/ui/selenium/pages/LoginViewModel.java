package com.ericsson.cifwk.taf.ui.selenium.pages;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import org.apache.commons.lang.StringUtils;

import java.util.List;

// START SNIPPET: LOGIN_VIEW_MODEL
public class LoginViewModel extends GenericViewModel {

    @UiComponentMapping(id = "loginForm")
    private Label loginFormLabel;

    @UiComponentMapping(id = "messagesBox")
    private Label errorMessagesLabel;

    @UiComponentMapping(id = "loginUsername")
    private TextBox usernameBox;

    @UiComponentMapping(id = "loginPassword")
    private TextBox passwordBox;

    @UiComponentMapping(id = "submit")
    private Button okButton;

    @UiComponentMapping(".message")
    private List<Label> allMessages;

    private void setUsername(String username) {
        usernameBox.setText(username);
    }

    private void setPassword(String password) {
        passwordBox.setText(password);
    }

    private void clickOk() {
        okButton.click();
    }

    // ................
    // END SNIPPET: LOGIN_VIEW_MODEL

    public boolean loginFailed() {
        // Make sure error message box appears and contains something
        return (errorMessagesLabel.isDisplayed() && !StringUtils.isBlank(errorMessagesLabel.getText()));
    }

    public void login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickOk();
    }

    @Override
    public boolean isCurrentView() {
        //return loginFormLabel.getText() != null;
        return loginFormLabel.isDisplayed() && usernameBox.isDisplayed() && passwordBox.isDisplayed();
    }

    public Label getLoginFormLabel() {
        return loginFormLabel;
    }

    public Label getErrorMessagesLabel() {
        return errorMessagesLabel;
    }

    public TextBox getUsernameBox() {
        return usernameBox;
    }

    public TextBox getPasswordBox() {
        return passwordBox;
    }

    public Button getOkButton() {
        return okButton;
    }


}
