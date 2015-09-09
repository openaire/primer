package eu.dnetlib.iis.primer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class FileSystemClassProvider extends ClassProvider {
    private File root;

    public FileSystemClassProvider(File root) {
        this.root = root;
    }

    @Override
    public List<String> getEntryNames() {
        List<String> result = new ArrayList<String>();
        appendChildren("", result);
        return result;
    }

    private void appendChildren(String relativePath, List<String> result) {
        for (File f : new File(root, relativePath).listFiles()) {
            String beginning = StringUtils.isNotEmpty(relativePath) ? relativePath : "";
            String newRelativePath = beginning + f.getName();

            if (f.isDirectory()) {
                newRelativePath = newRelativePath + '/';
                appendChildren(newRelativePath, result);
            }
            result.add(newRelativePath);
        }
    }

    @Override
    public void primeOne(String classpath, File destination, Resolver resolver) throws IOException, UnresolvableResourceException, ParseException {
        File src = new File(root, classpath);
        if (src.isDirectory()) {
            FileUtils.forceMkdir(destination);
        } else if (ImportLinkFileChecker.checkName(src)
                && ImportLinkFileChecker.checkHeader(new FileInputStream(src))) {
            new ImportLinkReader(new FileInputStream(src)).performResolution(destination.getParentFile(), resolver);
        } else {
            FileUtils.copyFile(src, destination);
        }
    }
}
