package eu.dnetlib.iis.primer;

import java.io.FileNotFoundException;

/**
 * This class retrieves a workflow package given its details.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public abstract class CoansysWorkflowPackageSupplier {
    public abstract CoansysWorkflowPackage getCoansysWorkflowPackage(String groupId, String artifactId, String version) throws FileNotFoundException;
}
