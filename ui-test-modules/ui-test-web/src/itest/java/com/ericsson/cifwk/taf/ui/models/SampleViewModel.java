package com.ericsson.cifwk.taf.ui.models;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class SampleViewModel extends GenericViewModel {

    @UiComponentMapping(id = "loginNoticeOk")
    private Button noticeOkButton;

    public Button getNoticeOkButton() {
        return noticeOkButton;
    }

}
