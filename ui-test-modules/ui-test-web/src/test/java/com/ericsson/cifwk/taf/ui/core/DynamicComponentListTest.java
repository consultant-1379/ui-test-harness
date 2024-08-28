package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DynamicComponentListTest extends AbstractBrowserAwareTest {

    @Before
    public void setUp() {
        openDefaultBrowserTab();
    }

    @Test
    public void genericModelComponentListShouldBeDynamic() {
        ViewModel genericView = browserTab.getGenericView();
        List<Label> appearingLabels = genericView.getViewComponents(".appearingDiv", Label.class);
        Assert.assertEquals(0, appearingLabels.size());

        sleep(2000);
        Assert.assertEquals(1, appearingLabels.size());

        sleep(2000);
        Assert.assertEquals(2, appearingLabels.size());
    }

    @Test
    public void customModelComponentListShouldBeDynamic() {
        // START SNIPPET: DYNAMIC_LIST_AUTO_UPDATED
        BasicComponentsView view = browserTab.getView(BasicComponentsView.class);
        List<Label> appearingLabels = view.getAllAppearingDivs();
        Assert.assertEquals(0, appearingLabels.size());

        sleep(2000);
        Assert.assertEquals(1, appearingLabels.size());

        sleep(2000);
        Assert.assertEquals(2, appearingLabels.size());
        // END SNIPPET: DYNAMIC_LIST_AUTO_UPDATED
    }

    @Test
    public void childrenListShouldBeDynamic() {
        BasicComponentsView view = openComponentsView();
        UiComponent containerOfTimeTriggeredComponents = view.getViewComponent("#timeTriggered");
        List<UiComponent> children = containerOfTimeTriggeredComponents.getChildren();

        Assert.assertEquals(3, children.size());

        sleep(3500);

        Assert.assertEquals(5, children.size());
    }

    @Test
    public void descendantListShouldBeDynamic() {
        BasicComponentsView view = openComponentsView();
        UiComponent containerOfTimeTriggeredComponents = view.getViewComponent("#container");
        List<UiComponent> descendants = containerOfTimeTriggeredComponents.getDescendantsBySelector(".appearingDiv");

        Assert.assertEquals(0, descendants.size());

        sleep(2000);
        Assert.assertEquals(1, descendants.size());

        sleep(2000);
        Assert.assertEquals(2, descendants.size());
    }

    @Test
    public void compositeComponentListShouldBeDynamic() {

        // list content is fixed in the moment of view initialization
        MyView view = browserTab.getView(MyView.class);
        Assert.assertEquals(0, view.size());

        sleep(2000);
        Assert.assertEquals(1, view.size());

        sleep(2000);
        Assert.assertEquals(2, view.size());
    }

    public static class MyView extends GenericViewModel {

        @UiComponentMapping("body")
        private MyComponent myComponent;

        public int size() {
            return myComponent.size();
        }

    }

    public static class MyComponent extends AbstractUiComponent {

        @UiComponentMapping(".appearingDiv")
        private List<Label> allAppearingDivsAsStaticList;

        public int size() {
            return allAppearingDivsAsStaticList.size();
        }

    }

}
