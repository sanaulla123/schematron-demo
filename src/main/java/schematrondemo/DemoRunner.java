package schematrondemo;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.oclc.purl.dsdl.svrl.FailedAssert;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.state.EValidity;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;

public class DemoRunner {
	private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);
	
	public static void main(String[] args) throws Exception {
		InputStream sampleStream = DemoRunner.class.getClassLoader().getResourceAsStream("sample.xml");
		if ( sampleStream == null ){
			log.error("File sample.xml not found in classpath");
			System.exit(1);
		}
		String schematronFileName = "sample_schematron.sch";
		URL schUrl = DemoRunner.class.getClassLoader().getResource(schematronFileName);
		if( schUrl != null){
			log.debug("SCH URL: {} ", schUrl.toExternalForm());
		}else{
			log.debug("SCH URL is null");
		}
		
		if ( schUrl == null ){
			log.error("File sample_schematron.sch not found in classpath");
			System.exit(1);
		}
		
		final ISchematronResource aResPure = SchematronResourcePure.fromURL(schUrl);
		if (!aResPure.isValidSchematron ()){
			log.error("Schematron rules file for {} is invalid ", schematronFileName);
			System.exit(1);
		}
		
		EValidity eValidity = aResPure.getSchematronValidity(new StreamSource(new InputStreamReader(sampleStream)));
		
		log.info("Schematron result: " + eValidity.isValid());
		if ( eValidity.isInvalid()){
			log.error("XML in invalid");
			sampleStream = DemoRunner.class.getClassLoader().getResourceAsStream("sample.xml");
			SchematronOutputType schematronValidationResult = aResPure.applySchematronValidationToSVRL(new StreamSource(new InputStreamReader(sampleStream)));
			for(Object object : schematronValidationResult.getActivePatternAndFiredRuleAndFailedAssert()){
				if ( object instanceof FailedAssert){
					FailedAssert failedAssert = (FailedAssert) object;
					String failedValidationMessage = failedAssert.getText().trim();
					String failedLocation = failedAssert.getLocation();
					String failedTest = failedAssert.getTest();
					log.info("Validation Failed: {}, Failed Location: {{}}, Failed Test: {{}} ", failedValidationMessage, failedLocation, failedTest);
				}
			}
			
		}else{
			log.error("XML is valid");
		}
	}
}
