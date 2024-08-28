package com.ericsson.cifwk.taf.ui.automation;

/**
 * Automation interface with a desktop region as context.
 * <p/>
 * Regions may be nested and retain the ancestor chain when narrowing down
 * their scope. All regions in a single chain share the same instances
 * for mouse, keyboard, and other device access.
 */
public interface AutomationRegion extends AutomationLocation {

    /**
     * Tries to find an image in current region immediately.
     *
     * @param imageFile location of image file to match
     * @return region occupied by the found target
     * @throws com.ericsson.cifwk.taf.ui.automation.impl.AutomationTargetNotFound if target image is not found
     */
    AutomationRegion find(String imageFile);

    /**
     * Waits for an image to appear in current region for the given period of time.
     *
     * @param imageFile  location of image file to match
     * @param waitMillis the maximum time to wait in milliseconds
     * @return region occupied by the found target
     * @throws com.ericsson.cifwk.taf.ui.automation.impl.AutomationTargetNotFound if target image is not found
     */
    AutomationRegion find(String imageFile, int waitMillis);

    /**
     * Returns the region to the left of current.
     *
     * @param width width in pixels
     * @return new region
     */
    AutomationRegion left(int width);

    /**
     * Returns the region to the right of current.
     *
     * @param width width in pixels
     * @return new region
     */
    AutomationRegion right(int width);

    /**
     * Returns the region above current.
     *
     * @param height height in pixels
     * @return new region
     */
    AutomationRegion above(int height);

    /**
     * Returns the region below current.
     *
     * @param height height in pixels
     * @return new region
     */
    AutomationRegion below(int height);

    /**
     * Returns the location of region center.
     *
     * @return location of center
     */
    AutomationLocation center();

    /**
     * Return the location of region top left corner.
     *
     * @return location of top left corner
     */
    AutomationLocation topLeft();

    /**
     * Return the location of region top right corner.
     *
     * @return location of top right corner
     */
    AutomationLocation topRight();

    /**
     * Return the location of region bottom left corner.
     *
     * @return location of bottom left corner
     */
    AutomationLocation bottomLeft();

    /**
     * Return the location of region bottom right corner.
     *
     * @return location of bottom right corner
     */
    AutomationLocation bottomRight();

    /**
     * Captures current region as an image and saves it to a file.
     *
     * @param directory directory path for the captured image
     */
    void capture(String directory);

}
