package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.List;

public class CustomForm extends AbstractUiComponent implements Iterable<Integer> {

    @UiComponentMapping("#firstName")
    private TextBox firstName;

    @UiComponentMapping("#lastName")
    private TextBox lastName;

    @UiComponentMapping(".homeAddress")
    private AddressFields homeAddress;

    @UiComponentMapping(".workAddress")
    private AddressFields workAddress;

    @UiComponentMapping(".credential")
    private List<TextBox> allPersonCredentials;

    @UiComponentMapping("#submitButtonId")
    private Button submitButton;


    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public void setFirstName(String firstName) {
        this.firstName.setText(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.setText(lastName);
    }

    public void setPersonHomeAddress(String city, String street) {
        homeAddress.setAddress(city, street);
    }

    public AddressFields getPersonHomeAddressField() {
        return homeAddress;
    }

    public TextBox getWorkZipCodeField() {
        return workAddress.getZipCodeField();
    }

    public AddressFields getWorkAddress() {
        return workAddress;
    }

    public boolean allPersonCredentialsEntered() {
        Preconditions.checkState(!allPersonCredentials.isEmpty(), "No credential fields found");

        boolean allEntered = true;
        for (TextBox credential : allPersonCredentials) {
            if (StringUtils.isBlank(credential.getText())) {
                allEntered = false;
            }
        }
        return allEntered;
    }

    public List<TextBox> getPersonCredentialsList() {
        return allPersonCredentials;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new CustomerFormIterator(this);
    }
}
