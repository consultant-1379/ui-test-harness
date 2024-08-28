package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.RadioButton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticComponentListTest extends AbstractBrowserAwareTest {

    @Before
    public void setUp() {
        openDefaultBrowserTab();
    }

    @Test
    public void viewModelListIsStatic() {

        // list content is fixed in the moment of view initialization
        BasicComponentsView view = browserTab.getView(BasicComponentsView.class);
        List<Label> appearingLabels = view.getAllAppearingDivsAsStaticList();
        Assert.assertEquals(0, appearingLabels.size());

        sleep(2000);
        Assert.assertEquals(0, appearingLabels.size());

        sleep(2000);
        Assert.assertEquals(0, appearingLabels.size());

        // reinitializing the view
        view = browserTab.getView(BasicComponentsView.class);
        appearingLabels = view.getAllAppearingDivsAsStaticList();
        Assert.assertEquals(2, appearingLabels.size());
    }

    @Test
    public void compositeComponentListIsStatic() {

        // list content is fixed in the moment of view initialization
        MyView view = browserTab.getView(MyView.class);
        Assert.assertEquals(0, view.size());

        sleep(2000);
        Assert.assertEquals(0, view.size());

        sleep(2000);
        Assert.assertEquals(0, view.size());

        // reinitializing the view
        view = browserTab.getView(MyView.class);
        Assert.assertEquals(2, view.size());
    }

    @Test
    public void staticListItemIsStillDynamic() {
        BasicComponentsView view = browserTab.getView(BasicComponentsView.class);
        List<RadioButton> staticList = view.getRadioGroupAsStaticList();
        RadioButton staticListItem = staticList.get(0);

        // initial state of item in the static list
        assertThat(staticListItem.isSelected()).isFalse();

        // updating items state in the background (via dynamic list)
        view.getRadioGroup().get(0).click();

        // static list item is dynamic, so it noticed the state update
        assertThat(staticListItem.isSelected()).isTrue();
    }

    public static class MyView extends GenericViewModel {

        @UiComponentMapping("body")
        private MyComponent myComponent;

        public int size() {
            return myComponent.size();
        }

    }

    public static class MyComponent extends AbstractUiComponent {

        @StaticList
        @UiComponentMapping(".appearingDiv")
        private List<Label> allAppearingDivsAsStaticList;

        public int size() {
            return allAppearingDivsAsStaticList.size();
        }

    }

}
