package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.UiToolkit;
import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.google.common.base.Stopwatch;
import org.junit.Test;

import static com.google.common.base.Stopwatch.createStarted;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImplicitRetryTest extends AbstractBrowserAwareTest {

    @Test
    public void shouldReturnViewWithoutWaitingForAsyncComponent() {

        // opening page in browser
        openDefaultBrowserTab();
        Stopwatch autowiringTime = createStarted();

        // getting UI component without initializing it
        Label appearingDiv = browserTab.getView(BasicComponentsView.class).getAppearingDiv();
        assertFalse(appearingDiv.exists());

        // checking that wait didn't occur (component appears in 3 seconds)
        String errorMessage = format("Autowiring time took longer (%s) than a second", autowiringTime);
        assertTrue(errorMessage, autowiringTime.elapsed(MILLISECONDS) < 1000L);
    }

    @Test
    public void shouldNotWaitForComponentOnBasicQueries() {

        // non existing UI component
        BasicComponentsView view = openComponentsView();
        Label appearingDiv = view.getAppearingDiv();
        assertFalse(appearingDiv.exists());
        assertFalse(appearingDiv.isDisplayed());

        // component is still absent
        assertFalse(appearingDiv.exists());
    }

    @Test
    public void shouldWaitForComponentImplicitly() {

        // non existing UI component
        BasicComponentsView view = openComponentsView();
        Label appearingDiv = view.getAppearingDiv();
        assertFalse(appearingDiv.exists());

        // implicit wait for component on any UI method
        assertEquals("Text appearing in 3 seconds...", appearingDiv.getText());
        assertTrue(appearingDiv.exists());
    }

    @Test
    public void shouldWaitForNotVisibleComponent() {

        // not visible UI component
        BasicComponentsView view = openComponentsView();
        Label visibleInFuture = view.getVisibleInFutureDiv();
        assertTrue(visibleInFuture.exists());
        //assertFalse(visibleInFuture.isDisplayed());

        // implicit waiting for component to be visible (on any UI method)
        sleep(5000);
        visibleInFuture.click();
        assertEquals("Text becomes visible in 3 seconds...", visibleInFuture.getText());
        assertTrue(visibleInFuture.exists());
        assertTrue(visibleInFuture.isDisplayed());
    }

    @Test
    public void shouldWaitForDisabledComponent() {

        // not visible UI component
        BasicComponentsView view = openComponentsView();
        Button enabledInFutureButton = view.getEnabledInFutureButton();
        assertFalse(enabledInFutureButton.isEnabled());

        // implicit wait for component on any UI method
        // Selenium does allow clicking disabled buttons - no wait for component occurs
        enabledInFutureButton.click();
        assertFalse(enabledInFutureButton.isEnabled());
    }

    @Test
    public void shouldThrowExceptionOnTimeout() {
        BasicComponentsView view = openComponentsView();
        UiComponent notMappedProperlyComponent = view.getNotFound();
        assertFalse(notMappedProperlyComponent.exists());
        try {
            notMappedProperlyComponent.getText();
            fail("Exception expected");
        } catch (UiComponentNotFoundException e) {
            // OK
        }
    }

    @Test
    public void shouldThrowExceptionOnDisappearedComponent() {
        BasicComponentsView view = openComponentsView();
        Label hidingDiv = view.getHidingDiv();
        assertTrue(hidingDiv.exists());
        assertEquals("Text hiding in 3 seconds...", hidingDiv.getText());

        // UI component disappears
        UiToolkit.pause(4000);
        assertFalse(hidingDiv.exists());

        Stopwatch disappearedComponentAccessTime = createStarted();
        try {
            hidingDiv.getText();
            fail("Exception expected");
        } catch (UiComponentNotFoundException e) {

            // checking that exception wasn't raised by time out
            String errorMessage = format("Attempt to access disappeared component took longer (%s) than a second", disappearedComponentAccessTime);
            assertTrue(errorMessage, disappearedComponentAccessTime.elapsed(MILLISECONDS) < 1000L);
        }
    }

    @Test
    public void shouldWaitForCompositeAppearingChild() {

        // composite component binding
        openDefaultBrowserTab();
        MyViewModel view = browserTab.getView(MyViewModel.class);
        Label appearingLabel = view.getContainer().getInnerContainer().getAppearingLabel();
        assertFalse(appearingLabel.exists());

        // implicit waiting for component
        assertEquals("Text appearing in 3 seconds...", appearingLabel.getText());
        assertTrue(appearingLabel.exists());
    }

    @Test
    public void shouldWaitForAppearingCompositeChildSequentialAccess() {

        // composite component binding
        openDefaultBrowserTab();
        MyViewModel view = browserTab.getView(MyViewModel.class);
        MyCompositeComponent appearingComposite = view.getContainer().getInnerContainer().getAppearingComposite();
        Label appearingChild = appearingComposite.getAppearingCompositeChild();

        // access to composite child triggers implicit wait for composite
        sleep(1000);
        assertFalse(appearingComposite.exists());
        assertFalse(appearingChild.exists());
        assertTrue(appearingComposite.exists());

        // access to composite child triggers implicit wait for child
        assertFalse(appearingChild.exists());
        assertEquals("Composite component text appearing in 5 seconds...", appearingChild.getText());
        assertTrue(appearingChild.exists());
    }

    @Test
    public void shouldWaitForAppearingCompositeChildRandomAccess() {

        // composite component binding
        openDefaultBrowserTab();
        MyViewModel view = browserTab.getView(MyViewModel.class);
        MyCompositeComponent appearingComposite = view.getContainer().getInnerContainer().getAppearingComposite();
        Label appearingChild = appearingComposite.getAppearingCompositeChild();

        // access to composite child triggers implicit wait for composite
        assertFalse(appearingComposite.exists());
        assertEquals("Composite component text appearing in 5 seconds...", appearingChild.getText());
        assertTrue(appearingComposite.exists());
        assertTrue(appearingChild.exists());
    }

    public static class MyViewModel extends GenericViewModel {

        @UiComponentMapping("#container")
        private MyComponent container;

        public MyComponent getContainer() {
            return container;
        }
    }

    protected static class MyComponent extends AbstractUiComponent {

        @UiComponentMapping("#timeTriggered")
        private MyInnerComponent innerContainer;

        public MyInnerComponent getInnerContainer() {
            return innerContainer;
        }
    }

    protected static class MyInnerComponent extends AbstractUiComponent {

        @UiComponentMapping("#appearingDiv")
        private Label appearingLabel;

        @UiComponentMapping("#appearingCompositeComponent")
        private MyCompositeComponent appearingComposite;

        public Label getAppearingLabel() {
            return appearingLabel;
        }

        public MyCompositeComponent getAppearingComposite() {
            return appearingComposite;
        }
    }

    protected static class MyCompositeComponent extends AbstractUiComponent {

        @UiComponentMapping("#appearingCompositeComponentChild")
        private Label appearingCompositeChild;

        public Label getAppearingCompositeChild() {
            return appearingCompositeChild;
        }
    }

}
