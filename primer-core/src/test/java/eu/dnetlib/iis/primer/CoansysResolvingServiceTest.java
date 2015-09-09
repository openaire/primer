package eu.dnetlib.iis.primer;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class CoansysResolvingServiceTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void basicTest() throws IOException, UnresolvableResourceException {
        File dir = folder.newFolder();
        File repo = folder.newFolder();
        String filename = "inner-workflow-1.2-SNAPSHOT-oozie-job.tar.gz";
        String path = "eu/dnetlib/iis/primer";
        IOUtils.copy(getClass().getClassLoader().getResourceAsStream(path + "/" + filename),
                new FileOutputStream(new File(repo, filename)));

        Resolver resolver = new Resolver();
        resolver.addService("coansys", new CoansysResolvingService(new FileSystemCoansysWorkflowPackageSupplier(repo)));
        resolver.resolve(dir, "coansys", "pl.edu.icm.coansys:inner-workflow:1.2-SNAPSHOT");

        Assert.assertEquals(1, dir.list().length);
        Assert.assertTrue(new File(dir, "workflow.xml").exists());
    }

    @Test
    public void emptyVersionTest() throws IOException, UnresolvableResourceException {
        File dir = folder.newFolder();
        File repo = folder.newFolder();
        String filename = "inner-workflow-1.2-SNAPSHOT-oozie-job.tar.gz";
        String path = "eu/dnetlib/iis/primer";
        IOUtils.copy(getClass().getClassLoader().getResourceAsStream(path + "/" + filename),
                new FileOutputStream(new File(repo, filename)));

        Resolver resolver = new Resolver();
        resolver.addService("coansys", new CoansysResolvingService(new FileSystemCoansysWorkflowPackageSupplier(repo)));
        resolver.resolve(dir, "coansys", "pl.edu.icm.coansys:inner-workflow");

        Assert.assertEquals(1, dir.list().length);
        Assert.assertTrue(new File(dir, "workflow.xml").exists());
    }
}
