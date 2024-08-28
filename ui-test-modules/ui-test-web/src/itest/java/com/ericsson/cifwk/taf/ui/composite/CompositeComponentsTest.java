package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTestUtils;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CompositeComponentsTest {

    private static final int WAIT_TIMEOUT = 10_000;
    private BrowserTab tab;
    private CustomWidgetPage view;

    @Before
    public void setUp() {
        String testPage = BrowserTestUtils.findHtmlPage("composite_components.htm");
        Browser browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        this.tab = browser.open(testPage);
        this.view = tab.getView(CustomWidgetPage.class);
        tab.waitUntilComponentIsDisplayed("#container", WAIT_TIMEOUT);
    }

    @Test
    public void shouldWorkWithEmbeddedComponents() {
        String firstName = "John";
        String lastName = "Smyth";
        String homeCity = "Athlone";
        String homeStreet = "Main St., 15";

        view.registerPerson(firstName, lastName);
        view.setPersonHomeAddress(homeCity, homeStreet);

        assertEquals(firstName, view.getFirstName());
        assertEquals(lastName, view.getLastName());
        assertEquals(homeCity + ", " + homeStreet, view.getPersonHomeAddressField().getAddress());
    }

    @Test
    public void nestedComponentsShouldUseRelativeSelectors() {
        String homeCity = "Athlone";
        String homeStreet = "Main St., 15";

        AddressFields personHomeAddressField = view.getPersonHomeAddressField();
        // All 3 AddressFields blocks have the same CSS classes for city and street
        // but only this one should get updated
        personHomeAddressField.setAddress(homeCity, homeStreet);

        assertEquals(homeCity, view.getViewComponent("#homeCity").getText());
        assertEquals(homeStreet, view.getViewComponent("#homeStreet").getText());

        // Make sure that only embedded fields, addressed by a common CSS class, were updated
        assertEquals("", view.getViewComponent("#officeCity").getText());
        assertEquals("", view.getViewComponent("#officeStreet").getText());
        assertEquals("", view.getViewComponent("#managersHomeCity").getText());
        assertEquals("", view.getViewComponent("#managersHomeStreet").getText());
    }

    @Test
    public void shouldWaitForCustomComponent() {
        AddressFields workAddress = view.getViewComponent("#personWorkAddress", AddressFields.class);
        tab.waitUntilComponentIsDisplayed(workAddress, WAIT_TIMEOUT);
    }

    @Test
    public void shouldReportNotExistingEmbeddedComponentProperly() {
        TextBox notExistingField = view.getWorkAddress().getNotExistingField();
        try {
            notExistingField.click();
            Assert.fail("UiComponentNotFoundException expected");
        } catch (UiComponentNotFoundException e) {
            assertEquals("UiComponentMappingDetails [id=null, name=null, selector=#customForm, selectorType=DEFAULT] > " +
                    "[.workAddress, selectorType=DEFAULT] > " +
                    "[#notExisting, selectorType=DEFAULT]",
                    e.getMappingInfo());
        }
    }

    @Test
    public void shouldWorkInGenericModel() {
        ViewModel genericView = tab.getGenericView();
        CustomForm form = genericView.getViewComponent("#customForm", CustomForm.class);
        verifyCustomFormFunctionality(form);
    }

    @Test
    public void shouldSupportOnChildElementLevel() {
        ViewModel genericView = tab.getGenericView();
        UiComponent container = genericView.getViewComponent("#container");
        List<UiComponent> children = container.getChildren();
        CustomForm form = null;
        for (UiComponent child : children) {
            if ("customForm" .equals(child.getId())) {
                form = child.as(CustomForm.class);
                break;
            }
        }
        verifyCustomFormFunctionality(form);
    }

    @Test
    public void shouldSupportOnDescendantLevel() {
        String homeCity = "Athlone";
        String homeStreet = "Main St., 15";

        view.setPersonHomeAddress(homeCity, homeStreet);

        ViewModel genericView = tab.getGenericView();
        CustomForm form = genericView.getViewComponent("#customForm", CustomForm.class);
        List<UiComponent> descendants = form.getDescendantsBySelector(".homeAddress");
        assertEquals(1, descendants.size());
        AddressFields homeAddress = descendants.get(0).as(AddressFields.class);

        assertEquals(homeCity + ", " + homeStreet, homeAddress.getAddress());
    }

    @Test
    public void shouldSupportListsOfComposites() {
        List<AddressFields> otherAddresses = view.getOtherAddresses();
        assertEquals(2, otherAddresses.size());

        for (AddressFields otherAddress : otherAddresses) {
            assertTrue(otherAddress.exists());
            assertEquals("N/A", otherAddress.getAddress());
        }
    }

    @Test
    public void shouldSupportListsOfComponentsInNestedComposites() {
        // Fill in credentials
        view.registerPerson("John", "");

        CustomForm personDataForm = view.getPersonDataForm();
        assertEquals("John", personDataForm.getFirstName());
        assertEquals("", personDataForm.getLastName());

        List<TextBox> personCredentialsList = personDataForm.getPersonCredentialsList();
        assertEquals(2, personCredentialsList.size());

        assertFalse(personDataForm.allPersonCredentialsEntered());

        view.registerPerson("John", "Smyth");
        assertTrue(personDataForm.allPersonCredentialsEntered());
    }

    @Test
    public void shouldSupportIteratorOnCompositeComponent(){
        CustomForm form = view.getPersonDataForm();
        assertTrue(form.iterator().hasNext());
    }

    private void verifyCustomFormFunctionality(CustomForm form) {
        assertNotNull(form);
        assertTrue(form.exists());

        String firstName = "Jack";
        String lastName = "Daniels";

        form.setFirstName(firstName);
        form.setLastName(lastName);

        assertEquals(firstName, view.getFirstName());
        assertEquals(lastName, view.getLastName());

        AddressFields workAddress = form.getWorkAddress();
        tab.waitUntilComponentIsDisplayed(workAddress, WAIT_TIMEOUT);

        String newZipCode = "New ZIP";
        workAddress.setZipCode(newZipCode);
        assertEquals(newZipCode, view.getWorkZipCodeFieldImmediately().getText());
    }

}
