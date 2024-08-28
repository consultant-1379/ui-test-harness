package com.ericsson.cifwk.taf.ui;

/**
 * <p>Interface for components that wish to expose the internal tool/driver they use.
 * <p>Example: <code>WebDriver driver = ((InternalDriverAware) browserTab).getInternalDriver();</code>
 *
 * @author Kirill Shepitko kirill.shepitko@ericsson.com
 *         Date: 13/01/2016
 */
public interface InternalDriverAware {

    /**
     * Returns the internal implementation used
     *
     * @param <D> a tool class that is known to be used as a key driver.
     * @return
     */
    <D> D getInternalDriver();

}
