package eu.dnetlib.iis.primer;

import java.io.File;
import java.io.IOException;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public abstract class ResolvingService {
    private Resolver parent;

    public Resolver getParent() {
        return parent;
    }

    public void setParent(Resolver parent) {
        this.parent = parent;
    }

    public abstract void resolve(File target, String args) throws IOException, UnresolvableResourceException;
}
