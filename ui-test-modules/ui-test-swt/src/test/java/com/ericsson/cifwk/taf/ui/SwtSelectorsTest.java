package com.ericsson.cifwk.taf.ui;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SwtSelectorsTest {

    @Test
    public void build() {
        SwtSelector selector = SwtSelectors.forType("type").withText("text").inCurrentWindow();

        assertThat("type").isEqualTo(selector.getType());
        assertThat("text").isEqualTo(selector.getText());
        assertThat(selector.getContainer()).isNull();

        selector = SwtSelectors.forType("type").withText("text").inContainer("container");
        assertThat("type").isEqualTo(selector.getType());
        assertThat("text").isEqualTo(selector.getText());
        assertThat("container").isEqualTo(selector.getContainer());
    }

}
