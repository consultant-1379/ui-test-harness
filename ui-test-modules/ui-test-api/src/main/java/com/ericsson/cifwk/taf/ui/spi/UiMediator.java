package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.core.*;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;

import java.util.List;

/**
 * A mediator between the TAF UI facade and appropriate underlying framework. If a specified operation is not supported by the underlying framework,
 * an <code>UnsupportedOperationException</code> should be thrown.
 */
public interface UiMediator extends UiEventProcessor, ScreenshotProvider {

    List<UiComponentStateManager> retrieve(UiComponentMappingDetails details);

    /**
     * Returns the contents of the clipboard as text
     *
     * @return the contents of the clipboard as text
     */
    String copy();

    /**
     * Sends the set of keystrokes to the UI
     *
     * @param key
     * @param modifiers
     */
    void sendKey(int key, int... modifiers);

    /**
     * Sends the set of keystrokes to the UI
     *
     * @param keys
     * @param modifiers
     */
    void sendKeys(String keys, int... modifiers);

    /**
     * Closes the resources used by the mediator
     */
    void close();

    /**
     * Maximizes the UI window
     */
    void maximize();

    /**
     * Minimizes the UI window
     */
    void minimize();

    /**
     * Returns the current URL (in case of Web-aware underlying frameworks).
     */
    String getCurrentUrl();

    /**
     * Returns the current window title.
     */
    String getTitle();

    /**
     * Goes back in window navigation history
     */
    void back();

    /**
     * Goes forward in window navigation history
     */
    void forward();

    /**
     * Refreshes the currently opened window
     */
    void refresh();

    /**
     * Returns the currently opened dialog message box (alert/info/confirmation)
     *
     * @return the currently opened dialog message box (alert/info/confirmation). <code>null</code> is returned if no dialog box is opened.
     */
    MessageBox getMessageBox();

    /**
     * Returns the size of current window
     *
     * @return the size of current window
     */
    UiComponentSize getWindowSize();

    /**
     * Requests the underlying framework to evaluate the <code>expression</code> and returns the evaluation result
     *
     * @param expression expression that underlying framework understands
     * @return evaluation result
     */
    Object evaluate(String expression);

    /**
     * Drags <code>object</code> and drops it on <code>target</code>
     *
     * @param object object to drag
     * @param target target to drop the source object to
     */
    void dragAndDropTo(UiComponent object, UiComponent target);

    /**
     * Set current window size
     *
     * @param width
     * @param height
     */
    void setWindowSize(int width, int height);

    /**
     * Creates an instance of {@link UiActions} bound to the current window.
     *
     * @return instance of {@link UiActions}.
     */
    UiActions newActionChain();

}
