/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.phpmd.java;

import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.phpmd.java.util.ExecutableUtil;
import org.phpmd.java.util.ValidationException;
import static org.junit.Assert.*;

/**
 *
 * @author manu
 */
public class PhpmdTest {

    private static final String RESOURCE_PATH = "org/phpmd/java/_files/";

    public PhpmdTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected=ValidationException.class)
    public void cliToolThrowsExceptionWhenNoRuleSetWasConfigured() throws Exception
    {
        Phpmd phpmd = new Phpmd();
        phpmd.addSource(this.getResource("complexity.php"));
        phpmd.run();
    }

    @Test(expected=ValidationException.class)
    public void cliToolThrowsExceptionForInvalidRuleSet() throws Exception
    {
        Phpmd phpmd = new Phpmd();
        phpmd.addSource(this.getResource("complexity.php"));
        phpmd.addRuleSet("codesite");
        phpmd.run();
    }

    @Test(expected=ValidationException.class)
    public void cliToolThrowsExceptionWhenNoSourceWasConfigured() throws Exception
    {
        Phpmd phpmd = new Phpmd();
        phpmd.addRuleSet("codesize");
        phpmd.run();
    }

    @Test(expected=ValidationException.class)
    public void cliToolThrowsExceptionForInvalidSourceFile()
    {
        Phpmd phpmd = new Phpmd();
        phpmd.addSource("notexistent.php");
        phpmd.addRuleSet("codesize");
        phpmd.run();
    }

    @Test
    public void cliToolReturnsExpectedReportInstance() throws Exception
    {
        Phpmd phpmd = new Phpmd();
        phpmd.setBlocking();
        phpmd.addSource(this.getResource("complexity.php"));
        phpmd.addRuleSet("codesize");
        
        assertTrue(phpmd.run() instanceof Report);
    }

    @Test
    public void cliToolReturnsExpectedReportInstanceWithOneRuleViolation() throws Exception
    {
        Phpmd phpmd = new Phpmd();
        phpmd.addSource(this.getResource("complexity.php"));
        phpmd.addRuleSet("codesize");

        assertEquals(1, phpmd.run().getRuleViolations().size());
    }

    @Test
    public void cliToolHandlesMultipleInputSourceFiles() throws Exception
    {
        Phpmd phpmd = new Phpmd();
        phpmd.addSource(this.getResource("complexity.php"));
        phpmd.addSource(this.getResource("complexity2.php"));
        phpmd.addRuleSet("codesize");

        assertEquals(2, phpmd.run().getRuleViolations().size());
    }

    @Test
    public void cliToolHandlesInputSourceDirectory() throws Exception
    {
        Phpmd phpmd = new Phpmd();
        phpmd.addSource(this.getResource(""));
        phpmd.addRuleSet("codesize");

        assertEquals(2, phpmd.run().getRuleViolations().size());
    }

    private File getResource(String name) throws Exception
    {
        URL url = getClass().getClassLoader().getResource(RESOURCE_PATH + name);
        return new File(url.toURI());
    }

}