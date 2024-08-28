/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.taf.ui.sdk;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

import java.util.List;

public interface Table extends UiComponent {

    List<String> getColumnNames();

    String getCell(int row, String columnName);

    int getRowIndex(String cellValue, String columnName);

    int getRowCount();

    void select(int row);

    void deselect();

    int getSelectedRowCount();

}
