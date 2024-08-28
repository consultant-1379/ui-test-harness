package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import org.apache.commons.lang.StringUtils;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: CUSTOM_FORM_COMPONENT
public class CustomForm extends AbstractUiComponent {

    @UiComponentMapping("#firstName")
    private TextBox firstName;

    @UiComponentMapping("#lastName")
    private TextBox lastName;

    @UiComponentMapping(".homeAddress")
    private AddressFields homeAddress;

    @UiComponentMapping("#personWorkAddress")
    private AddressFields personWorkAddress;

    @UiComponentMapping("#submitButtonId")
    private Button submitButton;

    //  ..........................

    public boolean isDisplayed() {
        return firstName.exists() && lastName.exists();
    }

    public boolean isFilled() {
        return StringUtils.isNotBlank(firstName.getText())
                && StringUtils.isNotBlank(lastName.getText())
                && personWorkAddress.isFilled()
                && homeAddress.isFilled();
    }

    public void setFirstName(String firstName) {
        this.firstName.setText(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.setText(lastName);
    }

    public void setHomeAddress(String city, String street) {
        this.homeAddress.setAddress(city, street);
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public String getHomeAddress() {
        return homeAddress.getAddress();
    }
}
// END SNIPPET: CUSTOM_FORM_COMPONENT
