package eu.dnetlib.iis.primer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class ImportLinkFileCheckerTest {
    @Test
    public void checkHeaderBasicTest() {
        Assert.assertFalse(ImportLinkFileChecker.checkHeaderContents(
                "## ala ma kota"));
        Assert.assertTrue(ImportLinkFileChecker.checkHeaderContents(
                "## This is a classpath-based import file (this header is required)"));
    }

    @Test
    public void checkHeaderUnderlinesTest() {
        Assert.assertTrue(ImportLinkFileChecker.checkHeaderContents(
                "##_This_is_a_classpath_based_import_file_this_header_is_required"));
    }

    @Test
    public void checkHeaderMessInTheBeginningTest() {
        Assert.assertTrue(ImportLinkFileChecker.checkHeaderContents(
                "  ##_This_is_a_classpath_based_import_file_this_header_is_required"));
    }
}
