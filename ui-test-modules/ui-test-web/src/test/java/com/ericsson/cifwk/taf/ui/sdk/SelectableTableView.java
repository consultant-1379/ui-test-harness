package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;

/**
 * Created by ejambuc on 03/11/2014.
 */
public class SelectableTableView extends GenericViewModel {

    @UiComponentMapping(id = "row1")
    private UiComponent row1;

    @UiComponentMapping(id = "row2")
    private UiComponent row2;

    @UiComponentMapping(id = "row3")
    private UiComponent row3;

    @UiComponentMapping(id = "row4")
    private UiComponent row4;

    @UiComponentMapping(id = "selectedRows")
    private Button selectedRowsButton;

    @UiComponentMapping(id = "numberOfSelectedRows")
    private TextBox selectedRowsText;

    @UiComponentMapping(selector = ".outer", selectorType = SelectorType.CSS)
    private Label outerDiv;

    @UiComponentMapping(selector = ".inner", selectorType = SelectorType.CSS)
    private Button innerDiv;

    public UiComponent getRow1() {
        return row1;
    }

    public UiComponent getOuterDiv() {
        return outerDiv;
    }

    public UiComponent getInnerDiv() {
        return innerDiv;
    }

    public UiComponent getRow2() {
        return row2;
    }

    public UiComponent getRow3() {
        return row3;
    }

    public UiComponent getRow4() {
        return row4;
    }

    public Button getSelectedRowsButton() {
        return selectedRowsButton;
    }

    public TextBox getSelectedRowsText() {
        return selectedRowsText;
    }
}