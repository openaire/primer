package eu.dnetlib.iis.primer;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Workflow package abstraction.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class CoansysWorkflowPackage {
    private File file;

    public CoansysWorkflowPackage(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Could not find CoAnSys workflow package " + file.getAbsolutePath());
        }
        this.file = file;
    }

    /**
     * Unpacks a workflow into specified directory. To be more precise: workflow package should contain only a single
     * directory with the workflow. This method will extract its contents into target directory.
     *
     * @param target new workflow root directory
     */
    public void extract(File target) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
        TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);
        TarArchiveEntry entry;
        while ((entry = (TarArchiveEntry)tarIn.getNextEntry()) != null) {
            //The archive should contain only a single directory: we want to unpack its content.
            final File outputFile = new File(target, entry.getName().substring(entry.getName().indexOf('/') + 1));
            if (entry.isDirectory()) {
                FileUtils.forceMkdir(outputFile);
            } else {
                OutputStream outputFileStream = null;
                try {
                    outputFileStream = new FileOutputStream(outputFile);
                    IOUtils.copy(tarIn, outputFileStream);
                } finally {
                    IOUtils.closeQuietly(outputFileStream);
                }
            }
        }
    }
}
