package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.AbstractBrowserAwareTest;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.SelectableTableView;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 25/05/2016
 */
public class UiActionChainTest extends AbstractBrowserAwareTest {

    private SelectableTableView view;
    private UiActions actionChain;

    @Before
    public void setUp() {
        this.browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        this.browserTab = browser.open(findHtmlPage("selectable_table.htm"));
        view = browserTab.getView(SelectableTableView.class);
        actionChain = browserTab.newActionChain();
    }

    @Test
    public void shouldSelectTableRowCtrl() {
        UiActions actions = actionChain.click(view.getRow1()).keyDown(Keys.CONTROL).click(view.getRow3()).keyUp(Keys.CONTROL).click(view.getSelectedRowsButton());
        actions.perform();
        assertEquals("2", view.getSelectedRowsText().getText());
    }

    @Test
    public void shouldSelectTableRowWithoutCtrl() {
        UiActions actions = actionChain.click(view.getRow1()).click(view.getRow3()).click(view.getSelectedRowsButton());
        actions.perform();
        assertEquals("1", view.getSelectedRowsText().getText());
    }

    @Test
    public void shouldMouseOverAndClick() {
        UiActions actions = actionChain.mouseOver(view.getOuterDiv()).click(view.getInnerDiv());
        actions.perform();
        browserTab.waitUntilComponentIsDisplayed(SelectorType.CSS, "#SelectableTable", 1000);
        assertThat(browserTab.getTitle(), equalTo("Large Table with Dynamic Elements"));
    }

    @Test
    public void shouldContextClick() {
        browserType = BrowserType.HEADLESS;
        ViewModel view = openGenericView();
        actionChain = browserTab.newActionChain();

        UiComponent contextMenu = view.getViewComponent("#contextMenu");
        assertFalse(contextMenu.isDisplayed());

        UiActions actions = actionChain.contextClick(view.getViewComponent("#labelWithContextMenu"));
        actions.perform();

        view.waitUntilComponentIsDisplayed(contextMenu);
        assertEquals("Context Menu", contextMenu.getText());
    }

    @Test
    public void actionChainShouldBeBoundToTab() {
        Browser browser = UiToolkit.newBrowser(BrowserType.HEADLESS);

        String htmlPage = findHtmlPage(BASIC_UI_COMPONENTS_PAGE);
        BrowserTab tab1 = browser.open(htmlPage);
        BrowserTab tab2 = browser.open(htmlPage);

        ViewModel view2 = tab2.getGenericView();

        // Selecting checkboxes on tab2 shouldn't affect tab1
        CheckBox checkBoxOrange = view2.getCheckBox("#checkbox11");
        CheckBox checkBoxGrapes = view2.getCheckBox("#checkbox13");
        tab2.newActionChain().click(checkBoxOrange).click(checkBoxGrapes).perform();

        assertTrue(checkBoxOrange.isSelected());
        assertTrue(checkBoxGrapes.isSelected());

        ViewModel view1 = tab1.getGenericView();
        assertFalse(view1.getCheckBox("#checkbox11").isSelected());
        assertFalse(view1.getCheckBox("#checkbox13").isSelected());
    }
    
    @Test
    public void actionChainViaComponent() {
        Browser browser = UiToolkit.newBrowser(BrowserType.HEADLESS);
        String htmlPage = findHtmlPage(BASIC_UI_COMPONENTS_PAGE);
        BrowserTab tab = browser.open(htmlPage);
        ViewModel view = tab.getGenericView();

        // Selecting checkboxes
        CheckBox checkBox11 = view.getCheckBox("#checkbox11");
        CheckBox checkBox13 = view.getCheckBox("#checkbox13");
        checkBox11.createUiActions().click(checkBox11).click(checkBox13).perform();
        assertTrue(checkBox11.isSelected());
        assertTrue(checkBox13.isSelected());
    }

}
