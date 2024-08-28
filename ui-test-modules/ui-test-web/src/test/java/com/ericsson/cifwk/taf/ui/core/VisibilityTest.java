package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class VisibilityTest extends AbstractBrowserAwareTest {
    private ViewModel view;

    @Before
    public void setUp() {
        openDefaultBrowserTab();
        this.view = browserTab.getGenericView();
    }

    @Test
    public void exists() {
        Label hiddenDiv = view.getLabel("#hiddenDiv");
        Assert.assertTrue(hiddenDiv.exists());
        Assert.assertFalse(hiddenDiv.isDisplayed());
        Assert.assertEquals(0, hiddenDiv.getChildren().size());

        Label nothing = view.getLabel("#idontexist");
        Assert.assertFalse(nothing.isDisplayed());
        Assert.assertFalse(nothing.exists());
    }

    @Test
    public void shouldNotGetMissingElementChildren() {
        Label nothing = view.getLabel("#idontexist");
        Assert.assertFalse(nothing.exists());
        // No exception here:
        List<UiComponent> lazilyLoadedChildren = null;
        try {
            lazilyLoadedChildren = nothing.getChildren();
        } catch (Exception e) {
            Assert.fail("Unexpected exception" + e.getMessage());
        }
        // Exception should be here, after a sequence of failing attempts to find parent element:
        try {
            lazilyLoadedChildren.size();
            Assert.fail("UiComponentNotFoundException exception expected");
        } catch (UiComponentNotFoundException e) {
            Assert.assertThat(e.getMappingInfo(), equalTo("UiComponentMappingDetails [id=null, name=null, selector=#idontexist, selectorType=DEFAULT]"));
        }
    }

    @Test
    public void findDeletedAndReaddedComponent() {
        Label deletableText = view.getLabel("#deletableText");
        Assert.assertTrue(deletableText.exists());

        // Delete
        view.getButton("#deletingButton").click();
        Assert.assertFalse(deletableText.exists());

        // Undelete
        view.getButton("#deletingButton").click();
        Assert.assertTrue(deletableText.exists());
    }

    @Test
    public void findDeletedAndReaddedComponentFromList() {
        List<Label> deletableTextList = view.getViewComponents("#deletableText", Label.class);
        Assert.assertEquals(1, deletableTextList.size());
        Label deletableText = deletableTextList.get(0);
//        Assert.assertTrue(deletableText.exists());
        Assert.assertEquals(0, deletableText.getDescendantsBySelector("*").size());

        // Delete
        view.getButton("#deletingButton").click();
        Assert.assertEquals(0, deletableTextList.size());
//        Assert.assertEquals(0, deletableText.getDescendantsBySelector("*").size());
//        Assert.assertFalse(deletableText.exists());

        // Undelete
        view.getButton("#deletingButton").click();
        Assert.assertEquals(1, deletableTextList.size());
        Assert.assertEquals(0, deletableText.getDescendantsBySelector("*").size());
//        Assert.assertTrue(deletableText.exists());
    }

}
