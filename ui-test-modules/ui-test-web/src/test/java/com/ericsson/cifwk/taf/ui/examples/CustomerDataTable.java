package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;

import java.util.List;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: CUSTOMER_DATA_TABLE_COMPONENT
public class CustomerDataTable extends AbstractUiComponent {

    @UiComponentMapping("tr")
    private List<TableRow> rows;

    public List<TableRow> getRows() {
        return rows;
    }

    public static class TableRow extends AbstractUiComponent {
    }
}
// END SNIPPET: CUSTOMER_DATA_TABLE_COMPONENT
