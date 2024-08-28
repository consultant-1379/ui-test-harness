package com.ericsson.cifwk.taf.ui.core;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.MenuItem;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of {@link com.ericsson.cifwk.taf.ui.core.UiComponent}
 */
public abstract class AbstractUiComponent implements UiComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUiComponent.class);
    private UiComponentFactory componentFactory;
    protected UiComponentStateManager stateManager;
    protected String mappingInfo;
    private WaitHelper waitHelper;

    protected AbstractUiComponent() {
        // required by CGLIB
    }

    public AbstractUiComponent(UiComponentStateManager stateManager) {
        setStateManager(stateManager);
    }

    protected UiComponentStateManager getStateManager() {
        return stateManager;
    }

    @Override
    public final void setStateManager(UiComponentStateManager stateManager) {
        this.stateManager = stateManager;
        this.mappingInfo = (stateManager == null) ? null : stateManager.getMappingInfo();
        waitHelper = new WaitHelper(stateManager);
    }

    @Override
    public void waitUntil(GenericPredicate condition, long timeoutInMillis) {
        waitHelper.waitUntil(condition, timeoutInMillis);
    }

    @Override
    public void waitUntil(GenericPredicate condition) {
        waitHelper.waitUntil(condition, UiToolkit.getDefaultWaitTimeout());
    }

    @Override
    public <C extends UiComponent> void waitUntil(C component, Predicate<C> condition, long timeoutInMillis) {
        waitHelper.waitUntil(component, condition, timeoutInMillis);
    }

    @Override
    public <C extends UiComponent> void waitUntil(C component, Predicate<C> condition) {
        waitHelper.waitUntil(component, condition, UiToolkit.getDefaultWaitTimeout());
    }

    final void setAutowirer(UiComponentAutowirer autowirer) {
        this.componentFactory = new UiComponentFactoryImpl(autowirer);
    }

    @Override
    public String getProperty(String propertyName) {
        return stateManager.getProperty(propertyName);
    }

    public void setProperty(String name, String value) {
        stateManager.setProperty(name, value);
    }

    @Override
    public String getId() {
        return getProperty(UiProperties.ID);
    }

    @Override
    public String getComponentName() {
        return stateManager.getComponentName();
    }

    /**
     * Returns visible text of this component. All included tags are stripped.
     */
    @Override
    public String getText() {
        return getProperty(UiProperties.TEXT);
    }

    @Override
    public boolean isDisplayed() {
        try {
            return stateManager.isDisplayed();
        } catch (UiComponentNotFoundException e) { // NOSONAR
            LOGGER.trace("Target Element is not being displayed!!");
            return false;
        }
    }

    @Override
    public void focus() {
        stateManager.processEvent(UiEvent.FOCUS);
    }

    @Override
    public boolean hasFocus() {
        return stateManager.hasFocus();
    }

    @Override
    public void click() {
        stateManager.processEvent(UiEvent.CLICK);
    }

    @Override
    public void contextClick() {
        stateManager.processEvent(UiEvent.CONTEXT_CLICK);
    }

    @Override
    public boolean exists() {
        if (stateManager == null) {
            return false;
        }

        try {
            stateManager.isDisplayed();
        } catch (UiComponentNotFoundException e) { // NOSONAR
            return false;
        }

        return true;
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        stateManager.sendKeys(keysToSend);
    }

    @Override
    public void mouseOver(int... coordinates) {
        stateManager.mouseOverAt(coordinates);
    }

    @Override
    public void mouseDown(int... coordinates) {
        stateManager.mouseDownAt(coordinates);
    }

    @Override
    public void mouseUp(int... coordinates) {
        stateManager.mouseUpAt(coordinates);
    }

    @Override
    public void mouseMove(int... coordinates) {
        stateManager.mouseMoveAt(coordinates);
    }

    @Override
    public void mouseOut() {
        stateManager.mouseOut();
    }

    @Override
    public boolean isSelected() {
        return stateManager.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return stateManager.isEnabled();
    }

    @Override
    public UiComponentSize getSize() {
        return stateManager.getSize();
    }

    @Override
    public MenuItem getMenuItem(String menuName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return stateManager.getAsString();
    }

    @Override
    public List<UiComponent> getChildren() {
        List<UiComponentStateManager> stateManagers = getDynamicEntityList(new Supplier<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> get() {
                return processEvent(UiEvent.GET_CHILDREN);
            }
        });
        return createUiComponents(stateManagers, UiComponent.class);
    }

    @SuppressWarnings("unchecked")
    private List<UiComponentStateManager> getDynamicEntityList(final Supplier<List<UiComponentStateManager>> listSupplier) {
        return (List<UiComponentStateManager>) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{List.class},
                new DescendantStateManagerListInvocationHandler(listSupplier));
    }

    @Override
    public <T extends UiComponent> Optional<T> getFirstDescendantBySelector(String selector, Class<T> type){
        return getFirstDescendantBySelector(SelectorType.DEFAULT, selector, type);
    }

    @Override
    public <T extends UiComponent> Optional<T> getFirstDescendantBySelector(SelectorType selectorType, String selector, Class<T> type){
        final List<T> descendantsBySelector = getDescendantsBySelector(selectorType, selector, type);
        final T firstDescendant = descendantsBySelector.isEmpty() ? null : descendantsBySelector.get(0);
        return Optional.fromNullable(firstDescendant);
    }

    @Override
    public List<UiComponent> getDescendantsBySelector(String selector) {
        return getDescendantsBySelector(selector, UiComponent.class);
    }

    @Override
    public <T extends UiComponent> List<T> getDescendantsBySelector(String selector, Class<T> type){
        return getDescendantsBySelector(SelectorType.DEFAULT, selector, type);
    }

    private <T extends UiComponent> List<T> createUiComponents(List<UiComponentStateManager> stateManagers, final Class<T> componentClass) {
        return componentFactory.instantiateComponentList(stateManagers, componentClass, false);
    }

    @Override
    public List<UiComponent> getDescendantsBySelector(final SelectorType selectorType, final String selector) {
        return getDescendantsBySelector(selectorType, selector, UiComponent.class);
    }

    @Override
    public <T extends UiComponent> List<T> getDescendantsBySelector(final SelectorType selectorType, final String selector, final Class<T> type){
        List<UiComponentStateManager> stateManagers = getDynamicEntityList(new Supplier<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> get() {
                return processEvent(UiEvent.GET_DESCENDANTS_BY_SELECTOR,
                        Collections.singletonMap(UiEventData.UI_COMPONENT_SELECTOR, new UiComponentMappingDetails(selectorType, selector)));
            }
        });
        return createUiComponents(stateManagers, type);
    }

    /**
     * @deprecated
     */
    @Override
    @Deprecated
    public UiComponentSelector getComponentSelector() {
        UiComponentMappingDetails selector = stateManager.getComponentDetails();
        return new UiComponentSelector(selector.getSelectorType(), selector.getSelector());
    }

    @Override
    public UiComponentMappingDetails getComponentDetails() {
        return stateManager.getComponentDetails();
    }

    @Override
    public <T extends UiComponent> T as(Class<T> clazz) {
        if (clazz.isAssignableFrom(this.getClass()))
            return clazz.cast(this);

        return componentFactory.instantiateComponent(stateManager, clazz);
    }

    protected <T> T processEvent(UiEvent event) {
        return processEvent(event, null);
    }

    protected <T> T processEvent(UiEvent event, Map<String, ?> arguments) {
        final AtomicReference<T> result = new AtomicReference<>();
        UiComponentCallback callback = new UiComponentCallback() {
            @SuppressWarnings("unchecked")
            @Override
            public void onFinish(Object value) {
                result.set((T) value);
            }
        };
        if (arguments != null) {
            stateManager.processEvent(event, arguments, callback);
        } else {
            stateManager.processEvent(event, callback);
        }

        return result.get();
    }

    @Override
    public UiActions createUiActions() {
        return stateManager.createUiActions();
    }

    @Override
    public Object evaluate(String expression) {
        return stateManager.evaluate(this, expression);
    }

}
