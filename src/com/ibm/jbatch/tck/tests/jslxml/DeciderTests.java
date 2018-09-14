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
import java.util.logging.Logger;

import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;






import com.ibm.jbatch.tck.artifacts.common.StatusConstants;
import com.ibm.jbatch.tck.artifacts.specialized.DeciderTestsBatchlet;
import com.ibm.jbatch.tck.utils.JobOperatorBridge;

public class DeciderTests extends ServiceEETest implements StatusConstants {
	private final static Logger logger = Logger.getLogger(DeciderTests.class.getName());
	private static JobOperatorBridge jobOp = null;

	private final static String FORCE_STOP_EXITSTATUS = "STEP_COMPLETE_BUT_FORCE_JOB_STOPPED_STATUS";
	private final static String FORCE_FAIL_EXITSTATUS = "STEP_COMPLETE_BUT_FORCE_JOB_FAILED_STATUS";


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
	 * @testName: testDeciderEndNormal
	 * @assertion: Tests that decision child elements: next, step, end, fail, all work as expected (by verifying batch status)
	 *             Tests exit status globbing ('*' and '?') 
	 *             Tests StepExecution from previous step passed to Decider
	 *             Tests that decider can be configured via property
	 *             Tests that JobContext can be utilized in decider (the same instance as that of the earlier steps).
	 *             Tests setting of <stop>, <end>, <fail> @exit-status attribute value.
	 *             
	 * @test_Strategy: The test methods in this class have a tighter coupling than usual.  That's because they all use the
	 *                 same artifacts, the same basic JSL, and the same basic <decision> element (with the same set of glob 
	 *                 patterns). We set up different "branches" in the job execution sequence depending on exit status, 
	 *                 (e.g. stop vs. fail, end with one exit status vs. another), and we take the various branches in the
	 *                 various test methods.
	 *                    
	 *                 1. Batchlet is coded to end with one of two different exit status values depending on @BatchProperty populated by job param set either to:
	 *                       jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.NORMAL_VALUE);
	 *                              OR
	 *                       jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.SPECIAL_VALUE);
	 *                    Batch status is tested as well
	 *                 2. Decider returns value based on configured property, step execution exit status, and job context transient data. 
	 *                 3. JobContext utilized to adjust "core exit status" by prefixing the number of times the step has run.
	 *                 4. Special globbing chars '*' and '?' are used in the @on values to test exit status globbing.
	 *                 5. Test asserts specific exit status as well as batch status.  In addition to testing the overall test flow this 
	 *                    tests the @exit-status attributes of fail, stop, end.
	 *                    
	 */
	
	
	public void testDeciderEndNormal() throws Fault {

		String METHOD = "testDeciderEndNormal";
TestUtil.logTrace(METHOD);

		try {
			// 1. Here "EndSpecial" is the exit status the decider will return if the step exit status
			// is the "special" exit status value.  It is set as a property on the decider.
			TestUtil.logMsg("Build job parameters for EndSpecial exit status");

			Properties jobParameters = new Properties();

			// 2. Here "EndNormal" is the exit status the decider will return if the step exit status
			// is the "normal" exit status value.  It is set as a property on the batchlet and passed
			// along to the decider via stepContext.setTransientUserData().
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value EndNormal");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "EndNormal");
			// 3. This "ACTUAL_VALUE" is a property set on the batchlet.  It will either indicate to end
			// the step with a "normal" or "special" exit status.            
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.NORMAL_VALUE + "");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.NORMAL_VALUE);

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value EndSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "EndSpecial");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_incompleterun", jobParameters); 

			TestUtil.logMsg("Expected JobExecution getExitStatus()=EndNormal");
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getStatus()=COMPLETED");
			TestUtil.logMsg("Actual JobExecution getStatus()="+jobExec.getBatchStatus());
			assertObjEquals("EndNormal", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testDeciderEndSpecial
	 * @assertion: see testDeciderEndNormal
	 * @test_Strategy: see testDeciderEndNormal
	 */
	
	
	public void testDeciderEndSpecial() throws Fault {
		String METHOD = "testDeciderEndSpecial";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters for EndSpecial exit status");	    

			Properties jobParameters = new Properties();        
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "EndNormal");
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value EndNormal");
			// 1. This is the only test parameter that differs from testDeciderEndNormal().
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.SPECIAL_VALUE );
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.SPECIAL_VALUE);
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value EndSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "EndSpecial");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_incompleterun", jobParameters); 

			// 2. And the job exit status differs accordingly.
			TestUtil.logMsg("Expected JobExecution getExitStatus()=EndSpecial");
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getBatchStatus()=COMPLETED");
			TestUtil.logMsg("Actual JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("EndSpecial", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	// See the first two test methods for an explanation of parameter values.
	/*
	 * @testName: testDeciderStopNormal
	 * @assertion: see testDeciderEndNormal
	 * @test_Strategy: see testDeciderEndNormal
	 */
	
	
	public void testDeciderStopNormal() throws Fault {
		String METHOD = " testDeciderStopNormal";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters for StopSpecial exit status");

			Properties jobParameters = new Properties();    
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value StopNormal");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "StopNormal");

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.NORMAL_VALUE );
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.NORMAL_VALUE);

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value StopSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "StopSpecial");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_incompleterun", jobParameters); 

			TestUtil.logMsg("Expected JobExecution getExitStatus()=StopNormal");
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getBatchStatus()=STOPPED");
			TestUtil.logMsg("Actual JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("StopNormal", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}

	}

	// See the first two test methods for an explanation of parameter values.
	/*
	 * @testName: testDeciderStopSpecial
	 * @assertion: see testDeciderEndNormal
	 * @test_Strategy: see testDeciderEndNormal
	 */
	
	
	public void testDeciderStopSpecial() throws Fault {
		String METHOD = "testDeciderStopSpecial";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters for StopSpecial exit status");

			Properties jobParameters = new Properties();     
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value StopNormal");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "StopNormal");

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.SPECIAL_VALUE );
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.SPECIAL_VALUE);

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value StopSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "StopSpecial");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_incompleterun", jobParameters); 

			TestUtil.logMsg("Expected JobExecution getExitStatus()=StopSpecial");
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getBatchStatus()=STOPPED");
			TestUtil.logMsg("Actual JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("StopSpecial", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	// See the first two test methods for an explanation of parameter values.
	/*
	 * @testName: testDeciderFailNormal
	 * @assertion: see testDeciderEndNormal
	 * @test_Strategy: see testDeciderEndNormal
	 */
	
	
	public void testDeciderFailNormal() throws Fault {

		String METHOD = "testDeciderFailNormal";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters for FailSpecial exit status");	    	

			Properties jobParameters = new Properties();        
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value FailNormal");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "FailNormal");

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.NORMAL_VALUE );
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.NORMAL_VALUE);

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value FailSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "FailSpecial");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_incompleterun", jobParameters); 

			TestUtil.logMsg("Expected JobExecution getExitStatus()=FailNormal");
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getBatchStatus()=FAILED");
			TestUtil.logMsg("Actual JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("FailNormal", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.FAILED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	// See the first two test methods for an explanation of parameter values.
	/*
	 * @testName: testDeciderFailSpecial
	 * @assertion: see testDeciderEndNormal
	 * @test_Strategy: see testDeciderEndNormal
	 */
	
	
	public void testDeciderFailSpecial() throws Fault {
		String METHOD = "testDeciderFailSpecial";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters for FailSpecial exit status");

			Properties jobParameters = new Properties();        
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value FailNormal");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "FailNormal");

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.SPECIAL_VALUE );
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.SPECIAL_VALUE);

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value FailSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "FailSpecial");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_incompleterun", jobParameters); 

			TestUtil.logMsg("Expected JobExecution getExitStatus()=FailSpecial");
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getBatchStatus()=FAILED");
			TestUtil.logMsg("Actual JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("FailSpecial", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.FAILED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderNextNormal
	 * @assertion: see testDeciderEndNormal
	 * @test_Strategy: see testDeciderEndNormal
	 */
	
	
	public void testDeciderNextNormal() throws Fault {
		String METHOD = "testDeciderNextNormal";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters for NextSpecial exit status");

			Properties jobParameters = new Properties();        
			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value NextNormal");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "NextNormal");

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.NORMAL_VALUE );
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.NORMAL_VALUE);

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value NextSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "NextSpecial");

			TestUtil.logMsg("Create single job listener deciderTestJobListener and get JSL");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_completerun", jobParameters); 

			TestUtil.logMsg("Expected JobExecution getExitStatus()="+GOOD_JOB_EXIT_STATUS);
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getBatchStatus()=COMPLETED");
			TestUtil.logMsg("Actual JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(GOOD_JOB_EXIT_STATUS, jobExec.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderNextSpecial
	 * @assertion: see testDeciderEndNormal
	 * @test_Strategy: see testDeciderEndNormal
	 */
	
	
	public void testDeciderNextSpecial() throws Fault {
		String METHOD = "testDeciderNextSpecial";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters for NextSpecial exit status");	

			Properties jobParameters = new Properties();        

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTION + " with value NextNormal");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTION, "NextNormal");

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.ACTUAL_VALUE + " with value " + DeciderTestsBatchlet.SPECIAL_VALUE + "");
			jobParameters.setProperty(DeciderTestsBatchlet.ACTUAL_VALUE, DeciderTestsBatchlet.SPECIAL_VALUE);

			TestUtil.logMsg("Set job parameters property " + DeciderTestsBatchlet.SPECIAL_EXIT_STATUS + " with value NextSpecial");
			jobParameters.setProperty(DeciderTestsBatchlet.SPECIAL_EXIT_STATUS, "NextSpecial");

			TestUtil.logMsg("Create single job listener deciderTestJobListener and get JSL");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_decider_completerun", jobParameters); 

			// This actually exits with the exact same status as the "...NextNormal" test.
			TestUtil.logMsg("Expected JobExecution getExitStatus()="+GOOD_JOB_EXIT_STATUS);
			TestUtil.logMsg("Actual JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("Expected JobExecution getBatchStatus()=COMPLETED");
			TestUtil.logMsg("Actual JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(GOOD_JOB_EXIT_STATUS, jobExec.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderExitStatusIsSetOnJobContext
	 * @assertion:  Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 * @test_Strategy:  The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *   be equal to the return value of the last decision (i.e. Decider#decide). Note the test doesn't necessarily 
	 *   confirm that the exit status is set on the JobContext directly, but this is the intent behind the test method name.
	 */
	
	
	public void testDeciderExitStatusIsSetOnJobContext() throws Fault {

		String METHOD = "testDeciderExitStatusIsSetOnJobContext";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "true");

			jobParameters.setProperty("stop.job.after.this.step", "None");
			jobParameters.setProperty("fail.job.after.this.step", "step1");


			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.FAILED, jobExec.getBatchStatus());
			assertObjEquals("1:"+FORCE_FAIL_EXITSTATUS, jobExec.getExitStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderCannotbeFirstElementOnStart
	 * @assertion: Tests that the first execution element of a job cannot be a decision 
	 *  (it's a misnomer to say 'decider' but we'll leave the test name alone).
	 * @test_Strategy: Since an implementation may either throw a JobStartException or 
	 *  begin an execution which fails, we will pass the test if we see either JobStartException
	 *  or BatchStatus of FAILED.
	 */
	
	
	public void testDeciderCannotbeFirstElementOnStart() throws Fault {

		String METHOD = "testDeciderCannotbeFirstElementOnStart";
TestUtil.logTrace(METHOD);

		try {
			boolean seenException = false;
			JobExecution jobExec = null;
			try {
				jobExec = jobOp.startJobAndWaitForResult("decider_as_first_job_element_fails"); 
			} catch (JobStartException e) {
				TestUtil.logMsg("Caught JobStartException:  " + e.getLocalizedMessage());
				seenException = true;
			}

			// If we caught an exception we'd expect that a JobExecution would not have been created,
			// though we won't validate that it wasn't created.  

			// If we didn't catch an exception that we require that the implementation fail the job execution.
			if (!seenException) {
				TestUtil.logMsg("Didn't catch JobStartException, Job Batch Status = " + jobExec.getBatchStatus());
				assertWithMessage("Job should have failed because of decision as first execution element.", BatchStatus.FAILED, jobExec.getBatchStatus());
			}
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderTransitionFromStepAndAllowRestart
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed the 
	 *                 StepExecution corresponding to the new, restarted execution (not the original execution). 
	 *                 (See Sec. 10.8 Restart Processing)
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Steps are configured with allow-start-if-complete = true.  Job parameter is used to vary 
	 *                     exit status on original vs. restart execution and, on restart, the
	 *                     StepExecution exit status is asserted to be the one on the restarted execution. 
	 *                     JobContext transient user data is used to assert the correct number of decider invocations have
	 *                     been performed.
	 */
	
	
	public void testDeciderTransitionFromStepAndAllowRestart() throws Fault {
		String METHOD = "testDeciderTransitionFromStepAndAllowRestart";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "true");

			jobParameters.setProperty("stop.job.after.this.step", "step1");
			jobParameters.setProperty("fail.job.after.this.step", "None");


			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("1:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("3:flow1step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}


	}

	/*
	 * @testName: testDeciderTransitionFromStepWithinFlowAndAllowRestart
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed the 
	 *                 StepExecution corresponding to the new, restarted execution (not the original execution). 
	 *                 (See Sec. 10.8 Restart Processing)
	 *              3. Tests that a decision within a flow can terminate the top-level job through appropriate transition elements
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Steps are configured with allow-start-if-complete = true.  Job parameter is used to vary 
	 *                     exit status on original vs. restart execution and, on restart, the
	 *                     StepExecution exit status is asserted to be the one on the restarted execution. 
	 *                     JobContext transient user data is used to assert the correct number of decider invocations have
	 *                     been performed.
	 *                  3. A decision within a flow is configured to stop (based on exit status matching against a <stop> element).
	 */
	
	
	public void testDeciderTransitionFromStepWithinFlowAndAllowRestart() throws Fault {

		String METHOD = "testDeciderTransitionFromStepWithinFlowAndAllowRestart";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "true");

			jobParameters.setProperty("stop.job.after.this.step", "flow1step1");
			jobParameters.setProperty("fail.job.after.this.step", "None");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("2:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("3:flow1step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderTransitionFromFlowAndAllowRestart
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed the 
	 *                 StepExecution corresponding to the new, restarted execution (not the original execution). 
	 *                 (See Sec. 10.8 Restart Processing)
	 *              3. Tests that when a flow is followed by a decision, that the decision element's Decider#decide 
	 *                 will be passed the StepExecution of the last step in the preceding flow.
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Steps are configured with allow-start-if-complete = true.  Job parameter is used to vary 
	 *                     exit status on original vs. restart execution and, on restart, the
	 *                     StepExecution exit status is asserted to be the one on the restarted execution. 
	 *                     JobContext transient user data is used to assert the correct number of decider invocations have
	 *                     been performed.
	 */
	
	
	public void testDeciderTransitionFromFlowAndAllowRestart() throws Fault {

		String METHOD = "testDeciderTransitionFromFlowAndAllowRestart";
TestUtil.logTrace(METHOD);


		try {
			TestUtil.logMsg("Build job parameters.");    
			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "true");

			jobParameters.setProperty("stop.job.after.this.step", "flow1step2");
			jobParameters.setProperty("fail.job.after.this.step", "None");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("3:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("3:flow1step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}



	/*
	 * @testName: testDeciderTransitionFromSplitAndAllowRestart
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed StepExecution(s) 
	 *                 corresponding to the new, restarted execution (not the original execution). 
	 *                 (See Sec. 10.8 Restart Processing)
	 *              3. Tests that when a split is followed by a decision, that the decision element's Decider#decide 
	 *                 will be passed the StepExecution(s) of each of the last steps of the member flows in the preceding split.
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Steps are configured with allow-start-if-complete = true.  Job parameter is used to vary 
	 *                     exit status on original vs. restart execution and, on restart, the
	 *                     StepExecution exit status is asserted to be the one on the restarted execution. 
	 *                     JobContext transient user data is used to assert the correct number of decider invocations have
	 *                     been performed.
	 *              	3. NOTE: TODO for future - Perhaps the strategy in asserting that EACH StepExecution is passed should be
	 *                     tightened.  We could go further to assert that the full list of StepExecution(s) is what we'd expect.
	 */
	
	
	public void testDeciderTransitionFromSplitAndAllowRestart() throws Fault {

		String METHOD = "testDeciderTransitionFromSplitAndAllowRestart";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "true");

			jobParameters.setProperty("stop.job.after.this.step", "split1flow1step2");
			jobParameters.setProperty("stop.job.after.this.step2", "split1flow2step2");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_from_split_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("4:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");
			jobParameters.setProperty("stop.job.after.this.step2", "None");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("4:split1flow2step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderTransitionFromStepAndAllowRestartFalse
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed the 
	 *                 StepExecution corresponding to the original execution (since the step doesn't re-execute on the
	 *                 new, restart execution),  with the original StepExecution's exit status.
	 *                 (See Sec. 10.8 Restart Processing)
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Allow-start-if-complete for the steps is set to "false".   (This is where the "False" in the
	 *                  test method name comes from).   Decision is configured to expect the StepExecution exitStatus
	 *                  from the original execution, and the test will fail if not.  I.e. the exit status from the earlier 
	 *                  execution is confirmed to have been persisted. Job parameter is used to allow Decider#decide
	 *                  to return a different result on restart.
	 *                  JobContext transient user data is used to assert the correct number of decider invocations have
	 *                  been performed.
	 */
	
	
	public void testDeciderTransitionFromStepAndAllowRestartFalse() throws Fault {

		String METHOD = "testDeciderTransitionFromStepAndAllowRestartFalse";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "false");

			jobParameters.setProperty("stop.job.after.this.step", "step1");
			jobParameters.setProperty("fail.job.after.this.step", "None");


			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("1:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");
			restartJobParameters.setProperty("is.restart", "true");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("3:flow1step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderTransitionFromStepWithinFlowAndAllowRestartFalse
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed the 
	 *                 StepExecution corresponding to the original execution (since the step doesn't re-execute on the
	 *                 new, restart execution),  with the original StepExecution's exit status.
	 *                 (See Sec. 10.8 Restart Processing)
	 *              3. Tests that a decision within a flow can terminate the top-level job through appropriate transition elements
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Allow-start-if-complete for the steps is set to "false".   (This is where the "False" in the
	 *                     test method name comes from).   Decision is configured to expect the StepExecution exitStatus
	 *                     from the original execution, and the test will fail if not.  I.e. the exit status from the earlier 
	 *                     execution is confirmed to have been persisted. Job parameter is used to allow Decider#decide
	 *                     to return a different result on restart.      
	 *                     JobContext transient user data is used to assert the correct number of decider invocations have
	 *                     been performed.
	 *                  3. A decision within a flow is configured to stop (based on exit status matching against a <stop> element).
	 */
	
	
	public void testDeciderTransitionFromStepWithinFlowAndAllowRestartFalse() throws Fault {

		String METHOD = "testDeciderTransitionFromStepWithinFlowAndAllowRestartFalse";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "false");

			jobParameters.setProperty("stop.job.after.this.step", "flow1step1");
			jobParameters.setProperty("fail.job.after.this.step", "None");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("2:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");
			restartJobParameters.setProperty("is.restart", "true");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("3:flow1step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testDeciderTransitionFromFlowAndAllowRestartFalse
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed the 
	 *                 StepExecution corresponding to the original execution (since the step doesn't re-execute on the
	 *                 new, restart execution),  with the original StepExecution's exit status.
	 *                 (See Sec. 10.8 Restart Processing)
	 *              3. Tests that when a flow is followed by a decision, that the decision element's Decider#decide 
	 *                 will be passed the StepExecution of the last step in the preceding flow.
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Allow-start-if-complete for the steps is set to "false".   (This is where the "False" in the
	 *                     test method name comes from).   Decision is configured to expect the StepExecution exitStatus
	 *                     from the original execution, and the test will fail if not.  I.e. the exit status from the earlier 
	 *                     execution is confirmed to have been persisted. Job parameter is used to allow Decider#decide
	 *                     to return a different result on restart.
	 *                     JobContext transient user data is used to assert the correct number of decider invocations have
	 *                     been performed.
	 */
	
	
	public void testDeciderTransitionFromFlowAndAllowRestartFalse() throws Fault {

		String METHOD = "testDeciderTransitionFromFlowAndAllowRestartFalse";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "false");

			jobParameters.setProperty("stop.job.after.this.step", "flow1step2");
			jobParameters.setProperty("fail.job.after.this.step", "None");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("3:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");
			restartJobParameters.setProperty("is.restart", "true");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("3:flow1step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}


	/*
	 * @testName: testDeciderTransitionFromSplitAndAllowRestartFalse
	 * @assertion:  1. Tests that the exit return value of Decider#decide is set as the value of the job's exit status.
	 *              2. Tests that the decider re-executes on a restart and the decide() method is passed the 
	 *                 StepExecution corresponding to the original execution (since the step doesn't re-execute on the
	 *                 new, restart execution),  with the original StepExecution's exit status.
	 *                 (See Sec. 10.8 Restart Processing)
	 *              3. Tests that when a split is followed by a decision, that the decision element's Decider#decide 
	 *                 will be passed the StepExecution(s) of each of the last steps of the member flows in the preceding split.
	 * @test_Strategy:  1. The exit status is not set via JobContext#setExitStatus or other means, but is asserted to 
	 *                     be equal to the return value of the last decision (i.e. Decider#decide).
	 *                  2. Allow-start-if-complete for the steps is set to "false".   (This is where the "False" in the
	 *                     test method name comes from).   Decision is configured to expect the StepExecution exitStatus
	 *                     from the original execution, and the test will fail if not.  I.e. the exit status from the earlier 
	 *                     execution is confirmed to have been persisted. Job parameter is used to allow Decider#decide
	 *                     to return a different result on restart.
	 *                     JobContext transient user data is used to assert the correct number of decider invocations have
	 *                     been performed.
	 *              	3. NOTE: TODO for future - Perhaps the strategy in asserting that EACH StepExecution is passed should be
	 *                     tightened.  We could go further to assert that the full list of StepExecution(s) is what we'd expect.
	 */
	
	
	public void testDeciderTransitionFromSplitAndAllowRestartFalse() throws Fault {

		String METHOD = "testDeciderTransitionFromSplitAndAllowRestartFalse";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Build job parameters.");    

			Properties jobParameters = new Properties();
			jobParameters.setProperty("step.complete.but.force.job.stopped.status", FORCE_STOP_EXITSTATUS);
			jobParameters.setProperty("step.complete.but.force.job.failed.status", FORCE_FAIL_EXITSTATUS);

			jobParameters.setProperty("allow.start.if.complete", "false");

			jobParameters.setProperty("stop.job.after.this.step", "split1flow1step2");
			jobParameters.setProperty("stop.job.after.this.step2", "split1flow2step2");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("decider_transitions_from_split_on_restart", jobParameters); 

			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
			assertObjEquals("4:"+FORCE_STOP_EXITSTATUS, jobExec.getExitStatus());

			Properties restartJobParameters = new Properties(jobParameters);
			restartJobParameters.setProperty("stop.job.after.this.step", "None");
			restartJobParameters.setProperty("is.restart", "true");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution jobExec2 = jobOp.restartJobAndWaitForResult(jobExec.getExecutionId(), restartJobParameters);

			assertObjEquals("4:split1flow2step2_CONTINUE", jobExec2.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec2.getBatchStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}



	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

}
