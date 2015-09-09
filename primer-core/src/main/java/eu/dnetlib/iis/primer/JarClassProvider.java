package eu.dnetlib.iis.primer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class JarClassProvider extends ClassProvider {
    private JarFile jar;

    public JarClassProvider(File jar) throws IOException {
        this.jar = new JarFile(jar);
    }

    @Override
    public List<String> getEntryNames() {
        List<String> result = new ArrayList<String>();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            result.add(entry.getName());
        }
        return result;
    }

    @Override
    public void primeOne(String classpath, File target, Resolver resolver) throws IOException, UnresolvableResourceException, ParseException {
        final JarEntry entry = jar.getJarEntry(classpath);

        if (entry.isDirectory()) {
            FileUtils.forceMkdir(target);
        } else if (ImportLinkFileChecker.checkName(new File(entry.getName()))
                && ImportLinkFileChecker.checkHeader(jar.getInputStream(entry))) {
            new ImportLinkReader(jar.getInputStream(entry)).performResolution(target.getParentFile(), resolver);
        } else {
            FileUtils.forceMkdir(target.getParentFile());

            FileOutputStream output = null;
            try {
                output = new FileOutputStream(target);
                InputStream input = jar.getInputStream(entry);
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
            }
        }
    }
}
