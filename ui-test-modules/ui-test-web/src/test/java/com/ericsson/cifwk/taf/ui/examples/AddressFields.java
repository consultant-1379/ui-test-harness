package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import org.apache.commons.lang.StringUtils;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: ADDRESS_FIELDS_COMPONENT
public class AddressFields extends AbstractUiComponent {

    @UiComponentMapping(".city")
    private TextBox city;

    @UiComponentMapping(".street")
    private TextBox street;

    public void setAddress(String city, String street) {
        this.city.setText(city);
        this.street.setText(street);
    }

    public String getAddress() {
        return city.getText() + " " + street.getText();
    }

    public boolean isFilled() {
        return StringUtils.isNotBlank(city.getText())
                && StringUtils.isNotBlank(street.getText());
    }
}
// END SNIPPET: ADDRESS_FIELDS_COMPONENT
