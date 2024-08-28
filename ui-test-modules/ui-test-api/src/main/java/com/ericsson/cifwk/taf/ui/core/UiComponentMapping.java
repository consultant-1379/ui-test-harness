package com.ericsson.cifwk.taf.ui.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a dynamically mapped element(s) in {@link com.ericsson.cifwk.taf.ui.sdk.ViewModel} instances.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface UiComponentMapping {
    /**
     * @return name of the component to find
     */
    String name() default "";

    /**
     * @return id of the component to find
     */
    String id() default "";

    /**
     * @return Selector wildcard to search the component by. Wildcard processing and syntax is up to
     * the UI provider and is determined by <code>selectorType</code> attribute.
     * <p>For example, in "#myId" will search for a component by id "myId"</p>
     */
    String selector() default "";

    /**
     * @return Type of the selector used in <code>selector</code> attribute.
     * If <code>selector</code> attribute is undefined, <code>selectorType</code> is ignored.
     * Default selector type is obsolete, and is just a way to tell the implementing platform that
     * it should treat the selector by the way which is the default for it. For example, Selenium
     * implementation will treat <code>SelectorType.DEFAULT</code> as <code>SelectorType.CSS</code> -
     * therefore, selectors used in Browser models will be treated as CSS selectors.
     */
    SelectorType selectorType() default SelectorType.DEFAULT;

    /**
     * @return If defined, treated as <code>selector</code> attribute (and <code>selectorType</code> is <code>SelectorType.DEFAULT</code>
     */
    String value() default "";
}
