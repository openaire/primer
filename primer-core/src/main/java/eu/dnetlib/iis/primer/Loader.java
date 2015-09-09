package eu.dnetlib.iis.primer;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class Loader {
    private final Logger log = Logger.getLogger(Loader.class);

    private SortedMap<String, ClassProvider> index = new TreeMap<String, ClassProvider>();

    public void addClassProvider(ClassProvider classProvider) {
        List<String> entryNames = classProvider.getEntryNames();
        Collections.sort(entryNames);
        String exclusionPrefix = null;
        for (String s : entryNames) {
            if (exclusionPrefix != null && s.startsWith(exclusionPrefix)) {
                log.warn("Omitting " + s + " as it is a descendant of inconsistently duplicated " + exclusionPrefix);
            } else {
                exclusionPrefix = null;
                if (index.containsKey(toggleTailingSlash(s))) {
                    log.warn("Inconsistent duplicated entries for " + s + ". Will use the first one.");
                    exclusionPrefix = s;
                } else {
                    if (index.containsKey(s)) {
                        log.warn("Duplicated entries for " + s);
                    }
                    index.put(s, classProvider);
                }
            }
        }
    }

    private static String toggleTailingSlash(String s) {
        if (s.endsWith("/")) {
            return s.substring(0, s.length() - 1);
        } else {
            return s + "/";
        }
    }

    public void prime(String classpath, File destination, Resolver resolver)
            throws UnresolvableResourceException {
        boolean didAnything = false;
        SortedMap<String, ClassProvider> tail = index.tailMap(classpath);
        for (Map.Entry<String, ClassProvider> entry : tail.entrySet()) {
            if (!entry.getKey().startsWith(classpath)) {
                break;
            }

            try {
                entry.getValue().primeOne(
                        entry.getKey(),
                        new File(destination, entry.getKey().substring(classpath.length())),
                        resolver);
                didAnything = true;
            } catch (Exception e) {
                throw new UnresolvableResourceException(e);
            }
        }

        if (!didAnything) {
            throw new UnresolvableResourceException("Could not resolve " + classpath + " using the classpath");
        }
    }
}
