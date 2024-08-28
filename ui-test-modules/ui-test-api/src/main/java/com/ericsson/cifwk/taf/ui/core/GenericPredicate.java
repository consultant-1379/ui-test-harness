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

public abstract class GenericPredicate implements Predicate<Object> {

    public final boolean apply(Object input) {
        return apply();
    }

    public abstract boolean apply();

}
