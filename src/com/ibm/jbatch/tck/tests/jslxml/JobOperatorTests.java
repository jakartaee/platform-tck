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

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.operations.JobExecutionAlreadyCompleteException;
import javax.batch.operations.JobExecutionIsRunningException;
import javax.batch.operations.JobRestartException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;







import com.ibm.jbatch.tck.utils.JobOperatorBridge;
import com.ibm.jbatch.tck.utils.TCKJobExecutionWrapper;

public class JobOperatorTests extends ServiceEETest {

	private final static Logger logger = Logger.getLogger(JobOperatorTests.class.getName());

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

	/* cleanup */
	public void  cleanup()
	{		

	}

	
	
	public static void setUp() throws Fault {
		jobOp = new JobOperatorBridge();
	}

	
	public static void tearDown() throws Fault {
	}

	private void begin(String str) {
		TestUtil.logMsg("Begin test method: " + str);
	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

	/*
	 * @testName: testJobOperatorStart
	 * 
	 * @assertion:  Job Operator - start
	 * @test_Strategy: start a job that completes successfully with no exceptions thrown.
	 * @throws JobStartException               
	 */
	
	
	public void testJobOperatorStart() throws Fault {

		String METHOD = "testJobOperatorStart";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_1step.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_1step");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		} 

	}

	/*
	 * @testName: testJobOperatorRestart
	 * 
	 * @assertion:  Job Operator - restart.  Tests also that a restart JobExecution is associated with the
	 *             same JobInstance as the original execution.
	 * @test_Strategy: start a job that is configured to fail. Change configuration of job to ensure success. Restart job.
	 *                 Test that job completes successfully and no exceptions are thrown.
	 * @throws JobExecutionAlreadyCompleteException
	 * @throws NoSuchJobExecutionException
	 * @throws JobExecutionNotMostRecentException 
	 * @throws JobRestartException
	 *                 
	 */
	
	
	public void testJobOperatorRestart() throws Fault {

		String METHOD = "testJobOperatorRestart";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.next.writepoints=10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.next.writepoints", "10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long jobInstanceId = execution1.getInstanceId();
			long lastExecutionId = execution1.getExecutionId();
			TCKJobExecutionWrapper exec = null;
			TestUtil.logMsg("Got Job instance id: " + jobInstanceId + "");
			TestUtil.logMsg("Got Job execution id: " + lastExecutionId + "");
			{
				TestUtil.logMsg("Invoke restartJobAndWaitForResult for execution id: " + lastExecutionId + "");
				exec = jobOp.restartJobAndWaitForResult(lastExecutionId, jobParams);
				TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+exec.getBatchStatus());
				TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+exec.getExitStatus());
				TestUtil.logMsg("execution #2 Job instance id="+exec.getInstanceId());
				assertWithMessage("Testing execution #2", BatchStatus.COMPLETED, exec.getBatchStatus());
				assertWithMessage("Testing execution #2", "COMPLETED", exec.getExitStatus());
				assertWithMessage("Testing execution #2", jobInstanceId, exec.getInstanceId());  
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOperatorRestartAlreadyCompleteException
	 * 
	 * @assertion: testJobOperatorRestartAlreadyCompleteException
	 * @test_Strategy: start a job that is configured to fail. Change configuration of job to ensure success. Restart job
	 *                 and let it run to completion.  Then restart another time and confirm a JobExecutionAlreadyCompleteException
	 *                 is caught.
	 *                 
	 *                 The first start isn't serving a huge purpose, just making the end-to-end test slightly more interesting.
	 *                 
	 *                 Though it might be interesting to restart from the first executionId, this would force the issue
	*                  as to whether the implementation throws JobExecutionAlreadyCompleteException or JobExecutionNotMostRecentException.
	*                  We don't want to specify a behavior not in the spec, and the spec purposely avoids overspecifying exception behavior.
	*                  The net is we only restart from the second id.
	*                   
	 * @throws JobExecutionAlreadyCompleteException
	 * @throws NoSuchJobExecutionException
	 * @throws JobExecutionNotMostRecentException 
	 * @throws JobRestartException
	 *                 
	 */
	
	
	public void testJobOperatorRestartAlreadyCompleteException() throws Fault {

		String METHOD = "testJobOperatorRestartAlreadyCompleteException";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.next.writepoints=10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.next.writepoints", "10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long jobInstanceId = execution1.getInstanceId();
			long firstExecutionId = execution1.getExecutionId();
			TCKJobExecutionWrapper exec = null;
			TestUtil.logMsg("Got Job instance id: " + jobInstanceId + "");
			TestUtil.logMsg("Got Job execution id: " + firstExecutionId + "");
			{
				TestUtil.logMsg("Invoke restartJobAndWaitForResult for execution id: " + firstExecutionId + "");
				exec = jobOp.restartJobAndWaitForResult(firstExecutionId, jobParams);
				TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+exec.getBatchStatus());
				TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+exec.getExitStatus());
				TestUtil.logMsg("execution #2 Job instance id="+exec.getInstanceId());
				assertWithMessage("Testing execution #2", BatchStatus.COMPLETED, exec.getBatchStatus());
				assertWithMessage("Testing execution #2", "COMPLETED", exec.getExitStatus());
				assertWithMessage("Testing execution #2", jobInstanceId, exec.getInstanceId());  
			}

