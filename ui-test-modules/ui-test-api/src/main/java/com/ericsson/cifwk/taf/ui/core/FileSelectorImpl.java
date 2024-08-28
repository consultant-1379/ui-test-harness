package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.FileSelector;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Default FileSelector representation.
 */
class FileSelectorImpl extends AbstractUiComponent implements FileSelector {

    @Override
    public void select(String resourceName) {
        select(toTempFile(resourceName));
    }

    @Override
    public void select(InputStream inputStream) {
        select(toTempFile(inputStream));
    }

    @Override
    public void select(File file) {
        stateManager.sendKeysSafely(file.getAbsolutePath());
    }

    @Override
    public String getValue() {
        return getProperty(UiProperties.VALUE);
    }

    private File toTempFile(String resourceName) {
        URL resource = Resources.getResource(resourceName);
        try {
            return toTempFile(Resources.asByteSource(resource).openStream());
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private File toTempFile(InputStream is) {
        try {
            Path tempFile = Files.createTempFile("taf-ui", ".tmp");
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile.toFile();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

}
