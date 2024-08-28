<head>
   <title>UI Test Toolkit - SWT FAQ & HOWTOs</title>
</head>

# SWT FAQ & HOWTOs

<!-- MACRO{toc} -->

## Do I need direct access to SWT application to write test scenario?
No. Tests are executed remotely. There are also some tools to help you investigate SWT application remotely. You need TAF SWT
agent port (10001 by default) to be opened for you. Port is defined at OSGi container (OSGi HTTP service) where your SWT application is deployed.

## How do I know what window is opened and what is it’s title?

Please open /swt-agent/windows/ to see the list of currently opened window titles.

## How can I overview SWT application visually (if I do not have direct access)?

You can take snapshots by opening /swt-agent/windows/&lt;Window%20Title&gt;.png in your browser or via API

<!-- MACRO{snippet|id=SWT_FAQ_1|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

## How can I get details of UI components of the window (including menu items)?

Please use component introspection URL’s: /swt-agent/windows/&lt;Window%20Title&gt;.json or /swt-agent/swt-windows/&lt;Window%20Title&gt;

## How can I select window submenu item?

<!-- MACRO{snippet|id=SWT_FAQ_2|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

## How can I select UI component context menu?

<!-- MACRO{snippet|id=SWT_FAQ_3|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

## My test is blocked by opened message dialog. How should I proceed?

SWT dialogs may block callers (expecting the user to choose option in another thread). In such cases please start new thread:

<!-- MACRO{snippet|id=SWT_FAQ_4|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

## How to sync asynchronously opening message dialog with test code?

```java
    Thread.sleep();
```java

for some time. In future there will be

```java
    DesktopWindow.waitUntilX();
```java

support.

## I need to select file in native platform file dialog?

SWT Bot does not support native dialogs. However there is workaround - sending key strokes to dialog parent window:

<!-- MACRO{snippet|id=SWT_FAQ_5|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

(sendKeys() also supports special symbols like delete character - "\u007F")

## How should I close resources after test?

<!-- MACRO{snippet|id=SWT_FAQ_6|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->
