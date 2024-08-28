package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WindowsTest extends AbstractBrowserAwareTest {
    private BasicComponentsView view;

    @Before
    public void setUp() {
        this.view = openComponentsView();
    }

    @Test
    @Ignore("Phantomjs' GhostDriver or HTMLUnitDriver can't handle message Boxes like other drivers")
    public void getAlertWindow_nonHeadless() {
        Button button = view.getButton();
        Assert.assertNull(browserTab.getMessageBox());
        button.click();
        MessageBox messageBox = browserTab.getMessageBox();
        Assert.assertNotNull(messageBox);
        Assert.assertEquals("Button clicked", messageBox.getText());
        messageBox.clickOk();
        Assert.assertNull(browserTab.getMessageBox());
    }

    @Test
    public void openNewPage() {
        assertEquals(1, browser.getOpenedWindowsAmount());

        browserTab.open(findHtmlPage("first.htm"));
        assertEquals(1, browser.getOpenedWindowsAmount());

        browserTab.open(findHtmlPage("second.htm"));
        assertEquals(1, browser.getOpenedWindowsAmount());
    }

    @Test
    public void openNewBrowser() {
        assertEquals(1, browser.getOpenedWindowsAmount());

        browser.open(findHtmlPage("first.htm"));
        assertEquals(2, browser.getOpenedWindowsAmount());
    }

    @Test
    public void newPageIsOpenedInNonActiveTab() {
        BrowserTab activeTab = browser.open(findHtmlPage("first.htm"));
        Assert.assertThat(browserTab.getCurrentUrl(), endsWith("basic_ui_components.htm"));
        Assert.assertThat(activeTab.getCurrentUrl(), endsWith("first.htm"));

        // switchWindow() is not required
        browserTab.open(findHtmlPage("second.htm"));
        Assert.assertThat(browserTab.getCurrentUrl(), endsWith("second.htm"));
        Assert.assertThat(activeTab.getCurrentUrl(), endsWith("first.htm"));
    }

    @Test
    public void fetchFromNonActiveTab() {
        browserTab.open(findHtmlPage("login.htm"));
        int componentCount = countDivsInTab(browserTab);
        BrowserTab activeTab = browser.open(findHtmlPage("first.htm"));
        assertEquals(componentCount, countDivsInTab(browserTab));
        browser.switchWindow(browserTab);
        assertEquals(componentCount, countDivsInTab(browserTab));
        browser.switchWindow(activeTab);
        assertEquals(componentCount, countDivsInTab(browserTab));
        assertNotEquals(componentCount, countDivsInTab(activeTab));
    }

    private int countDivsInTab(BrowserTab tab) {
        return tab.getGenericView().getComponentCount(SelectorType.CSS, "div");
    }

    @Test
    public void detectNewJsPopUpWindows() {
        Assert.assertEquals(1, browser.getOpenedWindowsAmount());
        Link newPopupOpener = view.getNewPopupOpener();
        newPopupOpener.click();
        UiToolkit.pause(2000);
        Assert.assertEquals(2, browser.getOpenedWindowsAmount());
    }

    @Test
    public void detectNewWindows() {
        Assert.assertEquals(1, browser.getOpenedWindowsAmount());
        Link newWindowOpener = view.getNewWindowOpener();
        newWindowOpener.click();
        UiToolkit.pause(2000);
        Assert.assertEquals(2, browser.getOpenedWindowsAmount());
    }

    @Test
    public void maximizeAndGetSize() {
        browserTab.setSize(100, 100);
        UiComponentSize originalSize = browserTab.getSize();
        browserTab.maximize();
        UiComponentSize maxSize = browserTab.getSize();
        Assert.assertTrue(originalSize.getWidth() < maxSize.getWidth());
        Assert.assertTrue(originalSize.getHeight() < maxSize.getHeight());
    }

    @Override
    protected BrowserType getBrowserType() {
        return BrowserType.PHANTOMJS;
    }
}
