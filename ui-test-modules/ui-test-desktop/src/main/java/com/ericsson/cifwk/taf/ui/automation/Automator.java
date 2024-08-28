package com.ericsson.cifwk.taf.ui.automation;

public interface Automator {

    /**
     * Starts automation for the whole desktop region.
     *
     * @return an automation region
     */
    AutomationRegion automateDesktop();

    /**
     * Starts automation for a region matching an image.
     *
     * @param imageFile location of image file to match
     * @return an automation region
     */
    AutomationRegion automateRegion(String imageFile);

    /**
     * Starts automation for a region matching an image.
     * <p/>
     * Waits for a given number of milliseconds for the image to appear.
     *
     * @param imageFile  location of image file to match
     * @param waitMillis the maximum time to wait in milliseconds
     * @return an automation region
     */
    AutomationRegion automateRegion(String imageFile, int waitMillis);

    /**
     * Starts automation for selected desktop region.
     *
     * @return an automation region
     */
    AutomationRegion automateRegion(int x, int y, int width, int height);

    /**
     * Suspends current thread for the given number of milliseconds.
     *
     * @param millis amount of milliseconds to pause for
     */
    void pause(long millis);

    /**
     * Opens an application, or executes a system command.
     *
     * @param command system command
     */
    void run(String command);

    /**
     * Opens an application, or executes a system command.
     * <p/>
     * Waits for the application image to appear
     * for a given number of milliseconds.
     *
     * @param command    system command
     * @param imageFile  location of image file to match
     * @param waitMillis the maximum time to wait in milliseconds
     */
    void run(String command, String imageFile, int waitMillis);

    /**
     * Preforms a click with the left mouse button.
     *
     * @param location location to move to
     */
    void click(AutomationLocation location);

    /**
     * Preforms a click with the right mouse button.
     *
     * @param location location to move to
     */
    void rightClick(AutomationLocation location);

    /**
     * Preforms a double-click with the left mouse button.
     *
     * @param location location to move to
     */
    void doubleClick(AutomationLocation location);

    /**
     * Starts dragging with the left mouse button at given location.
     */
    void drag();

    /**
     * Starts dragging with the left mouse button at given location.
     *
     * @param location location to start from
     */
    void drag(AutomationLocation location);

    /**
     * Stop dragging with the left mouse button.
     */
    void drop();

    /**
     * Stop dragging with the left mouse button at given location.
     *
     * @param location location to move to
     */
    void drop(AutomationLocation location);

    /**
     * Performs key presses for the given key and modifier keys.
     *
     * @param keyCode          virtual key code of the main key to press
     * @param modifierKeyCodes virtual key codes of modifier keys
     * @see java.awt.event.KeyEvent
     */
    void sendKey(int keyCode, int... modifierKeyCodes);

    /**
     * Performs key presses for the characters in a given string of text.
     *
     * @param keys             string of characters to press
     * @param modifierKeyCodes virtual key codes of modifier keys
     * @see java.awt.event.KeyEvent
     */
    void sendKeys(String keys, int... modifierKeyCodes);

    /**
     * Places given text on clipboard an pastes it.
     *
     * @param text text to paste
     */
    void paste(String text);

    /**
     * Returns current clipboard contents as string.
     *
     * @return clipboard contents as string
     */
    String clipboardAsString();

    /**
     * Saves clipboard contents to a file,
     * if clipboard contains text or image data.
     *
     * @param directory directory path for the captured image
     */
    void saveClipboard(String directory);

}
