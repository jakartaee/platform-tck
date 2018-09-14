/*
 * Copyright 2013 International Business Machines Corp.
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

import java.util.Properties;

import javax.batch.operations.JobExecutionNotMostRecentException;
import javax.batch.runtime.JobExecution;







import com.ibm.jbatch.tck.utils.JobOperatorBridge;

public class RestartNotMostRecentTests extends ServiceEETest {
	
	private JobOperatorBridge jobOp = null;
	
	/*
	 * @testName: testRestartNotMostRecentException
	 * 
	 * @assertion: FIXME
	 * 
	 * @test_Strategy: FIXME
	 */
	
	
	public void testRestartNotMostRecentException() throws Fault {
		String METHOD = "testRestartNotMostRecentException";
TestUtil.logTrace(METHOD);
		
		try {
			TestUtil.logMsg("starting job");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			jobParams.put("execution.number", "1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_attributes_restart_true_test", jobParams);
			
			Properties restartParams = new Properties();
			TestUtil.logMsg("execution.number=2");
			restartParams.put("execution.number", "2");
			jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartParams);
		
			try {
				TestUtil.logMsg("Trying to execute the first job execution again.");
				jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartParams);
				assertWithMessage("It should have thrown JobExecutionNotMostRecentException", false);
			} catch(JobExecutionNotMostRecentException e) {
				assertWithMessage("JobExecutionNotMostRecentException thrown", true);
			}

			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());
			TestUtil.logMsg("job completed");
			
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
