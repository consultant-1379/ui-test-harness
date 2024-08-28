package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.BrowserCookie;
import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import com.ericsson.cifwk.taf.ui.spi.AbstractUiEventProcessor;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;

class SeleniumUiMediator extends AbstractUiEventProcessor implements UiMediator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUiMediator.class);
    /**
     * List of temporary exceptions (the ones that sometimes can be legitimately received from operations on WebElement
     * due to the change of element's availability)
     */
    private static final Set<Class<? extends WebDriverException>> TEMPORARY_EXCEPTIONS
            = newHashSet(asList(StaleElementReferenceException.class, ElementNotVisibleException.class, WebDriverException.class));

    private final WebDriver driver;

    SeleniumUiMediator(WebDriver driver) {
        this.driver = driver;
    }

    List<UiComponentStateManager> createStateManagers(List<WebElement> elements, String mappingInfo) {
        List<UiComponentStateManager> result = new ArrayList<>(elements.size());
        for (WebElement element : elements) {
            result.add(
                    new SeleniumUiComponentStateManager(driver, new SeleniumUiComponentStateManagerFactory(this),
                            element, mappingInfo, this));
        }

        return result;
    }

    @Override
    public List<UiComponentStateManager> retrieve(final UiComponentMappingDetails details) {
        List<UiComponentStateManager> stateManagers = new ArrayList<>();
        if(!Strings.isNullOrEmpty(details.getId())){
            stateManagers.addAll(getById(details));
        } else if (!Strings.isNullOrEmpty(details.getName())){
            stateManagers.addAll(getByName(details));
        } else if(!Strings.isNullOrEmpty(details.getSelector())){
            stateManagers.addAll(getBySelector(details));
        }
        if(stateManagers.isEmpty()){
            throw new UiComponentNotFoundException(details.toString());
        }
        return stateManagers;
    }

    private List<UiComponentStateManager> getByName(final UiComponentMappingDetails details) {
        String name = details.getName();
        List<WebElement> elements = findElements(By.name(name));
        return createStateManagers(elements, details.toString());
    }

    private List<UiComponentStateManager> getById(final UiComponentMappingDetails details) {
        String id = details.getId();
        List<WebElement> elements = findElements(By.id(id));
        return createStateManagers(elements, details.toString());
    }

    private List<UiComponentStateManager> getBySelector(final UiComponentMappingDetails details) {
        String selector = details.getSelector();
        SelectorType selectorType = details.getSelectorType();
        List<WebElement> elements = getWebElements(selector, selectorType);
        return createStateManagers(elements, details.toString());
    }

    private List<WebElement> getWebElements(final String selector, final SelectorType selectorType) {
        final List<WebElement> elements;
        switch (selectorType) {
            case XPATH:
                elements = findByXpathExpression(selector);
                break;
            case CSS:
            case DEFAULT:
                elements = findByCssSelector(selector);
                break;
            default:
                throw new UnsupportedOperationException("'" + selectorType + "' selector is not supported yet");
        }
        return elements;
    }

    List<WebElement> findByXpathExpression(String expression) {
        return findElements(By.xpath(expression));
    }

    List<WebElement> findByCssSelector(String expression) {
        return findElements(By.cssSelector(expression));
    }

    private List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    @Override
    public String copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendKey(int key, int... modifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendKeys(String keys, int... modifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        // intentionally empty
    }

    @Override
    public void maximize() {
        driver.manage().window().maximize();
    }

    @Override
    public void minimize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public void back() {
        driver.navigate().back();
    }

    @Override
    public void forward() {
        driver.navigate().forward();
    }

    @Override
    public void refresh() {
        driver.navigate().refresh();
    }

    @Override
    public MessageBox getMessageBox() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, UiToolkit.getDefaultWaitTimeout() / 1000);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            return new SeleniumMessageBox(alert);
        } catch (TimeoutException e) {
            LOGGER.debug("Timed out", e);
            return null;
        } catch (NoAlertPresentException e) {
            LOGGER.debug("No Alert present", e);
            return null;
        }
    }

    @Override
    public UiComponentSize getWindowSize() {
        Window window = driver.manage().window();
        Dimension windowSize = window.getSize();

        return new UiComponentSize(windowSize.getWidth(), windowSize.getHeight());
    }

    private UiComponent createUiComponent(WebElement element) {
        UiComponentStateManagerFactory stateManagerFactory = new SeleniumUiComponentStateManagerFactory(this);
        UiComponentFactory componentFactory = new UiComponentFactoryImpl(stateManagerFactory.getAutowirer());
        return componentFactory.instantiateComponent(
                new SeleniumUiComponentStateManager(driver, stateManagerFactory, element, null, this),
                UiComponent.class);
    }

    /**
     * Evaluates the expression as a Javascript expression. The script fragment provided will be executed as the body of an anonymous function.
     * <p/>
     * <p/>
     * Within the script, use <code>document</code> to refer to the current document. Note that local
     * variables will not be available once the script has finished executing, though global variables
     * will persist.
     * <p/>
     * <p/>
     * If the script has a return value (i.e. if the script contains a <code>return</code> statement),
     * then the following steps will be taken:
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
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        Object scriptResult = javascriptExecutor.executeScript(expression);
        LOGGER.info("Script result = {}", scriptResult);
        return convertWebElements(scriptResult);
    }

    @VisibleForTesting
    Object convertWebElements(Object rawObject) {
        if(rawObject instanceof List){
            List<Object> objects = (List<Object>) rawObject;
            List<Object> typedCastedobjects = new ArrayList<>();
            for (Object object : objects) {
                if(object instanceof WebElement){
                    typedCastedobjects.add(createUiComponent((WebElement)object));
                }else if (object instanceof List) {
                    typedCastedobjects.add(convertWebElements(object));
                }else {
                    typedCastedobjects.add(object);
                }
            }
            return typedCastedobjects;
        }else if(rawObject instanceof WebElement) {
            return createUiComponent((WebElement)rawObject);
        }
        return rawObject;
    }

    @Override
    public void dragAndDropTo(UiComponent object, UiComponent target) {
        WebElement sourceElt = getWebElement(object);
        WebElement targetElt = getWebElement(target);

        Actions actions = new Actions(driver);
        // Doesn't work with HTML5 drag&drops: https://code.google.com/p/selenium/issues/detail?id=3604
        actions.dragAndDrop(sourceElt, targetElt).build().perform();
    }

    @Override
    public Optional<InputStream> takeScreenshot() {
        LOGGER.info("Taking screenshot...");
        if (!(driver instanceof TakesScreenshot)) {
            LOGGER.warn("Driver does not support taking screenshots : {}", driver.getClass().getName());
            return Optional.absent();
        }

        String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        byte[] bytes = BaseEncoding.base64().decode(base64);
        LOGGER.info("Taking screenshot completed");
        return Optional.<InputStream>of(new ByteArrayInputStream(bytes));
    }

    @Override
    public void setWindowSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @Override
    public UiActions newActionChain() {
        return new SeleniumUiActions(driver);
    }

    private WebElement getWebElement(UiComponent object) {
        SeleniumElementAwareComponent stateManager = (SeleniumElementAwareComponent) ((AbstractUiComponent) object).getStateManager();
        return stateManager.getElement();
    }

    @Override
    public void processEvent(UiEvent event, Map<String, ?> arguments, UiComponentCallback callback) {
        switch (event) {
            case WEB_GET_COOKIES:
                getCookies(arguments, callback);
                break;
            case DROP_FILE_TO_UPLOAD:
                dropFileToUpload((String) arguments.get(UiProperties.PATH), (UiComponent) arguments.get(UiProperties.TARGET));
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void getCookies(Map<String, ?> arguments, UiComponentCallback callback) {
        if (arguments.isEmpty() || !arguments.containsKey(UiProperties.COOKIE_NAME)) {
            callback.onFinish(getAllCookies());
        } else {
            String cookieName = (String) arguments.get(UiProperties.COOKIE_NAME);
            Cookie cookie = driver.manage().getCookieNamed(cookieName);
            callback.onFinish(createBrowserCookie(cookie));
        }
    }

    static Set<Class<? extends WebDriverException>> getTemporaryExceptionList() {
        return TEMPORARY_EXCEPTIONS;
    }

    Set<BrowserCookie> getAllCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        Set<BrowserCookie> result = newHashSet();
        for (Cookie cookie : cookies) {
            BrowserCookie browserCookie = createBrowserCookie(cookie);
            result.add(browserCookie);
        }
        return result;
    }

    private BrowserCookie createBrowserCookie(Cookie cookie) {
        if (cookie == null) {
            return null;
        }
        return new BrowserCookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getExpiry(), cookie.isSecure());
    }

    private void dropFileToUpload(String filePath, UiComponent dropTarget) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        if (!isJQuerySupported()) {
            addJQuerySupport();
        }

        // append input to HTML to add file path
        String tmpFileInputId = "tafUiTmpFileUploadInput";
        javascriptExecutor.executeScript(tmpFileInputId + " = $('<input id=\"" + tmpFileInputId + "\"/>').attr({type:'file'}).appendTo('body');");

        // set file path to file input
        driver.findElement(By.id(tmpFileInputId)).sendKeys(filePath);

        // fire mock event pointing to inserted file path
        javascriptExecutor.executeScript("e = $.Event('drop'); " +
                "e.originalEvent = {dataTransfer : { files : " + tmpFileInputId + ".get(0).files } }; " +
                "$(arguments[0]).trigger(e);", getWebElement(dropTarget));

        // remove tmp file input
        javascriptExecutor.executeScript("$('#" + tmpFileInputId + "').remove()");
    }

    private void addJQuerySupport() {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("var jq = document.createElement('script');jq.src = '//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js';document.getElementsByTagName('head')[0].appendChild(jq);");
        try {
            // small delay needed for jQuery injection to kick in
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isJQuerySupported() {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        Object result = javascriptExecutor.executeScript("return typeof jQuery == 'undefined'");
        return !(Boolean) result;
    }

}
