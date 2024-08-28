package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTestUtils;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.GlobalScope;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.core.UiComponentPredicates;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ericsson.cifwk.taf.ui.core.SelectorType.XPATH;
import static org.assertj.core.api.Assertions.assertThat;

public class GlobalScopeTest {

    private CustomViewModel view;

    @Before
    public void setUp() {
        String testPage = BrowserTestUtils.findHtmlPage("global_scope.html");
        Browser browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        BrowserTab tab = browser.open(testPage);
        view = tab.getView(CustomViewModel.class);
    }

    @Test
    public void cssMapping() {
        view.css1();
        view.checkNotification("Component 1 saved");

        view.css2();
        view.checkNotification("Component 2 saved");
    }

    @Test
    public void xpathMapping() {
        view.xpathAction1();
        view.checkNotification("Component 1 saved");

        view.xpathAction2();
        view.checkNotification("Component 2 saved");
    }

    @Test
    public void globalListMapping() {
        view.checkGlobalItems();
        view.checkLocalItems();
    }

    @Test
    public void globalScopeWithMultiple() {
        view.checkDynamicGlobalSection();
    }

    public static class CustomViewModel extends GenericViewModel {

        @UiComponentMapping("#component1")
        private CustomComponent component1;

        @UiComponentMapping("#component2")
        private CustomComponent component2;

        void css1() {
            component1.saveCss();
        }

        public void css2() {
            component2.saveCss();
        }

        void xpathAction1() {
            component1.saveXpath();
        }

        public void xpathAction2() {
            component2.saveXpath();
        }

        void checkNotification(String expectedNotification) {

            // taking notification globally
            assertThat(getLabel(".notification").getText()).isEqualTo(expectedNotification);

            // taking notification via component which is mapped to page subsection
            assertThat(component1.getCssNotification()).isEqualTo(expectedNotification);
            assertThat(component1.getXpathNotification()).isEqualTo(expectedNotification);
            assertThat(component2.getCssNotification()).isEqualTo(expectedNotification);
            assertThat(component2.getXpathNotification()).isEqualTo(expectedNotification);
        }

        void checkGlobalItems() {
            List<String> items = component1.getGlobalItems();
            List<String> items2 = component2.getGlobalItems();
            assertThat(items).containsExactlyElementsOf(items2);
            assertThat(items).hasSize(3);
            assertThat(items.iterator().next()).isEqualTo("1");
        }

        void checkLocalItems() {
            assertThat(component1.getLocalItems()).isEmpty();
            assertThat(component2.getLocalItems()).isEmpty();
        }

        void checkDynamicGlobalSection() {
            assertThat(component1.getDynamicGlobalContent()).isEqualTo("Dynamic global section");
        }

    }

    public static class CustomComponent extends AbstractUiComponent {

        @UiComponentMapping(".button")
        private Button cssSaveButton;

        @UiComponentMapping(selectorType = XPATH, selector = "//form/input")
        private Button xpathSaveButton;

        @GlobalScope
        @UiComponentMapping(".notification")
        private Label cssGlobalNotification;

        @GlobalScope
        @UiComponentMapping(selectorType = XPATH, selector = "//div[@class=\"notification\"]")
        private Label xpathGlobalNotification;

        @GlobalScope
        @UiComponentMapping(".globalItem")
        List<Label> globalItems;

        @UiComponentMapping(".globalItem")
        // using the same mapping as in global items, nothing should be found in local (by default) scope
        List<Label> localItems;

        @GlobalScope
        @UiComponentMapping(".dynamicGlobalSection")
        Label dynamicGlobalSection;

        void saveCss() {
            cssSaveButton.click();
        }

        void saveXpath() {
            xpathSaveButton.click();
        }

        String getCssNotification() {
            return cssGlobalNotification.getText();
        }

        String getXpathNotification() {
            return xpathGlobalNotification.getText();
        }

        List<String> getGlobalItems() {
            return transform(globalItems);
        }

        List<String> getLocalItems() {
            return transform(localItems);
        }

        private List<String> transform(List<Label> items) {
            return Lists.newArrayList(Collections2.transform(items, new Function<Label, String>() {
                @Override
                public String apply(Label input) {
                    return input.getText();
                }
            }));
        }

        String getDynamicGlobalContent() {
            waitUntil(dynamicGlobalSection, UiComponentPredicates.DISPLAYED);
            waitUntil(dynamicGlobalSection, UiComponentPredicates.HIDDEN);
            waitUntil(dynamicGlobalSection, UiComponentPredicates.DISPLAYED);
            return dynamicGlobalSection.getText();
        }
    }

}
