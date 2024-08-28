package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

public interface BrowserTab extends UiWindow, ConditionWait {

    void open(String url);

    void markAsClosed();

    /**
     * @return url currently opened in tab
     */
    String getCurrentUrl();

    /**
     * Goes back in browser history
     */
    void back();

    /**
     * Goes forward in browser history
     */
    void forward();

    /**
     * Reloads the page
     */
    void refreshPage();

    Set<BrowserCookie> getCookies();

    BrowserCookie getCookie(String cookieName);

    /**
     * <p>Drops the <code>file</code> to <code>dropTarget</code> HTML 5 page element that performs file upload.</p>
     * <p><b>NOTE:</b> this functionality is implemented by jQuery injection into the page (if it's not used already),
     * creation of input file HTML element, and firing of jQuery-based drop event. This may conflict with event handling
     * functionality if your page (there are known conflicts with UI SDK pages). Therefore the work of this feauture
     * is NOT guaranteed.</p>
     *
     * @param file       file on the SUT. If the test will be run on grid, need to ensure it's available on the grid.
     * @param dropTarget page component to drop the file to
     */
    void dragAndDropForUpload(File file, UiComponent dropTarget);

    /**
     * <p>Drops the file, materialized from <code>inputStream</code>, to <code>dropTarget</code> HTML 5 page element that
     * performs file upload.</p>
     * <p><b>NOTE:</b> this functionality is implemented by jQuery injection into the page (if it's not used already),
     * creation of input file HTML element, and firing of jQuery-based drop event. This may conflict with event handling
     * functionality if your page (there are known conflicts with UI SDK pages). Therefore the work of this feauture
     * is NOT guaranteed.</p>
     *
     * @param inputStream    input stream
     * @param dropTarget     page component to drop the file (loaded from <code>inputStream</code>) to
     * @param targetFileName name of the file to be uploaded. Just name, no path.
     */
    void dragAndDropForUpload(InputStream inputStream, UiComponent dropTarget, String targetFileName);

    /**
     * Set window size
     *
     * @param width  tab window width
     * @param height tab window height
     */
    void setSize(int width, int height);

    /**
     * Set window size
     *
     * @param resolution pre-defined tab window screen resolution
     * @see com.ericsson.cifwk.taf.ui.BrowserSetup.Resolution
     */
    void setSize(BrowserSetup.Resolution resolution);

    /**
     * Creates an instance of {@link UiActions} bound to this tab.
     *
     * @return instance of {@link UiActions}.
     */
    UiActions newActionChain();


    WebElement waitUntilComponentIsDisplayedUsingDriver(UiComponent component, long timeoutInMillis);

}