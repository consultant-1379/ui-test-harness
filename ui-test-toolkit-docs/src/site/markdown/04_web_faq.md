<head>
   <title>UI Test Toolkit - FAQ & HOWTOs</title>
</head>

# FAQ & HOWTOs

<!-- MACRO{toc} -->

## Why UI Test Toolkit is better than Selenium?

1. UI Test Toolkit doesn't cover just Web: it provides the ability to test SWT and desktop applications, too.
2. UI Test Toolkit provides almost the same set of classes to work with Web and desktop applications - easy to create composite UI tests that span multiple layers of UI.
3. UI Test Toolkit Web components are dynamic and fault-tolerant: you never have to refresh the component, its state is always up-to-date. UiComponent never becomes stale, as it can do in Selenium.
4. UI Test Toolkit has extensible API: you can create the hierarchy of your own UI components.

## How can I get the children elements and treat them as concrete UI components?

UiComponent provides a method that will allow you to refer to UiComponent as to a specific UI component.

<!-- MACRO{snippet|id=GET_CHILD_ELEMENTS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

## How can I switch between windows?

Using `Browser.switchWindow()` - see an example [here](01_web.html#a3._Use_this_browser_instance_to_open_appropriate_tabs_-_they_will_be_opened_inside_this_browser_and_you_can_switch_between_them).

If your test opens new windows (usual windows or Javascript pop-ups) using HTML controls - clicking buttons, links, etc. (or even without any
actions - time-triggered window opening, for example), UI Test Toolkit will detect this anyway.

If a browser window was opened by you or by page code, it will become the default one in `Browser` - so you don't have to switch to it.

If you opened it manually using `Browser.open()`, and you have a `BrowserTab`s reference, you don't need to switch to
it as well - just use your tab and view models you have initiated from it.

You need to switch to a window manually (using `Browser.switchWindow()`) only if you have lost a reference to
appropriate `BrowserTab` instance or if you have never had one (window was opened anonymously).

<!-- MACRO{snippet|id=SWITCH_WINDOWS_CHECKING_URLS|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

If you want to switch to a window that you haven't opened manually, but it's opened anyway (after button click, for example), you will have to iterate through
your opened windows and check for appropriate URL for existence of a page element:

<!-- MACRO{snippet|id=USING_WINDOWS_COLLECTION|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

## How can I get access to window alerts?

`BrowserTab` class has method `getMessageBox()`. It returns the currently opened dialogue message box (alert/confirmation) - `MessageBox`.
If method returns `null`, it means that there are no browser message boxes opened.

Please note that this method works only with browser message boxes, not artificial pop-ups in HTML.

## How can I upload file?

Please use `FileSelector` UI component (which can be bound to file type inputs). Component allows you to select content from local file, from
class path resource or from input stream. File selector does not upload the file. You need to trigger application specific action yourself.

<!-- MACRO{snippet|id=UPLOAD_FILE|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

## How to download file and retrieve it?

If your test case requires to download file in browser and verify it you may use a custom browser profile and a
utility class `GridFileDownloadRequest`.

Create profile for automatic file download. Set download directory for later file lookup.
This is optional and is not required if you have Profile installed on the grid (needs to be a [grid with UI Test Toolkit extension] (07_desktop.html)).

<!-- MACRO{snippet|id=UPLOAD_FILE|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

Open browser session:

<!-- MACRO{snippet|id=DOWNLOAD_FILE_BROWSER_SESSION|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

To download file from Selenium node use GridFileDownloadRequest:

<!-- MACRO{snippet|id=DOWNLOAD_FILE_REQUEST|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

## What mouse events does UI Test Toolkit support?

`UiComponent` **supports the following operations which raise appropriate Javascript events:**

* void mouseOver(int… coordinates)

* void mouseDown(int… coordinates)

* void mouseUp(int… coordinates)

* void mouseMove(int… coordinates)

* void mouseOut()

* void contextClick()

If no coordinates are provided, event is triggered in the centre of the current control. 
`UiComponent` supports `void contextClick()` method for right-click mouse event.

`BrowserTab` supports drag-and-drop operation: `void dragAndDropTo(UiComponent draggable, UiComponent target).`

## Does UI Test Toolkit support the work with Javascript?

Yes. `BrowserTab` has a method `Object evaluate(String expression)` which runs the `expression` as Javascript on the
current page, and returns a result.

## Can I define Web Browser screen resolution?

Yes you can - you can set and change Web browser screen size(resolution).

<!-- MACRO{snippet|id=REDEFINE_RESOLUTION|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

This functionality works in both type of test execution, local test execution and on the Selenium Grid.

## How should I close the resources after test?
Use `Browser.close()` to close a particular browser or `UiToolkit.closeAllWindows()` to close all windows.
Put one of these calls into a test method that runs after each test or test group.

## How to do stateful interactions with UI Test Toolkit Web API

UI Test Toolkit allows you to perform stateful interactions on web components such as selecting multiple rows in a table. Stateful interactions use the
`UiActions` interface and work using the builder design pattern. You can get a `UiActions` object from appropriate `BrowserTab`:

<!-- MACRO{snippet|id=ACTION_CHAINS1|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

Methods can be chained together to perform stateful interactions on Web UI components. The `perform()` method carries out
the actions you have chained together. This stateful interaction allows you to carry out more complex actions
such as CTRL clicks, highlighting multiple rows in a table, etc. in one transaction.

<!-- MACRO{snippet|id=ACTION_CHAINS2|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiWebCodeExamples.java} -->

The above code snippet highlights two rows in a table using control click.

:-------  | :-----------------------------
**NOTE:** | You can use Selenium's `org.openqa.selenium.Keys` for reference to special/functional keys (CTRL, ALT, etc.).

If a UI component is supplied to a method (see below) the action will be performed on the centre of the element.

```java
.click(uiComponent)
```java

When methods are chained, attributes such as mouse cursor position are inherited from previous methods in the chain if no element is supplied.

:-------  | :-----------------------------
**NOTE:** | Methods will only be perfomed once you call the `perform()` method.

```java
UiActions actions = browserTab.newActionChain();

actions.keyDown(Keys.CONTROL);
actions.click(button);
actions.perform();
```java

Currently, `UiActions` objects are only available for Web UI components (not desktop or SWT).

## How to take a screen shot

`BrowserTab` and `ViewModel` classes have a method `waitUntilComponentIsDisplayed()`. When using this
method and the UI element is not found, a screen shot will be taken with the current UI content. This screen shot will be added, as an attachment, to the Allure Report.

You can also manually take a screen shot by using the `takeScreenshot(String name)` method. The string passed to
this method will be the name that appears on the `.png` image that will be attached to the Allure Report.
