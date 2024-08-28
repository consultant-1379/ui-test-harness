package com.ericsson.cifwk.taf.ui.core;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

class SeleniumUiComponentStateManagerFactory extends GenericUiComponentStateManagerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUiComponentStateManagerFactory.class);

    private UiComponentStateManagerProxyFactory stateManagerProxyFactory;

    public SeleniumUiComponentStateManagerFactory(UiMediator mediator) {
        super(mediator);
        stateManagerProxyFactory = new SeleniumUiComponentStateManagerProxyFactory();
    }

    /**
     * Overridden to return a proxied instance of <code>UiComponentStateManager</code>,
     * which always returns a fresh instance of component state manager,
     * or throws <code>UiComponentNotFoundException</code> if nothing was found.
     */
    @Override
    public UiComponentStateManager createStateManager(final UiComponentMappingDetails details) {
        return stateManagerProxyFactory.createStateManagerProxy(mediator, new Supplier<UiComponentStateManager>() {
            @Override
            public UiComponentStateManager get() {
                return mediator.retrieve(details).get(0);
            }
        }, details.toString());
    }

    @Override
    public UiComponentStateManager createStateManager(final UiComponentStateManager parentStateManager, final UiComponentMappingDetails mappingDetails) {
        final String compositeMappingInfo = getCompositeMappingInfo(parentStateManager, mappingDetails);
        return stateManagerProxyFactory.createStateManagerProxy(mediator, new Supplier<UiComponentStateManager>() {
            @Override
            public UiComponentStateManager get() {
                List<UiComponentStateManager> descendants = parentStateManager.getDescendantsByExpression(mappingDetails);
                if (descendants.isEmpty()) {
                    throw new UiComponentNotFoundException(compositeMappingInfo);
                }
                return descendants.get(0);
            }
        }, compositeMappingInfo);
    }

    /**
     * Overridden to return a proxied list, which always returns a fresh list of component state managers.
     */
    @Override
    public List<UiComponentStateManager> createStateManagers(UiComponentMappingDetails mappingDetails) {
        return createStateManagersInternal(mappingDetails, areDynamicListsOff(mappingDetails));
    }

    private List<UiComponentStateManager> createStateManagersInternal(final UiComponentMappingDetails details, boolean isStaticList) {
        if (isStaticList) {
            return proxyStateManagers(new Supplier<List<UiComponentStateManager>>() {
                @Override
                public List<UiComponentStateManager> get() {
                    return SeleniumUiComponentStateManagerFactory.super.createStateManagers(details);
                }
            });
        } else {
            return proxyStateManagerList(new Supplier<List<UiComponentStateManager>>() {
                @Override
                public List<UiComponentStateManager> get() {
                    try {
                        return mediator.retrieve(details);
                    } catch (UiComponentNotFoundException ignored){
                        LOGGER.trace("No components bound to collection. That is OK.", ignored);
                        return new ArrayList<>();
                    }
                }
            });
        }
    }

    @Override
    public List<UiComponentStateManager> createStateManagers(final UiComponentStateManager parentStateManager,
                                                             final UiComponentMappingDetails mappingDetails) {
        Supplier<List<UiComponentStateManager>> instanceSupplier = new Supplier<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> get() {
                return parentStateManager.getDescendantsByExpression(mappingDetails);
            }
        };
        return areDynamicListsOff(mappingDetails) ? proxyStateManagers(instanceSupplier) : proxyStateManagerList(instanceSupplier);
    }

    @SuppressWarnings("unchecked")
    @VisibleForTesting
    List<UiComponentStateManager> proxyStateManagerList(Supplier<List<UiComponentStateManager>> instanceSupplier) {
        return (List<UiComponentStateManager>) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{List.class},
                new DynamicStateManagerListInvocationHandler(stateManagerProxyFactory, mediator, instanceSupplier));
    }

    @VisibleForTesting
    List<UiComponentStateManager> proxyStateManagers(final Supplier<List<UiComponentStateManager>> sourceSupplier) {
        SimpleRetrySchemaInvocation simpleRetrySchemaInvocation = new SimpleRetrySchemaInvocation();
        return simpleRetrySchemaInvocation.perform(new Callable<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> call() throws Exception {
                List<UiComponentStateManager> result = Lists.newArrayList();
                for (UiComponentStateManager stateManager : sourceSupplier.get()) {
                    result.add(stateManagerProxyFactory.createStateManagerProxy(mediator, stateManager));
                }
                return result;
            }
        }, "instantiate UI component list");
    }

    @VisibleForTesting
    String getCompositeMappingInfo(UiComponentStateManager parentStateManager, UiComponentMappingDetails mappingDetails) {
        if (mappingDetails.isGlobalScope()) {
            return "MappingBySelector" + getShortRepresentation(mappingDetails);
        }

        return parentStateManager.getMappingInfo() + " > " + getShortRepresentation(mappingDetails);
    }

    @VisibleForTesting
    protected String getShortRepresentation(UiComponentMappingDetails mappingDetails) {
        StringBuilder sb = new StringBuilder("[");
        if (StringUtils.isNotBlank(mappingDetails.getId())) {
            sb.append("#").append(mappingDetails.getId());
        } else if (StringUtils.isNotBlank(mappingDetails.getSelector())) {
            sb.append(mappingDetails.getSelector());
        }
        sb.append(", selectorType=").append(mappingDetails.getSelectorType()).append("]");
        return sb.toString();
    }

    @VisibleForTesting
    boolean areDynamicListsOff(UiComponentMappingDetails mappingDetails) {
        return mappingDetails.isStaticList();
    }

}
