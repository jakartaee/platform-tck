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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;







import com.ibm.jbatch.tck.utils.JobOperatorBridge;

public class FlowTransitioningTests extends ServiceEETest {

	private JobOperatorBridge jobOp = null;

	/**
	 * @testName: testFlowTransitionToStep
	 * @assertion: Section 5.3 Flow
	 * @test_Strategy: 1. setup a job consisting of one flow (w/ 3 steps) and one step
	 * 				   2. start job 
	 * 				   3. create a list of step id's as they are processed
	 * 				   4. return the list from step 3 as job exit status
	 * 				   5. compare that list to our transition list
	 * 		           6. verify that in fact we transition from each step within the flow, then to the flow "next" step
	 * 
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testFlowTransitionToStep() throws Fault {

		String METHOD = "testFlowTransitionToStep";
TestUtil.logTrace(METHOD);

		try {

			String[] transitionList = {"flow1step1", "flow1step2", "flow1step3", "step1"};
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("flow_transition_to_step", null);
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());

			String[] jobTransitionList = jobExec.getExitStatus().split(",");
			assertWithMessage("transitioned to exact number of steps", transitionList.length, jobTransitionList.length);
			for (int i = 0; i < jobTransitionList.length; i++) {
				assertWithMessage("Flow transitions", transitionList[i], jobTransitionList[i].trim());
			}

			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("Job completed");
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/**
	 * @testName: testFlowTransitionToStepOutOfScope
	 * @assertion: Section 5.3 Flow
	 * @test_Strategy: 1. setup a job consisting of one flow (w/ 3 steps) and one step
	 * 				   2. start job 
	 * 				   3. this job should fail because the flow step flow1step2 next to outside the flow 
	 *                   (Alternatively, the implementation may choose to validate and prevent this from starting,
	 *                   by throwing a JobStartException).
	 * 
	 * 	<flow id="flow1">
	 *		<step id="flow1step1" next="flow1step2">
	 *			<batchlet ref="flowTransitionToStepTestBatchlet"/>
	 *		</step>
	 *		<step id="flow1step2" next="step1">
	 *			<batchlet ref="flowTransitionToStepTestBatchlet"/>
	 *		</step>
	 *		<step id="flow1step3">
	 *			<batchlet ref="flowTransitionToStepTestBatchlet"/>
	 *		</step>
	 *	</flow>

	 *	<step id="step1">
	 *		<batchlet ref="flowTransitionToStepTestBatchlet"/>
	 * 	</step>
	 *
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	
	public void testFlowTransitionToStepOutOfScope() throws Fault {

		String METHOD = " testFlowTransitionToStepOutOfScope";
TestUtil.logTrace(METHOD);

		try {
			boolean seenException = false;
			TestUtil.logMsg("starting job");
			JobExecution jobExec = null;
			try {
				jobExec = jobOp.startJobAndWaitForResult("flow_transition_to_step_out_of_scope", null);
			} catch (JobStartException e) {
				TestUtil.logMsg("Caught JobStartException:  " + e.getLocalizedMessage());
				seenException = true;
			}

			// If we caught an exception we'd expect that a JobExecution would not have been created,
			// though we won't validate that it wasn't created.  

			// If we didn't catch an exception that we require that the implementation fail the job execution.
			if (!seenException) {
				TestUtil.logMsg("Didn't catch JobStartException, Job Batch Status = " + jobExec.getBatchStatus());
				assertWithMessage("Job should have failed because of out of scope execution elements.", 
						BatchStatus.FAILED, jobExec.getBatchStatus());
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/**
	 * @testName: testFlowTransitionToDecision
	 * @assertion: Section 5.3 Flow
	 * @test_Strategy: 1. setup a job consisting of one flow (w/ 3 steps) and one decision
	 * 				   2. start job 
	 * 				   3. flow will transition to decider which will change the exit status
	 * 				   4. compare that the exit status set by the decider matches that of the job
	 * 
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testFlowTransitionToDecision() throws Fault {

		String METHOD = "testFlowTransitionToDecision";
TestUtil.logTrace(METHOD);

		try {
			String exitStatus = "ThatsAllFolks";
			// based on our decider exit status
			/*
			<decision id="decider1" ref="flowTransitionToDecisionTestDecider">
				<end exit-status="ThatsAllFolks" on="DECIDER_EXIT_STATUS*VERY GOOD INVOCATION" />
			</decision>
			 */
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("flow_transition_to_decision", null);
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());

			assertWithMessage("Job Exit Status is from decider", exitStatus, jobExec.getExitStatus());
			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("Job completed");
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/**
	 * @testName: testFlowTransitionWithinFlow
	 * @assertion: Section 5.3 Flow
	 * @test_Strategy: 1. setup a job consisting of one flow (w/ 3 steps and 1 decision)
	 * 				   2. start job 
	 * 				   3. within the flow step1 will transition to decider then to step2 and finally step3.
	 * 				   4. create a list of step id's as they are processed
	 * 				   4. return the list from step 3 as job exit status
	 * 				   5. compare that list to our transition list
	 * 		           6. verify that in fact we transition from each step within the flow, then to the flow "next" step
	 * 
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testFlowTransitionWithinFlow() throws Fault {

		String METHOD = "testFlowTransitionWithinFlow";
TestUtil.logTrace(METHOD);

		try {
			String[] transitionList = {"flow1step1", "flow1step2", "flow1step3"};
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("flow_transition_within_flow", null);
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());

			String[] jobTransitionList = jobExec.getExitStatus().split(",");
			assertWithMessage("transitioned to exact number of steps", transitionList.length, jobTransitionList.length);
			for (int i = 0; i < jobTransitionList.length; i++) {
				assertWithMessage("Flow transitions", transitionList[i], jobTransitionList[i].trim());
			}

			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("Job completed");
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
