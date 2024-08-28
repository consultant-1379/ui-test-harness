package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import org.apache.commons.lang3.StringUtils;

public class AddressFields extends AbstractUiComponent {

    @UiComponentMapping(".city")
    private TextBox city;

    @UiComponentMapping(".street")
    private TextBox street;

    @UiComponentMapping("#workZipCode")
    private TextBox zipCode;

    @UiComponentMapping("#notExisting")
    private TextBox notExisting;

    public void setAddress(String city, String street) {
        this.city.setText(city);
        this.street.setText(street);
    }

    public String getAddress() {
        String cityName = city.getText();
        String streetName = street.getText();

        if (StringUtils.isBlank(cityName) && StringUtils.isBlank(streetName)) {
            return "N/A";
        }
        return cityName + ", " + streetName;
    }

    public TextBox getZipCodeField() {
        return zipCode;
    }

    public TextBox getNotExistingField() {
        return notExisting;
    }

    public void setZipCode(String zipCodeStr) {
        zipCode.setText(zipCodeStr);
    }

}