			long secondExecutionId = exec.getExecutionId();
			TestUtil.logMsg("execution #2 Job execution id="+ secondExecutionId);


			// Restart from second execution id (not first, see strategy comment above).
			TestUtil.logMsg("Now invoke restart again, expecting JobExecutionAlreadyCompleteException, for execution id: " + secondExecutionId + "");
			boolean seenException = false;
			try {
				jobOp.restartJobAndWaitForResult(secondExecutionId, jobParams);
			} catch (JobExecutionAlreadyCompleteException e) {
				TestUtil.logMsg("Caught JobExecutionAlreadyCompleteException as expected");
				seenException = true;
			}
			assertWithMessage("Caught JobExecutionAlreadyCompleteException for bad restart #2" , seenException);

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}
	
	/*
	 * @testName: testJobOperatorAbandonJobDuringARestart
	 * 
	 * @assertion: testJobOperatorAbandonJobDuringARestart
	 * @test_Strategy: start a job that is configured to fail. Change configuration of job to cause the job to sleep for 5 seconds. Restart job
	 *                 and let it run without waiting for completion.  Attempt to abandon the job and confirm a JobExecutionIsRunningException
	 *                 is caught.
	 *                 
	 *                  
	 * @throws JobExecutionAlreadyCompleteException
	 * @throws NoSuchJobExecutionException
	 * @throws JobExecutionNotMostRecentException 
	 * @throws JobRestartException
	 *                 
	 */
	
	
	public void testJobOperatorAbandonJobDuringARestart() throws Fault {

		String METHOD = "testJobOperatorRestartAlreadyCompleteException";
TestUtil.logTrace(METHOD);
		begin(METHOD);
		
		String DEFAULT_SLEEP_TIME = "5000";

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			jobParams.put("execution.number", "1");
			TestUtil.logMsg("execution.number=1");
			String sleepTime = System.getProperty("JobOperatorTests.testJobOperatorTestAbandonActiveRestart.sleep",DEFAULT_SLEEP_TIME);
			jobParams.put("sleep.time", sleepTime);
			TestUtil.logMsg("sleep.time=" + sleepTime + "");
			

			TestUtil.logMsg("Locate job XML file: abandonActiveRestart.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("abandonActiveRestart", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long jobInstanceId = execution1.getInstanceId();
			long firstExecutionId = execution1.getExecutionId();
			TCKJobExecutionWrapper exec = null;
			TestUtil.logMsg("Got Job instance id: " + jobInstanceId + "");
			TestUtil.logMsg("Got Job execution id: " + firstExecutionId + "");
			
			jobParams = new Properties();
			jobParams.put("execution.number", "2");
			TestUtil.logMsg("execution.number=2");
			sleepTime = System.getProperty("JobOperatorTests.testJobOperatorTestAbandonActiveRestart.sleep",DEFAULT_SLEEP_TIME);
			jobParams.put("sleep.time", sleepTime);
			TestUtil.logMsg("sleep.time=" + sleepTime + "");
			
			TestUtil.logMsg("Invoke restartJobWithoutWaitingForResult for execution id: " + firstExecutionId + "");
			exec = jobOp.restartJobWithoutWaitingForResult(firstExecutionId, jobParams);
			long secondExecutionId = exec.getExecutionId();			
			{
				TestUtil.logMsg("Invoke restartJobAndWaitForResult for execution id: " + firstExecutionId + "");
				
				boolean seen = false;
				try {
					jobOp.abandonJobExecution(secondExecutionId);
				}
				catch (JobExecutionIsRunningException jobRunningEx){
					TestUtil.logMsg("Caught JobExecutionIsRunningException as expected");
					seen = true;
				}
				assertWithMessage("Caught JobExecutionIsRunningException for abandon attempt during restart" , seen);
				
			}

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}
	
	/*
	 * @testName: testJobOperatorRestartJobAlreadyAbandoned
	 * 
	 * @assertion: testJobOperatorRestartJobAlreadyAbandoned
	 * @test_Strategy: start a job that is configured to fail. Once Job fails, abandon it.
	 *                 Attempt to restart the already abandon the job and confirm a JobRestartException
	 *                 is caught.
	 *                 
	 *                  
	 * @throws JobExecutionAlreadyCompleteException
	 * @throws NoSuchJobExecutionException
	 * @throws JobExecutionNotMostRecentException 
	 * @throws JobRestartException
	 *                 
	 */
	
	
	public void testJobOperatorRestartJobAlreadyAbandoned() throws Fault {

		String METHOD = "testJobOperatorRestartAlreadyCompleteException";
TestUtil.logTrace(METHOD);
		begin(METHOD);
		
		String DEFAULT_SLEEP_TIME = "1";

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			jobParams.put("execution.number", "1");
			TestUtil.logMsg("execution.number=1");
			String sleepTime = System.getProperty("JobOperatorTests.testJobOperatorTestRestartAlreadAbandonedJob.sleep",DEFAULT_SLEEP_TIME);
			jobParams.put("sleep.time", sleepTime);
			TestUtil.logMsg("sleep.time=" + sleepTime + "");
			

			TestUtil.logMsg("Locate job XML file: abandonActiveRestart.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("abandonActiveRestart", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());
			long jobInstanceId = execution1.getInstanceId();
			long firstExecutionId = execution1.getExecutionId();
			
			jobOp.abandonJobExecution(firstExecutionId);
			
			TestUtil.logMsg("Got Job instance id: " + jobInstanceId + "");
			TestUtil.logMsg("Got Job execution id: " + firstExecutionId + "");
			
			jobParams = new Properties();
			jobParams.put("execution.number", "2");
			TestUtil.logMsg("execution.number=2");
			
			TestUtil.logMsg("Invoke restartJobWithoutWaitingForResult for execution id: " + firstExecutionId + "");
						
			{
				TestUtil.logMsg("Invoke restartJobAndWaitForResult for execution id: " + firstExecutionId + "");
				
				boolean seen = false;
				try {
					jobOp.restartJobWithoutWaitingForResult(firstExecutionId, jobParams);
				}
				catch (JobRestartException jobRestartEx){
					TestUtil.logMsg("Caught JobRestartException as expected");
					seen = true;
				}
				assertWithMessage("Caught JobRestartException for abandon attempt during restart" , seen);
				
			}

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}
	
	/*
	 * @testName: testInvokeJobWithUserStop
	 * @assertion: The batch status of a job is set to stopped after it is stopped through the job operator
	 * @test_Strategy: Issue a job that runs in an infinite loop. Issue a job operator stop and verify the 
	 * batch status.
	 */
	
	
	public void testInvokeJobWithUserStop() throws Fault {
		String METHOD = "testInvokeJobWithUserStop";
TestUtil.logTrace(METHOD);
		begin(METHOD);
		
		final String DEFAULT_SLEEP_TIME = "1000";

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_longrunning.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParameters = new Properties();
			TestUtil.logMsg("run.indefinitely=true");
			jobParameters.setProperty("run.indefinitely" , "true");

			TestUtil.logMsg("Invoking startJobWithoutWaitingForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobWithoutWaitingForResult("job_batchlet_longrunning", jobParameters);

			int sleepTime = Integer.parseInt(System.getProperty("JobOperatorTests.testInvokeJobWithUserStop.sleep",DEFAULT_SLEEP_TIME));
			TestUtil.logMsg("Thread.sleep(" +  sleepTime + ")");
			Thread.sleep(sleepTime);

			TestUtil.logMsg("Invoking stopJobAndWaitForResult for Execution #1");
			jobOp.stopJobAndWaitForResult(jobExec);

			JobExecution jobExec2 = jobOp.getJobExecution(jobExec.getExecutionId());
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec2.getBatchStatus());
			assertObjEquals(BatchStatus.STOPPED, jobExec2.getBatchStatus());

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOperatorGetStepExecutions
	 * 
	 * @assertion:  Job Operator - getStepExecutions
	 * @test_Strategy: start a job that completes successfully. Get the list of all StepException objects associated with job execution id.
	 *                 Test that all objects retrieved are of type StepExecution.
	 * @throws Fault
	 */
	
	
	public void testJobOperatorGetStepExecutions() throws Fault {

		String METHOD = "testJobOperatorGetStepExecutions";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_1step.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_1step");

			TestUtil.logMsg("Obtaining StepExecutions for execution id: " + jobExec.getExecutionId() + "");
			List<StepExecution> stepExecutions = jobOp.getStepExecutions(jobExec.getExecutionId());

			assertObjEquals(1, stepExecutions.size());

			for (StepExecution step : stepExecutions) {
				// make sure all steps finish successfully
				showStepState(step);
				TestUtil.logMsg("Step status="+step.getBatchStatus());
				assertObjEquals(BatchStatus.COMPLETED, step.getBatchStatus());
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		} 

	}

	/*
	 * @testName: testJobOpGetJobNames
	 * 
	 * @assertion:  Job Operator - getJobNames
	 * @test_Strategy: This test is a bit weak in that, while the first time it runs,
	 *                 it does perform a real validation that the newly-submitted job
	 *                 is added to the getJobNames() result set, on subsequent runs
	 *                 it may have already been there from before and so isn't newly 
	 *                 verifying anything.  This is a simple function to implement so not
	 *                 a big deal.
	 */
	
	
	public void testJobOpGetJobNames() throws Fault {

		String METHOD = "testJobOpGetJobNames";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		String jobName ="job_unique_get_job_names";
		
		try {
			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			List<String> jobNames = jobOp.getJobNames();
			jobOp.startJobWithoutWaitingForResult("job_unique_get_job_names");
			if (jobNames.contains(jobName)) {
				TestUtil.logMsg("JobOperator.getJobNames() already includes " + jobName + ", test is not so useful");
			} else {
				TestUtil.logMsg("JobOperator.getJobNames() does not include " + jobName + " yet.");
			}

			jobNames = jobOp.getJobNames();
			assertWithMessage("Now JobOperator.getJobNames() definitely includes " + jobName, jobNames.contains(jobName));
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testAbandoned
	 * 
	 * @assertion:  Job Operator - abandon()
	 * @test_Strategy: run a job that completes successfully. Abandon the job. Test to ensure the Batch Status for the said job is marked as 'ABANDONED'
	 * @throws Fault
	 * 
	 */
	
	
	public void testAbandoned() throws Fault {

		String METHOD = "testAbandoned";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_4steps.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_4steps");
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());

			jobOp.abandonJobExecution(jobExec.getExecutionId());

			JobExecution jobExec2 = jobOp.getJobExecution(jobExec.getExecutionId());
			assertObjEquals(BatchStatus.ABANDONED, jobExec2.getBatchStatus());

		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testJobOpgetJobInstanceCount
	 * 
	 * @assertion:  Job Operator - getJobInstanceCount
	 * @test_Strategy: Retrieve the job instance count for a known job name. Run that job. 
	 *                 Retrieve the job instance count for that job again. Test that the count has increased by 1.
	 * @throws Fault
	 * 
	 */
	  
	
	public void testJobOpgetJobInstanceCount() throws Fault {
		String METHOD = "testJobOpgetJobInstanceCount";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {

			int countTrackerBEFORE = 0;

			try {
				countTrackerBEFORE = jobOp.getJobInstanceCount("chunksize5commitinterval5");
			} catch (NoSuchJobException e) {
				// Can continue.
			}

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.faile=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long jobInstanceId = execution1.getInstanceId();
			long lastExecutionId = execution1.getExecutionId();
			TestUtil.logMsg("Got Job instance id: " + jobInstanceId + "");
			TestUtil.logMsg("Got Job execution id: " + lastExecutionId + "");

			int countTrackerAFTER = jobOp.getJobInstanceCount("chunksize5commitinterval5");

			assertWithMessage("job count for job1 increased by 1", 1, countTrackerAFTER - countTrackerBEFORE);

			List<String> jobNames = jobOp.getJobNames();

			for (String jobname : jobNames) {
				TestUtil.logMsg(jobname + " instance count : " + jobOp.getJobInstanceCount(jobname) + " - ");
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testJobOpgetJobInstanceCountException
	 * 
	 * @assertion:  Job Operator - getJobInstanceCountException
	 * @test_Strategy: Retrieve the job instance count for a known job name. Run that job. 
	 *                 Retrieve the job instance count for a job name that does not exist. Test that the NoSuchJobException is returned.
	 * @throws Fault
	 * 
	 */
	  
	
	public void testJobOpgetJobInstanceCountException() throws Fault {
		String METHOD = "testJobOpgetJobInstanceCountException";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			int countTrackerBEFORE = 0;

			try {
				countTrackerBEFORE = jobOp.getJobInstanceCount("ChunkStopOnEndOn");
			} catch (NoSuchJobException e) {
				// Can continue.
			}

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.faile=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long jobInstanceId = execution1.getInstanceId();
			long lastExecutionId = execution1.getExecutionId();
			TestUtil.logMsg("Got Job instance id: " + jobInstanceId + "");
			TestUtil.logMsg("Got Job execution id: " + lastExecutionId + "");

			boolean seenException = false;
			try {
				int countTrackerAFTER = jobOp.getJobInstanceCount("NoSuchJob");
			} catch (NoSuchJobException noJobEx) {
				TestUtil.logMsg("Confirmed we caught NoSuchJobException");
				seenException = true;
			}
			assertWithMessage("Saw NoSuchJobException for job 'NoSuchJob'", seenException);
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOpgetJobInstances
	 * 
	 * @assertion:  Job Operator - getJobInstances
	 * @test_Strategy: start a job 10 times which will ensure at least one job instance known to the runtime. 
	 *                 Retrieve a list of job instance ids for the job name just started. Ask for the first 200 found.
	 *                 Test that size grows by 10. 
	 * @throws Fault
	 * 
	 */
	  
	
	public void testJobOpgetJobInstances() throws Fault {
		String METHOD = " testJobOpgetJobInstances";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		int submitTimes = 10;
		
		try {
			int countTrackerBEFORE = 0;
			int countTrackerAFTER = 0;

			try {
				countTrackerBEFORE = jobOp.getJobInstanceCount("chunksize5commitinterval5");
				TestUtil.logMsg("Before test ran the JobInstance count for chunksize5commitinterval5 was " + countTrackerBEFORE );
			} catch (NoSuchJobException e) {
				TestUtil.logMsg("Not an error, but just the first time executing this job ");
			}

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.faile=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			for (int i=0; i < submitTimes; i++) {
				jobOp.startJobWithoutWaitingForResult("chunksize5commitinterval5", jobParams);
			}

			List<JobInstance> jobInstances = null;
			try {
				jobInstances = jobOp.getJobInstances("chunksize5commitinterval5", 0, 10);
				countTrackerAFTER = jobOp.getJobInstanceCount("chunksize5commitinterval5");
				assertWithMessage("Check that we see: " + submitTimes + " new submissions", 
						submitTimes, countTrackerAFTER - countTrackerBEFORE);
			} catch (NoSuchJobException noJobEx) {
				TestUtil.logMsg("Failing test, caught NoSuchJobException");
				throw noJobEx;
			}

			TestUtil.logMsg("Size of Job Instances list = " + jobInstances.size() + "");
			assertWithMessage("Testing that a list of Job Instances were obtained", 10, jobInstances.size());

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOpgetJobInstancesException
	 * 
	 * @assertion:  Job Operator - getJobInstancesException
	 * @test_Strategy: Retrieve a list of job instances for a job name that does not exist. 
	 *                 Test that the NoSuchJobException is thrown.
	 * @throws  Exception 
	 * 
	 */
	  
	
	public void testJobOpgetJobInstancesException() throws Fault {
		String METHOD = "testJobOpgetJobInstancesException";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.faile=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");


			TestUtil.logMsg("Locate job XML file: /chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			List<JobInstance> jobIds = null;
			boolean seenException = false;
			try {
				jobIds = jobOp.getJobInstances("NoSuchJob", 0, 12);
			} catch (NoSuchJobException noJobEx) {
				seenException = true;
				TestUtil.logMsg("Confirmed we caught NoSuchJobException");
			}
			assertWithMessage("Saw NoSuchJobException for job 'NoSuchJob'", seenException);
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOperatorGetParameters
	 * 
	 * @assertion:  Job Operator - getParameters
	 * @test_Strategy: Start a job with a set of parameters. Restart the job with a set of override parameters. Once completed, retrieve the 
	 *                 parameters object associated with the job instance. Test that the object retrieved is a Properties object. 
	 *                 Test that the NoSuchJobException is thrown.
	 * @throws Fault
	 * 
	 */
	  
	
	public void testJobOperatorGetParameters() throws Fault {
		String METHOD = "testJobOperatorGetParameters";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties originalJobParams = new Properties();
			Properties restartJobParams = null;
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.next.writepoints=10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			originalJobParams.put("execution.number", "1");
			originalJobParams.put("readrecord.fail", "12");
			originalJobParams.put("app.arraysize", "30");
			originalJobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			originalJobParams.put("app.next.writepoints", "10,15,20,25,30");
			originalJobParams.put("app.commitinterval", "5");
			
			// Expected parameters on restart only.
			String N1="extra.parm.name1";  String V1="extra.parm.value1";
			String N2="extra.parm.name2";  String V2="extra.parm.value2";

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", originalJobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long jobInstanceId = execution1.getInstanceId();
			long lastExecutionId = execution1.getExecutionId();
			TCKJobExecutionWrapper execution2 = null;
			TestUtil.logMsg("Got Job instance id: " + jobInstanceId + "");
			TestUtil.logMsg("Got Job execution id: " + lastExecutionId + "");
			{
				TestUtil.logMsg("Invoke clone original job execution parameters");
				// Shallow copy is OK for simple <String,String> of java.util.Properties
				restartJobParams = (Properties)originalJobParams.clone(); 
				TestUtil.logMsg("Put some extra parms in the restart execution");
				restartJobParams.put(N1, V1);
				restartJobParams.put(N2, V2);
				TestUtil.logMsg("Invoke restartJobAndWaitForResult for execution id: " + lastExecutionId + "");
				execution2 = jobOp.restartJobAndWaitForResult(lastExecutionId, restartJobParams);
				TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+execution2.getBatchStatus());
				TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+execution2.getExitStatus());
				TestUtil.logMsg("execution #2 Job instance id="+execution2.getInstanceId());
				assertWithMessage("Testing execution #2", BatchStatus.COMPLETED, execution2.getBatchStatus());
				assertWithMessage("Testing execution #2", "COMPLETED", execution2.getExitStatus());
				assertWithMessage("Testing execution #2", jobInstanceId, execution2.getInstanceId());  
			}
			
			// Test original execution
			Properties jobParamsFromJobOperator = jobOp.getParameters(execution1.getExecutionId());
			Properties jobParamsFromJobExecution = execution1.getJobParameters();
  			assertWithMessage("Comparing original job params with jobOperator.getParameters", originalJobParams, jobParamsFromJobOperator);
			TestUtil.logMsg("JobOperator.getParameters() matches for original execution ");
			assertWithMessage("Comparing original job params with jobExecution.getParameters", originalJobParams, jobParamsFromJobExecution);
			TestUtil.logMsg("JobExecution.getParameters() matches for original execution ");
			
			// Test restart execution
			Properties restartJobParamsFromJobOperator = jobOp.getParameters(execution2.getExecutionId());
			Properties restartJobParamsFromJobExecution = execution2.getJobParameters();
			assertWithMessage("Comparing restart job params with jobOperator.getParameters", restartJobParams, restartJobParamsFromJobOperator);
			TestUtil.logMsg("JobOperator.getParameters() matches for restart execution ");
			assertWithMessage("Comparing restart job params with jobExecution.getParameters", restartJobParams, restartJobParamsFromJobExecution);
			TestUtil.logMsg("JobExecution.getParameters() matches for restart execution ");
			
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOperatorGetJobInstances
	 * 
	 * @assertion:  Job Operator - getJobInstances
	 * @test_Strategy: Start a specific job four times, all of which will finish successfully. Retrieve two separate lists of JobExecutions for the job.
	 *                 List 1 will contain JobExecution Objects for job start 1 - 3. List 2 will contain JobExecution Objects for job start 2 - 4.
	 *                 Test that the second and third JobExecution objects of List 1 is equivalent to the first and second JobExecution objects in List 2.
	 *                                   
	 * @throws Fault
	 * 
	 */
	  
	
	public void testJobOperatorGetJobInstances() throws Fault {
		String METHOD = "testJobOperatorGetJobInstances";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=31");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "31");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "COMPLETED", execution1.getExitStatus());

			TestUtil.logMsg("Create job parameters for execution #2:");
			jobParams = new Properties();
			TestUtil.logMsg("execution.number=2");
			TestUtil.logMsg("readrecord.fail=31");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "31");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #2");
			JobExecution execution2 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);
			TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #2", BatchStatus.COMPLETED, execution2.getBatchStatus());
			assertWithMessage("Testing execution #2", "COMPLETED", execution2.getExitStatus());

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #3");
			JobExecution execution3 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);
			TestUtil.logMsg("execution #3 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #3 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #3", BatchStatus.COMPLETED, execution3.getBatchStatus());
			assertWithMessage("Testing execution #3", "COMPLETED", execution3.getExitStatus());

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #4");
			JobExecution execution4 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);
			TestUtil.logMsg("execution #4 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #4 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #4", BatchStatus.COMPLETED, execution4.getBatchStatus());
			assertWithMessage("Testing execution #4", "COMPLETED", execution4.getExitStatus());

			List <JobInstance> jobInstances012 = jobOp.getJobInstances("chunksize5commitinterval5",0,3);
			List <JobInstance> jobInstances123 = jobOp.getJobInstances("chunksize5commitinterval5",1,3);

			for (int i=0; i<3; i++){
				logger.fine("AJM: instance id012["+i+"] = " + jobInstances012.get(i).getInstanceId());
				logger.fine("AJM: instance id123["+i+"] = " + jobInstances123.get(i).getInstanceId());
			}

			assertWithMessage("job instances should not be equal", jobInstances012.get(0).getInstanceId()!=jobInstances123.get(0).getInstanceId()); 
			assertWithMessage("job instances should be equal", jobInstances012.get(1).getInstanceId()==jobInstances123.get(0).getInstanceId()); 
			assertWithMessage("job instances should be equal", jobInstances012.get(2).getInstanceId()==jobInstances123.get(1).getInstanceId()); 
			assertWithMessage("job instances should not be equal", jobInstances012.get(2).getInstanceId()!=jobInstances123.get(2).getInstanceId()); 

			TestUtil.logMsg("Size of jobInstancesList = " + jobInstances012.size() + "");
			TestUtil.logMsg("Testing retrieval of the JobInstances list, size = " + jobInstances012.size() + "");
			assertWithMessage("Testing retrieval of the JobInstances list", jobInstances012.size() > 0);
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOperatorGetRunningJobExecutions
	 * 
	 * @assertion:  Job Operator - getRunningExecutions
	 * @test_Strategy: start a job which will ensure at least one job execution is known to the runtime. Job will be long running. Testcase does not wait for job to complete.
	 *                 Retrieve a list of JobExecution(s) for the job name just started that are in running state. Ensure that at least one JobExecution is returned
	 *                 Test that 
	 * @throws Fault
	 * 
	 */
	
	
	public void testJobOperatorGetRunningJobExecutions() throws Fault {
		String METHOD = "testJobOperatorGetRunningJobExecutions";
TestUtil.logTrace(METHOD);
		begin(METHOD);
		
		final String DEFAULT_SLEEP_TIME = "1000";
		final String DEFAULT_APP_TIME_INTERVAL = "10000";

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			String timeinterval = System.getProperty("JobOperatorTests.testJobOperatorGetRunningJobExecutions.app.timeinterval",DEFAULT_APP_TIME_INTERVAL);
			
			jobParams.put("app.timeinterval", timeinterval);

			TestUtil.logMsg("Invoke startJobWithoutWaitingForResult for execution #1");
			JobExecution execution1 = jobOp.startJobWithoutWaitingForResult("job_batchlet_step_listener", jobParams);

			Properties newJobParameters = new Properties();
			newJobParameters.put("app.timeinterval", timeinterval);
			TestUtil.logMsg("Invoke startJobWithoutWaitingForResult");

			JobExecution exec = jobOp.startJobWithoutWaitingForResult("job_batchlet_step_listener", newJobParameters);

			// Sleep to give the runtime the chance to start the job.  The job has a delay built into the stepListener afterStep() 
			// so we aren't worried about the job finishing early leaving zero running executions.
			int sleepTime = Integer.parseInt(System.getProperty("ExecutionTests.testJobOperatorGetRunningJobExecutions.sleep",DEFAULT_SLEEP_TIME));
			TestUtil.logMsg("Thread.sleep(" + sleepTime + ")");
			Thread.sleep(sleepTime);

			List<Long> jobExecutions = jobOp.getRunningExecutions("job_batchlet_step_listener");
			assertWithMessage("Found job instances in the RUNNING state", jobExecutions.size() > 0);

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOperatorGetRunningJobInstancesException
	 * 
	 * @assertion:  Job Operator - getJobInstances
	 * @test_Strategy: start a job which will ensure at least one job instance known to the runtime. Job will be long running. Testcase does not wait for job to complete.
	 *                 Retrieve a list of job instance ids for a job name that does not exist in running state. Ensure that NoSuchJobException exception is thrown
	 *                 Test that 
	 * @throws Fault
	 * 
	 */
	
	
	public void testJobOperatorGetRunningJobInstancesException() throws Fault {
		String METHOD = "testJobOperatorGetRunningJobInstancesException";
TestUtil.logTrace(METHOD);
		begin(METHOD);
		
		final String DEFAULT_APP_TIME_INTERVAL = "10000";

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
		
			String timeinterval = System.getProperty("JobOperatorTests.testJobOperatorGetRunningJobInstancesException.app.timeinterval",DEFAULT_APP_TIME_INTERVAL);
			
			jobParams.put("app.timeinterval", timeinterval);

			TestUtil.logMsg("Invoke startJobWithoutWaitingForResult for execution #1");
			JobExecution execution1 = jobOp.startJobWithoutWaitingForResult("job_batchlet_step_listener", jobParams);

			Properties restartJobParameters = new Properties();
			restartJobParameters.put("app.timeinterval", timeinterval);

			TestUtil.logMsg("Invoke startJobWithoutWaitingForResult");
			JobExecution exec = jobOp.startJobWithoutWaitingForResult("job_batchlet_step_listener", restartJobParameters);

			boolean seenException = false;
			try {
				TestUtil.logMsg("Check for an instance of a non-existent job");
				jobOp.getRunningExecutions("JOBNAMEDOESNOTEXIST");
			}
			catch (NoSuchJobException e) {
				TestUtil.logMsg("Confirmed that exception caught is an instanceof NoSuchJobException");
				seenException = true;
			}
			assertWithMessage("Saw NoSuchJobException for job 'JOBNAMEDOESNOTEXIST'", seenException);
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}
	/*
	 * @testName: testJobOperatorGetJobExecution
	 * 
	 * @assertion:  Job Operator - getJobExecution
	 * @test_Strategy: start a job which will run to successful completion.
	 *                 Retrieve a JobExecution object using the execution ID returned by the start command.
	 *                 Ensure the object returned is an instance of JobExecution
	 *                  
	 * @throws Fault 
	 * 
	 */
	 
	
	public void testJobOperatorGetJobExecution() throws Fault {
		String METHOD = "testJobOperatorGetJobExecution";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=12");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.next.writepoints=10,15,20,25,30");
			TestUtil.logMsg("app.commitinterval=5");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.next.writepoints", "10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunksize5commitinterval5", jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long jobInstanceId = execution1.getInstanceId();
			long lastExecutionId = execution1.getExecutionId();
			TCKJobExecutionWrapper exec = null;

			{

				TestUtil.logMsg("Invoke restartJobAndWaitForResult for execution id: " + lastExecutionId + "");
				exec = jobOp.restartJobAndWaitForResult(lastExecutionId, jobParams);
				lastExecutionId = exec.getExecutionId();
				TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+exec.getBatchStatus());
				TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+exec.getExitStatus());
				TestUtil.logMsg("execution #2 Job instance id="+exec.getInstanceId());
				assertWithMessage("Testing execution #2", BatchStatus.COMPLETED, exec.getBatchStatus());
				assertWithMessage("Testing execution #2", "COMPLETED", exec.getExitStatus());
				assertWithMessage("Testing execution #2", jobInstanceId, exec.getInstanceId());  
			}

			TestUtil.logMsg("Testing retrieval of a JobExecution obj");
			JobExecution jobEx = jobOp.getJobExecution(lastExecutionId);
			TestUtil.logMsg("Status retreived from JobExecution obj: " + jobEx.getBatchStatus() + "");
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testJobOperatorGetJobExecutions
	 * 
	 * @assertion:  Job Operator - getJobExecutions and JobExecution APIs
	 * @test_Strategy: start a job which will fail, then restart and run to successful completion.
	 *                 Validate the two JobExecution instances, e.g. ensure both map to the same
	 *                 JobInstance when getJobInstance() is passed the respective executionIds.
	 *                 Also ensure that getJobExecutions() on the instance returns these same two executionIds. 
	 * @throws Fault 
	 * 
	 */
	 
	
	public void testJobOperatorGetJobExecutions() throws Fault {
		String METHOD = "testJobOperatorGetJobExecutions";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		String jobName = "chunksize5commitinterval5";
		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,5,10,15,20,25,30");
			jobParams.put("app.next.writepoints", "10,15,20,25,30");
			jobParams.put("app.commitinterval", "5");

			TestUtil.logMsg("Locate job XML file: chunksize5commitinterval5.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult(jobName, jobParams);
		    
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing execution #1", "FAILED", execution1.getExitStatus());

			long execution1ID= execution1.getExecutionId();
			TCKJobExecutionWrapper execution2 = null;

			{
				TestUtil.logMsg("Invoke restartJobAndWaitForResult for execution id: " + execution1ID + "");
				execution2= jobOp.restartJobAndWaitForResult(execution1ID, jobParams);
				TestUtil.logMsg("execution #2 JobExecution getBatchStatus()="+execution2.getBatchStatus());
				TestUtil.logMsg("execution #2 JobExecution getExitStatus()="+execution2.getExitStatus());
				TestUtil.logMsg("execution #2 Job instance id="+execution2.getInstanceId());
				assertWithMessage("Testing execution #2", BatchStatus.COMPLETED, execution2.getBatchStatus());
				assertWithMessage("Testing execution #2", "COMPLETED", execution2.getExitStatus());
			}
			
			// Execution 1 and 2 have the same instanceId
			assertWithMessage("Testing execution #1 and execution #2 use the same instanceId", 
					execution1.getInstanceId(), execution2.getInstanceId());  

			long execution2ID= execution2.getExecutionId();
			JobInstance jobInstance = jobOp.getJobInstance(execution2ID);
			
			// Verify getJobExecutions() based on instance gives us the same two JobExecution(s);
			List<JobExecution> jobExecutions = jobOp.getJobExecutions(jobInstance);
			assertWithMessage("Testing list size of JobExecutions", 2, jobExecutions.size());
			boolean seen1 = false; boolean seen2=false;
			for (JobExecution je : jobExecutions){
				if (je.getExecutionId() == execution1ID) {
					assertWithMessage("Dup of execution 1", !seen1);
					TestUtil.logMsg("Seen execution #1 ");
					seen1= true;
				} else if (je.getExecutionId() == execution2ID) {
					assertWithMessage("Dup of execution 2", !seen2);
					TestUtil.logMsg("Seen execution #2 ");
					seen2= true;
				} 
			}
			assertWithMessage("Seen both of the two JobExecutions", seen1 && seen2);
			
			assertWithMessage("Job name from JobInstance matches", jobName, jobInstance.getJobName());
			assertWithMessage("Job name from JobExecution 1 matches", jobName, execution1.getJobName());
			assertWithMessage("Job name from JobExecution 2 matches", jobName, execution2.getJobName());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}


	private void showStepState(StepExecution step) {


		TestUtil.logMsg("---------------------------");
		TestUtil.logMsg("getStepName(): " + step.getStepName() + " - ");
		TestUtil.logMsg("getJobExecutionId(): " + step.getStepExecutionId() + " - ");
		//System.out.print("getStepExecutionId(): " + step.getStepExecutionId() + " - ");
		Metric[] metrics = step.getMetrics();

		for (int i = 0; i < metrics.length; i++) {
			TestUtil.logMsg(metrics[i].getType() + ": " + metrics[i].getValue() + " - ");
		}

		TestUtil.logMsg("getStartTime(): " + step.getStartTime() + " - ");
		TestUtil.logMsg("getEndTime(): " + step.getEndTime() + " - ");
		//System.out.print("getLastUpdateTime(): " + step.getLastUpdateTime() + " - ");
		TestUtil.logMsg("getBatchStatus(): " + step.getBatchStatus() + " - ");
		TestUtil.logMsg("getExitStatus(): " + step.getExitStatus());
		TestUtil.logMsg("---------------------------");
	}
}
