/*
 * Copyright 2012, 2020 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.jbatch.tck.tests.jslxml;


import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import static com.ibm.jbatch.tck.utils.AssertionUtils.assertObjEquals;

import java.io.File;
import java.util.Properties;

import jakarta.batch.runtime.JobExecution;

import com.ibm.jbatch.tck.utils.JobOperatorBridge;







public class PropertySubstitutionTests extends ServiceEETest {

	private static JobOperatorBridge jobOp;

	public static void setup(String[] args, Properties props) throws Fault {
		String METHOD = "setup";

TestUtil.logTrace(METHOD);

		try {
			jobOp = new JobOperatorBridge();
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	
	
	public static void setUp() throws Fault {
		jobOp = new JobOperatorBridge();
	}

	
	public static void tearDown() throws Fault {
	}

	
	public void cleanup() throws Fault {
		// Clear this property for next test
		System.clearProperty("property.junit.result");
	}

	/*
	 * @testName: testBatchArtifactPropertyInjection
	 * 
	 * @assertion: Section 6.3.1, The @BatchProperty may be used on a class
	 * field for any class identified as a batch programming model artifact
	 * 
	 * @test_Strategy: Include a property element on a Batchlet artifact and
	 * verify the java value of the String is set to the same value as in the
	 * job xml.
	 */
	
	
	public void testBatchArtifactPropertyInjection() throws Fault {
		String METHOD = "testBatchArtifactPropertyInjection";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Set system property: property.junit.propName=myProperty1");
			System.setProperty("property.junit.propName", "myProperty1");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2");

			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals("value1", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}


	/*
	 * @testName: testInitializedPropertyIsOverwritten
	 * 
	 * @assertion: The java initialized value is overwritten with the required
	 * injected value.
	 * 
	 * @test_Strategy: Include a property element on a Batchlet artifact and
	 * verify the java value of the String is set to the same value as in the
	 * job xml even if the Java field is initialized.
	 */
	
	
	public void testInitializedPropertyIsOverwritten() throws Fault {

		String METHOD = "testInitializedPropertyIsOverwritten";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Set system property: property.junit.propName=myProperty2");
			System.setProperty("property.junit.propName", "myProperty2");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2");

			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals("value2", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testPropertyWithJobParameter
	 * 
	 * @assertion: Job Parameters submitted on start are substituted in the job
	 * XML using the following syntax, #{jobParameters['<property-name>']}
	 * 
	 * @test_Strategy: Submit job parameters through JobOperator.start(String,
	 * Properties) and verify that xml string is substituted correctly by
	 * injecting the property into a batch artifact
	 */
	
	
	public void testPropertyWithJobParameter() throws Fault {

		String METHOD = "testPropertyWithJobParameter";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParameters = new Properties();
			String expectedResult = "mySubmittedValue";
			TestUtil.logMsg("mySubmittedPropr=" + expectedResult + "");
			jobParameters.setProperty("mySubmittedProp", expectedResult);

			TestUtil.logMsg("Set system property: property.junit.propName=mySubmittedProp");
			System.setProperty("property.junit.propName", "mySubmittedProp");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2", jobParameters);

			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals(expectedResult, result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}


	/*
	 * @testName: testDefaultPropertyName
	 * 
	 * @assertion: If a name value is not supplied on the @BatchProperty
	 * qualifier the property name defaults to the java field name.
	 * 
	 * @test_Strategy: Supply an xml property element with the name matching the
	 * Java field name and verify that the java field value injected matches the
	 * value provided through the job xml
	 */
	
	
	public void testDefaultPropertyName() throws Fault {

		String METHOD = "testDefaultPropertyName";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Set system property:property.junit.propName=property4");
			System.setProperty("property.junit.propName", "property4");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2");

			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals("value4", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testGivenPropertyName
	 * 
	 * @assertion: The name provided on the @BatchProperty annotation maps to the job xml name of the property element
	 * 
	 * @test_Strategy: Supply an xml property element with the name matching the
	 * @BatchProperty name and verify that the java field value injected matches the
	 * value provided through the job xml.
	 */
	
	
	public void testGivenPropertyName() throws Fault {

		String METHOD = "testGivenPropertyName";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Set system property:property.junit.propName=myProperty4");
			System.setProperty("property.junit.propName", "myProperty4");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2");

			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals("value4", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testPropertyInnerScopePrecedence
	 * 
	 * @assertion: A batch artifacts properties overrides any parent properties defined in 
	 * an outer XML scope. Child properties always override parent properties with the same name.
	 * 
	 * @test_Strategy: Issue a job with a step level property and batchlet property with the same name.
	 * Verify that the injected property value is from the artifact level property.
	 */
	
	
	public void testPropertyInnerScopePrecedence() throws Fault {

		String METHOD = "testPropertyInnerScopePrecedence";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Set system property:property.junit.propName=batchletProp");
			System.setProperty("property.junit.propName", "batchletProp");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2");

			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals("batchletPropValue", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testPropertyQuestionMarkSimple
	 * 
	 * @assertion: Section 5.7, The ?:...; syntax is honored for property
	 * defaulting, if a property cannot be resolved.
	 * 
	 * @test_Strategy: A jobParameter property is used in the XML which is never passed
	 * in programmatically resulting in an unresolved property. The property then defaults 
	 * to the default value expression which is verified through it's Java value in the 
	 * batctlet artifact. 
	 */
	
	
	public void testPropertyQuestionMarkSimple() throws Fault {

		String METHOD = "testPropertyQuestionMarkSimple";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Set system property:property.junit.propName=defaultPropName1");
			System.setProperty("property.junit.propName", "defaultPropName1");

			TestUtil.logMsg("Set system property:file.name.junit=myfile1.txt");
			System.setProperty("file.name.junit", "myfile1.txt");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2");

			// String result = jobExec.getStatus();
			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals("myfile1.txt", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testPropertyQuestionMarkComplex
	 * 
	 * @assertion: Section 5.7, Accord to the spec grammar multiple properties can each have their own default 
	 * values and also be concatenated with string literals.
	 * 
	 * @test_Strategy: Two jobParameter properties are used in the XML which are never passed
	 * in programmatically resulting in an unresolved property. The properties then default 
	 * to the default value expression.  This is verified through the injected Java value in the 
	 * batctlet artifact. 
	 */
	
	
	public void testPropertyQuestionMarkComplex() throws Fault {

		String METHOD = "testPropertyQuestionMarkComplex";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Set system property:property.junit.propName=defaultPropName2");
			System.setProperty("property.junit.propName", "defaultPropName2");

			TestUtil.logMsg("Set system property:file.name.junit=myfile2.txt");
			System.setProperty("file.name.junit", "myfile2");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2");

			// String result = jobExec.getStatus();
			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");

			assertObjEquals(File.separator + "myfile2.txt", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testPropertyWithConcatenation
	 * 
	 * @assertion: Section 5.7, Properties can be concatenated with string literals.
	 * 
	 * @test_Strategy: Supply an xml property element with the name matching the
	 * @BatchProperty name and verify that the java field value injected matches the
	 * value provided through the job xml. 
	 */
	
	
	public void testPropertyWithConcatenation() throws Fault {

		String METHOD = "testPropertyWithConcatenation";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParameters = new Properties();
			TestUtil.logMsg("myFilename=testfile1");
			jobParameters.setProperty("myFilename", "testfile1");

			TestUtil.logMsg("Set system property:file.name.junit=myConcatProp");
			System.setProperty("property.junit.propName", "myConcatProp");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2", jobParameters);

			String result = System.getProperty("property.junit.result");
			TestUtil.logMsg("Test result: " + result + "");
			assertObjEquals("testfile1.txt", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testJavaSystemProperty
	 * 
	 * @assertion: Section 5.7, Java system properties can be substituted in the XML using 
	 * the following systax#{systemProperties['some.property']}, and properties can be 
	 * concatenated.
	 * 
	 * @test_Strategy: Supply an xml property element with the name matching the
	 * @BatchProperty name and verify that the java field value injected matches the
	 * value provided through the job xml. The "file.separator" system property is used
	 * in this test.
	 * 
	 */
	
	
	public void testJavaSystemProperty() throws Fault {

		String METHOD = "testJavaSystemProperty";

TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_properties2.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParameters = new Properties();
			TestUtil.logMsg("myFilename=testfile2");
			jobParameters.setProperty("myFilename", "testfile2");

			TestUtil.logMsg("Set system property:file.name.junit=myJavaSystemProp");
			System.setProperty("property.junit.propName", "myJavaSystemProp");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_properties2", jobParameters);
			String result = System.getProperty("property.junit.result");

			String pathSep = System.getProperty("file.separator");

			TestUtil.logMsg("Test result: " + pathSep + "test" + pathSep + "testfile2.txt");
			assertObjEquals(pathSep + "test" + pathSep + "testfile2.txt", result);
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}
}
