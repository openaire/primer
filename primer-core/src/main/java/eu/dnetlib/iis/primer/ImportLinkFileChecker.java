package eu.dnetlib.iis.primer;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public final class ImportLinkFileChecker {
    public static final String IMPORT_LINK_FILE_NAME = "import.txt";
    public static final String IMPORT_LINK_HEADER =
            "## This is a classpath-based import file (this header is required)";

    private static final String SPLITTING_REGEX = "[^A-Za-z0-9#]+";

    private ImportLinkFileChecker() {}

    /**
     * Checks if a file has a name following import link file rules.
     */
    public static boolean checkName(File f) {
        return f.getName().equalsIgnoreCase(IMPORT_LINK_FILE_NAME);
    }

    /**
     * Removes import link file header from a reader.
     */
    public static void dropHeader(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    /**
     * Checks if a file has a header following import link file rules.
     */
    public static boolean checkHeader(InputStream is) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String header = reader.readLine();
            return checkHeaderContents(header);
        } catch (IOException e) {
            return false;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Checks if header contents follow import link file rules.
     */
    public static boolean checkHeaderContents(String header) {
        String[] actual = splitHeader(header);
        String[] expected = splitHeader(IMPORT_LINK_HEADER);
        return Arrays.equals(actual, expected);
    }

    private static String[] splitHeader(String header) {
        return header.trim().toLowerCase(Locale.ENGLISH).split(SPLITTING_REGEX);
    }
}
