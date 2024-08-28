package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

import java.io.File;
import java.io.InputStream;

/**
 * Implementation-agnostic FileSelector representation.
 */
public interface FileSelector extends UiComponent {

    void select(String resourceName);

    void select(InputStream inputStream);

    void select(File file);

    String getValue();

}
