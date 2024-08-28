package com.ericsson.cifwk.taf.ui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwtSelectorTest {

    private SwtSelector swtSelector;

    @Before
    public void setUp() {
        swtSelector = new SwtSelector();
    }

    @Test
    public void toJson() {
        assertEquals("{\"index\":0,\"nativeWidget\":false}", swtSelector.toJson());
    }

}
