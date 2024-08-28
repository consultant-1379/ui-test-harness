<head>
   <title>UI test toolkit - Web</title>
</head>

# UI Test Toolkit Web

<!-- MACRO{toc} -->

## First steps

Here are the few basic steps that describe how to start Web testing with UI Test Toolkit.

### 1. Create a test Maven project

Make sure you have the following artifacts in your POM:

```xml
<dependency>
    <groupId>com.ericsson.de</groupId>
    <artifactId>ui-test-web</artifactId>
</dependency>
```

All needed UI Test Toolkit classes for Web testing will be available to you out of the box.

### 2. Create an instance of appropriate Web browser

**There are few browser types that are currently supported by UI Test Toolkit:**

* Firefox

* Chrome

* Internet Explorer

* PhantomJS (headless)

* HTMLUnit (headless)

The first four require an appropriate browser installation. The first three open browser windows and actually do the programmed test
actions (clicks the links, enters texts, etc.) when you run the tests. [PhantomJS](http://phantomjs.org/) is a headless webkit that
provides the same level of functionality as the browsers but less overhead as a window is not opened - there are however some issues
with it, see [Known issues](#known-issues). The Headless browser has less functionality again - it doesn't support multiple windows and
alerts, see [Known issues](#known-issues) section for more details), but doesn't require any software installations, and works much faster.
Headless is fastest, followed by PhantomJS, Chrome, Firefox and IE last.

Each browser type has some minor issues, which is due to issues in the underlying framework (Selenium). Check out the [Known issues](#known-issues)
section to see the full list.

<!-- MACRO{snippet|id=INSTANTIATE_BROWSERS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

### 3. Use this browser instance to open appropriate tabs - they will be opened inside this browser, and you can switch between them

<!-- MACRO{snippet|id=SWITCH_WINDOWS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

If you use a non-headless browser, the window will open immediately after instantiating the instance of the`Browser`.
When you call `Browser.open()`, it will open the provided URL in a tab (current one, if it's still blank, or a new
one if some URL is already opened in the browser).

<!-- MACRO{snippet|id=OPEN_MULTIPLE_WINDOWS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

`BrowserTab` gives you the same functionality as the normal browser tab does: you can get current URL, move back and forward, reload the page,
get information from the pop-up windows and alerts. Please refer to [API documentation](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/ui/package-frame.html) for full details.

You can open a new page in the current tab, by executing `browserTab.open(url)` method:

<!-- MACRO{snippet|id=OPEN_IN_THE_SAME_WINDOW|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

### 4. Find and use the page elements

`BrowserTab` instance provides a possibility to get another key object in UI Test Toolkit - `ViewModel` instance.
`ViewModel` is a combination of Model and View that represents a part of the UI window. Basically
the `ViewModel` is a projection of current view - a part of it, or the whole page.
You can get it in 2 different ways:

<!-- MACRO{snippet|id=VIEW_MODEL_TYPES|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

Generic view model is a class that allows you to search dynamically for the page components. Custom view model extends ViewModel and provides component autowiring.
Once you get access to page components, you can start manipulating them and do your testing.

## Generic view model

Here's an example of generic view model usage - simple form submission:

<!-- MACRO{snippet|id=GENERIC_VIEW_MODEL_FORM_SUBMISSION|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

After first part of actions the form will be submitted and UI Test Toolkit agent will be redirected to another page. However, from code perspective
there's no clean separation between pages, and it's not clear when the transition happens.

## Custom view model

Pages can be complicated and full of controls - so it's reasonable to split the appropriate view into few smaller ones - each representing each field
group, for example - to avoid creating huge overloaded (and buggy) page representations. Also, when tester is dealing with page controls, like
clicking the button, he may be redirected to another location - in this case he just has to call `BrowserTab.getView(Class<T extends ViewModel> class)` - and get a
View with all elements prepped and ready for use.

![Complex form](images/complex_form.png)

And here's an example of a page breakdown:

<!-- MACRO{snippet|id=BREAKDOWN_INTO_CUSTOM_VIEWS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

## Autowiring

As opposed to generic `ViewModel` instances, custom models allow annotation-driven element autowiring:

<!-- MACRO{snippet|id=LOGIN_VIEW_MODEL|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/selenium/pages/LoginViewModel.java} -->

<!-- MACRO{snippet|id=APPLIST_VIEW_MODEL|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/selenium/pages/AppListViewModel.java} -->

The main autowiring unit is UiComponentMapping annotation. It provides more possibilities for element search than a generic view API.
Search rules are defined by a few annotation attributes:

<span style="color:#ba3925;">Table 1. UiComponentMapping annotation attributes</span>

Attribute             | Description
:-----------          | :---------------------
id                    | ID of the component to find. In case of Web this is interpreted as element's "id" attribute, and also as CSS element ID.
name                  | Name of the component to find. In case of Web this is interpreted as element's "name" attribute (for example, form elements have this attribute).
selector              | Selector wildcard to search for the component by. The wildcard processing and syntax is up to the UI provider and is determined by selectorType attribute. For example, "#myId" selector in combination with selectorType=SelectorType.CSS will define a search for a component with ID "myId". If selectorType=SelectorType.XPATH, an XPath expression is expected here. Actually selector is a universal expression that covers the search functionality provided by id and name attributes.
selectorType          | Type of the selector used in selector attribute. If selector attribute is undefined, selectorType is ignored. Default selector type is obsolete, and is just a way to tell the implementing platform that it should treat the selector by the way which is default for it. For example, Web implementation will treat SelectorType.DEFAULT as SelectorType.CSS - therefore, selectors used in Web view models will be treated as CSS selectors by default.
default value(string) | If defined, treated as selector attribute

So the following declarations have the same result in Web views:

<!-- MACRO{snippet|id=COMPONENT_MAPPING_BY_ID1|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->
<!-- MACRO{snippet|id=COMPONENT_MAPPING_BY_ID2|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->
<!-- MACRO{snippet|id=COMPONENT_MAPPING_BY_ID3|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

---------- | -------------
**NOTE**   | You still can avail of quick element lookup in your custom model, because it extends the `GenericViewModel` class. You can do it for internal use inside your view model - but it would be a bad idea to use `getComponent()` methods inside public getters: in this case element will be looked up every time you call a getter. Don't be lazy, create a field annotated with `@UiComponentMapping`.

---------- | -------------
**NOTE**   | If you map the annotation to a single element, but the search finds more than one element from the search criteria, autowiring engine will set the value of the field to the first element found. Therefore, if you have multiple elements that match your criteria, map them to the List of appropriate components (`List<Button>`, `List<CheckBox>`, or even `List<UiComponent>`).

### So, the benefits of custom view models are:

* cleaner code (components and mappings are encapsulated in GenericViewModel subclass).
* easy to create page representations.
* ability to keep convenience methods (that manage few fields) inside the model.
* automatically autowire the components you need for tests - keeping the UI mapping stuff inside the View layer, which is correct from architectural point of view.

## What UI Test Toolkit components to choose for page element mapping?

Currently there are the following basic UI components provided in UI Test Toolkit:

Component                                             | Description
:---------------------------------------------------- | :---------------------------------------
<!-- MACRO{snippet|id=BUTTON_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->             | Button representation. Only "click" event is actually supported. Button can be represented by any Web element - <span style="color:#ba3925;">&lt;input type="button"&gt;, &lt;input type="submit"&gt;, &lt;span&gt;, &lt;div&gt;, &lt;button&gt;</span> - just anything which handles a click event
<!-- MACRO{snippet|id=LABEL_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->              | Label representation. You can use this element if you want to use your <span style="color:#ba3925;">&lt;div&gt;'s,&lt;span&gt;'s</span> and other similar elements in tests.
<!-- MACRO{snippet|id=LINK_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->               | Hyperlink representation. `click()` opens link URL in browser.
<!-- MACRO{snippet|id=TEXTBOX_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->            | Text box representation.
<!-- MACRO{snippet|id=SELECT_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->             | Dropdown and listbox representation.
<!-- MACRO{snippet|id=OPTION_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->             | Dropdown and listbox element.
<!-- MACRO{snippet|id=RADIO_BUTTON_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->       | Radio button representation.
<!-- MACRO{snippet|id=CHECKBOX_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->           | Check box representation.
<!-- MACRO{snippet|id=FILE_SELECTOR_CLASS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SdkDefaultComponents.java} -->      | File selector representation. Allows to select classpath resource or stream content or local file to be uploaded (Web) or to be opened (Desktop).

---------- | -------------
**NOTE**   | These components are not designed to fit UI SDK controls. For example, `Select` and `CheckBox` map to appropriate HTML elements. If you need a component for a particular UI SDK control, create a [custom component](02_custom_components.html) or have a look at `/OSS/com.ericsson.cds/uisdk-composite-components` for the ones that already exist.

All of these elements implement `UiComponent` interface that specifies basic operations for UI elements - like <span style="color:#ba3925;">isDisplayed(),
exists(), click(), getText(), getProperty(), mouse…()</span> operations. If you have an element that is not represented by any of the above concrete SDK components (table cell, for example),
but these basic operations are enough for you, you can map it as `UiComponent`:

<!-- MACRO{snippet|id=MAP_AS_UI_COMPONENT|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

Much better alternative though is creating a custom component for this table, and use it - see [Custom components](02_custom_components.html).

## Waiting for conditions

Sometimes you need to suspend your test flow in order to wait for some elements to appear/disappear.
To achieve this, you can use `wait...` methods available in `ViewModel` and `UiComponent`.
They will suspend execution until an element appears on the page, or a timeout occurs:

These methods will wait either until the UI component appears (and return it), or until timeout occurs (then `WaitTimedOutException` is thrown).

* <!-- MACRO{snippet|id=WAIT_UNTIL_METHOD1|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->
* <!-- MACRO{snippet|id=WAIT_UNTIL_METHOD2|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->
* <!-- MACRO{snippet|id=WAIT_UNTIL_METHOD3|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->
* <!-- MACRO{snippet|id=WAIT_UNTIL_METHOD4|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->

In this case the default timeout will apply (15 seconds). If you want to customize it, use `UiToolkit.setDefaultWaitTimeout(<milliseconds>)`. This sets the default timeout that applies to all UI "wait" operations.

If you want to use a non-default timeout value, you can define it as an optional parameter:

* <!-- MACRO{snippet|id=WAIT_UNTIL_MILLIS_METHOD1|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->
* <!-- MACRO{snippet|id=WAIT_UNTIL_MILLIS_METHOD2|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->
* <!-- MACRO{snippet|id=WAIT_UNTIL_MILLIS_METHOD3|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->
* <!-- MACRO{snippet|id=WAIT_UNTIL_MILLIS_METHOD4|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->

It is also possible to define custom predicate to wait for:

* <!-- MACRO{snippet|id=WAIT_UNTIL_PREDICATE_METHOD1|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->
* <!-- MACRO{snippet|id=WAIT_UNTIL_PREDICATE_METHOD2|file=ui-test-modules/ui-test-api/src/main/java/com/ericsson/cifwk/taf/ui/ConditionWait.java} -->

Example:

<!-- MACRO{snippet|id=BUTTON_DRIVEN_COMPONENT|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/ButtonDrivenComponent.java} -->

There are several already defined predicates in UiComponentPredicates (like `DISPLAYED`, `HIDDEN`, `HAS_TEXT`, `CHILD_ADDED`).
Others will be added on demand.

See more detailed information about these methods in [Javadoc](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/ui/core/UiComponentPredicates.html).

## Dynamic components

Please note that the components that you get are dynamic. It means that if you have got an instance of `TextBox`, which value is updated by some
Javascript function, you don't need to refresh the model to get the new value - next time you poll the value using `getText()` you get the new value.

If you have component or page section which appears in some time (being loaded asynchronously) and can't currently be found you will never get `null`. This saves you
from defensive checking against `NullPointerException`. You can use methods `isDisplayed()` and `exists()` to check your component presence on the page.
ANY other method will trigger implicit waits for the component (since 2.12.12) according to default retry schema - wait for 100 millis, then wait for 200 millis, 500,
1000, 2000, 4000, 4000, 4000, etc.. There is also total timeout can be defined (7 seconds by default). If component is still not found `UiComponentNotFoundException` will be thrown.
This value is selected to be an optimum between long tests execution (e.g. in case if some basic UI component became unmapped) and async component not being waited for explicitly. If your Web application
has slower components you can always fallback to explicit waits (predicates) or override timeout value (with property `taf_ui.implicit_wait.total_timeout_millis`).
Retry schema can be overriden with property `taf_ui.implicit_wait.retry_schema`. The same retry schema is applied if component is present, but not
visible (you will receive `UiComponentNotVisibleException` on timeout), as well as on `StaleElementReferenceException`. This all
gives you a big benefit if you are working with elements that appear only after some time or after some actions. Now you can rely on UI Test Toolkit framework (even if you don't know
how the component is loaded - synchronously or asynchronously) and eliminate defensive coding (waiting for every component before working with it).

Once you autowire or dynamically look up an element, it's there forever. If you have moved from the page where it belongs, and then try to poll the value when
return back - you will not get some `StaleElementException` like in Selenium. You will get the real-time element value.

## Dynamic lists

By default all lists of components in UI Test Toolkit Web are dynamic - i.e., their contents are always up-to-date with the page.

Here's an example of this feature. Let's imagine that elements are added to container incrementally:

<!-- MACRO{snippet|id=DYNAMIC_LIST_AUTO_UPDATED|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/core/DynamicComponentListTest.java} -->

Nice, isn't it? However, this reduces the performance, as each method invocation triggers list re-initialization. When working with grid, this means a lot of HTTP requests.
If you don't need this feature, annotate your component list binding in view model as `@StaticList`:

<!-- MACRO{snippet|id=STATIC_LIST_EXAMPLE|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/sdk/BasicComponentsView.java} -->

## Advanced use

These are the basics of UI Test Toolkit, please have a look at further topics:

* [Custom components] (02_custom_components.html)
* [Advanced browser options] (03_browsers.html)
* [More how-tos] (04_web_faq.html)

## Best use of UI Test Toolkit Web SDK

<span style="color:#ba3925;">Main guidelines are:</span>

* Use custom view models to break down your pages into convenient groups of component sets. However, if you have a simple page to test, go for generic view model.
* Use composite components
* Operator should act as a controller, that deals with model (ViewModel in this case) inside it and exposes the way to check the state of the scenario.

## API Documentation

[UI Test Toolkit API Documentation](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/ui/package-frame.html)

## Current Functionality

UI test automation

The acceptance tests for the [Web toolkit features in TAF UI] (https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/snapshot/tools/taf-ui-sdk.html)
can be found in this Git repository:
https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaffit (`ERICTAFtaffit_CXP9030250` module, `ui` package)

Package also contains tests for Selenium+Sikuli grid functionality, File Download with browser and further import from grid to test environment for verification.

<a name="known-issues"></a>
## Known issues and workarounds

<span style="color:#ba3925;">Known issues:</span>

* All browsers:

    * BrowserTab.dragAndDrop() doesn't support HTML5-style drag&drop - only old-style drag&drop (for example, as in jQuery UI). There's an issue raised in Selenium project: https://code.google.com/p/selenium/issues/detail?id=3604. Selenium team suggested a workaround for developers, but it doesn't work at least with the Firefox driver - so it's not currently used in UI Test Toolkit.

* Internet Explorer:

    * Mouse up, down and hover events sometimes do not working properly. Mouse button press or release is almost immediately replaced by "mouse over" event, even if the mouse was not moved. There are multiple issues reported for IE WebDriver in Selenium for mouse events ( see http://code.google.com/p/selenium/wiki/InternetExplorerDriver ).

* PhantomJS

    * Doesn't support alert windows. This feature is not implemented in Selenium PhantomJS driver, which is under the hood of UI Test Toolkit PhantomJS browser.

* HTMLUnit:

    * CTRL+A key combination doesn't work.
    * Doesn't support alert windows. This feature is not implemented in Selenium HtmlUnit driver, which is under the hood of UI Test Toolkit headless browser.
    * UiComponent.getSize() returns the size of the window, not the component.

