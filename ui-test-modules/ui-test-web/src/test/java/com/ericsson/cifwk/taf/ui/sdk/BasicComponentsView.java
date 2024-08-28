package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.StaticList;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;

import java.util.List;

// START SNIPPET: STATIC_LIST_EXAMPLE
public class BasicComponentsView extends GenericViewModel {

    @StaticList
    @UiComponentMapping(".appearingDiv")
    private List<Label> allAppearingDivsAsStaticList;

    // ........
    // END SNIPPET: STATIC_LIST_EXAMPLE

    @UiComponentMapping(id = "selectId")
    private Select select;

    @UiComponentMapping(id = "textBoxId")
    private TextBox textBox;

    @UiComponentMapping(id = "linkId")
    private Link link;

    @UiComponentMapping(id = "labelId")
    private Label label;

    @UiComponentMapping(id = "buttonId")
    private Button button;

    @UiComponentMapping("h2")
    private UiComponent heading;

    @UiComponentMapping("notFound")
    private UiComponent notFound;

    @UiComponentMapping(".commonClass")
    private List<UiComponent> multipleElementsWithSameClass;

    @UiComponentMapping("notFound")
    private List<UiComponent> notFoundMany;

    @UiComponentMapping(id = "hidingDiv")
    private Label hidingDiv;

    @UiComponentMapping(id = "hiddenDiv")
    private Label hiddenDiv;

    @UiComponentMapping(id = "appearingDiv")
    private Label appearingDiv;

    @UiComponentMapping(id = "quickAppearingDiv")
    private Label quickAppearingDiv;

    @UiComponentMapping(id = "visibleInFutureDiv")
    private Label visibleInFutureDiv;

    @UiComponentMapping(id = "enabledInFutureButton")
    private Button enabledInFutureButton;

    @UiComponentMapping(id = "newPopupOpener")
    private Link newPopupOpener;

    @UiComponentMapping(id = "newWindowOpener")
    private Link newWindowOpener;

    @UiComponentMapping(name = "radio1")
    private List<RadioButton> radioGroup;

    @StaticList
    @UiComponentMapping(name = "radio1")
    private List<RadioButton> radioGroupAsStaticList;

    @UiComponentMapping(id = "fileSelector")
    private FileSelector fileSelector;

    @UiComponentMapping(id = "hiddenFileSelector")
    private FileSelector hiddenFileSelector;

    @UiComponentMapping(".appearingDiv")
    private List<Label> allAppearingDivs;

    public Select getSelect() {
        return select;
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public Link getLink() {
        return link;
    }

    public Label getLabel() {
        return label;
    }

    public Button getButton() {
        return button;
    }

    public UiComponent getGenericComponent() {
        return heading;
    }

    public List<UiComponent> getMultipleElementsWithSameClass() {
        return multipleElementsWithSameClass;
    }

    public UiComponent getNotFound() {
        return notFound;
    }

    public List<UiComponent> getNotFoundMany() {
        return notFoundMany;
    }

    public Label getHidingDiv() {
        return hidingDiv;
    }

    public Label getAppearingDiv() {
        return appearingDiv;
    }

    public Link getNewPopupOpener() {
        return newPopupOpener;
    }

    public Link getNewWindowOpener() {
        return newWindowOpener;
    }

    public Label getQuickAppearingDiv() {
        return quickAppearingDiv;
    }

    public Label getHiddenDiv() {
        return hiddenDiv;
    }

    public FileSelector getFileSelector() {
        return fileSelector;
    }

    public FileSelector getHiddenFileSelector() {
        return hiddenFileSelector;
    }

    public List<Label> getAllAppearingDivs() {
        return allAppearingDivs;
    }

    public Label getVisibleInFutureDiv() {
        return visibleInFutureDiv;
    }

    public Button getEnabledInFutureButton() {
        return enabledInFutureButton;
    }

    public List<RadioButton> getRadioGroup() {
        return radioGroup;
    }

    public List<RadioButton> getRadioGroupAsStaticList() {
        return radioGroupAsStaticList;
    }

    public List<Label> getAllAppearingDivsAsStaticList() {
        return allAppearingDivsAsStaticList;
    }
}
