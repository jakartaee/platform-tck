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

public class SplitTransitioningTests extends ServiceEETest {

	private JobOperatorBridge jobOp = null;

	/**
	 * @testName: testSplitTransitionToStep
	 * @assertion: Section 5.4 Split
	 * @test_Strategy: 1. setup a job consisting of one split (w/ 2 flows) and one step
	 * 				   2. start job 
	 * 				   3. add step id from step context to job context exit status
	 * 				   4. verify that the split indeed transitioned to the step
	 * 
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testSplitTransitionToStep() throws Fault {

		String METHOD = "testSplitTransitionToStep";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("split_transition_to_step", null);
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());

			assertWithMessage("Split transitioned to step", "step1", jobExec.getExitStatus());

			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("job completed");
		} catch (Exception e) {
			handleException(METHOD, e); 
		}
	}

	/**
	 * @testName: testSplitTransitionToStepOutOfScope
	 * @assertion: Section 5.4 Split
	 * @test_Strategy: 1. setup a job consisting of one split (w/ 2 flows) and one step
	 * 				   2. start job 
	 * 				   3. this job should fail because the split flow 'flow1' next to outside the split
	 * 
	 *	<split id="split1">
	 *	   <flow id="flow1" next="step1">
	 *			<step id="flow1step1" next="flow1step2">
	 *				<batchlet ref="splitTransitionToDecisionTestBatchlet"/>
	 *			</step>
	 *			<step id="flow1step2">
	 *				<batchlet ref="splitTransitionToDecisionTestBatchlet"/>
	 *			</step>
	 *		</flow>
	 *		<flow id="flow2">
	 *			<step id="flow1step3" next="flow1step4">
	 *				<batchlet ref="splitTransitionToDecisionTestBatchlet"/>
	 *			</step>
	 *			<step id="flow1step4">
	 *				<batchlet ref="splitTransitionToDecisionTestBatchlet"/>
	 *			</step>
	 *		</flow>
	 *	</split>
	 *
	 *	<step id="step1">
	 *		<batchlet ref="splitTransitionToStepTestBatchlet"/>
	 *	</step>
	 *
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	
	public void testSplitTransitionToStepOutOfScope() throws Fault {

		String METHOD = "testSplitTransitionToStepOutOfScope";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("starting job");
			
			boolean seenException = false;
			JobExecution jobExec = null;
			try {
				jobExec = jobOp.startJobAndWaitForResult("split_transition_to_step_out_of_scope", null);
			} catch (JobStartException e) {
				TestUtil.logMsg("Caught JobStartException:  " + e.getLocalizedMessage());
				seenException = true;
			}
			
			// If we caught an exception we'd expect that a JobExecution would not have been created,
			// though we won't validate that it wasn't created.  
			
			// If we didn't catch an exception that we require that the implementation fail the job execution.
			if (!seenException) {
				TestUtil.logMsg("Didn't catch JobstartException, Job Batch Status = " + jobExec.getBatchStatus());
				assertWithMessage("Job should have failed because of out of scope execution elements.", BatchStatus.FAILED, jobExec.getBatchStatus());
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/**
	 * @testName: testSplitTransitionToDecision
	 * @assertion: Section 5.4 Split
	 * @test_Strategy: 1. setup a job consisting of one split (w/ 2 flows) and one decision
	 * 				   2. start job 
	 * 				   3. split will transition to decider which will change the exit status
	 * 				   4. compare that the exit status set by the decider matches that of the job
	 * 
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 
	public void testSplitTransitionToDecision() throws Fault {

		String METHOD = "testSplitTransitionToDecision";
TestUtil.logTrace(METHOD);

		try {
			String exitStatus = "ThatsAllFolks";
			// based on our decider exit status
			/*
			<decision id="decider1" ref="flowTransitionToDecisionTestDecider">
				<end exit-status="ThatsAllFolks" on="DECIDER_EXIT_STATUS*2" />
			</decision>
			 */
			TestUtil.logMsg("starting job");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("split_transition_to_decision", null);
			TestUtil.logMsg("Job Status = " + jobExec.getBatchStatus());

			assertWithMessage("Job Exit Status is from decider", exitStatus, jobExec.getExitStatus());
			assertWithMessage("Job completed", BatchStatus.COMPLETED, jobExec.getBatchStatus());
			TestUtil.logMsg("job completed");
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

	/* cleanup */
	public void  cleanup()
	{		

	}

	public void setup(String[] args, Properties props) throws Fault {

		String METHOD = "setup";
TestUtil.logTrace(METHOD);

		try {
			jobOp = new JobOperatorBridge();
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	} 

	
		public void beforeTest() throws ClassNotFoundException {
		jobOp = new JobOperatorBridge(); 
	}

	
	public void afterTest() {
		jobOp = null;
	}
}
