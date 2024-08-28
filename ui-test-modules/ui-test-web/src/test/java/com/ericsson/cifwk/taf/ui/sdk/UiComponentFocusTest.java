package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.core.AbstractBrowserAwareTest;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Vladimirs Iljins (vladimirs.iljins@ericsson.com)
 *         12/02/2016
 */
public class UiComponentFocusTest extends AbstractBrowserAwareTest {

    private static final String SAMPLE_TEXT = "Yet another sample text";
    private BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
        view.getLabel().click(); //give focus to the page initially
    }

    @Test
    public void focusBasicUiComponents() throws Exception {
        List<UiComponent> components = Arrays.asList(
                view.getTextBox(),
                view.getButton(),
                view.getLink(),
                view.getSelect(),
                view.getSelect("#listboxId"),
                view.getCheckBox("#checkbox11"),
                view.getRadioButton("#radio11"),
                view.getFileSelector()
        );

        for (UiComponent component : components) {
            assertFalse(component.hasFocus());

            component.focus();

            assertTrue(component.hasFocus());
        }
    }

    @Test
    public void focusAndSubmit() throws Exception {
        Label label = view.getLabel("#buttonClickedLabelId");
        assertFalse(label.isDisplayed());

        view.getButton().focus();
        view.newActionChain().sendKeys(Keys.SPACE).perform();

        assertTrue(label.isDisplayed());
    }

    @Test
    public void focusAndTypeText() throws Exception {
        TextBox textBox = view.getTextBox();

        textBox.focus();
        view.newActionChain().sendKeys(SAMPLE_TEXT).perform();

        assertThat(textBox.getText(), containsString(SAMPLE_TEXT));
    }

    @Override
    protected BrowserType getBrowserType() {
        return BrowserType.PHANTOMJS;
    }

}
