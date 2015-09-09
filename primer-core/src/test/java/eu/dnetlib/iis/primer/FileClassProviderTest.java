package eu.dnetlib.iis.primer;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class FileClassProviderTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void basicTest() throws IOException, UnresolvableResourceException {
        File sourceDir = folder.newFolder();

        File dirs = new File(sourceDir, "some/random/path");
        if (!dirs.mkdirs()) {
            throw new IOException("Could not create directory " + dirs);
        }

        File f1 = new File(sourceDir, "some/random/f1.txt");
        if (!f1.createNewFile()) {
            throw new IOException("Could not create file " + f1);
        }

        File f2 = new File(sourceDir, "some/random/path/f2.txt");
        if (!f2.createNewFile()) {
            throw new IOException("Could not create file " + f2);
        }

        ClassProvider classProvider = new FileSystemClassProvider(sourceDir);
        Loader loader = new Loader();
        loader.addClassProvider(classProvider);

        File dest = folder.newFolder();
        loader.prime("some/random", dest, new Resolver());

        Assert.assertTrue(new File(dest, "f1.txt").exists());
        Assert.assertTrue(new File(dest, "path/f2.txt").exists());
    }

    @Test
    public void basicImportTest() throws IOException, UnresolvableResourceException {
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

        ClassProvider classProvider = new FileSystemClassProvider(sourceDir);
        Loader loader = new Loader();
        loader.addClassProvider(classProvider);

        Resolver resolver = new Resolver();
        resolver.addService("classpath", new ClasspathResolvingService(loader));

        File dest = folder.newFolder();
        loader.prime("some/random", dest, resolver);

        Assert.assertTrue(new File(dest, "f1.txt").exists());
        Assert.assertTrue(new File(dest, "path/f2.txt").exists());
    }

    @Test
    public void fileImportTest() throws IOException, UnresolvableResourceException {
        File sourceDir = folder.newFolder();

        File dirs1 = new File(sourceDir, "some/random/path");
        if (!dirs1.mkdirs()) {
            throw new IOException("Could not create directory " + dirs1);
        }

        File importFile = new File(sourceDir, "some/random/path/import.txt");
        Writer writer = new FileWriter(importFile);
        writer.append(ImportLinkFileChecker.IMPORT_LINK_HEADER + '\n');
        writer.append("f2.txt classpath some/other/path/f2.txt");
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

        ClassProvider classProvider = new FileSystemClassProvider(sourceDir);
        Loader loader = new Loader();
        loader.addClassProvider(classProvider);

        Resolver resolver = new Resolver();
        resolver.addService("classpath", new ClasspathResolvingService(loader));

        File dest = folder.newFolder();
        loader.prime("some/random", dest, resolver);

        Assert.assertTrue(new File(dest, "f1.txt").exists());
        Assert.assertTrue(new File(dest, "path/f2.txt").exists());
    }

    @Test
    public void getEntryNamesTest() throws IOException {
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

        Set entries = new HashSet(new FileSystemClassProvider(sourceDir).getEntryNames());

        Set expected = new HashSet();
        expected.add("some/");
        expected.add("some/random/");
        expected.add("some/random/import.txt");
        expected.add("some/random/f1.txt");
        expected.add("some/other/");
        expected.add("some/other/path/");
        expected.add("some/other/path/f2.txt");

        Assert.assertEquals(expected, entries);
    }
}
