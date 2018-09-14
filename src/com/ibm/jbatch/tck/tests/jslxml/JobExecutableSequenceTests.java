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

public class JobExecutableSequenceTests extends ServiceEETest {

	private JobOperatorBridge jobOp = null;

	/**
	 * @testName: testJobExecutableSequenceToUnknown
	 * @assertion: Section 5.3 Flow
	 * @test_Strategy: 1. setup a job consisting of 3 steps (step1 next to step2, step2 next to unknown, step3 unreachable
	 * 				   2. start job 
	 * 				   3. job should fail because it shouldn't be able to transition to unknown
	 * 
	 * @throws JobStartException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	
	public void testJobExecutableSequenceToUnknown() throws Fault {

		String METHOD = "testJobExecutableSequenceToUnknown";
TestUtil.logTrace(METHOD);

		try {

			TestUtil.logMsg("starting job");
			JobExecution jobExec = null;
			boolean seenException = false;
			try {
				jobExec = jobOp.startJobAndWaitForResult("job_executable_sequence_invalid", null);
			} catch (JobStartException e) {
				TestUtil.logMsg("Caught JobStartException:  " + e.getLocalizedMessage());
				seenException = true;
			}
			// If we caught an exception we'd expect that a JobExecution would not have been created,
			// though we won't validate that it wasn't created.  
			// If we didn't catch an exception that we require that the implementation fail the job execution.
			if (!seenException) {
				TestUtil.logMsg("Didn't catch JobStartException, Job Batch Status = " + jobExec.getBatchStatus());
				assertWithMessage("Job should have failed because of out of scope execution elements.", BatchStatus.FAILED, jobExec.getBatchStatus());
			}
			TestUtil.logMsg("job failed");
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
