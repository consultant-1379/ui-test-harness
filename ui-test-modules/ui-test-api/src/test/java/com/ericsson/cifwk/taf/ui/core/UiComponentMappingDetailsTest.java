package com.ericsson.cifwk.taf.ui.core;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class UiComponentMappingDetailsTest {
    @UiComponentMapping(id = "id")
    private UiComponent mappingById;

    @UiComponentMapping(name = "name")
    private UiComponent mappingByName;

    @UiComponentMapping(selectorType = SelectorType.XPATH, selector = "//a")
    private UiComponent mappingBySelector;

    @UiComponentMapping("defaultSelector")
    private UiComponent mappingByDefaultSelector;

    @Test
    public void from() throws Exception {
        UiComponentMappingDetails details = getMappingDetailsFromField("mappingById");
        Assert.assertEquals("id", details.getId());
        Assert.assertNull(details.getName());
        Assert.assertNull(details.getSelector());
        Assert.assertEquals(SelectorType.DEFAULT, details.getSelectorType());

        details = getMappingDetailsFromField("mappingByName");
        Assert.assertNull(details.getId());
        Assert.assertEquals("name", details.getName());
        Assert.assertNull(details.getSelector());
        Assert.assertEquals(SelectorType.DEFAULT, details.getSelectorType());

        details = getMappingDetailsFromField("mappingBySelector");
        Assert.assertNull(details.getId());
        Assert.assertNull(details.getName());
        Assert.assertEquals("//a", details.getSelector());
        Assert.assertEquals(SelectorType.XPATH, details.getSelectorType());

        details = getMappingDetailsFromField("mappingByDefaultSelector");
        Assert.assertNull(details.getId());
        Assert.assertNull(details.getName());
        Assert.assertEquals("defaultSelector", details.getSelector());
        Assert.assertEquals(SelectorType.DEFAULT, details.getSelectorType());
    }

    private UiComponentMappingDetails getMappingDetailsFromField(
            String fieldName) throws NoSuchFieldException {
        Field field = this.getClass().getDeclaredField(fieldName);
        UiComponentMapping annotation = field.getAnnotation(UiComponentMapping.class);
        return UiComponentMappingDetails.from(annotation);
    }

}
