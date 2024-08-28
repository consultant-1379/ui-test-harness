package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Supplier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Proxy;

import static com.ericsson.cifwk.taf.ui.core.SelectorType.CSS;
import static com.ericsson.cifwk.taf.ui.core.SelectorType.XPATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeleniumUiComponentStateManagerFactoryTest {

    @Mock
    private UiMediator uiMediator;

    @Spy
    @InjectMocks
    private SeleniumUiComponentStateManagerFactory unit;

    @Test
    public void testGetCompositeMappingInfo() throws Exception {
        UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        when(stateManager.getMappingInfo()).thenReturn("parentMapping");

        Assert.assertEquals("parentMapping > [.clazz, selectorType=CSS]",
                unit.getCompositeMappingInfo(stateManager, new UiComponentMappingDetails(null, null, ".clazz", CSS)));
        Assert.assertEquals("parentMapping > [#id, selectorType=CSS]",
                unit.getCompositeMappingInfo(stateManager, new UiComponentMappingDetails("id", null, null, CSS)));
        Assert.assertEquals("parentMapping > [//div[0], selectorType=XPATH]",
                unit.getCompositeMappingInfo(stateManager, new UiComponentMappingDetails(null, null, "//div[0]", XPATH)));
    }

    @Test
    public void getShortRepresentation() {
        assertThat(unit.getShortRepresentation(new UiComponentMappingDetails(null, null, ".clazz", CSS))).isEqualTo("[.clazz, selectorType=CSS]");
        assertThat(unit.getShortRepresentation(new UiComponentMappingDetails("id", null, null, XPATH))).isEqualTo("[#id, selectorType=XPATH]");
    }

    @Test
    public void shouldProxyStateManagersIfDynamicListsAreOff() {
        verifyProxying(true);
    }

    @Test
    public void shouldProxyListIfDynamicListsAreOn() {
        verifyProxying(false);
    }

    @SuppressWarnings("unchecked")
    private void verifyProxying(boolean staticList) {
        doReturn(null).when(unit).proxyStateManagerList(any(Supplier.class));
        doReturn(null).when(unit).proxyStateManagers(any(Supplier.class));
        UiComponentStateManager parentStateManager = mock(UiComponentStateManager.class);
        UiComponentMappingDetails mappingDetails = new UiComponentMappingDetails();
        mappingDetails.setStaticList(staticList);
        unit.createStateManagers(parentStateManager, mappingDetails);
        if (staticList) {
            verify(unit).proxyStateManagers(any(Supplier.class));
        } else {
            verify(unit).proxyStateManagerList(any(Supplier.class));
        }
    }

    private void verifyObjectIsProxied(Object object, boolean isProxied) {
        assertThat(Proxy.isProxyClass(object.getClass()), is(isProxied));
    }

}