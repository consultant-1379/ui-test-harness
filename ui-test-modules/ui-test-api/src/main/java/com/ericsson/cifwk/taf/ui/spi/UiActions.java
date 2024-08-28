package com.ericsson.cifwk.taf.ui.spi;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

/**
 * Utility to run multiple atomic UI actions in one transaction (for example: click, press few keys, release keys).
 * <p>
 * Created by ejambuc on 23/10/2014.
 */
public interface UiActions {

    /**
     * Performs a click action on a given UI Component
     *
     * @param clickable
     * @return this <code>UiActions</code> instance
     */
    UiActions click(UiComponent clickable);

    /**
     * Performs a click on the current mouse location
     *
     * @return this <code>UiActions</code> instance
     */
    UiActions click();

    /**
     * Performs a right-click action on a given UI Component
     *
     * @param clickable
     * @return this <code>UiActions</code> instance
     */
    UiActions contextClick(UiComponent clickable);

    /**
     * Performs a right-click on the current mouse location
     *
     * @return this <code>UiActions</code> instance
     */
    UiActions contextClick();

    /**
     * Performs the down aspect of a left mouse click on the current location of the mouse cursor.
     * Does not release the click.
     *
     * @return this <code>UiActions</code> instance
     */
    UiActions mouseDown();

    /**
     * Releases the mousedown operation.
     *
     * @return this <code>UiActions</code> instance
     */
    UiActions mouseUp();

    /**
     * Releases the mousedown action on a given UI Component.
     *
     * @return this <code>UiActions</code> instance
     */
    UiActions mouseUp(UiComponent clickable);

    /**
     * Performs the mousedown action on a given UI Component
     *
     * @param clickable
     * @return this <code>UiActions</code> instance
     */
    UiActions mouseDown(UiComponent clickable);

    /**
     * Performs the mouseOver(hover) action on a given UI Component.
     *
     * @param clickable
     * @return this <code>UiActions</code> instance
     */
    UiActions mouseOver(UiComponent clickable);

    /**
     * Press down the given key.
     *
     * @param key see {@see org.openqa.selenium.Keys}
     * @return this <code>UiActions</code> instance
     */
    UiActions keyDown(CharSequence key);

    /**
     * Press down the given key on specified component.
     *
     * @param component the component to press the key upon
     * @param key       see {@see org.openqa.selenium.Keys}
     * @return this <code>UiActions</code> instance
     */
    UiActions keyDown(UiComponent component, CharSequence key);

    /**
     * Releases the given key.
     *
     * @param key see {@see org.openqa.selenium.Keys}
     * @return this <code>UiActions</code> instance
     */
    UiActions keyUp(CharSequence key);

    /**
     * Releases the given key on specified component.
     *
     * @param component the component to release the key upon
     * @param key       see {@see org.openqa.selenium.Keys}
     * @return this <code>UiActions</code> instance
     */
    UiActions keyUp(UiComponent component, CharSequence key);

    /**
     * Performs a double click on the current mouse location.
     *
     * @return this <code>UiActions</code> instance
     */
    UiActions doubleClick();

    /**
     * Performs a double click on the given UI component
     *
     * @param clickable
     * @return this <code>UiActions</code> instance
     */
    UiActions doubleClick(UiComponent clickable);

    /**
     * Sends the series of keys to UI.
     *
     * @param keys see {@see org.openqa.selenium.Keys}
     * @return this <code>UiActions</code> instance
     */
    UiActions sendKeys(CharSequence... keys);

    /**
     * Sends the series of keys to UI component.
     *
     * @param component the component to send the key strokes to
     * @param keys      see {@see org.openqa.selenium.Keys}
     * @return this <code>UiActions</code> instance
     */
    UiActions sendKeys(UiComponent component, CharSequence... keys);

    /**
     * Performs the actions held by the MultipleActionsBuilder.
     * Convenience method will also call a build() prior to perform.
     */
    void perform();

}
