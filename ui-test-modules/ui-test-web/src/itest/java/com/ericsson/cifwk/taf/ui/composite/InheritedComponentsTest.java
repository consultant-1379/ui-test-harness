package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTestUtils;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InheritedComponentsTest {

    private MySubView view;

    private Browser browser;

    @Before
    public void setUp() {
        String testPage = BrowserTestUtils.findHtmlPage("inherited_components.html");
        browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        BrowserTab tab = browser.open(testPage);
        view = tab.getView(MySubView.class);
    }

    @After
    public void tearDown() {
        browser.close();
    }

    @Test
    public void subView() {
        verifyFullName(view);
    }

    @Test
    public void subComponent_getViewComponent() {
        verifyFullAddress(view.getViewComponent(".homeAddress",FullAddress.class));
    }

    @Test
    public void subComponent_as() {
        verifyFullAddress(view.getViewComponent("body").as(FullAddress.class));
    }

    @Test
    public void subComponent_list() {
        Iterator<FullAddress> addresses = view.getAddresses().iterator();
        verifyFullAddress(addresses.next());
        verifyFullAddress(addresses.next());
    }

    @Test
    public void subComponent_getDescendantsBySelector() {
        UiComponent body = view.getViewComponent("body");
        UiComponent address = body.getDescendantsBySelector(".homeAddress").iterator().next();
        verifyFullAddress(address.as(FullAddress.class));
    }

    private void verifyFullName(MySubView subView) {
        assertEquals("John", subView.getFirstName());
        assertEquals("Smith", subView.getLastName());
    }

    private void verifyFullAddress(FullAddress fullAddress) {
        assertEquals("Galey 2", fullAddress.getHouseNo());
        assertEquals("Main St., 15", fullAddress.getStreet());
        assertEquals("Athlone", fullAddress.getCity());
        assertEquals("Ireland", fullAddress.getCountry());
    }

    public static class LocalAddress extends AbstractUiComponent {

        @UiComponentMapping(".houseNo")
        private TextBox houseNo;

        @UiComponentMapping(".street")
        private TextBox street;

        public String getHouseNo() {
            return houseNo.getText();
        }

        public String getStreet() {
            return street.getText();
        }

     }

    public static class FullAddress extends LocalAddress{

        @UiComponentMapping(".city")
        private TextBox city;

        @UiComponentMapping(".country")
        private TextBox country;

        public String getCity() {
            return city.getText();
        }

        public String getCountry() {
            return country.getText();
        }
    }

    public static class MyView extends GenericViewModel {

        @UiComponentMapping("#firstName")
        private TextBox firstName;

        public String getFirstName() {
            return firstName.getText();
        }
    }

    public static class MySubView extends MyView {

        @UiComponentMapping("#lastName")
        private TextBox lastName;

        @UiComponentMapping(".address")
        private List<FullAddress> addresses;

        public String getLastName() {
            return lastName.getText();
        }

        public List<FullAddress> getAddresses() {
            return addresses;
        }
    }

}


