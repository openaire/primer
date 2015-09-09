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
public class JarClassProviderTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void basicTest() throws IOException, UnresolvableResourceException {
        ClassProvider classProvider = new JarClassProvider(new File(
                getClass().getClassLoader().getResource("eu/dnetlib/iis/primer/TestJar.jar").getPath()));
        Loader loader = new Loader();
        loader.addClassProvider(classProvider);

        Resolver resolver = new Resolver();
        resolver.addService("classpath", new ClasspathResolvingService(loader));

        File dest = folder.newFolder();
        loader.prime("some/other/path", dest, resolver);

        Assert.assertFalse(new File(dest, "test/import.txt").exists());
        Assert.assertTrue(new File(dest, "test/f3.txt").exists());
        Assert.assertTrue(new File(dest, "test/sth/path/f3.txt").exists());
    }
}
