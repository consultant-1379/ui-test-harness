package com.ericsson.cifwk.taf.ui.examples;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.google.common.base.Preconditions;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 19/04/2017
 */
// START SNIPPET: CUSTOM_WIDGET_PAGE
public class CustomWidgetPage extends GenericViewModel {

    @UiComponentMapping("#customForm")
    private CustomForm form;

    public void registerPerson(String firstName, String lastName, String city, String street) {
        Preconditions.checkState(form.isDisplayed());

        form.setFirstName(firstName);
        form.setLastName(lastName);
        form.setHomeAddress(city, street);
    }

    public String getFirstName() {
        return form.getFirstName();
    }

    public String getLastName() {
        return form.getLastName();
    }

    public String getAddress() {
        return form.getHomeAddress();
    }

    public void validate() {
        Preconditions.checkState(form.isFilled());
    }

    // ...............
}
// END SNIPPET: CUSTOM_WIDGET_PAGE
