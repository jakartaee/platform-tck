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







public class JobLevelPropertiesTests extends ServiceEETest {

	private JobOperatorBridge jobOp = null;
	
	private String FOO_VALUE = "bar";

	/**
	 * @testName: testJobLevelPropertiesCount
	 * @assertion: Section 5.1.3 Job Level Properties
	 * @test_Strategy: set a list of properties to job should add them to the job context properties.
	 * 				   Also tests that job parameters and other JSL properties (from other levels of nested elements) do
	 *                 not appear in JobContext.getProperties().  A naming convention is used rather than counting
	 *                 so as not to prevent a runtime from adding some unknown-to-TCK impl-specific properties. 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testJobLevelPropertiesCount() throws Fault {
		
		String METHOD = "testJobLevelPropertiesCount";
TestUtil.logTrace(METHOD);
		String SHOULD_BE_UNAVAILABLE_PROP_PREFIX = "com.ibm.jbatch.tck.tests.jslxml.JobLevelPropertiesTests";
		Properties jobParams = new Properties();
		jobParams.put(SHOULD_BE_UNAVAILABLE_PROP_PREFIX + ".parm1", "should.not.appear.in.job.context.properties");
		
		try {
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_level_properties_count", jobParams);
	
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());
			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			assertWithMessage("Job completed", "VERY GOOD INVOCATION", jobExec.getExitStatus());
			TestUtil.logMsg("job completed");
			
		} catch (Exception e) {
            handleException(METHOD, e);
        }
	}
	
	/**
	 * @testName: testJobLevelPropertiesPropertyValue
	 * @assertion: Section 5.1.3 Job Level Properties
	 * @test_Strategy: set a job property value should equal value set on job context property.  
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testJobLevelPropertiesPropertyValue() throws Fault {
		
		String METHOD = "testJobLevelPropertiesPropertyValue";
TestUtil.logTrace(METHOD);


		try {
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_level_properties_value");
	
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());
			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());			
			TestUtil.logMsg("job completed");
			
			assertWithMessage("Property value", FOO_VALUE, jobExec.getExitStatus());
			
			TestUtil.logMsg("Job batchlet return code is the job property foo value " + FOO_VALUE);
		} catch (Exception e) {
            handleException(METHOD, e);
        }
	}

	/**
	 * @testName: testJobLevelPropertiesShouldNotBeAvailableThroughStepContext
	 * @assertion: Section 5.1.3 Job Level Properties
	 * @test_Strategy: set a job property value should not be available to step context 
	 * 
	 * @throws InterruptedException
	 */
	 
	public void testJobLevelPropertiesShouldNotBeAvailableThroughStepContext() throws Fault {
		
		String METHOD = "testJobLevelPropertiesShouldNotBeAvailableThroughStepContext";
TestUtil.logTrace(METHOD);
		
		try {
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_level_properties_scope");
	
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());
			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("job completed");
			
			assertWithMessage("Job Level Property is not available through step context", BatchStatus.COMPLETED.name(), jobExec.getExitStatus());
			TestUtil.logMsg("Job batchlet return code is the job.property read through step context (expected value=COMPLETED) " + jobExec.getExitStatus());
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
