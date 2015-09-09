package eu.dnetlib.maven.plugin.primer;

import eu.dnetlib.iis.primer.*;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Locale;

/**
 * This plugin processes import.txt files in specified classpath and stores the results.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
@Mojo(name = "prime")
public class PrimerMojo extends AbstractMojo {
    /**
     * Class providers. They may be either directories with appropriate layout or jars. Wildcards are supported
     * in the last path element.
     */
    @Parameter(property = "prime.classProviders")
    private List<File> classProviderFiles;

    /**
     * A directory containing CoAnSys workflow packages.
     */
    @Parameter
    private File coansysPackageDir;

    /**
     * A package to be primed.
     */
    @Parameter
    private String classpath = "";

    /**
     * A directory where a primed package will be put.
     */
    @Parameter
    private File destination;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Loader loader = new Loader();
        for (File file : classProviderFiles) {
            getLog().debug(file.toString());
            try {
                for (File f : file.getParentFile().listFiles((FileFilter) new WildcardFileFilter(file.getName()))) {
                    try {
                        getLog().info(f.toString());
                        if (f.getName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
                            loader.addClassProvider(new JarClassProvider(f));
                        } else {
                            loader.addClassProvider(new FileSystemClassProvider(f));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Resolver resolver = new Resolver();
        ClasspathResolvingService classpathResolvingService = new ClasspathResolvingService(loader);
        resolver.addService("classpath", classpathResolvingService);

        if (coansysPackageDir != null) {
            resolver.addService(
                    "coansys",
                    new CoansysResolvingService(new FileSystemCoansysWorkflowPackageSupplier(coansysPackageDir)));
        }

        getLog().info("Priming " + classpath + " into " + destination.getAbsolutePath());

        try {
            loader.prime(classpath, destination, resolver);
        } catch (Exception e) {
            throw new MojoExecutionException("Error while priming " + classpath + " to " + destination, e);
        }
    }
}
