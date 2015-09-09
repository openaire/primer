package eu.dnetlib.iis.primer.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;

import java.io.File;

/**
 * 
 * @author Mateusz Fedoryszak
 *
 */
//@Category(IntegrationTest.class)
public class ExistenceTest {
    private static final File primedClassDir = new File("target/primed");
	@Ignore 
	/** TODO: This test references some workflow from IIS code base. 
	 * This shouldn't be the case because "primer" should be an independent project.
	 * We need to create another sandbox example of using this plugin.*/
	public void testConverter() {
        Assert.assertTrue(new File(primedClassDir, "my_test_workflow/oozie_app/workflow.xml").exists());
	}
	
}
