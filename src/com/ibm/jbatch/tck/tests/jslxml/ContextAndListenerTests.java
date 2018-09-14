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







public class ContextAndListenerTests extends ServiceEETest {

	private final static Logger logger = Logger.getLogger(ContextAndListenerTests.class.getName());
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

	/*
	 * @testName: testExamineJobContextInArtifact
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testExamineJobContextInArtifact() throws Fault {

		String METHOD = "testExamineJobContextInArtifact()";
TestUtil.logTrace(METHOD);

		try {

			TestUtil.logMsg("Locate job XML file: oneArtifactIsJobAndStepListener.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("app.timeinterval=10");
			jobParams.put("app.timeinterval", "10");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("JobContextTestBatchlet", jobParams);
		
			
			String testString = "JobName=job1;JobInstanceId=" + jobOp.getJobInstance(execution1.getExecutionId()).getInstanceId() + ";JobExecutionId=" + execution1.getExecutionId();
			TestUtil.logMsg("EXPECTED JobExecution getBatchStatus()=COMPLETED");
			TestUtil.logMsg("ACTUAL JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("EXPECTED JobExecution getExitStatus()="+testString);
			TestUtil.logMsg("ACTUAL JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing batch status", BatchStatus.COMPLETED, execution1.getBatchStatus());
			assertWithMessage("Testing exit status", testString, execution1.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}
	
	/*
	 * @testName: testExamineStepContextInArtifact
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testExamineStepContextInArtifact() throws Fault {

		String METHOD = "testExamineStepContextInArtifact()";
TestUtil.logTrace(METHOD);

		try {

			TestUtil.logMsg("Locate job XML file: oneArtifactIsJobAndStepListener.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("app.timeinterval=10");
			jobParams.put("app.timeinterval", "10");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("StepContextTestBatchlet", jobParams);
		
			List<StepExecution> steps = jobOp.getStepExecutions(execution1.getExecutionId());
			
			assertWithMessage("list of step executions == 1", steps.size() == 1);
			
			String testString = "StepName=step1;StepExecutionId=" + steps.get(0).getStepExecutionId();
			TestUtil.logMsg("EXPECTED JobExecution getBatchStatus()=COMPLETED");
			TestUtil.logMsg("ACTUAL JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("EXPECTED JobExecution getExitStatus()="+testString);
			TestUtil.logMsg("ACTUAL JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing batch status", BatchStatus.COMPLETED, execution1.getBatchStatus());
			assertWithMessage("Testing exit status", testString, execution1.getExitStatus());
			
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}
	
	/*
	 * @testName: testOneArtifactIsJobAndStepListener
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testOneArtifactIsJobAndStepListener() throws Fault {

		String METHOD = "testOneArtifactIsJobAndStepListener";
TestUtil.logTrace(METHOD);

		try {
			String expectedStr = "BeforeJob" + 
					"BeforeStep" + "UnusedExitStatusForPartitions" + "AfterStep" +
					"BeforeStep" + "UnusedExitStatusForPartitions" + "AfterStep" + 
					"AfterJob";

			TestUtil.logMsg("Locate job XML file: oneArtifactIsJobAndStepListener.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("app.timeinterval=10");
			jobParams.put("app.timeinterval", "10");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("oneArtifactIsJobAndStepListener", jobParams);

			TestUtil.logMsg("EXPECTED JobExecution getBatchStatus()=COMPLETED");
			TestUtil.logMsg("ACTUAL JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("EXPECTED JobExecution getExitStatus()="+expectedStr);
			TestUtil.logMsg("ACTUAL JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing batch status", BatchStatus.COMPLETED, execution1.getBatchStatus());
			assertWithMessage("Testing exit status", expectedStr, execution1.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testgetException
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testgetException() throws Fault {

		String METHOD = "testgetException";
TestUtil.logTrace(METHOD);

		try {
			String expectedStr = "MyChunkListener: found instanceof MyParentException";

			TestUtil.logMsg("Locate job XML file: oneArtifactIsJobAndStepListener.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("fail.immediate=true");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("job_chunk_getException", jobParams);

			TestUtil.logMsg("EXPECTED JobExecution getBatchStatus()=FAILED");
			TestUtil.logMsg("ACTUAL JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("EXPECTED JobExecution getExitStatus()="+expectedStr);
			TestUtil.logMsg("ACTUAL JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing batch status", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing exit status", expectedStr, execution1.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testgetExceptionListenerBased
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testgetExceptionListenerBased() throws Fault {

		String METHOD = "testgetExceptionListenerBased";
TestUtil.logTrace(METHOD);

		try {
			String expectedStr = "MyChunkListener: found instanceof MyParentException";

			TestUtil.logMsg("Locate job XML file: oneArtifactIsJobAndStepListener.xml");

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("fail.immediate=true");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("job_chunk_getExceptionListeners", jobParams);

			TestUtil.logMsg("EXPECTED JobExecution getBatchStatus()=FAILED");
			TestUtil.logMsg("ACTUAL JobExecution getBatchStatus()="+execution1.getBatchStatus());
			TestUtil.logMsg("EXPECTED JobExecution getExitStatus()="+expectedStr);
			TestUtil.logMsg("ACTUAL JobExecution getExitStatus()="+execution1.getExitStatus());
			assertWithMessage("Testing batch status", BatchStatus.FAILED, execution1.getBatchStatus());
			assertWithMessage("Testing exit status", expectedStr, execution1.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

    /*
     * @testName: testJobContextIsUniqueForMainThreadAndPartitions
     * 
     * @assertion: FIXME
     * 
     * @test_Strategy: FIXME
     */
    
    
    public void testJobContextIsUniqueForMainThreadAndPartitions() throws Fault {

        String METHOD = "testJobContextIsUniqueForMainThreadAndPartitions";
TestUtil.logTrace(METHOD);
        begin(METHOD);

        try {
            TestUtil.logMsg("Locate job XML file: job_partitioned_1step.xml");

            TestUtil.logMsg("Invoke startJobAndWaitForResult");
            JobExecution jobExecution = jobOp.startJobAndWaitForResult("job_partitioned_1step");

            TestUtil.logMsg("JobExecution getBatchStatus()="+jobExecution.getBatchStatus());
            assertObjEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
            assertObjEquals("COMPLETED", jobExecution.getExitStatus());
            
        } catch (Exception e) {
            handleException(METHOD, e);
        }
    }
	
