package eu.dnetlib.iis.primer;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class CoansysWorkflowPackageTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void basicTest() throws IOException {
        File dir = folder.newFolder();
        File packageFile = new File(getClass().getClassLoader()
                .getResource("eu/dnetlib/iis/primer/inner-workflow-1.2-SNAPSHOT-oozie-job.tar.gz").getPath());
        CoansysWorkflowPackage wfPackage = new CoansysWorkflowPackage(packageFile);
        wfPackage.extract(dir);

        Assert.assertEquals(1, dir.list().length);
        Assert.assertTrue(new File(dir, "workflow.xml").exists());
    }
}
