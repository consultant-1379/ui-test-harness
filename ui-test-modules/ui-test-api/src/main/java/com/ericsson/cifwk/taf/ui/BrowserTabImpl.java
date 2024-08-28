package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.AbstractUiComponent;
import com.ericsson.cifwk.taf.ui.core.ProxyObjectCommons;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentAutowirer;
import com.ericsson.cifwk.taf.ui.core.UiComponentCallback;
import com.ericsson.cifwk.taf.ui.core.UiComponentMappingDetails;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.core.UiComponentStateManagerFactory;
import com.ericsson.cifwk.taf.ui.core.UiEvent;
import com.ericsson.cifwk.taf.ui.core.UiProperties;
import com.ericsson.cifwk.taf.ui.debug.DebugBrowserTab;
import com.ericsson.cifwk.taf.ui.debug.DebugDelayer;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentBasedDelayer;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.ericsson.cifwk.taf.ui.spi.UiWindowProvider;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation-agnostic representation of a browser tab (a Web page)
 */
public class BrowserTabImpl extends AbstractUiWindow implements BrowserTab, DebugBrowserTab, InternalDriverAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserTabImpl.class);

    private static final Set<Class<?>> VERIFIED_VIEW_MODELS = Sets.newConcurrentHashSet();

    private final UiComponentAutowirer componentAutowirer;

    private final UiComponentStateManagerFactory stateManagerFactory;

    private final UiWindowProvider<BrowserTab> windowProvider;

    private final String descriptor;

    private final UiMediator mediator;

    protected boolean closed;

    public BrowserTabImpl(
            UiComponentAutowirer componentAutowirer,
            UiComponentStateManagerFactory stateManagerFactory,
            UiWindowProvider<BrowserTab> windowProvider,
            String descriptor,
            UiMediator mediator) {
        this.componentAutowirer = componentAutowirer;
        this.stateManagerFactory = stateManagerFactory;
        this.windowProvider = windowProvider;
        this.descriptor = descriptor;
        this.mediator = mediator;
        this.closed = false;
    }

    @Override
    public String getCurrentUrl() {
        return mediator.getCurrentUrl();
    }

    /**
     * @return current page title
     */
    @Override
    public String getTitle() {
        return mediator.getTitle();
    }

    @Override
    public void back() {
        mediator.back();
    }

    @Override
    public void forward() {
        mediator.forward();
    }

    @Override
    public ViewModel getGenericView() {
        GenericViewModel viewModel = new GenericViewModel();
        populateGenericViewModel(viewModel);
        return stubViewModel(viewModel, GenericViewModel.class);
    }

    @Override
    public void markAsClosed() {
        closed = true;
    }

    /**
     * @return a unique windows descriptor representing this tab
     */
    @Override
    public String getWindowDescriptor() {
        return descriptor;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void refreshPage() {
        mediator.refresh();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T getView(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            if (!(instance instanceof GenericViewModel)) {
                throw new IllegalArgumentException("View model class must extend " + GenericViewModel.class.getName());
            }
            GenericViewModel genericViewModel = (GenericViewModel) instance;
            populateGenericViewModel(genericViewModel);
            componentAutowirer.initialize(genericViewModel);
            // TODO: temporary workaround for final methods in ViewModel, use ByteBuddy to stub them.
            if (noPublicFinalMethods(clazz)) {
                return stubViewModel((T) genericViewModel, clazz);
            } else {
                if (!VERIFIED_VIEW_MODELS.contains(clazz)) {
                    LOGGER.warn("View model {} has public final methods - won't be synchronized when using multiple tabs", clazz);
                    VERIFIED_VIEW_MODELS.add(clazz);
                }
                return (T) genericViewModel;
            }
        } catch (Exception e) {
            throw new RuntimeException(e); // NOSONAR
        }
    }

    @VisibleForTesting
    <T extends ViewModel> boolean noPublicFinalMethods(Class<T> clazz) {
        Method[] classMethods = clazz.getMethods();
        for (Method method : classMethods) {
            // We analyze only methods of GenericViewModel successors
            Class<?> vmClass = method.getDeclaringClass();
            Class<?> vmSuperClass = vmClass.getSuperclass();
            if (vmSuperClass != null
                    && GenericViewModel.class.isAssignableFrom(vmSuperClass)
                    && Modifier.isFinal(method.getModifiers())) {
                return false;
            }
        }
        return true;
    }

    private void populateGenericViewModel(GenericViewModel viewModel) {
        viewModel.setWindowDescriptor(descriptor);
        viewModel.setComponentStateManagerFactory(stateManagerFactory);
        viewModel.setDelayer(getDelayer());
        viewModel.setMediator(mediator);
    }

    @SuppressWarnings("unchecked")
    private <T extends ViewModel> T stubViewModel(T original, Class<T> modelClass) {
        ProxyFactory factory = new ProxyFactory();
        try {
            factory.setSuperclass(modelClass);
            factory.setFilter(new ViewModelMethodFilter());
            Class<T> clazz = factory.createClass();
            MethodHandler handler = new GenericViewModelMethodHandler<>(windowProvider, original);
            T instance = clazz.newInstance();
            ((ProxyObject) instance).setHandler(handler);
            return instance;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public MessageBox getMessageBox() {
        return mediator.getMessageBox();
    }

    /**
     * Opens a URL in the browser tab
     *
     * @param url url to open
     */
    @Override
    public void open(String url) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("url", url);
        attributes.put("_self", "");
        windowProvider.get(attributes);
    }

    public void activate() {
        windowProvider.switchWindow(this);
    }

    @Override
    protected UiComponentBasedDelayer getDelayer() {
        return windowProvider.getExecutionDelayer();
    }

    @Override
    public void maximize() {
        mediator.maximize();
    }

    @Override
    public UiComponentSize getSize() {
        return mediator.getWindowSize();
    }

    /**
     * Evaluates the expression as a Javascript expression. The script fragment provided will be executed as the body of an anonymous
     * function.
     * <p/>
     * <p/>
     * Within the script, use <code>document</code> to refer to the current document. Note that local variables will not be available once
     * the script has finished executing, though global variables will persist.
     * <p/>
     * <p/>
     * If the script has a return value (i.e. if the script contains a <code>return</code> statement), then the following steps will be
     * taken:
     * <p/>
     * <ul>
     * <li>For an HTML element, this method returns a Uicomponent</li>
     * <li>For a decimal, a Double is returned</li>
     * <li>For a non-decimal number, a Long is returned</li>
     * <li>For a boolean, a Boolean is returned</li>
     * <li>For all other cases, a String is returned.</li>
     * <li>For an array, return a List<Object> with each object following the rules above. We support nested lists.<UiComponent></li>
     * <li>Unless the value is null or there is no return value, in which null is returned</li>
     * </ul>
     */
    @Override
    public Object evaluate(String expression) {
        return mediator.evaluate(expression);
    }

    /**
     * Drags <code>object</code> and drops to <code>target</code>. Doesn't support HTML5 native drag-and-drop
     *
     * @param object draggable object
     * @param target drop target
     */
    @Override
    public void dragAndDropTo(UiComponent object, UiComponent target) {
        mediator.dragAndDropTo(object, target);
    }

    @Override
    public String copy() {
        return mediator.copy();
    }

    @Override
    public void sendKey(int key, int... modifiers) {
        mediator.sendKey(key, modifiers);
    }

    @Override
    public void sendKeys(String keys, int... modifiers) {
        mediator.sendKeys(keys, modifiers);
    }

    @Override
    public Set<BrowserCookie> getCookies() {
        final AtomicReference<Set<BrowserCookie>> result = new AtomicReference<>();
        mediator.processEvent(UiEvent.WEB_GET_COOKIES, new UiComponentCallback() {
            @SuppressWarnings("unchecked")
            @Override
            public void onFinish(Object value) {
                result.set((Set<BrowserCookie>) value);
            }
        });
        return result.get();
    }

    @Override
    public BrowserCookie getCookie(String cookieName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(cookieName), "Cookie name cannot be blank");

        final AtomicReference<BrowserCookie> result = new AtomicReference<>();
        Map<String, String> argumentMap = Maps.newHashMap();
        argumentMap.put(UiProperties.COOKIE_NAME, cookieName);
        mediator.processEvent(UiEvent.WEB_GET_COOKIES, argumentMap, new UiComponentCallback() {
            @Override
            public void onFinish(Object value) {
                result.set((BrowserCookie) value);
            }
        });
        return result.get();
    }

    @Override
    public void dragAndDropForUpload(File file, UiComponent to) {
        Map<String, Object> argumentMap = Maps.newHashMap();
        argumentMap.put(UiProperties.PATH, file.getAbsolutePath());
        argumentMap.put(UiProperties.TARGET, to);
        mediator.processEvent(UiEvent.DROP_FILE_TO_UPLOAD, argumentMap);
    }

    @Override
    public void dragAndDropForUpload(InputStream inputStream, UiComponent dropTarget, String targetFileName) {
        try {
            Path tmpFilePath = Paths.get(System.getProperty("java.io.tmpdir"), targetFileName);
            Path uploadableFilePath = Files.createFile(tmpFilePath);
            File file = uploadableFilePath.toFile();
            writeInputStreamToFile(file.getAbsolutePath(), inputStream, false);
            dragAndDropForUpload(file, dropTarget);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void setSize(int width, int height) {
        mediator.setWindowSize(width, height);
    }

    @Override
    public void setSize(BrowserSetup.Resolution resolution) {
        setSize(resolution.width, resolution.height);
    }

    @Override
    public void takeScreenshot(String screenshotNameInTheReport) {
        createScreenshot(screenshotNameInTheReport);
    }

    @Override
    public UiActions newActionChain() {
        return mediator.newActionChain();
    }

    @Override
    public String toString() {
        return "BrowserTab [descriptor=" + descriptor + ", closed=" + closed + "]";
    }

    @Override
    protected UiMediator getMediator() {
        return mediator;
    }

    @Override
    public <D> D getInternalDriver() {
        return windowProvider.getInternalDriver();
    }

    @Override
    public WebElement waitUntilComponentIsDisplayedUsingDriver(UiComponent component, long timeoutInMillis){
        UiComponentMappingDetails componentInfoGeneral = component.getComponentDetails();
        String selectorGeneral = componentInfoGeneral.getSelector();
        SelectorType typeGeneral = componentInfoGeneral.getSelectorType();
        LOGGER.info("Selector Type being:: " + typeGeneral);
        LOGGER.info("Selector Name  being:: " + selectorGeneral);

        WebDriver webDriver = this.getInternalDriver();
        WebDriverWait waitUserList=new WebDriverWait(webDriver, TimeUnit.MILLISECONDS.toSeconds(timeoutInMillis));
        WebElement elementGeneral = null;

        if(SelectorType.CSS.equals(typeGeneral)){
            LOGGER.info("We are waiting for an Element of CSS Identity!!");
            elementGeneral = waitUserList.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selectorGeneral)));
        } else if(SelectorType.XPATH.equals(typeGeneral)){
            LOGGER.info("We are waiting for an Element of XPATH Identity!!");
            elementGeneral = waitUserList.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(selectorGeneral)));
        }
        return elementGeneral;
    }



    @Override
    public UiComponentBasedDelayer setDebugMode(int delay) {
        UiComponentBasedDelayer delayer = windowProvider.getExecutionDelayer();
        ((DebugDelayer) delayer).setDebugMode(mediator, delay);
        return delayer;
    }

    @Override
    public void unsetDebugMode() {
        ((DebugDelayer) windowProvider.getExecutionDelayer()).unsetDebugMode();
    }

    private class ViewModelMethodFilter implements MethodFilter {
        public boolean isHandled(Method method) {
            // ignore basic Object methods
            String methodName = method.getName();
            return !ProxyObjectCommons.shouldSkipPreProcessingFor(methodName);
        }
    }

}