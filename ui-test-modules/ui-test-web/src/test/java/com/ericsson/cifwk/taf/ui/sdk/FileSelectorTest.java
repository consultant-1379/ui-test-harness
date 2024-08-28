package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FileSelectorTest extends UiComponentTest {

    public static final String BASIC_UI_COMPONENTS_HTM = "html_pages/basic_ui_components.htm";

    private FileSelector fileSelector;
    private FileSelector hiddenFileSelector;

    @Before
    public void setUp() {
        BasicComponentsView view = openComponentsView();
        this.fileSelector = view.getFileSelector();
        this.hiddenFileSelector = view.getHiddenFileSelector();
    }

    @Override
    protected UiComponent getComponent() {
        return fileSelector;
    }

    @Test
    public void select() {
        fileSelector.select(BASIC_UI_COMPONENTS_HTM);
        String selectedFileName = fileSelector.getProperty(UiProperties.VALUE);
        // The following assertion works only in headless browser
        // assertTrue(selectedFileName.contains(TEMP_FOLDER));
        assertTrue(selectedFileName.contains("taf-ui"));
        assertTrue(selectedFileName.contains(".tmp"));
    }

    @Test
    public void selectHidden() {
        hiddenFileSelector.select(BASIC_UI_COMPONENTS_HTM);
        String selectedFileName = hiddenFileSelector.getProperty(UiProperties.VALUE);
        assertTrue(selectedFileName.contains("taf-ui"));
        assertTrue(selectedFileName.contains(".tmp"));
    }

    @Test
    public void getProperty() {
        Assert.assertEquals("file", fileSelector.getProperty(UiProperties.NAME));
    }

}
