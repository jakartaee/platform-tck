/*
 * Copyright 2012 International Business Machines Corp.
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

import static com.ibm.jbatch.tck.utils.AssertionUtils.assertWithMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;

import com.ibm.jbatch.tck.utils.JobOperatorBridge;







public class StepLevelPropertiesTests extends ServiceEETest {

	private JobOperatorBridge jobOp = null;

	private int PROPERTIES_COUNT = 3;

	private String FOO_VALUE = "bar";

	/**
	 * @testName: testStepLevelPropertiesCount
	 * @assertion: Section 5.2.3 Step Level Properties
	 * @test_Strategy: set a list of properties to the step should add them to the step context properties
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testStepLevelPropertiesCount() throws Fault {

		String METHOD = "testStepLevelPropertiesCount";
TestUtil.logTrace(METHOD);
		String SHOULD_BE_UNAVAILABLE_PROP_PREFIX = "com.ibm.jbatch.tck.tests.jslxml.StepLevelPropertiesTests";
		
		Properties jobParams = new Properties();
		jobParams.put(SHOULD_BE_UNAVAILABLE_PROP_PREFIX + ".parm1", "should.not.appear.in.step.context.properties");
		
		try {

			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("step_level_properties_count");

			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());
			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			assertWithMessage("Job completed", "VERY GOOD INVOCATION", jobExec.getExitStatus());
			TestUtil.logMsg("job completed");
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/**
	 * @testName: testStepLevelPropertiesPropertyValue
	 * @assertion: Section 5.2.3 Step Level Properties
	 * @test_Strategy: set a step property value should equal value set on step context property 
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testStepLevelPropertiesPropertyValue() throws Fault {

		String METHOD = "testStepLevelPropertiesPropertyValue";
TestUtil.logTrace(METHOD);

		try {

			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("step_level_properties_value");

			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());
			assertWithMessage("Job completed",BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("job completed");

			assertWithMessage("Property value", FOO_VALUE, jobExec.getExitStatus());

			TestUtil.logMsg("Job batchlet return code is the step property foo value " + FOO_VALUE);
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/**
	 * @testName: testStepLevelPropertiesShouldNotBeAvailableThroughJobContext
	 * @assertion: Section 5.2.3 Step Level Properties
	 * @test_Strategy: set a step property value should not be available to job context 
	 * 
	 * @throws InterruptedException
	 */
	 
	public void testStepLevelPropertiesShouldNotBeAvailableThroughJobContext() throws Fault {

		String METHOD = "testStepLevelPropertiesShouldNotBeAvailableThroughJobContext";
TestUtil.logTrace(METHOD);

		try {

			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("step_level_properties_scope");

			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());
			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("job completed");
			assertWithMessage("Step Level Property is not available through job context",BatchStatus.COMPLETED.name(), jobExec.getExitStatus());
			TestUtil.logMsg("Job batchlet return code is the step.property read through job context (expected value=COMPLETED) " + jobExec.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

	public void setup(String[] args, Properties props) throws Fault {

		String METHOD = "setup";
TestUtil.logTrace(METHOD);

		try {
			jobOp = new JobOperatorBridge();
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/* cleanup */
	public void  cleanup()
	{		

	}

	
		public void beforeTest() throws ClassNotFoundException {
		jobOp = new JobOperatorBridge();

	}

	
	public void afterTest() {
		jobOp = null;
	}

}
