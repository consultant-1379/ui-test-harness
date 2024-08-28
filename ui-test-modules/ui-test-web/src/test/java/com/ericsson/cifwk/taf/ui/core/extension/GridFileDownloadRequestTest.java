package com.ericsson.cifwk.taf.ui.core.extension;

import com.ericsson.cifwk.taf.ui.Browser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.not;

public class GridFileDownloadRequestTest {

    private GridFileDownloadRequest unit;

    @Before
    public void setUp() {
        Browser browser = mock(Browser.class);
        unit = spy(new GridFileDownloadRequest(browser));
        File empty = mock(File.class);
        File full = mock(File.class);
        doReturn(0L).when(empty).length();
        doReturn(555L).when(full).length();
        doThrow(new RuntimeException("file is not there")).doReturn(empty).doReturn(full).when(unit).requestDownload(any(String.class));
        doReturn(true).when(unit).isFileDownloadRequestCreated();
    }

    @Test
    public void testDownload()  {
        File downloaded = unit.download("/path/to/file.pdf");
        verify(unit, times(3)).requestDownload(any(String.class));
        assertThat(downloaded.length(), not(0L));
    }
}
