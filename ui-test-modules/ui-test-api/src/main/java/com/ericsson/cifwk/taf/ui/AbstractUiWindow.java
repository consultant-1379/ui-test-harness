/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.taf.ui;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractUiWindow extends AbstractConditionWait {

    public void writeInputStreamToFile(String path, InputStream stream, boolean appendFile) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path, appendFile);
            ByteStreams.copy(stream, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

}
