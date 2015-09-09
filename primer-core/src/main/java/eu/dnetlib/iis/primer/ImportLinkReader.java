package eu.dnetlib.iis.primer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
public class ImportLinkReader {
    private final Logger log = Logger.getLogger(ImportLinkReader.class);

    private BufferedReader reader;

    public ImportLinkReader(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void performResolution(File workingDir, Resolver resolver)
            throws IOException, UnresolvableResourceException, ParseException {
        log.debug("Performing resolution in " + workingDir);
        String line;
        int lineNo = 0;
        ImportLinkFileChecker.dropHeader(reader);
        while ((line = reader.readLine()) != null) {
            ++lineNo;
            if (StringUtils.isBlank(line)) {
                continue;
            }
            final int COLUMN_COUNT = 3;
            String[] splits = line.split("\\s+", COLUMN_COUNT);
            if (splits.length < COLUMN_COUNT) {
                throw new ParseException("Malformed line in " + workingDir + File.separator + "import.txt file", lineNo);
            }
            resolver.resolve(new File(workingDir, splits[0]), splits[1], splits[2]);
        }
    }


}
