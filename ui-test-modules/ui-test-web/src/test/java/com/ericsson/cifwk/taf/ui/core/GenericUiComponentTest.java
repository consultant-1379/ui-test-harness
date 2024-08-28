package com.ericsson.cifwk.taf.ui.core;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.UiComponentTest;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

public class GenericUiComponentTest extends UiComponentTest {
    private UiComponent component;
    private BasicComponentsView view;

    @Before
    public void setUp() {
        BasicComponentsView view = openComponentsView();
        this.component = view.getGenericComponent();
        this.view = openComponentsView();
    }

    @Override
    protected UiComponent getComponent() {
        return component;
    }

    @Test
    public void getText() {
        Assert.assertEquals("Sample controls", component.getText());
    }

    @Test
    public void testToString() {
        String expected = "UiComponentMappingDetails [id=null, name=null, selector=h2, selectorType=DEFAULT]";
        Assert.assertTrue(component.toString() + " didn't contain " + expected
                , component.toString().contains(expected));
    }

    @Test
    public void getComponentSelector() {
        BasicComponentsView view = openComponentsView();

        checkComponentSelector(view, view.getViewComponent("#sampleControls"),
                SelectorType.CSS, "#sampleControls");
        checkComponentSelector(view, view.getViewComponent("option[value='option1 value']"),
                SelectorType.CSS, "#option1Id");
        checkComponentSelector(view, view.getViewComponent(SelectorType.CSS, "option[value='m_option1 value']", UiComponent.class),
                SelectorType.XPATH, "/html/body/div/div/div[6]/select/option");
        checkComponentSelector(view, view.getViewComponent(SelectorType.XPATH, "/html/body/div/div/div[5]/select/option[3]", UiComponent.class),
                SelectorType.CSS, "#option3Id");
        checkComponentSelector(view, view.getViewComponent(SelectorType.XPATH, "//h2", UiComponent.class),
                SelectorType.XPATH, "/html/body/div/h2");

        UiComponent notExisting = view.getViewComponent("#i_dont_exist");
        try {
            notExisting.getComponentSelector();
            Assert.fail("Expected UiComponentNotFound exception");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
    }

    private void checkComponentSelector(ViewModel view, UiComponent component, SelectorType expectedSelectorType, String expectedSelector) {
        String componentId = component.getId();
        String componentText = component.getText();
        String componentName = component.getComponentName();

        UiComponentSelector componentSelector = component.getComponentSelector();
        Assert.assertEquals(expectedSelectorType, componentSelector.getSelectorType());
        Assert.assertEquals(expectedSelector, componentSelector.getSelectorExpression());

        // Make sure we can find the same component using the same selector
        UiComponent secondaryComponent = view.getViewComponent(expectedSelectorType, expectedSelector, UiComponent.class);
        Assert.assertEquals(componentId, secondaryComponent.getId());
        Assert.assertEquals(componentText, secondaryComponent.getText());
        Assert.assertEquals(componentName, secondaryComponent.getComponentName());

        // Make sure the found component produces the same selector
        Assert.assertEquals(expectedSelectorType, secondaryComponent.getComponentSelector().getSelectorType());
        Assert.assertEquals(expectedSelector, secondaryComponent.getComponentSelector().getSelectorExpression());
    }

    @Test
    public void getChildren() {
        BasicComponentsView view = openComponentsView();

        //		<div id="sampleControls">
        UiComponent sampleControlsDiv = view.getViewComponent("#sampleControls");
        List<UiComponent> children = sampleControlsDiv.getChildren();
        Assert.assertEquals(10, children.size());

        //		<div class="controlBox" id="dropdownBoxContainer">
        UiComponent uiComponent = children.get(4);
        //		Assert.assertTrue(uiComponent instanceof Label);
        Assert.assertEquals("div", uiComponent.getComponentName());
        Assert.assertEquals("controlBox", uiComponent.getProperty("class"));
        Assert.assertEquals("dropdownBoxContainer", uiComponent.getId());

        children = uiComponent.getChildren();
        Assert.assertEquals(1, children.size());

        //		<select id="selectId" name="selectName" class="commonClass">
        uiComponent = children.get(0);
        //		Assert.assertTrue(uiComponent instanceof Select);
        Assert.assertEquals("select", uiComponent.getComponentName());
        Assert.assertEquals("commonClass", uiComponent.getProperty("class"));
        Assert.assertEquals("selectId", uiComponent.getId());

        children = uiComponent.getChildren();
        Assert.assertEquals(3, children.size());

        //		<option id="option1Id" value="option1 value">option1 title</option>
        uiComponent = children.get(0);
        //		Assert.assertTrue(uiComponent instanceof Option);
        Assert.assertEquals("option", uiComponent.getComponentName());
        Assert.assertEquals("option1 value", uiComponent.getProperty("value"));
        Assert.assertEquals("option1Id", uiComponent.getId());

        Assert.assertEquals(0, uiComponent.getChildren().size());
    }

    @Test
    public void getChildrenByXpathSelector() {
        BasicComponentsView view = openComponentsView();

        //		<div class="controlBox" id="dropdownBoxContainer">
        UiComponent dropdownBoxContainer = view.getViewComponent("#dropdownBoxContainer");
        List<UiComponent> selects = dropdownBoxContainer.getDescendantsBySelector(SelectorType.XPATH, "select");
        Assert.assertEquals(1, selects.size());
        UiComponent select = selects.get(0);

        Assert.assertEquals(3, select.getDescendantsBySelector(SelectorType.XPATH, "/*").size());
        Assert.assertEquals(3, select.getDescendantsBySelector(SelectorType.XPATH, "./*").size());
        Assert.assertEquals(3, select.getDescendantsBySelector(SelectorType.XPATH, "*").size());
        Assert.assertEquals(3, select.getDescendantsBySelector(SelectorType.XPATH, "/option").size());
        Assert.assertEquals(3, select.getDescendantsBySelector(SelectorType.XPATH, "//option").size());
        Assert.assertEquals(3, select.getDescendantsBySelector(SelectorType.XPATH, "option").size());
        Assert.assertEquals(0, select.getDescendantsBySelector(SelectorType.XPATH, "/select/option").size());

        //		<div id="sampleControls">
        UiComponent sampleControlsDiv = view.getViewComponent("#sampleControls");
        // Direct children DIVs
        Assert.assertEquals(10, sampleControlsDiv.getDescendantsBySelector(SelectorType.XPATH, "div").size());
        // All DIVs in this DIV
        Assert.assertEquals(13, sampleControlsDiv.getDescendantsBySelector(SelectorType.XPATH, "//div").size());
        // All SELECTs somewhere inside
        Assert.assertEquals(2, sampleControlsDiv.getDescendantsBySelector(SelectorType.XPATH, "//div//select").size());
    }

    @Test
    public void getChildrenByCssSelector() {
        BasicComponentsView view = openComponentsView();

        //		<div class="controlBox" id="dropdownBoxContainer">
        UiComponent dropdownBoxContainer = view.getViewComponent("#dropdownBoxContainer");
        List<UiComponent> selects = dropdownBoxContainer.getDescendantsBySelector("select.commonClass");

        Assert.assertEquals(1, selects.size());
        UiComponent select = selects.get(0);

        Assert.assertEquals(0, select.getDescendantsBySelector(SelectorType.CSS, "#hiddenDiv").size());
        Assert.assertEquals(false, select.getFirstDescendantBySelector(SelectorType.CSS, "#hiddenDiv", UiComponent.class).isPresent());
        Assert.assertEquals(3, select.getDescendantsBySelector(SelectorType.CSS, "option").size());
        Assert.assertEquals(true, select.getFirstDescendantBySelector(SelectorType.CSS, "option", TextBox.class).isPresent());
        Assert.assertEquals(0, select.getDescendantsBySelector(".controlBox > label").size());

        //		<div id="sampleControls">
        UiComponent sampleControlsDiv = view.getViewComponent("#sampleControls");
        Assert.assertEquals(6, sampleControlsDiv.getDescendantsBySelector(SelectorType.CSS, ".controlBox > label").size());
        Assert.assertEquals(6, sampleControlsDiv.getDescendantsBySelector("option").size());
    }

    @Test
    public void getChildrenAndDescendantsDifference() {
        UiComponent container = view.getViewComponent("#childrenContainer");
        assertEquals(2, container.getChildren().size());
        assertEquals(4, container.getDescendantsBySelector("div").size());
    }
}
