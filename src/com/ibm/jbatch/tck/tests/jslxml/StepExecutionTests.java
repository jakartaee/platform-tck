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

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;

import com.ibm.jbatch.tck.artifacts.reusable.MyBatchletImpl;
import com.ibm.jbatch.tck.artifacts.reusable.MyPersistentUserData;
import com.ibm.jbatch.tck.utils.JobOperatorBridge;







public class StepExecutionTests extends ServiceEETest {

	private final static Logger logger = Logger.getLogger(StepExecutionTests.class.getName());

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

	
	
	public static void setUp()throws Fault {
		jobOp = new JobOperatorBridge();
	}

	
	public static void cleanup() throws Fault {
	}

	private void begin(String str) {
		TestUtil.logMsg("Begin test method: " + str + "");
	}

	/*
	 * @testName: testOneStepExecutionStatus
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testOneStepExecutionStatus() throws Fault {

		String METHOD = "testOneStepExecutionStatus";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_1step.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_1step");

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());

			assertObjEquals(1, steps.size());

			for (StepExecution step : steps) {
				// make sure all steps finish successfully
				showStepState(step);
				TestUtil.logMsg("Step status = " + step.getBatchStatus() + "");
				assertObjEquals(BatchStatus.COMPLETED, step.getBatchStatus());
			}

			TestUtil.logMsg("Job execution status = " + jobExec.getBatchStatus() + "");
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testFourStepExecutionStatus
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testFourStepExecutionStatus() throws Fault {

		String METHOD = "testFourStepExecutionStatus";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_4steps.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_4steps");

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());
			assertObjEquals(4, steps.size());

			Set<Long> stepExecutionsSeen = new HashSet<Long>();
			for (StepExecution step : steps) {
				// check that each step completed successfully
				showStepState(step);
				TestUtil.logMsg("Step status = " + step.getBatchStatus() + "");
				assertObjEquals(BatchStatus.COMPLETED, step.getBatchStatus());
				
				// Let's also make sure all four have unique IDs, to make sure the JobExecution id isn't being used say
				assertWithMessage("New StepExecution id", !stepExecutionsSeen.contains(step.getStepExecutionId()));
				stepExecutionsSeen.add(step.getStepExecutionId());
			}
			TestUtil.logMsg("Job execution status = " + jobExec.getBatchStatus() + "");
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testFailedStepExecutionStatus
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testFailedStepExecutionStatus() throws Fault {
		String METHOD = "testFailedStepExecutionStatus";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_failElement.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_failElement");

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());
			assertObjEquals(1, steps.size());
			for (StepExecution step : steps) {
				// check that each step completed successfully
				// TODO: shouldn't the step status be failed here ???
				showStepState(step);
			}

			TestUtil.logMsg("Job execution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Job execution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("TEST_FAIL", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.FAILED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testStoppedStepExecutionStatus
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testStoppedStepExecutionStatus() throws Fault {
		String METHOD = "testStoppedStepExecutionStatus";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_stopElement.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_stopElement");

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());
			assertObjEquals(1, steps.size());
			for (StepExecution step : steps) {
				// check that each step completed successfully
				// TODO: shouldn't the step status be stopped here ???
				showStepState(step);
			}

			TestUtil.logMsg("Job execution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	} 

	/*
	 * @testName: testPersistedStepData
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testPersistedStepData() throws Fault {
		String METHOD = "testPersistedStepData";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_persistedData.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParameters = new Properties();
			TestUtil.logMsg("force.failure=true");
			jobParameters.setProperty("force.failure" , "true");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_persistedData", jobParameters);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.FAILED, jobExec.getBatchStatus());

			//This job should only have one step.
			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());
			StepExecution stepExec = steps.get(0);
			assertObjEquals(1, steps.size());

			TestUtil.logMsg("execution #1 StepExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.FAILED, stepExec.getBatchStatus());
			assertObjEquals(4, ((MyPersistentUserData)stepExec.getPersistentUserData()).getData());

			//jobParameters.setProperty("force.failure" , "false");
			TestUtil.logMsg("Invoke restartJobAndWaitForResult with execution id: " + jobExec.getExecutionId() + "");
			JobExecution restartedJobExec = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(),jobParameters);

			//This job should only have one step.

			steps = jobOp.getStepExecutions(restartedJobExec.getExecutionId());
			stepExec = steps.get(0);

			TestUtil.logMsg("execution #1 StepExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, stepExec.getBatchStatus());
			assertObjEquals(5, ((MyPersistentUserData)stepExec.getPersistentUserData()).getData());		

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}


	/*
	 * @testName: testStepExecutionExitStatus
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testStepExecutionExitStatus() throws Fault {
		String METHOD = "testStepExecutionExitStatus";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_failElement.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_failElement");

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());
			assertObjEquals(1, steps.size());
			
			StepExecution step = steps.get(0);
			showStepState(step);
			assertWithMessage("Check step exit status", MyBatchletImpl.GOOD_EXIT_STATUS, step.getExitStatus());
			assertWithMessage("Check step batch status", BatchStatus.COMPLETED, step.getBatchStatus());
			TestUtil.logMsg("Job batch status =" + jobExec.getBatchStatus() + "");
			TestUtil.logMsg("Job exit status =" + jobExec.getExitStatus() + "");
			assertWithMessage("Check job batch status", BatchStatus.FAILED, jobExec.getBatchStatus());
			assertWithMessage("Check job exit status", "TEST_FAIL", jobExec.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testStepInFlowStepExecution
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testStepInFlowStepExecution() throws Fault {
		String METHOD = "testStepInFlowStepExecution";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_failElement.xml");
			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("flow_transition_to_step");

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());
			assertObjEquals(4, steps.size());
			for (StepExecution step : steps) {
				showStepState(step);
				// Conveniently all four steps have same exit and batch status.
				assertWithMessage("Check step exit status", MyBatchletImpl.GOOD_EXIT_STATUS, step.getExitStatus());
				assertWithMessage("Check step batch status", BatchStatus.COMPLETED, step.getBatchStatus());
			}

			// Now check job level status
			TestUtil.logMsg("Job batch status =" + jobExec.getBatchStatus() + "");
			TestUtil.logMsg("Job exit status =" + jobExec.getExitStatus() + "");
			assertWithMessage("Check job batch status", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			assertWithMessage("Check job exit status", "flow1step1, flow1step2, flow1step3, step1", jobExec.getExitStatus());

		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testStepInFlowInSplitStepExecution
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testStepInFlowInSplitStepExecution() throws Fault {
		String METHOD = "testStepInFlowInSplitStepExecution";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: split_batchlet_4steps.xml");
			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("split_batchlet_4steps");
			
			// Saves debugging time to check these two first in case they fail
			assertWithMessage("Check job batch status", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			assertWithMessage("Check job exit status", "COMPLETED", jobExec.getExitStatus());

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> steps = jobOp.getStepExecutions(jobExec.getExecutionId());
			assertObjEquals(4, steps.size());
			for (StepExecution step : steps) {
				showStepState(step);
				// Conveniently all four steps have same exit and batch status.
				assertWithMessage("Check step exit status", MyBatchletImpl.GOOD_EXIT_STATUS, step.getExitStatus());
				assertWithMessage("Check step batch status", BatchStatus.COMPLETED, step.getBatchStatus());
			}
			TestUtil.logMsg("Job batch status =" + jobExec.getBatchStatus() + "");
			TestUtil.logMsg("Job exit status =" + jobExec.getExitStatus() + "");

		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	private void showStepState(StepExecution step) {
		TestUtil.logMsg("---------------------------");
		TestUtil.logMsg("getStepName(): " + step.getStepName() + " - ");
		TestUtil.logMsg("getStepExecutionId(): " + step.getStepExecutionId() + " - ");
		Metric[] metrics = step.getMetrics();

		for (int i = 0; i < metrics.length; i++) {
			TestUtil.logMsg(metrics[i].getType() + ": " + metrics[i].getValue() + " - ");
		}

		TestUtil.logMsg("getStartTime(): " + step.getStartTime() + " - ");
		TestUtil.logMsg("getEndTime(): " + step.getEndTime() + " - ");
		TestUtil.logMsg("getBatchStatus(): " + step.getBatchStatus() + " - ");
		TestUtil.logMsg("getExitStatus(): " + step.getExitStatus());
		TestUtil.logMsg("---------------------------");
	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

}
