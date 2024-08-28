package com.ericsson.cifwk.taf.ui.selenium;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class JavascriptStagingViewModel extends GenericViewModel {
    @UiComponentMapping(id = "hidingDiv")
    private Label disappearingLabel;

    @UiComponentMapping(id = "appearingDiv")
    private Label appearingLabel;

    @UiComponentMapping(id = "timestampPresenter")
    private TextBox timestampBox;

    @UiComponentMapping(id = "timestampGenerator")
    private Button refreshTimestampButton;

    Label getDisappearingLabel() {
        return disappearingLabel;
    }

    public void setDisappearingLabel(Label disappearingLabel) {
        this.disappearingLabel = disappearingLabel;
    }

    public Label getAppearingLabel() {
        return appearingLabel;
    }

    public void setAppearingLabel(Label appearingLabel) {
        this.appearingLabel = appearingLabel;
    }

    TextBox getTimestampBox() {
        return timestampBox;
    }

    public void setTimestampBox(TextBox timestampBox) {
        this.timestampBox = timestampBox;
    }

    Button getRefreshTimestampButton() {
        return refreshTimestampButton;
    }

    public void setRefreshTimestampButton(Button timeButton) {
        this.refreshTimestampButton = timeButton;
    }
}
