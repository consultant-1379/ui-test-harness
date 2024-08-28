package com.ericsson.cifwk.taf.ui.core;

import com.ericsson.cifwk.taf.ui.sdk.BasicComponentsITestView;
import com.ericsson.cifwk.taf.ui.selenium.AbstractBrowserAwareITest;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import org.junit.Test;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DynamicStateManagerStaleElementsITest extends AbstractBrowserAwareITest {

    /**
     * To verify the fix for CIS-25157
     */
    @Test
    public void shouldSurviveStaleExceptionOnStateManagerProxyAttempt() {
        final AtomicReference<WebElement> testElementRef = new AtomicReference<WebElement>();
        List<UiComponentStateManager> dynamicList = getDynamicListWithSpiedStateManager(new Predicate<SeleniumUiComponentStateManager>() {
            @Override
            public boolean apply(SeleniumUiComponentStateManager testStateManager) {
                WebElement testElement = getOriginalWebElementSpied(testStateManager);
                testElementRef.set(testElement);
                doThrow(new StaleElementReferenceException("fake stale exception"))
                        .doCallRealMethod()
                        .when(testElement).getAttribute(UiProperties.ID);
                doReturn(testElement).when(testStateManager).getElement();
                return true;
            }
        });
        verify(testElementRef.get(), never()).getAttribute(UiProperties.ID);
        dynamicList.get(0);
        verify(testElementRef.get(), times(2)).getAttribute(UiProperties.ID);
    }

    @Test(expected = StaleElementReferenceException.class)
    public void shouldGiveUpOnEndlessStaleExceptionsOnRetryTimeout() {
        final AtomicReference<WebElement> testElementRef = new AtomicReference<WebElement>();
        List<UiComponentStateManager> dynamicList = getDynamicListWithSpiedStateManager(new Predicate<SeleniumUiComponentStateManager>() {
            @Override
            public boolean apply(SeleniumUiComponentStateManager testStateManager) {
                WebElement testElement = getOriginalWebElementSpied(testStateManager);
                testElementRef.set(testElement);
                doThrow(new StaleElementReferenceException("fake stale exception"))
                        .when(testElement).getAttribute(UiProperties.ID);
                doReturn(testElement).when(testStateManager).getElement();
                return true;
            }
        });

        verify(testElementRef.get(), never()).getAttribute(UiProperties.ID);
        try {
            dynamicList.get(0);
        } finally {
            verify(testElementRef.get(), atLeast(2)).getAttribute(UiProperties.ID);
        }
    }

    private List<UiComponentStateManager> getDynamicListWithSpiedStateManager(Predicate<SeleniumUiComponentStateManager> stateManagerStubber) {
        BasicComponentsITestView view = openComponentsView();
        final SeleniumUiComponentStateManager testStateManager = getOriginalStateManagerSpied(view);

        stateManagerStubber.apply(testStateManager);

        UiMediator mediator = view.getMediator();

        SeleniumUiComponentStateManagerFactory stateManagerFactory = new SeleniumUiComponentStateManagerFactory(mediator);
        return stateManagerFactory.proxyStateManagerList(new Supplier<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> get() {
                return asList((UiComponentStateManager) testStateManager);
            }
        });
    }

    private SeleniumUiComponentStateManager getOriginalStateManagerSpied(BasicComponentsITestView view) {
        List<UiComponent> components1 = view.getViewComponents("#textBoxId", UiComponent.class);
        UiComponent realComponent = components1.get(0);
        UiComponentStateManager stateManagerProxy = ((AbstractUiComponent) realComponent).getStateManager();
        UiComponentStateManager stateManager = ((StateManagerAware) stateManagerProxy).getStateManager();
        return spy((SeleniumUiComponentStateManager) stateManager);
    }

    private WebElement getOriginalWebElementSpied(UiComponentStateManager stateManager) {
        WebElement realWebElement = ((SeleniumUiComponentStateManager) stateManager).getElement();
        return spy(realWebElement);
    }
}