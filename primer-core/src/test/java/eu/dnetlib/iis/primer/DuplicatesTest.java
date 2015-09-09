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
public class DuplicatesTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void firstDirTest() throws IOException, UnresolvableResourceException {
        Loader loader = new Loader();

        ClassProvider classProvider1 = new JarClassProvider(new File(
                getClass().getClassLoader().getResource("eu/dnetlib/iis/primer/TestJar.jar").getPath()));
        ClassProvider classProvider2 = new JarClassProvider(new File(
                getClass().getClassLoader().getResource("eu/dnetlib/iis/primer/DuplicateJar.jar").getPath()));

        loader.addClassProvider(classProvider1);
        loader.addClassProvider(classProvider2);

        Resolver resolver = new Resolver();
        resolver.addService("classpath", new ClasspathResolvingService(loader));

        File dest = folder.newFolder();
        loader.prime("", dest, resolver);

        Assert.assertTrue(new File(dest, "some").exists());
        Assert.assertTrue(new File(dest, "some").isDirectory());
    }

    @Test
    public void firstFileTest() throws IOException, UnresolvableResourceException {
        Loader loader = new Loader();

        ClassProvider classProvider1 = new JarClassProvider(new File(
                getClass().getClassLoader().getResource("eu/dnetlib/iis/primer/DuplicateJar.jar").getPath()));
        ClassProvider classProvider2 = new JarClassProvider(new File(
                getClass().getClassLoader().getResource("eu/dnetlib/iis/primer/TestJar.jar").getPath()));

        loader.addClassProvider(classProvider1);
        loader.addClassProvider(classProvider2);

        Resolver resolver = new Resolver();
        resolver.addService("classpath", new ClasspathResolvingService(loader));

        File dest = folder.newFolder();
        loader.prime("", dest, resolver);

        Assert.assertTrue(new File(dest, "some").exists());
        Assert.assertFalse(new File(dest, "some").isDirectory());
    }
}
