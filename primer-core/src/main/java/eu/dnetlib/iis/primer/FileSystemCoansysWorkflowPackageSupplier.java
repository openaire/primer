package eu.dnetlib.iis.primer;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;

/**
 * Retrieves workflow packages from a local directory.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class FileSystemCoansysWorkflowPackageSupplier extends CoansysWorkflowPackageSupplier {
    private File dir;
    private static String ARTIFACT_CLASSIFIER = "oozie-job";
    private static String ARTIFACT_TYPE = "tar.gz";
    public FileSystemCoansysWorkflowPackageSupplier(File dir) {
        this.dir = dir;
    }

    @Override
    public CoansysWorkflowPackage getCoansysWorkflowPackage(String groupId, String artifactId, String version)
            throws FileNotFoundException {
        if(StringUtils.isNotBlank(version)) {
            return new CoansysWorkflowPackage(getWorkflowPackageFile(artifactId, version));
        } else {
            File[] candidates = dir.listFiles((FileFilter) new WildcardFileFilter(getWorkflowPackageFileName(artifactId, "*")));
            if (candidates.length == 0) {
                throw new FileNotFoundException(String.format(
                        "Could not find CoAnSys workflow package matching pattern %s",
                        getWorkflowPackageFile(artifactId, "*").getAbsolutePath()));
            } else if (candidates.length > 1) {
                throw new FileNotFoundException(String.format(
                        "Filename pattern %s is ambiguous. Please specify CoAnSys workflow package version.",
                        getWorkflowPackageFile(artifactId, "*").getAbsolutePath()));
            } else {
                return new CoansysWorkflowPackage(candidates[0]);
            }
        }
    }

    private String getWorkflowPackageFileName(String artifactId, String version) {
        return artifactId + "-" + version + "-" + ARTIFACT_CLASSIFIER + "." + ARTIFACT_TYPE;
    }

    private File getWorkflowPackageFile(String artifactId, String version) {
        return new File(dir, getWorkflowPackageFileName(artifactId, version));
    }
}
