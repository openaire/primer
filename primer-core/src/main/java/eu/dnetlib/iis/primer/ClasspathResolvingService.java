package eu.dnetlib.iis.primer;

import java.io.File;
import java.io.IOException;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class ClasspathResolvingService extends ResolvingService {
    private final Loader loader;


    public ClasspathResolvingService(Loader loader) {
        this.loader = loader;
    }

    @Override
    public void resolve(File target, String args) throws IOException, UnresolvableResourceException {
        loader.prime(args, target, getParent());
    }
}