    /*
     * @testName: testJobContextIsUniqueForMainThreadAndFlowsInSplits
     * 
     * @assertion: FIXME
     * 
     * @test_Strategy: FIXME
     */
    
    
    public void testJobContextIsUniqueForMainThreadAndFlowsInSplits() throws Fault {

        String METHOD = "testJobContextIsUniqueForMainThreadAndFlowsInSplits";
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
     * @testName: testStepContextIsUniqueForMainThreadAndPartitions
     * 
     * @assertion: FIXME
     * 
     * @test_Strategy: FIXME
     */
    
    
    public void testStepContextIsUniqueForMainThreadAndPartitions() throws Fault {
        String METHOD = "testStepContextIsUniqueForMainThreadAndPartitions";
TestUtil.logTrace(METHOD);
        begin(METHOD);

        try {
            TestUtil.logMsg("Locate job XML file: job_partitioned_1step.xml");

            TestUtil.logMsg("Invoke startJobAndWaitForResult");
            JobExecution jobExecution = jobOp.startJobAndWaitForResult("job_partitioned_1step");

            TestUtil.logMsg("JobExecution getBatchStatus()=" + jobExecution.getBatchStatus() + "");
            
            assertObjEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
            
            List<StepExecution> stepExecs = jobOp.getStepExecutions(jobExecution.getExecutionId());
            
            //only one step in job
            StepExecution stepExec = stepExecs.get(0);
            
            //verify step context is defaulted because it was never set on main thread.
            assertObjEquals("COMPLETED", stepExec.getExitStatus());
            
        } catch (Exception e) {
            handleException(METHOD, e);
        }
    }

    
    public static void cleanup() throws Fault {
    }

    private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

    private void begin(String str) {
        TestUtil.logMsg("Begin test method: " + str + "");
    }
}
