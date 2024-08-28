package com.ericsson.cifwk.taf.ui.core;

/**
 * <p>
 * The type of selector that is used in {@link com.ericsson.cifwk.taf.ui.core.UiComponentMapping } annotations (defined in <code>selector</code> attribute or
 * default annotation value).
 * <p>
 * <ul>
 * <li><code>CSS</code> means that the selector is a CSS matcher</li>
 * <li><code>XPATH</code> means that the selector is an XPATH expression</li>
 * <li><code>FILE_PATH</code> means that the selector is a file location. Used only with {@link com.ericsson.cifwk.taf.ui.DesktopNavigator}</li>
 * <li><code>DEFAULT</code> means that the selector value will be treated as the default one (it's one of the values above) by the implementing framework
 * (Selenium, SWT, etc.). So decision will be made by implementing framework</li>
 * </ul>
 */
public enum SelectorType {
    DEFAULT, CSS, XPATH, FILE_PATH
}
