package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ekirshe on 5/28/15.
 */
public class BrowserTabImplTest {

    @Test
    public void noPublicFinalMethods() {
        BrowserTabImpl unit = getBrowserTab();
        assertTrue(unit.noPublicFinalMethods(NoPublicFinalsViewModel.class));
        assertFalse(unit.noPublicFinalMethods(PublicFinalsViewModel.class));
        assertFalse(unit.noPublicFinalMethods(InheritsFinalsViewModel.class));
    }

    private BrowserTabImpl getBrowserTab() {
        return new BrowserTabImpl(null, null, null, null, null);
    }

    public static class NoPublicFinalsViewModel extends GenericViewModel {

        public boolean method1() {
            return false;
        }

        protected final void method2() {
        }
    }

    public static class PublicFinalsViewModel extends GenericViewModel {

        public final boolean method1() {
            return false;
        }

        public void method2() {
        }
    }

    public static class InheritsFinalsViewModel extends PublicFinalsViewModel {

        public void method3() {
        }
    }


}