package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

import java.util.List;

class TestViewModel extends GenericViewModel {
    @UiComponentMapping(id = "loginForm")
    private Label loginFormLabel;

    @UiComponentMapping(id = "messagesBox")
    private Label errorMessagesLabel;

    @UiComponentMapping(name = "loginUsername")
    private TextBox usernameBox;

    @UiComponentMapping(selector = "loginPassword")
    private TextBox passwordBox;

    @UiComponentMapping(id = "submit")
    private Button okButton;

    @UiComponentMapping(".commonClass")
    private List<UiComponent> manyComponents;

    @UiComponentMapping(".commonTextBoxClass")
    private List<TextBox> manyTextBoxes;

    TestViewModel(UiComponentStateManagerFactory stateManagerFactory) {
        setComponentStateManagerFactory(stateManagerFactory);
    }

    public void setUsername(String username) {
        usernameBox.setText(username);
    }

    public void setPassword(String password) {
        passwordBox.setText(password);
    }

    public void clickOk() {
        okButton.click();
    }

    Label getLoginFormLabel() {
        return loginFormLabel;
    }

    Label getErrorMessagesLabel() {
        return errorMessagesLabel;
    }

    TextBox getUsernameBox() {
        return usernameBox;
    }

    TextBox getPasswordBox() {
        return passwordBox;
    }

    Button getOkButton() {
        return okButton;
    }

    List<UiComponent> getManyComponents() {
        return manyComponents;
    }

    List<TextBox> getManyTextBoxes() {
        return manyTextBoxes;
    }
}
