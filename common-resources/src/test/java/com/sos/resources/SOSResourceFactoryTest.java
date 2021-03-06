package com.sos.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.io.Files;

public class SOSResourceFactoryTest {

    @Test
    public void testAsFile() throws IOException {
        File f = SOSResourceFactory.asFile("com/sos/resources/test.txt");
        assertEquals("this file contains only dummy text.", Files.toString(f, Charset.defaultCharset()));
        SOSResourceFactory.removeTemporaryFiles();
    }

    @Test
    public void testNamedResource() {
        File tempFile = SOSResourceFactory.asFile(SOSProductionResource.SCHEDULER_XSD);
        File tempDir = ResourceHelper.getInstance().getWorkingDirectory();
        String s = this.getClass().getClassLoader().getResource(SOSProductionResource.SCHEDULER_XSD.getFullName()).toString();
        assertTrue(tempFile.exists());
        SOSResourceFactory.removeTemporaryFiles();
        assertFalse(tempFile.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsFileInvalid() throws IOException {
        File f = SOSResourceFactory.asFile("com/sos/resources/invalid.txt");
        SOSResourceFactory.removeTemporaryFiles();
    }

}
