package com.ericsson.cifwk.taf.ui.core;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Proxy;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsITestView;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.selenium.AbstractBrowserAwareITest;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;

public class SeleniumStateManagerProxyTest extends AbstractBrowserAwareITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumStateManagerProxyTest.class);

    @Test
    public void shouldProxyStateManagerFromList() {
        BasicComponentsITestView view = openComponentsView();
        List<UiComponent> multipleElementsWithSameClass = view.getMultipleElementsWithSameClass();
        UiComponent listComponent = multipleElementsWithSameClass.get(0);
        verifyStateManagerIsProxied(listComponent);
    }

    @Test
    public void shouldProxySingleStateManager() {
        BasicComponentsITestView view = openComponentsView();
        verifyStateManagerIsProxied(view.getAppearingDiv());
    }

    @Test
    public void shouldProxyDescendantStateManager() {
        BasicComponentsITestView view = openComponentsView();
        UiComponent dropdownBoxContainer = view.getViewComponent("#dropdownBoxContainer", UiComponent.class);
        List<UiComponent> descendantsBySelector = dropdownBoxContainer.getDescendantsBySelector("#selectId");
        assertFalse(descendantsBySelector.isEmpty());
        UiComponent descendantLvl1 = descendantsBySelector.get(0);
        verifyStateManagerIsProxied(descendantLvl1);
        List<UiComponent> options = descendantLvl1.getDescendantsBySelector("option");
        assertFalse(options.isEmpty());
        for (UiComponent option : options) {
            verifyStateManagerIsProxied(option);
        }
    }

    @Test
    public void shouldProxyChildStateManager() {
        BasicComponentsITestView view = openComponentsView();
        Select select = view.getSelect();
        List<UiComponent> children = select.getChildren();
        assertFalse(children.isEmpty());
        for (UiComponent child : children) {
            verifyStateManagerIsProxied(child);
        }
    }

    private void verifyStateManagerIsProxied(UiComponent simpleElt) {
        UiComponentStateManager stateManager = ((AbstractUiComponent) simpleElt).getStateManager();
        assertTrue(Proxy.isProxyClass(stateManager.getClass()));
    }
}
