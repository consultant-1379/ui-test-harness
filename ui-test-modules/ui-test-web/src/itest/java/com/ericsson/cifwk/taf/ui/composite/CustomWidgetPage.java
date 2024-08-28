package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.google.common.base.Preconditions;

import java.util.List;

public class CustomWidgetPage extends GenericViewModel {

    private static final int WAIT_TIMEOUT = 10_000;

    @UiComponentMapping("#customForm")
    private CustomForm form;

    @UiComponentMapping(".address")
    private List<AddressFields> otherAddresses;

    public void registerPerson(String firstName, String lastName) {
        Preconditions.checkState(form.isDisplayed());

        form.setFirstName(firstName);
        form.setLastName(lastName);
    }

    public void setPersonHomeAddress(String city, String street) {
        form.setPersonHomeAddress(city, street);
    }

    public String getFirstName() {
        return form.getFirstName();
    }

    public String getLastName() {
        return form.getLastName();
    }

    public AddressFields getPersonHomeAddressField() {
        return form.getPersonHomeAddressField();
    }

    public TextBox getWorkZipCodeFieldImmediately() {
        return form.getWorkZipCodeField();
    }

    public AddressFields getWorkAddress() {
        return form.getWorkAddress();
    }

    public List<AddressFields> getOtherAddresses() {
        return otherAddresses;
    }

    public CustomForm getPersonDataForm() {
        return form;
    }

    public TextBox getWorkZipCodeFieldWhenReady() {
        return (TextBox) waitUntilComponentIsDisplayed(form.getWorkZipCodeField(), WAIT_TIMEOUT);
    }
}