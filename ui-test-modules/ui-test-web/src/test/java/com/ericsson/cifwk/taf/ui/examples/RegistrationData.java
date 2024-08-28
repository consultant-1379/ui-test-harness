package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

import java.util.List;

import static com.ericsson.cifwk.taf.ui.examples.CustomerDataTable.TableRow;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: REGISTRATION_DATA_VM
public class RegistrationData extends GenericViewModel {

    @UiComponentMapping("#customerData")
    private CustomerDataTable table;

    public List<TableRow> getTableRows() {
        return table.getRows();
    }

    // ...............
}
// END SNIPPET: REGISTRATION_DATA_VM