package com.ericsson.cifwk.taf.ui.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marker interface to declare that {@link UiComponentMapping} selector in {@link AbstractUiComponent}
 * is applied to whole page (not to page section component is mapped to).
 * <p>
 * This is used to workaround non-modular HTML representation of some UI SDK components
 * (Dropdown, Select Box, Multi Select Box, Combo Box, Combo Multi Select Box, Popup Date Picker).
 * <p>
 * Once testability is improved in UI SDK components, global scope feature could be removed from TAF.
 *
 * @since 2.25.9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface GlobalScope {
}
