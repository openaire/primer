package eu.dnetlib.iis.primer;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class MixedClassProviderTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void basicTest() throws IOException, UnresolvableResourceException {
        File sourceDir = folder.newFolder();

        File dirs1 = new File(sourceDir, "some/different");
        if (!dirs1.mkdirs()) {
            throw new IOException("Could not create directory " + dirs1);
        }

        File importFile = new File(sourceDir, "some/different/import.txt");
        Writer writer = new FileWriter(importFile);
        writer.append(ImportLinkFileChecker.IMPORT_LINK_HEADER + '\n');
        writer.append("path classpath some/other/path/");
        writer.close();

        File dirs2 = new File(sourceDir, "some/other/path");
        if (!dirs2.mkdirs()) {
            throw new IOException("Could not create directory " + dirs2);
        }

        File f1 = new File(sourceDir, "some/different/f1.txt");
        if (!f1.createNewFile()) {
            throw new IOException("Could not create file " + f1);
        }

        File f2 = new File(sourceDir, "some/other/path/f2.txt");
        if (!f2.createNewFile()) {
            throw new IOException("Could not create file " + f2);
        }

        ClassProvider fsClassProvider = new FileSystemClassProvider(sourceDir);
        ClassProvider jarClassProvider = new JarClassProvider(new File(
                getClass().getClassLoader().getResource("eu/dnetlib/iis/primer/TestJar.jar").getPath()));
        Loader loader = new Loader();
        loader.addClassProvider(fsClassProvider);
        loader.addClassProvider(jarClassProvider);

        Resolver resolver = new Resolver();
        resolver.addService("classpath", new ClasspathResolvingService(loader));

        File dest = folder.newFolder();
        loader.prime("some/different", dest, resolver);

        Assert.assertTrue(new File(dest, "f1.txt").exists());
        Assert.assertTrue(new File(dest, "path/f2.txt").exists());
        Assert.assertFalse(new File(dest, "path/test/import.txt").exists());
        Assert.assertTrue(new File(dest, "path/test/f3.txt").exists());
        Assert.assertTrue(new File(dest, "path/test/sth/path/f3.txt").exists());
    }

    public void cyclesTest() throws IOException, UnresolvableResourceException {
        File sourceDir = folder.newFolder();

        File dirs1 = new File(sourceDir, "some/random");
        if (!dirs1.mkdirs()) {
            throw new IOException("Could not create directory " + dirs1);
        }

        File importFile = new File(sourceDir, "some/random/import.txt");
        Writer writer = new FileWriter(importFile);
        writer.append(ImportLinkFileChecker.IMPORT_LINK_HEADER + '\n');
        writer.append("path classpath some/other/path/");
        writer.close();

        File dirs2 = new File(sourceDir, "some/other/path");
        if (!dirs2.mkdirs()) {
            throw new IOException("Could not create directory " + dirs2);
        }

        File f1 = new File(sourceDir, "some/random/f1.txt");
        if (!f1.createNewFile()) {
            throw new IOException("Could not create file " + f1);
        }

        File f2 = new File(sourceDir, "some/other/path/f2.txt");
        if (!f2.createNewFile()) {
            throw new IOException("Could not create file " + f2);
        }

        ClassProvider fsClassProvider = new FileSystemClassProvider(sourceDir);
        ClassProvider jarClassProvider = new JarClassProvider(new File(
                getClass().getClassLoader().getResource("eu/dnetlib/iis/primer/TestJar.jar").getPath()));
        Loader loader = new Loader();
        loader.addClassProvider(fsClassProvider);
        loader.addClassProvider(jarClassProvider);

        Resolver resolver = new Resolver();
        resolver.addService("classpath", new ClasspathResolvingService(loader));

        File dest = folder.newFolder();
        loader.prime("some/random", dest, resolver);

        Assert.assertTrue(new File(dest, "f1.txt").exists());
        Assert.assertTrue(new File(dest, "path/f2.txt").exists());
        Assert.assertFalse(new File(dest, "path/test/import.txt").exists());
        Assert.assertTrue(new File(dest, "path/test/f3.txt").exists());
        Assert.assertTrue(new File(dest, "path/test/sth/path/f3.txt").exists());
    }
}
