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

import static com.ibm.jbatch.tck.utils.AssertionUtils.assertObjEquals;
import static com.ibm.jbatch.tck.utils.AssertionUtils.assertWithMessage;

import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;






import com.ibm.jbatch.tck.utils.JobOperatorBridge;
import com.ibm.jbatch.tck.utils.TCKJobExecutionWrapper;

public class StopOrFailOnExitStatusWithRestartTests extends ServiceEETest {

	private static JobOperatorBridge jobOp;

	private void begin(String str) {
		TestUtil.logMsg("Begin test method: " + str);
	}

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

	public static void cleanup() throws Fault {
	}

	/*
	 * @testName: testInvokeJobWithUserStopAndRestart
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithUserStopAndRestart() throws Fault {

		String METHOD = "testInvokeJobWithUserStopAndRestart";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		final String DEFAULT_SLEEP_TIME = "5000";

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_longrunning.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties overrideJobParams = new Properties();
			TestUtil.logMsg("run.indefinitely=true");
			overrideJobParams.setProperty("run.indefinitely" , "true");

			TestUtil.logMsg("Invoke startJobWithoutWaitingForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobWithoutWaitingForResult("job_batchlet_longrunning", overrideJobParams);

			long execID = execution1.getExecutionId(); 
			TestUtil.logMsg("StopRestart: Started job with execId=" + execID + "");

			int sleepTime = Integer.parseInt(System.getProperty("StopOrFailOnExitStatusWithRestartTests.testInvokeJobWithUserStop.sleep",DEFAULT_SLEEP_TIME));
			TestUtil.logMsg("Sleep " +  sleepTime  + "");
			Thread.sleep(sleepTime); 

			BatchStatus exec1BatchStatus = execution1.getBatchStatus();
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+ exec1BatchStatus + "");
			
			// Bug 5614 - Tolerate STARTING state in addition to STARTED 
			boolean startedOrStarting = exec1BatchStatus == BatchStatus.STARTED || exec1BatchStatus == BatchStatus.STARTING;
			assertWithMessage("Found BatchStatus of " + exec1BatchStatus.toString() + "; Hopefully job isn't finished already, if it is fail the test and use a longer sleep time within the batch step-related artifact.", startedOrStarting);

			TestUtil.logMsg("Invoke stopJobAndWaitForResult");
			jobOp.stopJobAndWaitForResult(execution1);

			JobExecution postStopJobExecution = jobOp.getJobExecution(execution1.getExecutionId());
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+postStopJobExecution.getBatchStatus());
			assertWithMessage("The stop should have taken effect by now, even though the batchlet artifact had control at the time of the stop, it should have returned control by now.", 
					BatchStatus.STOPPED, postStopJobExecution.getBatchStatus());  

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+postStopJobExecution.getExitStatus());
			assertWithMessage("If this assert fails with an exit status of STOPPED, try increasing the sleep time. It's possible" +
					"the JobOperator stop is being issued before the Batchlet has a chance to run.", "BATCHLET CANCELED BEFORE COMPLETION", postStopJobExecution.getExitStatus());

			TestUtil.logMsg("Create job parameters for execution #2:");
			TestUtil.logMsg("run.indefinitely=false");
			overrideJobParams.setProperty("run.indefinitely" , "false");


			TestUtil.logMsg("Invoke restartJobAndWaitForResult with executionId: " + execution1.getInstanceId() + "");
			JobExecution execution2 = jobOp.restartJobAndWaitForResult(execution1.getExecutionId(),overrideJobParams);

			TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+execution2.getBatchStatus());
			assertWithMessage("If the restarted job hasn't completed yet then try increasing the sleep time.", 
					BatchStatus.COMPLETED, execution2.getBatchStatus());

			TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+execution2.getExitStatus());
			assertWithMessage("If this fails, the reason could be that step 1 didn't run the second time," + 
					"though it should since it won't have completed successfully the first time.", 
					"GOOD.STEP.GOOD.STEP", execution2.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithUncaughtExceptionFailAndRestart
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithUncaughtExceptionFailAndRestart() throws Fault {
		String METHOD = "testInvokeJobWithUncaughtExceptionFailAndRestart";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_longrunning.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParameters = new Properties();
			TestUtil.logMsg("throw.exc.on.number.3=true");
			jobParameters.setProperty("throw.exc.on.number.3" , "true");  // JSL default is 'false'

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			TCKJobExecutionWrapper firstJobExecution = jobOp.startJobAndWaitForResult("job_batchlet_longrunning", jobParameters);

			TestUtil.logMsg("Started job with execId=" + firstJobExecution.getExecutionId());       

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+firstJobExecution.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+firstJobExecution.getExitStatus());
			assertWithMessage("If the job hasn't failed yet then try increasing the sleep time.", BatchStatus.FAILED, firstJobExecution.getBatchStatus());    
			assertObjEquals("FAILED", firstJobExecution.getExitStatus());

			TestUtil.logMsg("Create job parameters for execution #2:");
			Properties overrideJobParams = new Properties();
			TestUtil.logMsg("throw.exc.on.number.3=false");
			TestUtil.logMsg("run.indefinitely=false");
			overrideJobParams.setProperty("throw.exc.on.number.3" , "false");
			overrideJobParams.setProperty("run.indefinitely" , "false");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult with executionId: " + firstJobExecution.getInstanceId() + "");
			JobExecution secondJobExecution = jobOp.restartJobAndWaitForResult(firstJobExecution.getExecutionId(),overrideJobParams);

			TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+secondJobExecution.getBatchStatus());
			assertWithMessage("If the restarted job hasn't completed yet then try increasing the sleep time.", 
					BatchStatus.COMPLETED, secondJobExecution.getBatchStatus());

			TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+secondJobExecution.getExitStatus());
			assertWithMessage("If this fails with only \"GOOD.STEP\", the reason could be that step 1 didn't run the second time," + 
					"though it should since it won't have completed successfully the first time.", 
					"GOOD.STEP.GOOD.STEP", secondJobExecution.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}



	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}
}
