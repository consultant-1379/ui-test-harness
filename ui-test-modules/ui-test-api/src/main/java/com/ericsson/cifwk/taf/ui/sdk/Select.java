package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

import java.util.List;

/**
 * Implementation-agnostic single-line-selection dropdown representation.
 */
public interface Select extends UiComponent {

    /**
     * Selects option by value (internal value)
     *
     * @param optionValue
     * @throws <code>ComponentNotFoundException</code> if there is no such value
     */
    void selectByValue(String optionValue);

    /**
     * Selects option by title (external value)
     *
     * @param optionTitle
     * @throws <code>ComponentNotFoundException</code> if there is no such title
     */
    void selectByTitle(String optionTitle);

    /**
     * Returns the current value (internal value) of this select
     *
     * @return current value (internal value) of this select
     * @throws <code>IllegalStateException</code> if many options are selected
     */
    String getValue();

    /**
     * Returns the title of currently selected option
     *
     * @return title of currently selected option
     * @throws <code>IllegalStateException</code> if many options are selected
     */
    @Override
    String getText();

    List<Option> getSelectedOptions();

    List<Option> getAllOptions();

    void clearSelection();
}
