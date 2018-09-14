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

import java.util.Properties;

import javax.batch.runtime.BatchStatus;






import com.ibm.jbatch.tck.utils.JobOperatorBridge;
import com.ibm.jbatch.tck.utils.TCKJobExecutionWrapper;

public class BatchletRestartStateMachineTests extends ServiceEETest {

	private static JobOperatorBridge jobOp = null;

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

	/* cleanup */
	public void  cleanup()
	{		

	}

	/*
	 * @testName: testTransitionElementOnAttrValuesWithRestartJobParamOverrides
	 * @assertion: 1) Tests Exit Status globbing against transition elements @on value 
	 *             2) Shows that the @on values are overrideable on restart, and the persisted exit status in matched against the @on values coming
	 *             from the restart job parameters.  
	 *             3) Also by using the same exit-status in execution 2 for step 2 for <fail> and <next> we verify that the globbing is done in 
	 *             sequential order (resulting in batch status = FAILED).
	 *             4) Also tests that for allow-start if complete we don't rerun just because we failed or stopped the job AFTER 
	 *             completing a step using the <stop>, <fail> transition elements.  
	 *             In this test, step1 is complete after execution 1 and does not reexecute on executions 2 or 3, in spite
	 *             of the fact that the job was stopped by step1's <stop> element on execution 1. 
	 *             Likewise step2 is complete after execution 2 and does not reexecute on execution 3 in spite of the
	 *             fact that execution 2 is failed by step2's <fail> element on execution 2.  
	 *             
	 * @test_Strategy:  Reproduce a simplified form of the JSL here:
	 * 
	 *----------------------------------------------------------------------------	
	 * <job id="overrideOnAttributeValuesUponRestartBatchlet"
	 *	<step id="step1">
	 *	    <!-- same batchlet in all three steps -->
	 *		<batchlet ref="overrideOnAttributeValuesUponRestartBatchlet">
	 *			<stop on="#{jobParameters['step1.stop']}"/>             [ ES.STEP1, ES.STOP, ES.STOP ]
	 *			<next on="#{jobParameters['step1.next']}" to="step2"/>  [ ES.XXX, ES.STEP1, ES.STEP1 ]		
	 *			<fail on="*" exit-status="FAILURE"/>  
	 *	<step id="step2">
	 *		<batchlet ref="overrideOnAttributeValuesUponRestartBatchlet">		
	 *			<fail on="#{jobParameters['step2.fail']}" exit-status="EXPECTED_FAILURE"/>  [ES.STEP2, ES.STEP2, ES.FAIL]
	 *			<next on="#{jobParameters['step2.next']}" to="step3"/>                      [ES.XXX, ES.STEP2, ES.STEP2]		
	 *			<fail on="*" exit-status="FAILURE"/>                <!-- Distinguish between first /fail element via @exit-status. --> 
	 *	<step id="step3">
	 *		<batchlet ref="overrideOnAttributeValuesUponRestartBatchlet">
	 *		<end on="ES.STEP3"/>
	 *		<fail on="*" exit-status="FAILURE"/>
	 *
	 *	----------------------------------------------------------------------------
	 * On first execution, stop after step1.
	 * 
	 * On second execution, step1 is already complete with allow-start-if-complete=false (by default), so the first execution is step2.  
	 * This executes, and then the step2 exit status matches the <fail on="ES.STEP2"> value, resulting in job execution FAILED and
	 * a JSL-specified (via the <fail> element's exit-status="EXPECTED_FAILURE" job exit status.
	 * 
	 * On the third execution, we this time do not execuute step1, or step 2, so we transition past each
	 * and the first step we execute is step 3.
	 *	----------------------------------------------------------------------------
	 *	
	 * Note that because we reuse the same batchlet across the three steps we perform some validation in 
	 * the batchlet, using the stepName and the execution number (as restart parameter), so that we validate we're never called
	 * in the wrong step in the wrong execution (e.g. in execution.number N we shouldn't be in step S).
	 */
	
	
	public void testTransitionElementOnAttrValuesWithRestartJobParamOverrides() throws Fault {

		String METHOD = "testTransitionElementOnAttrValuesWithRestartJobParamOverrides";
TestUtil.logTrace(METHOD);

		String EXECUTION2_EXPECTED_EXIT_STATUS_FROM_JSL_ATTRIBUTE = "EXPECTED_FAILURE";
		try {

			TCKJobExecutionWrapper execution1 = null;
			TCKJobExecutionWrapper execution2 = null;
			TCKJobExecutionWrapper execution3 = null;

			// Create new block to facilitate copy-pasting without inadvertent errors
			{
				TestUtil.logMsg("Create job parameters for execution #1:");
				Properties jobParams = new Properties();
				TestUtil.logMsg("execution.number=1");
				jobParams.setProperty("execution.number", "1");
				jobParams.setProperty("step1.stop", "ES.STEP1");
				jobParams.setProperty("step1.next", "ES.XXX");
				jobParams.setProperty("step2.fail", "ES.STEP2");
				jobParams.setProperty("step2.next", "ES.XXX");

				TestUtil.logMsg("Invoke startJobAndWaitForResult");
				execution1 = jobOp.startJobAndWaitForResult("overrideOnAttributeValuesUponRestartBatchlet", jobParams);

				TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
				TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
				assertWithMessage("Testing execution #1", BatchStatus.STOPPED, execution1.getBatchStatus());
				assertWithMessage("Testing execution #1", "STOPPED", execution1.getExitStatus());
			}

			{
				TestUtil.logMsg("Create job parameters for execution #2:");
				Properties restartJobParameters = new Properties();
				TestUtil.logMsg("execution.number=2");
				TestUtil.logMsg("step1.stop=ES.STOP");
				TestUtil.logMsg("step1.next=ES.STEP1");
				restartJobParameters.setProperty("execution.number", "2");
				restartJobParameters.setProperty("step1.stop", "ES.STOP");
				restartJobParameters.setProperty("step1.next", "ES.STEP1");
				restartJobParameters.setProperty("step2.fail", "ES.STEP2");
				restartJobParameters.setProperty("step2.next", "ES.STEP2");
				TestUtil.logMsg("Invoke restartJobAndWaitForResult with executionId: " + execution1.getExecutionId() + "");
				execution2 = jobOp.restartJobAndWaitForResult(execution1.getExecutionId(),restartJobParameters);				
				TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+execution2.getBatchStatus());
				TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+execution2.getExitStatus());
				assertWithMessage("Testing execution #2", BatchStatus.FAILED, execution2.getBatchStatus());
				// See JSL snippet above for where "SUCCESS" comes from
				assertWithMessage("Testing execution #2", EXECUTION2_EXPECTED_EXIT_STATUS_FROM_JSL_ATTRIBUTE, execution2.getExitStatus());				
			}

