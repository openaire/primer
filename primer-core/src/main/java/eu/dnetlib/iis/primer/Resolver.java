package eu.dnetlib.iis.primer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class Resolver {
    private Map<String, ResolvingService> services = new HashMap<String, ResolvingService>();
    public void addService(String protocol, ResolvingService r) {
        r.setParent(this);
        services.put(protocol, r);
    }

    public void resolve(File target, String protocol, String args) throws IOException, UnresolvableResourceException {
        ResolvingService r = services.get(protocol);
        if (r == null) {
            throw new RuntimeException("No resolver for protocol " + protocol);
        }

        r.resolve(target, args);
    }
}
