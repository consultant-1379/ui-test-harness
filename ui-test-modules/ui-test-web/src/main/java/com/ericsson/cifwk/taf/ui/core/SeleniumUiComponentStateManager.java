package com.ericsson.cifwk.taf.ui.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.ericsson.cifwk.taf.ui.sdk.Option;
import com.ericsson.cifwk.taf.ui.spi.AbstractUiEventProcessor;
import com.ericsson.cifwk.taf.ui.spi.UiActions;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SeleniumUiComponentStateManager extends AbstractUiEventProcessor implements UiComponentStateManager, SeleniumElementAwareComponent {

    private static final String VALUE = "value";
    private WebElement element;
    private UiMediator uiMediator;
    private final WebDriver driver;
    private final String mapping;
    private final UiComponentStateManagerFactory stateManagerFactory;
    private final UiComponentFactory componentFactory;
    private UiComponentMappingDetails thisComponentSelector;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUiComponent.class);

    SeleniumUiComponentStateManager(WebDriver driver, UiComponentStateManagerFactory stateManagerFactory, WebElement element, String mapping, UiMediator uiMediator) {
        this.driver = driver;
        this.stateManagerFactory = stateManagerFactory;
        this.element = element;
        this.uiMediator = uiMediator;
        if (mapping == null) {
            this.mapping = getElementXPathLocation(element);
        } else {
            this.mapping = mapping;
        }
        this.componentFactory = new UiComponentFactoryImpl(stateManagerFactory.getAutowirer());
    }

    @Override
    public String getProperty(String name) {
        if (StringUtils.equals(name, UiProperties.TEXT) || StringUtils.equals(name, "innerText")) {
            return getText();
        } else if (StringUtils.equals(name, UiProperties.VALUE)) {
            return getValue();
        } else if (StringUtils.equals(name, UiProperties.URL)) {
            return getElement().getAttribute("href");
        }
        return getElement().getAttribute(name);
    }

    protected String getText() {
        WebElement webElement = getElement();
        if (StringUtils.equalsIgnoreCase("select", webElement.getTagName())) {
            Select select = new Select(webElement);
            WebElement firstSelectedOption = select.getFirstSelectedOption();
            return firstSelectedOption.getText();
        } else if (StringUtils.equalsIgnoreCase("input", webElement.getTagName())) {
            String value = webElement.getAttribute(VALUE);
            if (!StringUtils.isBlank(value)) {
                return value;
            }
        }
        return webElement.getText();
    }

    protected String getValue() {
        WebElement webElement = getElement();
        if (StringUtils.equalsIgnoreCase("select", webElement.getTagName())) {
            Select select = new Select(webElement);
            WebElement firstSelectedOption = select.getFirstSelectedOption();
            return firstSelectedOption.getAttribute(VALUE);
        }
        return webElement.getAttribute(VALUE);
    }

    @Override
    public void setProperty(String name, String value) {
        if (name.equals(UiProperties.VALUE)) {
            getElement().clear();
            sendText(value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void sendText(String value) {
        Preconditions.checkArgument(value != null, "Text passed to element cannot be null");
        getElement().sendKeys(value);
    }

    @Override
    public void processEvent(UiEvent event, Map<String, ?> arguments, UiComponentCallback callback) {
        switch (event) {
            case CLICK:
                click();
                break;
            case CONTEXT_CLICK:
                contextClick();
                break;
            case SELECT:
                select(arguments);
                break;
            case FOCUS:
                focus();
                break;
            case SELECTED_OPTIONS:
                selectedOptions(callback);
                break;
            case ALL_OPTIONS:
                allOptions(callback);
                break;
            case GET_CHILDREN:
                getChildren(callback);
                break;
            case GET_DESCENDANTS_BY_SELECTOR:
                getDescendants(arguments, callback);
                break;
            case GET_SELECTOR:
                getSelector(callback);
                break;
            case CLEAR:
                clear();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void getSelector(UiComponentCallback callback) {
        callback.onFinish(getComponentDetails());
    }

    private void clear() {
        getElement().clear();
    }

    private void getDescendants(Map<String, ?> arguments, UiComponentCallback callback) {
        UiComponentMappingDetails selector = (UiComponentMappingDetails) arguments.get(UiEventData.UI_COMPONENT_SELECTOR);
        List<UiComponentStateManager> descendantsBySelector = getDescendantsByExpression(selector.getSelectorType(),
                selector.getSelector());
        callback.onFinish(descendantsBySelector);
    }

    private void getChildren(UiComponentCallback callback) {
        List<UiComponentStateManager> children = getDescendantsByExpression(SelectorType.XPATH, "./*");
        callback.onFinish(children);
    }

    private void allOptions(UiComponentCallback callback) {
        List<Option> options = getAllOptions();
        callback.onFinish(options);
    }

    private void selectedOptions(UiComponentCallback callback) {
        List<Option> selectedOptions = getSelectedOptions();
        callback.onFinish(selectedOptions);
    }

    private void contextClick() {
        Actions actions = new Actions(driver);
        actions
                .contextClick(getElement())
                .build()
                .perform();
    }

    private void click() {
        getElement().click();
    }

    private void focus() {
        WebElement webElement = getElement();
        new Actions(driver)
                .moveToElement(webElement)
                .perform(); //required with Firefox

        String js = "arguments[0].focus();";
        evaluateScript(js, webElement);
    }

    @Override
    public boolean hasFocus() {
        WebElement activeElement = driver
                .switchTo()
                .activeElement();
        return getElement().equals(activeElement);
    }

    String localizeXpathExpression(String selectorExpression) {
        if (StringUtils.startsWith(selectorExpression, "/")) {
            return "." + selectorExpression;
        } else if (StringUtils.startsWith(selectorExpression, "./")) {
            return selectorExpression;
        }
        return "./" + selectorExpression;
    }

    @Override
    public List<UiComponentStateManager> getDescendantsByExpression(SelectorType selectorType, String expression) {
        return getDescendantsFrom(selectorType, expression, getElement());
    }

    private List<UiComponentStateManager> getDescendantsFrom(SelectorType selectorType, String expression, SearchContext container) {
        Preconditions.checkArgument(selectorType != null);
        Preconditions.checkArgument(StringUtils.isNotBlank(expression));

        List<UiComponentStateManager> result = Lists.newArrayList();
        if (!exists()) {
            return result;
        }

        List<WebElement> descendantElements;
        switch (selectorType) {
            case XPATH:
                descendantElements = container.findElements(By.xpath(localizeXpathExpression(expression)));
                break;
            case CSS:
            case DEFAULT:
                descendantElements = container.findElements(By.cssSelector(expression));
                break;
            default:
                throw new UnsupportedOperationException();
        }
        for (WebElement childElement : descendantElements) {
            UiComponentMappingDetails childSelector = getComponentSelector(childElement);
            UiComponentStateManager childStateManager = stateManagerFactory.createStateManager(childSelector);
            result.add(childStateManager);
        }
        return result;
    }

    @Override
    public List<UiComponentStateManager> getDescendantsByExpression(UiComponentMappingDetails mappingDetails) {
        SelectorType selectorType = mappingDetails.getSelectorType();
        String selector = mappingDetails.getSelector();
        if (mappingDetails.isGlobalScope()) {
            return getDescendantsFrom(selectorType, selector, driver);
        }
        return getDescendantsByExpression(selectorType, selector);
    }

    private UiComponentMappingDetails getComponentSelector(WebElement element) {
        String id = element.getAttribute(UiProperties.ID);
        if (!StringUtils.isBlank(id)) {
            return new UiComponentMappingDetails(SelectorType.CSS, "#" + id);
        }
        return new UiComponentMappingDetails(SelectorType.XPATH, getElementXPathLocation(element));
    }

    @SuppressWarnings("squid:S00105")
    private String getElementXPathLocation(WebElement element) {
        String jscript = "function getElementXPath(elt) {\r\n" +
                "		var path = \"\";\r\n" +
                "		for (; elt && elt.nodeType == 1; elt = elt.parentNode) {\r\n" +
                "	            idx = getElementIdx(elt);\r\n" +
                "                   xname = elt.tagName;\r\n" +
                "		    if (idx > 1)\r\n" +
                "			xname += \"[\" + idx + \"]\";\r\n" +
                "		    path = \"/\" + xname + path;\r\n" +
                "		}\r\n" +
                "\r\n" +
                "		return path;\r\n" +
                "	}\r\n" +
                "\r\n" +
                "	function getElementIdx(elt) {\r\n" +
                "		var count = 1;\r\n" +
                "		for ( var sib = elt.previousSibling; sib; sib = sib.previousSibling) {\r\n" +
                "			if (sib.nodeType == 1 && sib.tagName == elt.tagName)\r\n" +
                "				count++\r\n" +
                "		}\r\n" +
                "\r\n" +
                "		return count;\r\n" +
                "	}" +
                "return getElementXPath(arguments[0]);";
        String result = (String) evaluateScript(jscript, element);
        return StringUtils.lowerCase(result);
    }

    private void select(Map<String, ?> arguments) {
        if (arguments.containsKey(UiProperties.VALUE)) {
            selectOptionByValue((String) arguments.get(UiProperties.VALUE));
        } else if (arguments.containsKey(UiProperties.TITLE)) {
            selectOptionByTitle((String) arguments.get(UiProperties.TITLE));
        }
    }

    @Override
    public boolean isDisplayed() {
        return getElement().isDisplayed();
    }

    @Override
    public boolean exists() {
        try {
            getElement().isDisplayed();
        } catch (NotFoundException e) { // NOSONAR
            return false;
        }
        return true;
    }

    private void selectOptionByValue(String optionValue) {
        Select selectElement = new Select(getElement());
        selectElement.selectByValue(optionValue);
    }

    private void selectOptionByTitle(String optionTitle) {
        Select selectElement = new Select(getElement());
        selectElement.selectByVisibleText(optionTitle);
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        getElement().sendKeys(keysToSend);
    }

    @Override
    public void sendKeysSafely(CharSequence... keysToSend) {
        String hidden = getProperty(UiProperties.HIDDEN);
        String readOnly = getProperty(UiProperties.READ_ONLY);
        WebElement webElement = getElement();
        if (Strings.isNullOrEmpty(hidden) || "false".equals(hidden) && (Strings.isNullOrEmpty(readOnly) || "false".equals(readOnly))) {
            webElement.sendKeys(keysToSend);
        } else {
            String id = getProperty(UiProperties.ID);
            removeAttribute(UiProperties.HIDDEN, id);
            removeAttribute(UiProperties.READ_ONLY, id);
            webElement.sendKeys(keysToSend);
            setAttribute(UiProperties.HIDDEN, hidden, id);
            setAttribute(UiProperties.READ_ONLY, readOnly, id);
        }

    }

    private void removeAttribute(String attribute, String id) {
        evaluateScript("document.getElementById('" + id + "').removeAttribute(\"" + attribute + "\");");
    }

    private void setAttribute(String attribute, String value, String id) {
        evaluateScript("document.getElementById('" + id + "').setAttribute(\"" + attribute + "\",\"" + value + "\");");
    }

    private Object evaluateScript(String expression, WebElement... elements) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        return elements.length > 0 ? javascriptExecutor.executeScript(expression, (Object[]) elements) : javascriptExecutor.executeScript(expression);
    }

    @Override
    public void mouseOverAt(int... coordinates) {
        mouseMoveAt(coordinates);
    }

    @Override
    public void mouseDownAt(int... coordinates) {
        verifyCoordinates(coordinates);

        Actions actions = new Actions(driver);
        WebElement webElement = getElement();
        if (coordinates.length != 0) {
            actions = actions.moveToElement(webElement, coordinates[0], coordinates[1]);
        } else {
            actions = actions.moveToElement(webElement);
        }
        actions
                .clickAndHold()
                .build()
                .perform();
    }

    @Override
    public void mouseUpAt(int... coordinates) {
        verifyCoordinates(coordinates);

        Actions actions = new Actions(driver);
        WebElement webElement = getElement();
        if (coordinates.length != 0) {
            actions = actions
                    .moveToElement(webElement, coordinates[0], coordinates[1])
                    .release();
        } else {
            actions = actions.release(webElement);
        }
        actions
                .build()
                .perform();
    }

    @Override
    public void mouseMoveAt(int... coordinates) {
        Actions actions = new Actions(driver);
        WebElement webElement = getElement();
        if (coordinates.length == 0) {
            actions = actions.moveToElement(webElement);
        } else {
            actions = actions.moveToElement(webElement, coordinates[0], coordinates[1]);
        }
        actions
                .build()
                .perform();
    }

    @Override
    public void mouseOut() {
        Actions actions = new Actions(driver);
        // Will not work in FF - see https://groups.google.com/forum/#!topic/selenium-users/oxmxWe-Roro
        actions
                .moveToElement(driver.findElement(By.tagName("head")))
                .build()
                .perform();
    }

    private static void verifyCoordinates(int... coordinates) {
        if (coordinates.length == 0 || coordinates.length == 2) {
            return;
        }
        throw new IllegalArgumentException("You should provide either zero or two (X, Y) coordinates");
    }

    private List<Option> getSelectedOptions() {
        Select selectElement = new Select(getElement());
        return createOptions(selectElement.getAllSelectedOptions());
    }

    private List<Option> getAllOptions() {
        Select selectElement = new Select(getElement());
        return createOptions(selectElement.getOptions());
    }

    private List<Option> createOptions(List<WebElement> allSelectedOptions) {
        List<Option> result = Lists.newArrayList();
        for (WebElement option : allSelectedOptions) {
            UiComponentMappingDetails optionSelector = getComponentSelector(option);
            UiComponentStateManager stateManager = stateManagerFactory.createStateManager(optionSelector);
            result.add(componentFactory.instantiateComponent(stateManager, Option.class));
        }

        return result;
    }

    @Override
    public void clearSelection() {
        Select selectElement = new Select(getElement());
        selectElement.deselectAll();
    }

    @Override
    public boolean isSelected() {
        return getElement().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getElement().isEnabled();
    }

    @Override
    // Non-final for test purposes
    public WebElement getElement() {
        return element;
    }

    @Override
    public UiComponentSize getSize() {
        Dimension size = getElement().getSize();
        return new UiComponentSize(size.getWidth(), size.getHeight());
    }

    public String getMappingInfo() {
        return mapping;
    }

    @Override
    public String getComponentName() {
        return StringUtils.lowerCase(getElement().getTagName());
    }

    @Override
    public String getAsString() {
        return "[" + getElement().toString() + "][" + mapping + "]";
    }

    @Override
    public synchronized UiComponentMappingDetails getComponentDetails() {
        if (thisComponentSelector == null) {
            thisComponentSelector = getComponentSelector(getElement());
        }
        return thisComponentSelector;
    }

    @Override
    public UiActions createUiActions() {
        return new SeleniumUiActions(driver);
    }

    @Override
    public Optional<InputStream> takeScreenshot() {
        return uiMediator.takeScreenshot();
    }

    @Override
    public Object evaluate(UiComponent uiComponent, String expression) {
        String getElementByJavaScript = "";

        // Selenium UiComponentMappingDetails will have CSS class identifiers mapped by XPATH if they do not have an id
        // XPATH
        if (uiComponent.getComponentDetails().getSelectorType().equals(SelectorType.XPATH)) {
            String xpathSelector = uiComponent.getComponentDetails().getSelector();
            getElementByJavaScript = "var element = document.evaluate( '" + xpathSelector
                    + "' ,document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null ).singleNodeValue;";
        }

        // CSS id
        else if (uiComponent.getComponentDetails().getSelectorType().equals(SelectorType.CSS)) {
            String cssSelector = uiComponent.getComponentDetails().getSelector();

            // selector is ID
            if (cssSelector.startsWith("#")) {
                cssSelector = cssSelector.replace("#", "");
                getElementByJavaScript = "var element = document.getElementById(\'" + cssSelector + "\');";
            }

            // other selectors
            else {
                throw new UnsupportedOperationException("evaluateJavaScript method not supported for CSS selector " + cssSelector);
            }
        }

        else {
            throw new UnsupportedOperationException("evaluateJavaScript method not supported for selector type selector "
                    + uiComponent.getComponentDetails().getSelectorType());
        }

        String javascriptAction = expression;


        String completeJavaScriptExpression = getElementByJavaScript + javascriptAction;

        return uiMediator.evaluate(completeJavaScriptExpression);
    }
}