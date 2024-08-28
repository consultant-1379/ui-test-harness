package com.ericsson.cifwk.taf.ui.composite;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserTestUtils;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.GlobalScope;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

// HEADLESS browser is too fast to constantly raise Stale Element exceptions
@Ignore
public class StaleCompositeComponentTest {

    public static final String VALUE = "Column 1";

    private CustomViewModel view;

    private BrowserTab tab;

    private Browser browser;

    @Before
    public void setUp() {
        String testPage = BrowserTestUtils.findHtmlPage("stale_composite_component.html");
        browser = UiToolkit.newBrowser(BrowserType.CHROME);
        tab = browser.open(testPage);
        view = tab.getView(CustomViewModel.class);
    }

    @After
    public void tearDown() {
        browser.close();
    }

    @Test
    public void implicitRetryStaleElement() {

        // regular sub-component
        TableRow row1 = view.getRow(1);

        // initializing component
        assertThat(row1.getCell(1).getText()).isEqualTo(VALUE);

        // staling component
        view.staleRow1();
        assertThat(row1.getCell(1).getText()).isEqualTo(VALUE);
    }

    @Test
    public void implicitRetryTimesOut() {

        // regular sub-component
        TableRow row1 = view.getRow(1);

        // initializing component
        assertThat(row1.getCell(1).getText()).isEqualTo(VALUE);

        // staling component
        view.staleRow1Long();

        try {
            row1.getCell(1);
            fail();
        } catch (StaleElementReferenceException e) {
            assertThat(e).hasMessageContaining("element is not attached to the page document");
        }
    }

    @Test
    public void stalingParentHasNoEffect() {

        // regular sub-component
        Label cell = view.getRow(1).getCell(1);

        // initializing component
        assertThat(cell.getText()).isEqualTo(VALUE);

        // staling component
        view.staleRow1Long();
        assertThat(cell.getText()).isEqualTo(VALUE);
    }

    @Test
    public void stalingCellDoesHasEffect() {

        // regular sub-component
        Label cell = view.getRow(1).getCell(1);

        // initializing component
        assertThat(cell.getText()).isEqualTo(VALUE);

        // staling component
        view.staleCell11Long();
        try {
            cell.getText();
            fail();
        } catch (StaleElementReferenceException e) {
            assertThat(e).hasMessageContaining("element is not attached to the page document");
        }
    }

    public static class CustomViewModel extends GenericViewModel {

        @UiComponentMapping(".customComponentHolder")
        private CustomComponent component;

        @UiComponentMapping(".row1Staler")
        private Button row1Staler;

        @UiComponentMapping(".row1FailStaler")
        private Button row1FailStaler;

        @UiComponentMapping(".row1Cell1FailStaler")
        private Button cell11FailStaler;

        public void staleRow1() {
            row1Staler.click();
        }

        public void staleRow1Long() {
            row1FailStaler.click();
        }

        public void staleCell11Long() {
            cell11FailStaler.click();
        }

        public TableRow getRow(int row) {
            return component.getRow(1);
        }
    }

    public static class CustomComponent extends AbstractUiComponent {

        @UiComponentMapping("h1")
        private Label header;

        @UiComponentMapping("tr")
        private List<TableRow> rows;

        @GlobalScope
        @UiComponentMapping(".sharedPartOfCustomComponent h1")
        private Label sharedHeader;

        @GlobalScope
        @UiComponentMapping(".sharedPartOfCustomComponent tr")
        private List<TableRow> sharedRows;

        public Label getHeader() {
            return header;
        }

        public Label getComponent(int row, int column) {
            return rows.get(row - 1).getCell(column);
        }

        public TableRow getRow(int row) {
            return rows.get(row - 1);
        }
    }

    public static class TableRow extends AbstractUiComponent {

        @UiComponentMapping("th")
        private List<Label> headerColumns;

        @UiComponentMapping("td")
        private List<Label> columns;

        public Label getCell(int oneBasedIndex) {
            if (headerColumns.isEmpty()) {
                return columns.get(oneBasedIndex - 1);
            }
            return headerColumns.get(oneBasedIndex - 1);
        }

    }

}
