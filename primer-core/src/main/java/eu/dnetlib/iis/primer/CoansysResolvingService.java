package eu.dnetlib.iis.primer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class enables usage of CoAnSys Oozie Workflow Packages.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class CoansysResolvingService extends ResolvingService {
    private CoansysWorkflowPackageSupplier packageSupplier;

    public CoansysResolvingService(CoansysWorkflowPackageSupplier packageSupplier) {
        this.packageSupplier = packageSupplier;
    }

    @Override
    public void resolve(File target, String args) throws IOException, UnresolvableResourceException {
        final String[] splits = args.split(":",3);
        final String groupId = splits[0];
        final String artifactId = splits[1];
        final String version = (splits.length == 3) ? splits[2] : null;
        try {
            packageSupplier.getCoansysWorkflowPackage(groupId, artifactId, version).extract(target);
        } catch (FileNotFoundException ex) {
            throw new UnresolvableResourceException("Could not resolve " + args + " CoAnSys workflow package", ex);
        }
    }
}
