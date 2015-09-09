package eu.dnetlib.iis.primer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public abstract class ClassProvider {
    /**
     * Returns a list of entries' names available in this class provider. Directory names should have a tailing slash.
     * @return
     */
    public abstract List<String> getEntryNames();

    public abstract void primeOne(String classpath, File destination, Resolver resolver) throws IOException, UnresolvableResourceException, ParseException;
}
