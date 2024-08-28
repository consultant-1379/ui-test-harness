package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.Option;
import com.ericsson.cifwk.taf.ui.sdk.Select;

import java.util.Collections;
import java.util.List;

/**
 * Implementation-agnostic single-line-selection dropdown representation.
 */
class SelectImpl extends AbstractUiComponent implements Select {

    /**
     * Selects option by value (internal value)
     *
     * @param optionValue
     * @throws <code>ComponentNotFoundException</code> if there is no such value
     */
    @Override
    public void selectByValue(String optionValue) {
        stateManager.processEvent(UiEvent.SELECT,
                Collections.singletonMap(UiProperties.VALUE, optionValue));
    }

    /**
     * Selects option by title (external value)
     *
     * @param optionTitle
     * @throws <code>ComponentNotFoundException</code> if there is no such title
     */
    @Override
    public void selectByTitle(String optionTitle) {
        stateManager.processEvent(UiEvent.SELECT,
                Collections.singletonMap(UiProperties.TITLE, optionTitle));
    }

    /**
     * Returns the current value (internal value) of this select
     *
     * @return current value (internal value) of this select
     * @throws <code>IllegalStateException</code> if many options are selected
     */
    @Override
    public String getValue() {
        checkIfMultipleSelection();
        return getProperty(UiProperties.VALUE);
    }

    /**
     * Returns the title of currently selected option
     *
     * @return title of currently selected option
     * @throws <code>IllegalStateException</code> if many options are selected
     */
    @Override
    public String getText() {
        checkIfMultipleSelection();
        return super.getText();
    }

    private void checkIfMultipleSelection() {
        if (getSelectedOptions().size() > 1) {
            throw new IllegalStateException("Multiple values are selected");
        }
    }

    @Override
    public List<Option> getSelectedOptions() {
        return processEvent(UiEvent.SELECTED_OPTIONS);
    }

    @Override
    public List<Option> getAllOptions() {
        return processEvent(UiEvent.ALL_OPTIONS);
    }

    @Override
    public void clearSelection() {
        stateManager.clearSelection();
    }
}
