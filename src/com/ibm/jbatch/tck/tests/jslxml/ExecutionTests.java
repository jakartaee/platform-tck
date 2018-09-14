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


import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;

import com.ibm.jbatch.tck.utils.JobOperatorBridge;








public class ExecutionTests extends ServiceEETest {

	private final static Logger logger = Logger.getLogger(ExecutionTests.class.getName());

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

	
	
	public static void setUp() throws Fault {
		jobOp = new JobOperatorBridge();
	}

	
	public static void cleanup() throws Fault {
	}

	private void begin(String str) {
		TestUtil.logMsg("Begin test method: " + str);
	}

	/*
	 * @testName: testInvokeJobWithOneBatchletStep
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithOneBatchletStep() throws Fault {
		String METHOD = "testInvokeJobWithOneBatchletStep";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_1step.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_1step");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithTwoStepSequenceOfBatchlets
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithTwoStepSequenceOfBatchlets() throws Fault {
		String METHOD = "testInvokeJobWithTwoStepSequenceOfBatchlets";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_2steps.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_2steps");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithFourStepSequenceOfBatchlets
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithFourStepSequenceOfBatchlets() throws Fault {
		String METHOD = "testInvokeJobWithFourStepSequenceOfBatchlets";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_4steps.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_4steps");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithNextElement
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithNextElement() throws Fault {
		String METHOD = "testInvokeJobWithNextElement";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_nextElement.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_nextElement");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithFailElement
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithFailElement() throws Fault {
		String METHOD = "testInvokeJobWithFailElement";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_failElement.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_failElement");

			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("TEST_FAIL", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.FAILED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithStopElement
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithStopElement() throws Fault {
		String METHOD = "testInvokeJobWithStopElement";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_stopElement.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_stopElement");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.STOPPED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobWithEndElement
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobWithEndElement() throws Fault {
		String METHOD = "testInvokeJobWithEndElement";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_batchlet_endElement.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_batchlet_endElement");

			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals("TEST_ENDED", jobExec.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobSimpleChunk
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testInvokeJobSimpleChunk() throws Fault {
		String METHOD = "testInvokeJobSimpleChunk";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_chunk_simple.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_chunk_simple");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * testName: testInvokeJobChunkWithFullAttributes
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 * Disabling per Bug 5379 (https://java.net/bugzilla/show_bug.cgi?id=5379)
	 * New tracking Bug 26611303
	 */
	

	
	public void testInvokeJobChunkWithFullAttributes() throws Fault {
		String METHOD = "testInvokeJobChunkWithFullAttributes";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_chunk_full_attributes.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_chunk_full_attributes");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testInvokeJobUsingTCCL
	 * @assertion: Section 10.5 Batch Artifact loading
	 * @test_Strategy: Implementation should attempt to load artifact using Thread Context Class Loader if implementation specific
	 * 	and archive loading are unable to find the specified artifact.
	 */
	
	
	public void testInvokeJobUsingTCCL() throws Fault {
		String METHOD = "testInvokeJobUsingTCCL";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Run job using job XML file: test_artifact_load_classloader");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("test_artifact_load_classloader");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testCheckpoint
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testCheckpoint() throws Fault {
		String METHOD = "testCheckpoint";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_chunk_checkpoint.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_chunk_checkpoint");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testSimpleFlow
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testSimpleFlow() throws Fault {
		String METHOD = "testSimpleFlow";
TestUtil.logTrace(METHOD);
		begin(METHOD);

		try {
			TestUtil.logMsg("Locate job XML file: job_flow_batchlet_4steps.xml");

			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_flow_batchlet_4steps");

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}


	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

}
