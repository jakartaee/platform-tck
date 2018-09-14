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

import com.ibm.jbatch.tck.artifacts.specialized.BatchletUsingStepContextImpl;
import com.ibm.jbatch.tck.utils.JobOperatorBridge;






public class ExecuteTests extends ServiceEETest {

	private final static Logger logger = Logger.getLogger(ExecuteTests.class.getName());
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

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

	/*
	 * @testName: testMyStepContextBatchlet
	 * @assertion: FIXME
	 * @test_Strategy: FIXME
	 */
	
	
	public void testMyStepContextBatchlet() throws Fault { 

		String METHOD = "testMyStepContextBatchlet";
TestUtil.logTrace(METHOD);

		try {

			TestUtil.logMsg("Invoke startJobAndWaitForResult");

			JobExecution jobExec = jobOp.startJobAndWaitForResult("test_batchlet_stepCtx"); 

			TestUtil.logMsg("EXPECTED JobExecution getExitStatus()="+BatchletUsingStepContextImpl.GOOD_JOB_EXIT_STATUS);
			TestUtil.logMsg("ACTUAL JobExecution getExitStatus()="+jobExec.getExitStatus());
			TestUtil.logMsg("EXPECTED JobExecution getBatchStatus()=COMPLETED");
			TestUtil.logMsg("ACTUAL JobExecution getBatchStatus()="+jobExec.getBatchStatus());
			assertObjEquals(BatchletUsingStepContextImpl.GOOD_JOB_EXIT_STATUS, jobExec.getExitStatus());
			assertObjEquals(BatchStatus.COMPLETED, jobExec.getBatchStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

}
