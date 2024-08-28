<head>
   <title>UI Test Toolkit - How to get started with SWT testing</title>
</head>

# How to get started with SWT testing

UI Test Toolkit SWT is able to control and test remote SWT applications deployed in OSGi container (TAF SWT Agent should be previously deployed to the same OSGi container).
QA engineers may use unified UI Test Toolkit API (the same API used for testing Web applications). It supports CSS subset and Squish-like expressions as UI component selectors.

<!-- MACRO{toc} -->

## 1. Create a test Maven project:

Make sure you have the following artifacts in your POM:

```xml
<dependency>
    <groupId>com.ericsson.de</groupId>
    <artifactId>ui-test-swt</artifactId>
</dependency>
```

## 2. Connect to SWT host

If your TAF SWT agent is already deployed:

<!-- MACRO{snippet|id=INIT_SWT_NAVIGATOR|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

If not, deploy it into target OSGi container if you need:

<!-- MACRO{snippet|id=INIT_SWT_NAVIGATOR_ADVANCED|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

## 3. Create an instance of appropriate window

<!-- MACRO{snippet|id=INIT_SWT_NAVIGATOR_WINDOW|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

## 4. Find and use the window elements

<span style="color:#ba3925;">Generic view model</span>

<!-- MACRO{snippet|id=INIT_SWT_VIEW_MODEL|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

<span style="color:#ba3925;">Custom view model</span>

<!-- MACRO{snippet|id=INIT_SWT_CUSTOM_VIEW_MODEL|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/UiSwtCodeExamples.java} -->

<!-- MACRO{snippet|id=SWT_CUSTOM_VIEW_MODEL1|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/BsimModel.java} -->

<!-- MACRO{snippet|id=SWT_CUSTOM_VIEW_MODEL2|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/SomeTabModel.java} -->

<!-- MACRO{snippet|id=SWT_CUSTOM_VIEW_MODEL3|file=ui-test-modules/ui-test-swt/src/test/java/com/ericsson/cifwk/taf/ui/examples/NewConfigurationModel.java} -->


Separate model can be created for screen logical partitions like panels, tabs, wizards or windows. Model can encapsulate every UI component bound.
Usually model users are not interested in UI components itself, but in actions provided by them. So, you shouldn’t provide UI component getters,
but rather business logics methods like triggering some action or getting business value (please see view model examples above). If you are building
view model around a tab, please note, that UI components on a tab are not searchable until tab is activated. The same applies to dynamic components
(which become visible on some action). View model can’t be built until every UI component of the model is visible. Split window into view models accordingly.

## 5. Selectors supported

<span style="color:#ba3925;">Supported subset of CSS selectors</span>

Fetch UI Component By  | Syntax                            | Example (selects Text Box)         | Comments
:-------------------   | :-----------                      | :--------------------------        | :---------------------------------
Text                   | &lt;ControlCaption/Text&gt;       | "Mihails"                          |
Label                  | &lt;Label&gt;+                    | "First name:+"                     | Label should be located exactly before fetched control (in order of adding controls in the application)
Tooltip                | &lt;Tooltip&gt;:hover             | "Please don't use nicknames:hover" |
Index                  | #&lt;index&gt;                    | "#0"                               | Index is zero based
SWTBot id              | #&lt;idValue&gt;                  | "#input.person.name"               | Is equivalent to "org.eclipse.swtbot.widget.key#input.person.name"
Custom id              | &lt;idKey&gt;#&lt;idValue&gt;     | "customId#input.person.name"       | Control ids may be explicitly set by application designers (to make UI tests friendly to application changes)
Custom id              | [&lt;idKey&gt;=&lt;idValue&gt;]   | "[customId=input.person.name]"     | Control ids may be explicitly set by application designers (to make UI tests friendly to application changes)

<span style="color:#ba3925;">Squish-like selectors</span>

Fetch UI Component By  | Example (selects Text Box)                                               | Comments
:-------------------   | :----------------------------------------------------                    | :---------------------------------
Text                   | {text = Mihails}                                                         |
Mnemonic text          | {mnemonicText = ''}                                                      |
Label                  | {label = First name:}                                                    | Label should be located exactly before fetched control (in order of adding controls in the application)
Tooltip                | {tooltip = "Please don't use nicknames}                                  |
Index                  | {index = 0}                                                              | Index is zero based
SWTBot id              | {id = #input.person.name}                                                | Is equivalent to "org.eclipse.swtbot.widget.key#input.person.name"
Custom id              | {customIdKey = customId, customIdValue = input.person.name}              | Control ids may be explicitly set by application designers (to make UI tests friendly to application changes)
Widget type            | {type = org.eclipse.ui.forms.widgets.Hyperlink}                          | Control ids may be explicitly set by application designers (to make UI tests friendly to application changes)
Wrapper type           | {wrapperType = org.eclipse.swtbot.swt.finder.widgets.SWTBotToggleButton} | Wrap found component into SWTBot adapter (which has more user friendly methods). This parameter could be eliminated in newer versions.
Init actions           | {initActions = [activate]}                                               | Component initialization actions (useful for tab activation).
Container              | {container = {text = Main Tab, type = org.eclipse.swt.custom.CTabItem}}  | Limit search scope by defining component parent. Currently is not supported.
Data                   | {data = dataKey}                                                         | Checks presence of given key in component data map.
Data properties        | {dataProperties = {dataKey = dataValue}}                                 | Checks presence of given key/value in component data map.
Widget properties      | {widgetProperties = {align = 1, gradientVertical = true}}                | Checks custom properties of UI component.

You can also combine any Squish selector of the above. Every selector property is optional.
