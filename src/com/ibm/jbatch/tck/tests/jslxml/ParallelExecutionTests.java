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

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.StepExecution;







import com.ibm.jbatch.tck.utils.JobOperatorBridge;
import com.ibm.jbatch.tck.utils.TCKJobExecutionWrapper;


public class ParallelExecutionTests extends ServiceEETest {

	private final static Logger logger = Logger.getLogger(ParallelExecutionTests.class.getName());

	private static final String TIME_TO_SLEEP_BEFORE_ISSUING_STOP = "1900"; 

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

	
	public static void cleanup() throws Fault {
	}


	private void begin(String str) {
		TestUtil.logMsg("Begin test method: " + str + "");
	}

	/*
	 * @testName: testInvokeJobWithOnePartitionedStep
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithOnePartitionedStep() throws Fault {
		String METHOD = "testInvokeJobWithOnePartitionedStep";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_partitioned_1step.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExecution = jobOp.startJobAndWaitForResult("job_partitioned_1step");

			TestUtil.logMsg("JobExecution getBatchStatus()="+jobExecution.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithOnePartitionedStepExitStatus
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithOnePartitionedStepExitStatus() throws Fault {
		String METHOD = "testInvokeJobWithOnePartitionedStepExitStatus";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		String DEFAULT_SLEEP_TIME = "2000";

		try {
			Properties jobParameters = new Properties();

			String sleepTime = System.getProperty("ParallelExecutionTests.testInvokeJobWithOnePartitionedStepExitStatus.sleep",DEFAULT_SLEEP_TIME);
			jobParameters.put("sleep.time", sleepTime);
			
			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution jobExecution = jobOp.startJobAndWaitForResult("job_partitioned_1step_exitStatusTest",jobParameters);

			TestUtil.logMsg("JobExecution getBatchStatus()="+jobExecution.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());

			List<StepExecution> stepExecutions = jobOp.getStepExecutions(jobExecution.getExecutionId());
			assertObjEquals(1, stepExecutions.size());

			for (StepExecution stepEx : stepExecutions) {
				assertObjEquals("STEP EXIT STATUS: 10", stepEx.getExitStatus());
				assertObjEquals(BatchStatus.COMPLETED, stepEx.getBatchStatus());
			}

			assertObjEquals("JOB EXIT STATUS: 10", jobExecution.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());

		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testStopRunningPartitionedStep
	 * 
	 * @assertion: Issuing a JobOperator.stop() will stop all partitioned
	 * threads
	 * 
	 * @test_Strategy: A partitioned batchlet is run in an infinite loop with 4
	 * instances. The Test verifies that the job returns with STOPPED status
	 * instead of running forever.
	 */
	
	
	public void testStopRunningPartitionedStep() throws Fault {
		String METHOD = "testStopRunningPartitionedStep";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_longrunning_partitioned.xml");

			TestUtil.logMsg("Create job parameters");
			Properties overrideJobParams = new Properties();
			TestUtil.logMsg("run.indefinitely=true");
			overrideJobParams.setProperty("run.indefinitely" , "true");

			TestUtil.logMsg("Invoke startJobWithoutWaitingForResult");
			JobExecution jobExecution =  jobOp.startJobWithoutWaitingForResult("job_batchlet_longrunning_partitioned", overrideJobParams);

			//Sleep long enough for parallel steps to fan out
			int sleepTime = Integer.parseInt(System.getProperty("ParallelExecutionTests.testStopRunningPartitionedStep.sleep",TIME_TO_SLEEP_BEFORE_ISSUING_STOP));
			TestUtil.logMsg("Sleep for " + TIME_TO_SLEEP_BEFORE_ISSUING_STOP);
			Thread.sleep(sleepTime);


			TestUtil.logMsg("Invoke stopJobAndWaitForResult");
			jobOp.stopJobAndWaitForResult(jobExecution);

			JobExecution jobExec2 = jobOp.getJobExecution(jobExecution.getExecutionId());
			TestUtil.logMsg("JobExecution getBatchStatus()=" + jobExec2.getBatchStatus() + "");
			assertObjEquals(BatchStatus.STOPPED, jobExec2.getBatchStatus());

		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testStopRestartRunningPartitionedStep
	 * 
	 * @assertion: A stopped partitioned step can be restarted to completion.
	 * 
	 * @test_Strategy: A partitioned batchlet is run in an infinite loop with 4
	 * instances. The Test verifies that the job returns with STOPPED status
	 * instead of running forever. The job is restarted and each partition must 
	 * restart and run to completion.
	 */
	
	
	public void testStopRestartRunningPartitionedStep() throws Fault {
		String METHOD = "testStopRestartRunningPartitionedStep";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_longrunning_partitioned.xml");

			TestUtil.logMsg("Create job parameters");
			Properties jobParams = new Properties();
			TestUtil.logMsg("run.indefinitely=true");
			jobParams.setProperty("run.indefinitely", "true");

			TestUtil.logMsg("Invoke startJobWithoutWaitingForResult");
			JobExecution origJobExecution = jobOp.startJobWithoutWaitingForResult("job_batchlet_longrunning_partitioned", jobParams);

			// Sleep long enough for parallel steps to fan out
			int sleepTime = Integer.parseInt(System.getProperty("ParallelExecutionTests.testStopRestartRunningPartitionedStep.sleep",TIME_TO_SLEEP_BEFORE_ISSUING_STOP));
			TestUtil.logMsg("Sleep for " + TIME_TO_SLEEP_BEFORE_ISSUING_STOP);
			Thread.sleep(sleepTime);

			TestUtil.logMsg("Invoke stopJobAndWaitForResult");
			jobOp.stopJobAndWaitForResult(origJobExecution);

			JobExecution jobExec2 = jobOp.getJobExecution(origJobExecution.getExecutionId());
			TestUtil.logMsg("JobExecution getBatchStatus()=" + jobExec2.getBatchStatus() + "");
			assertObjEquals(BatchStatus.STOPPED, jobExec2.getBatchStatus());

			TestUtil.logMsg("Create restart job parameters");
			Properties restartJobParams = new Properties();
			TestUtil.logMsg("run.indefinitely=true");
			restartJobParams.setProperty("run.indefinitely", "false");

			TestUtil.logMsg("Invoke restartJobAndWaitForResult");
			JobExecution restartedJobExec = jobOp.restartJobAndWaitForResult(origJobExecution.getExecutionId(), restartJobParams);

			TestUtil.logMsg("JobExecution getBatchStatus()=" + restartedJobExec.getBatchStatus() + "");
			assertObjEquals(BatchStatus.COMPLETED, restartedJobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobSimpleSplit
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobSimpleSplit() throws Fault {
		String METHOD = "testInvokeJobSimpleSplit";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_split_batchlet_4steps.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution execution = jobOp.startJobAndWaitForResult("job_split_batchlet_4steps");

			TestUtil.logMsg("JobExecution getBatchStatus()="+execution.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, execution.getBatchStatus());
			assertObjEquals("COMPLETED", execution.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testPartitionedPlanCollectorAnalyzerReducerComplete
	 * 
	 * @assertion: Section 8.7. Partitioned artifact and Chunk processing order.
	 * Partition mapper can generate a plan that will determine partition
	 * instances and properties.
	 * 
	 * @test_Strategy: A chunk is partitioned into exactly 3 partitions, using
	 * the mapper, each with their own checkpointing. The collector, analyzer,
	 * and reducer, each append to the exit status to verify that they are
	 * called in the correct order. The persistent data is used to remember how
	 * many times the step has been run. If the data is not persisted
	 */
	
	
	public void testPartitionedPlanCollectorAnalyzerReducerComplete() throws Fault {
		String METHOD = "testPartitionedPlanCollectorAnalyzerReducerComplete";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_partitioned_artifacts.xml");

			TestUtil.logMsg("Create Job parameters for Execution #1");
			Properties jobParams = new Properties();
			TestUtil.logMsg("numPartitionsProp=3");
			//append "CA" to expected exit status for each partition
			jobParams.setProperty("numPartitionsProp" , "3"); 

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution execution = jobOp.startJobAndWaitForResult("job_partitioned_artifacts", jobParams);

			TestUtil.logMsg("Execution exit status = " +  execution.getExitStatus());
			assertObjEquals("nullBeginCACACABeforeAfter", execution.getExitStatus());

			TestUtil.logMsg("Execution status = " + execution.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED,execution.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testZeroBasedPartitionedPlanCollectorAnalyzerReducerRollback
	 * 
	 * @assertion: Section 8.7. Partitioned artifact and Chunk processing order
	 * verifies that Rollback() is called on the reducer in case of a failure in
	 * any partition. Also verifies that the collector and analyzer are always
	 * called at the end of a partition even in the case or a failure, and
	 * partition properties are passed to partitions.
	 * 
	 * @test_Strategy: A mapper is used to generate exactly 3 partitions, with
	 * one partition marked to fail. Each partition appends to the exit status
	 * through the collector, analyzer, and finally the reducer.
	 */
	
	
	public void testZeroBasedPartitionedPlanCollectorAnalyzerReducerRollback() throws Fault {
		String METHOD = "testZeroBasedPartitionedPlanCollectorAnalyzerReducerRollback";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_partitioned_artifacts.xml");

			TestUtil.logMsg("Create Job parameters for Execution #1");
			Properties jobParams = new Properties();
			TestUtil.logMsg("numPartitionsProp=3");
			TestUtil.logMsg("failThisPartition=0");
			//append "CA" to expected exit status for each partition
			jobParams.setProperty("numPartitionsProp" , "3"); 
			jobParams.setProperty("failThisPartition" , "0"); //Remember we are 0 based

			TestUtil.logMsg("Invoke startJobAndWaitForResult");
			JobExecution execution = jobOp.startJobAndWaitForResult("job_partitioned_artifacts", jobParams);

			TestUtil.logMsg("Execution exit status = " +  execution.getExitStatus());
			assertObjEquals("nullBeginCACACARollbackAfter", execution.getExitStatus());

			TestUtil.logMsg("Execution status = " + execution.getBatchStatus());
			assertObjEquals(BatchStatus.FAILED,execution.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testPartitionedCollectorAnalyzerReducerChunkRestartItemCount10
	 * 
	 * @assertion: Section 8.7. Partitioned artifact and Chunk processing order.
	 * and persistent data in partitioned step is actually persisted, and a
	 * completed partition is not restarted.
	 * 
	 * @test_Strategy: A chunk is partitioned into 3 partitions each with their
	 * own checkpointing. The collector, analyzer, and reducer, each append to
	 * the exit status to verify that they are called in the correct order. (The
	 * 'C' in the exit status String represents a call to the (C)ollector and
	 * the 'A' a call to the (A)nalyzer). The persistent data is used to remember 
	 * how many times the step has been run. If the data is not persisted correctly 
	 * the partitioned steps will not be * able to complete because the persisted count will not get incremented One
	 * of the partitions completes on the first attempt. The other two fail and
	 * must be restarted. We verify that the completed partition is not rerun
	 * since it does not append any data to the exit status.
	 */
	
	
	public void testPartitionedCollectorAnalyzerReducerChunkRestartItemCount10() throws Fault {

		String METHOD = "testPartitionedCollectorAnalyzerReducerChunkRestartItemCount10";
TestUtil.logTrace(METHOD);
		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("readrecord.fail=23");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.writepoints=0,5,10,15,20,25,30");
			TestUtil.logMsg("app.next.writepoints=0,5,10,15,20,25,30");
			jobParams.put("readrecord.fail", "23");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.writepoints", "0,10,20,30");
			jobParams.put("app.next.writepoints", "20,30");

			TestUtil.logMsg("Locate job XML file: chunkrestartPartitionedCheckpt10.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			TCKJobExecutionWrapper execution1 = jobOp.startJobAndWaitForResult("chunkrestartPartitionedCheckpt10", jobParams);

			{ // Use block to reduce copy/paste errors
				TestUtil.logMsg("execution #1 JobExecution getBatchStatus()=" + execution1.getBatchStatus() + "");
				TestUtil.logMsg("execution #1 JobExecution getExitStatus()=" + execution1.getExitStatus() + "");
				assertWithMessage("Testing execution #1", BatchStatus.FAILED, execution1.getBatchStatus());

				// '2' each from partition that gets through the first two chunks and fails during the third
				// '4' for the partition that gets through all three chunks, plus one call for end of partition
				int CA_PAIRS = 2 + 2 + 4;   
				String CA = "CA"; // This is a (C)ollector(A)nalyzerPair

				String expectedExitStatus = "null"; // verifies clean exit status
				expectedExitStatus += "Begin";    // From PartitionReducer beginPartitionedStep()
				for (int i = 0; i < CA_PAIRS; i++, expectedExitStatus += CA);
				expectedExitStatus += "Rollback"; // From PartitionReducer rollbackPartitionedStep()
				expectedExitStatus += "After";    // From PartitionReducer afterPartitionedStepCompletion()

				// Should be: "nullBeginCACACACACACACACARollbackAfter"
				assertWithMessage("Testing execution #1", expectedExitStatus, execution1.getExitStatus());
			}

			{
				long execution1Id = execution1.getExecutionId();
				long execution1InstanceId = execution1.getInstanceId();
				TestUtil.logMsg("Invoke restartJobAndWaitForResult with execution id: " + execution1Id + "");
				TCKJobExecutionWrapper execution2 = jobOp.restartJobAndWaitForResult(execution1Id, jobParams);

				TestUtil.logMsg("execution #2 JobExecution getBatchStatus()=" + execution2.getBatchStatus() + "");
				TestUtil.logMsg("execution #2 JobExecution getExitStatus()=" + execution2.getExitStatus() + "");
				TestUtil.logMsg("execution #2 Job instance id=" + execution2.getInstanceId() + "");
				TestUtil.logMsg("execution #2 Job execution id=" + execution2.getExecutionId() + "");
				
				// '2' for each of the two partitions that process chunks #2, #3, and each make one C+A call 
				// at the end of the partition and '0' for the partition already complete.
				int CA_PAIRS = 2 + 2;
				String CA = "CA"; // This is a (C)ollector(A)nalyzerPair

				String expectedExitStatus = "null"; // verifies clean exit status
				expectedExitStatus += "Begin";    // From PartitionReducer beginPartitionedStep()
				for (int i = 0; i < CA_PAIRS; i++, expectedExitStatus += CA);
				expectedExitStatus += "Before"; // From PartitionReducer beforePartitionedStepCompletion()
				expectedExitStatus += "After";    // From PartitionReducer afterPartitionedStepCompletion()

				// Should be: "nullBeginCACACACABeforeAfter"
				assertWithMessage("Testing execution #2 exit status", expectedExitStatus, execution2.getExitStatus());
				assertWithMessage("Testing execution #2 batch status", BatchStatus.COMPLETED, execution2.getBatchStatus());
				assertWithMessage("Testing execution #2 instance ID", execution1InstanceId, execution2.getInstanceId());
			}

		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}
	
	/*
     * @testName: testPartitionedMapperOverrideFalseOnRestart
     * 
     * @assertion: Section 8.7. Partitioned artifact and Chunk processing order.
     * Partition mapper can generate a plan that will determine partition
     * instances and properties.
     * 
     * @test_Strategy: 
     */
    
    
    public void testPartitionedMapperOverrideFalseOnRestart() throws Fault {
        String METHOD = "testPartitionedMapperOverrideFalse";
TestUtil.logTrace(METHOD);
        begin(METHOD);

        try {
            TestUtil.logMsg("Locate job XML file: job_partitioned_artifacts.xml");

            TestUtil.logMsg("Create Job parameters for Execution #1");
            Properties jobParams = new Properties();
            TestUtil.logMsg("numPartitionsProp=3");
            TestUtil.logMsg("failThisPartition=0");
            //append "CA" to expected exit status for each partition
            jobParams.setProperty("numPartitionsProp" , "3"); 
            jobParams.setProperty("failThisPartition" , "0"); //Remember we are 0 based
            jobParams.setProperty("partitionsOverride", "false");

            TestUtil.logMsg("Invoke startJobAndWaitForResult");
            JobExecution execution = jobOp.startJobAndWaitForResult("job_partitioned_artifacts", jobParams);

            TestUtil.logMsg("Execution exit status = " +  execution.getExitStatus());
            assertObjEquals("nullBeginCACACARollbackAfter", execution.getExitStatus());

            TestUtil.logMsg("Execution status = " + execution.getBatchStatus());
            assertObjEquals(BatchStatus.FAILED,execution.getBatchStatus());
            
            TestUtil.logMsg("Set restart job parameters");
            jobParams.setProperty("numPartitionsProp" , "7"); 
            jobParams.setProperty("failThisPartition" , "5"); //Remember we are 0 based
            jobParams.setProperty("partitionsOverride", "false");
            
            JobExecution execution2 = jobOp.restartJobAndWaitForResult(execution.getExecutionId(), jobParams);
            TestUtil.logMsg("Execution exit status = " +  execution2.getExitStatus());
            assertObjEquals("nullBeginCABeforeAfter", execution2.getExitStatus());
            
        } catch (Exception e) {
            handleException(METHOD, e);
        }
    }

    
    /*
     * @testName: testPartitionedMapperOverrideTrueDiffPartitionNumOnRestart
     * 
     * @assertion: 
     * 
     * @test_Strategy: 
     */
    
    
    public void testPartitionedMapperOverrideTrueDiffPartitionNumOnRestart() throws Fault {
        String METHOD = "testPartitionedMapperOverrideFalse";
TestUtil.logTrace(METHOD);
        begin(METHOD);

        try {
            TestUtil.logMsg("Locate job XML file: job_partitioned_artifacts.xml");

            TestUtil.logMsg("Create Job parameters for Execution #1");
            Properties jobParams = new Properties();
            TestUtil.logMsg("numPartitionsProp=3");
            TestUtil.logMsg("failThisPartition=0");
            //append "CA" to expected exit status for each partition
            jobParams.setProperty("numPartitionsProp" , "2"); 
            jobParams.setProperty("failThisPartition" , "0"); //Remember we are 0 based
            jobParams.setProperty("partitionsOverride", "false");

            TestUtil.logMsg("Invoke startJobAndWaitForResult");
            JobExecution execution = jobOp.startJobAndWaitForResult("job_partitioned_artifacts", jobParams);

            TestUtil.logMsg("Execution exit status = " +  execution.getExitStatus());
            assertObjEquals("nullBeginCACARollbackAfter", execution.getExitStatus());

            TestUtil.logMsg("Execution status = " + execution.getBatchStatus());
            assertObjEquals(BatchStatus.FAILED,execution.getBatchStatus());
            
            TestUtil.logMsg("Set restart job parameters");
            jobParams.setProperty("numPartitionsProp" , "4"); 
            jobParams.setProperty("failThisPartition" , "3"); //Remember we are 0 based
            jobParams.setProperty("partitionsOverride", "true");
            
            JobExecution execution2 = jobOp.restartJobAndWaitForResult(execution.getExecutionId(), jobParams);
            TestUtil.logMsg("Execution exit status = " +  execution2.getExitStatus());
            assertObjEquals("nullBeginCACACACARollbackAfter", execution2.getExitStatus());
            
        } catch (Exception e) {
            handleException(METHOD, e);
        }
    }
    
    /*
     * @testName: testPartitionedMapperOverrideTrueSamePartitionNumOnRestart
     * 
     * @assertion: 
     * 
     * @test_Strategy: 
     */
    
    
    public void testPartitionedMapperOverrideTrueSamePartitionNumOnRestart() throws Fault {
        String METHOD = "testPartitionedMapperOverrideFalse";
TestUtil.logTrace(METHOD);
        begin(METHOD);

        try {
            TestUtil.logMsg("Locate job XML file: job_partitioned_artifacts.xml");

            TestUtil.logMsg("Create Job parameters for Execution #1");
            Properties jobParams = new Properties();
            TestUtil.logMsg("numPartitionsProp=3");
            TestUtil.logMsg("failThisPartition=0");
            //append "CA" to expected exit status for each partition
            jobParams.setProperty("numPartitionsProp" , "3"); 
            jobParams.setProperty("failThisPartition" , "0"); //Remember we are 0 based
            jobParams.setProperty("partitionsOverride", "false");

            TestUtil.logMsg("Invoke startJobAndWaitForResult");
            JobExecution execution = jobOp.startJobAndWaitForResult("job_partitioned_artifacts", jobParams);

            TestUtil.logMsg("Execution exit status = " +  execution.getExitStatus());
            assertObjEquals("nullBeginCACACARollbackAfter", execution.getExitStatus());

            TestUtil.logMsg("Execution status = " + execution.getBatchStatus());
            assertObjEquals(BatchStatus.FAILED,execution.getBatchStatus());
            
            TestUtil.logMsg("Set restart job parameters");
            jobParams.setProperty("numPartitionsProp" , "3"); 
            jobParams.setProperty("failThisPartition" , "1"); //Remember we are 0 based
            jobParams.setProperty("partitionsOverride", "true");
            
            JobExecution execution2 = jobOp.restartJobAndWaitForResult(execution.getExecutionId(), jobParams);
            TestUtil.logMsg("Execution exit status = " +  execution2.getExitStatus());
            assertObjEquals("nullBeginCACACARollbackAfter", execution2.getExitStatus());
            
        } catch (Exception e) {
            handleException(METHOD, e);
        }
    }
    
	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}
}
