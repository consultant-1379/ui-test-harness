<head>
   <title>UI Test Toolkit - Custom components</title>
</head>

# Custom components

You can create your own components. To do this, just extend UI Test Toolkit’s `AbstractUiComponent` or its appropriate subclass. You can override the existing methods and add your own convenience methods:

<!-- MACRO{snippet|id=SURNAME_FIELD_EXAMPLE|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/SurnameField.java} -->

However, in most of the cases adding custom behaviour to a component can be done by encapsulation in ViewModel. Where custom components are
really needed is when you need them to contain other components - see next chapter.

## Composite components

You can embed other UI components in your custom component. This allows to create components that can be reused for the 
same page fragment structure, and shared with other teams that use the same UI widgets.

For example, we have a registration form with the following fields:

```xml
    <form id="customForm">
        <div>
            <label for="firstName">First name</label><input type="text" id="firstName" name="firstName" class="credential"/>
            <label for="lastName">Last name</label><input type="text" id="lastName" name="lastName" class="credential"/>
        </div>

        <h3>Home address</h3>
        <div class="homeAddress">
            <label for="homeCity">City</label><input type="text" class="city" id="homeCity"/>
            <label for="homeStreet">Street</label><input type="text" class="street" id="homeStreet"/>
        </div>

        <h3>Work address</h3>
        <div id="personWorkAddress" class="workAddress">
            <label for="workCity">City</label><input type="text" id="workCity" class="city" value=""/>
            <label for="workStreet">Street</label><input type="text" id="workStreet" class="street" value=""/>
            <label for="workZipCode">ZIP code</label><input type="text" id="workZipCode" class="zip" value="ZIP-1234"/>
        </div>
        <input type="button" id="submitButtonId" value="Submit"/>
    </form>
```

Here we see the reusable component: address. Both home and work addresses contain the same fields with the same styles - city and
street, with appropriate CSS classes. We can create a composite component for them:

<!-- MACRO{snippet|id=ADDRESS_FIELDS_COMPONENT|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/AddressFields.java} -->

And the form itself can be represented as a custom component as well:

<!-- MACRO{snippet|id=CUSTOM_FORM_COMPONENT|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/CustomForm.java} -->

Here’s a view model that references this form and exposes convenience public methods to operators:

<!-- MACRO{snippet|id=CUSTOM_WIDGET_PAGE|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/CustomWidgetPage.java} -->

So custom component `CustomForm` has instances of another component `AddressFields` as fields,
and is referenced itself as a field in `ViewModel`. This adds a hierarchical nature to UI components, making them more natural and easier to use.

### Selectors of nested components

Please note that the selectors used for the components that are nested in another component are relative to the parent component.
Even if you’d like to reference an outer HTML element(by ID, for example), that will not work.

Example of valid selectors:

<!-- MACRO{snippet|id=REGISTRATION_DATA_VM|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/RegistrationData.java} -->

<!-- MACRO{snippet|id=CUSTOMER_DATA_TABLE_COMPONENT|file=ui-test-modules/ui-test-web/src/test/java/com/ericsson/cifwk/taf/ui/examples/CustomerDataTable.java} -->

Note that CSS (default) selector will match only `<tr>` elements inside the table with ID <span style="color:#ba3925;">customerData</span>. It will not match any other table rows, even if you try to reach them via ID.

## Share your custom UI SDK components

If placed in shared operators module, these components can be shared will all other users that work with the same UI. A good example of using
this feature would be creating UiComponents for UI SDK elements (drop-downs, tables, etc.) which would save HUGE amount of time for all other
people writing tests for pages powered by UI SDK.

Feel free to contribute your components to `/OSS/com.ericsson.cds/uisdk-composite-components` and send us pull requests (review links). 
If you miss some component or some component feature please contact TAF team 
(e.g. mihails.volkovs@ericsson.com, kirill.shepitko@ericsson.com or gerald.glennon@ericsson.com) before implementing them.
<br/>
There are already some UI SDK components available there, like:

* Table
* Date picker
* Checkbox
* Selectbox
* Dropdown
* Pagination

Please see the **full list of implemented components** at [UI SDK Components](https://confluence-nam.lmera.ericsson.se/display/TAF/UI-SDK+Composite+Components "Your contribution is welcome") page.

Both UI SDK Java and HTML components do evolve. So there could be **NON backward compatible changes** introduced in order to match the newest UI SDK components. There is a page [UI SDK Upgrade Instructions](https://confluence-nam.lmera.ericsson.se/display/TAF/UI-SDK+Composite+Components+upgrading+instructions "New library contains the latest features and bug fixes") helping you to migrate from one version of components library to another one.
