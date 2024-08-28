package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.AbstractBrowserAwareTest;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrowserTabTest extends AbstractBrowserAwareTest {

    @Before
    public void setUp() {
        openGenericView();
    }

    @Test
    public void evaluate() {
        String result = (String) browserTab.evaluate("return '' + new Date();");
        assertFalse(StringUtils.isBlank(result));
        Long longValue = (Long) browserTab.evaluate("return 2+3;");
        assertEquals(new Long("5"),longValue);
        UiComponent component = (UiComponent) browserTab.evaluate("return document.getElementById('textBoxId')");
        assertEquals("Checking if correct UiComponent is returned ","Sample text", component.getText());
        List<UiComponent> components = (List)browserTab.evaluate("return document.getElementById('sampleControls').children");
        assertEquals("Checking if list of UiComponent is returned",10,components.size());
        assertEquals("Checking the name of the first UiComponent in the list","div",components.get(0).getComponentName());
        List<Object> object = (List)browserTab.evaluate("return [2 , 3.5, true, 'hello'];");
        assertTrue(object.get(0) instanceof Long);
        assertTrue(object.get(1) instanceof Double);
        assertTrue(object.get(2) instanceof Boolean);
        assertTrue(object.get(3) instanceof String);
    }

    @Test
    public void shouldReturnWebDriver() {
        WebDriver driver = ((InternalDriverAware) browserTab).getInternalDriver();
        assertNotNull(driver);
    }

    @Test
    public void viewsShouldStayBoundToAppropriateTabs() {
        Browser browser = UiToolkit.newBrowser(BrowserType.HEADLESS);

        BrowserTab tab1 = browser.open(findHtmlPage("basic_ui_components.htm"));
        BrowserTab tab2 = browser.open(findHtmlPage("selectable_table.htm"));

        assertEquals(2, browser.getAmountOfOpenTabs());

        MyViewModel view1 = tab1.getView(MyViewModel.class);
        MyViewModel view2 = tab2.getView(MyViewModel.class);

        assertTrue(view1.checkBoxExists());
        assertFalse(view1.selectableTableExists());

        assertFalse(view2.checkBoxExists());
        assertTrue(view2.selectableTableExists());
    }

    @Test
    public void shouldHandleFinalMethods() {
        Browser browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        BrowserTab tab = browser.open(findHtmlPage("basic_ui_components.htm"));

        ViewModelWithFinals viewWithFinals = tab.getView(ViewModelWithFinals.class);
        assertNotNull(viewWithFinals.getSelectableTable());

        ViewModelThatInheritsFinals viewThatInheritsFinals = tab.getView(ViewModelThatInheritsFinals.class);
        assertNotNull(viewThatInheritsFinals.getSelectableTable());
    }

    public static class MyViewModel extends GenericViewModel {

        @UiComponentMapping("#checkbox11")
        private CheckBox checkBox;

        @UiComponentMapping("#SelectableTable")
        protected UiComponent selectableTable;

        public boolean checkBoxExists() {
            return checkBox.exists();
        }

        public boolean selectableTableExists() {
            return selectableTable.exists();
        }
    }

    public static class ViewModelWithFinals extends MyViewModel {

        public final UiComponent getSelectableTable() {
            return selectableTable;
        }
    }

    public static class ViewModelThatInheritsFinals extends ViewModelWithFinals {
        public void goodMethod() {
        }
    }

}
