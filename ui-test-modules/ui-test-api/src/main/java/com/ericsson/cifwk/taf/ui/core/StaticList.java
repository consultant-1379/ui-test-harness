package com.ericsson.cifwk.taf.ui.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks List of UI components as static
 * (as UI test performance optimization).
 * <p>
 * Static lists are initialized just once during view model or
 * composite component initialization and its size can't be updated.
 * If item with according selector was appended to the page it won't get inside list.
 * If item was removed from the page, it will stay in the list (it can be removed from static list only manually),
 * UiComponentNotFoundException will be thrown on access to item methods.
 * <p>
 * But static list items (UI components) remain to be dynamic.
 * If content of the list item HTML element is updated, it will automatically be reflected in UI component object.
 * <p>
 *
 * @since 2.29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface StaticList {
}
