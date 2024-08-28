package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: SURNAME_FIELD_EXAMPLE
public class SurnameField extends AbstractUiComponent {

    private static final String VALUE_ATTRIBUTE = "value";

    public void setSurname(String surname) {
        setProperty(VALUE_ATTRIBUTE, surname);
    }

    public String getSurname() {
        return getProperty(VALUE_ATTRIBUTE);
    }
}
// END SNIPPET: SURNAME_FIELD_EXAMPLE
