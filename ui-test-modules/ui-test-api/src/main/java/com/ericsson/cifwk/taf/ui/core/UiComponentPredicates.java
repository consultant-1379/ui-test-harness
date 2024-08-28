/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.taf.ui.core;

import com.google.common.base.Predicate;

public class UiComponentPredicates {

    public static final Predicate<UiComponent> DISPLAYED = new ComponentDisplayedPredicate();

    public static final Predicate<UiComponent> HIDDEN = new ComponentHiddenPredicate();

    public static final Predicate<UiComponent> HAS_TEXT = new HasTextPredicate();

    public static final ChildAddedPredicateBuilder CHILD_ADDED = new ChildAddedPredicateBuilder();

    private UiComponentPredicates() {
        // hidden
    }

    public static class ChildAddedPredicateBuilder {

        public Predicate<UiComponent> withCurrentChildrenCount(int currentChildrenCount) {
            return new ChildAddedPredicate(currentChildrenCount);
        }

        public Predicate<UiComponent> withNoChildren() {
            return new ChildAddedPredicate(0);
        }

    }

    private  static class ComponentDisplayedPredicate implements Predicate<UiComponent> {

        @Override
        public boolean apply(UiComponent component) {
            return component.isDisplayed();
        }

    }

    private  static class ComponentHiddenPredicate implements Predicate<UiComponent> {

        @Override
        public boolean apply(UiComponent component) {
            return !component.isDisplayed();
        }

    }

    private  static class ChildAddedPredicate implements Predicate<UiComponent> {

        private final int currentChildrenCount;

        public ChildAddedPredicate(int currentChildrenCount) {
            this.currentChildrenCount = currentChildrenCount;
        }

        @Override
        public boolean apply(UiComponent component) {
            return component.getChildren().size() > currentChildrenCount;
        }

    }

    private  static class HasTextPredicate implements Predicate<UiComponent> {

        @Override
        public boolean apply(UiComponent component) {
            return !component.getText().isEmpty();
        }

    }

}