			{
				TestUtil.logMsg("Create job parameters for execution #3:");
				Properties restartJobParameters = new Properties();
				TestUtil.logMsg("execution.number=3");
				TestUtil.logMsg("step1.stop=ES.STOP");
				TestUtil.logMsg("step1.next=ES.STEP1");
				TestUtil.logMsg("step2.fail=ES.FAIL");
				TestUtil.logMsg("step2.next=ES.STEP2");
				restartJobParameters.setProperty("execution.number", "3");
				restartJobParameters.setProperty("step1.stop", "ES.STOP");
				restartJobParameters.setProperty("step1.next", "ES.STEP1");
				restartJobParameters.setProperty("step2.fail", "ES.FAIL");
				restartJobParameters.setProperty("step2.next", "ES.STEP2");
				TestUtil.logMsg("Invoke restartJobAndWaitForResult with executionId: " + execution2.getExecutionId() + "");
				execution3 = jobOp.restartJobAndWaitForResult(execution2.getExecutionId(),restartJobParameters);
				TestUtil.logMsg("execution #3 JobExecution getBatchStatus()="+execution3.getBatchStatus());
				TestUtil.logMsg("execution #3 JobExecution getExitStatus()="+execution3.getExitStatus());				
				assertWithMessage("Testing execution #3", BatchStatus.COMPLETED, execution3.getBatchStatus());
				assertWithMessage("Testing execution #3", "COMPLETED", execution3.getExitStatus());  
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * Obviously would be nicer to have more granular tests for some of this function,
	 * but here we're going a different route and saying, if it's going to require
	 * restart it will have some complexity, so let's test a few different functions
	 * in one longer restart scenario.
	 */
	/*
	 * @testName: testAllowStartIfCompleteRestartExecution
	 * @assertion:    1. @restart attribute on <stop> transition element
	 *           :    2. tests difference between allow-start-if-complete = true or false (default)
	 * @test_Strategy: 
	 * 
	 * For N in (1,2,3,4,5), we expect executions 1-5 to each end in batch status of STOPPED, 
	 * with exit status EXECUTION.N
	 * 
	 * This simplified JSL (end tags not shown) makes this clear.
	 * 
	 * <job id="batchletRestartStateMachine"
	 *	<step id="step1" allow-start-if-complete="true">
	 *	   <batchlet ref="batchletRestartStateMachineImpl">
	 *		<!-- Remember these aren't regular expressions, so '.' is just a regular character. -->
	 *		  <stop on="STOP.1" exit-status="EXECUTION.1"/> 		
	 *		  <stop on="STOP.2" exit-status="EXECUTION.2" restart="step2" />
	 *		  <stop on="STOP.?" restart="step3" exit-status="EXECUTION.5"/>
	 *        <fail on="ILLEGAL.STATE"/>
	 *		  <next on="*" to="step2"/>
	 *	<step id="step2">
	 *		<batchlet ref="batchletRestartStateMachineImpl">
	 *		  <next on="GO" to="step3"/>
	 *        <fail on="ILLEGAL.STATE"/>
	 *		  <next on="*" to="step4"/> <!-- Shouldn't happen, here to test earlier exit status is persisted -->
	 *	<step id="step3" allow-start-if-complete="true">
	 *		<batchlet ref="batchletRestartStateMachineImpl">
	 *		  <stop on="STOP.3" exit-status="EXECUTION.3"/>
	 *		  <stop on="STOP.4" exit-status="EXECUTION.4"/>
	 *        <fail on="ILLEGAL.STATE"/>
	 *		  <next on="*" to="step4"/>		
	 *	<step id="step4" >
	 *		<batchlet ref="batchletRestartStateMachineImpl">
	 *        <fail on="ILLEGAL.STATE"/>
	 *		  <end on="*" exit-status="EXECUTION.6"/>
	 *</job>
	 *
	 * Note that because we reuse the same batchlet across the three steps we perform some validation in 
	 * the batchlet, using the stepName and the execution number (as restart parameter), so that we validate we're never called
	 * in the wrong step in the wrong execution (e.g. in execution.number N we shouldn't be in step S).
	 */
	
	
	public void testAllowStartIfCompleteRestartExecution() throws Fault {

		String METHOD = "testAllowStartIfCompleteRestartExecution";
TestUtil.logTrace(METHOD);

		try {
			long lastExecutionId = 0L;
			TCKJobExecutionWrapper exec = null;

			for (int i = 1; i <= 6; i++) {
				String execString = new Integer(i).toString();
				Properties jobParameters = new Properties();
				jobParameters.put("execution.number", execString);
				if (i == 1) {
					TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
					exec = jobOp.startJobAndWaitForResult("batchletRestartStateMachine", jobParameters);
				} else {
					TestUtil.logMsg("Invoke restartJobAndWaitForResult");
					exec = jobOp.restartJobAndWaitForResult(lastExecutionId, jobParameters);
				}
				lastExecutionId = exec.getExecutionId();

				TestUtil.logMsg("Execution #" + i + " JobExecution getBatchStatus()="+exec.getBatchStatus());
				TestUtil.logMsg("Execution #" + i + " JobExecution getExitStatus()="+exec.getExitStatus());
				if (i == 6) {
					assertWithMessage("Testing execution #" + i, BatchStatus.COMPLETED, exec.getBatchStatus());
				} else {
					assertWithMessage("Testing execution #" + i, BatchStatus.STOPPED, exec.getBatchStatus());
				}
				assertWithMessage("Testing execution #" + i, "EXECUTION." + execString, exec.getExitStatus());
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}
	
	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

}
