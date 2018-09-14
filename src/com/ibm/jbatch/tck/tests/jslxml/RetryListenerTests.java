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
import javax.batch.runtime.JobExecution;

import com.ibm.jbatch.tck.utils.JobOperatorBridge;






public class RetryListenerTests extends ServiceEETest {

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
	 * @testName: testRetryReadListener
	 * 
	 * @assertion: Test will finish in FAILED status, with the onRetryReadException invoked.
	 * 
	 * @test_Strategy: Test that the onRetryReadException listener is invoked when a retryable exception occurs on a read.
	 */
	
	
	public void testRetryReadListener() throws Fault {
		String METHOD = "testRetryReadListener";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=8,13,22");
			TestUtil.logMsg("app.arraysize=30");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "8,13,22");
			jobParams.put("app.arraysize", "30");

			TestUtil.logMsg("Locate job XML file: job_retry_listener_test.xml");



			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_retry_listener_test",jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()=" + jobExec.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()=" + jobExec.getExitStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, jobExec.getBatchStatus());
			assertWithMessage("Testing execution #1", "Retry listener invoked", jobExec.getExitStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testRetryProcessListener
	 * 
	 * @assertion: Test will finish in FAILED status, with the onRetryProcessException invoked.
	 * 
	 * @test_Strategy: Test that the onRetryProcessException listener is invoked when a retryable exception occurs on a process.
	 */
	
	
	public void testRetryProcessListener() throws Fault {
		String METHOD = "testRetryProcessListener";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("processrecord.fail=8,13,22");
			TestUtil.logMsg("app.arraysize=30");
			jobParams.put("execution.number", "1");
			jobParams.put("processrecord.fail", "8,13,22");
			jobParams.put("app.arraysize", "30");

			TestUtil.logMsg("Locate job XML file: job_retry_listener_test.xml");



			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_retry_listener_test",jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()=" + jobExec.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()=" + jobExec.getExitStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, jobExec.getBatchStatus());
			assertWithMessage("Testing execution #1", "Retry listener invoked", jobExec.getExitStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testRetryWriteListener
	 * 
	 * @assertion: Test will finish in FAILED status, with the onRetryWriteException invoked.
	 * 
	 * @test_Strategy: Test that the onRetryWriteException listener is invoked when a retryable exception occurs on a write.
	 */
	
	
	public void testRetryWriteListener() throws Fault {
		String METHOD = "testRetryWriteListener";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("writerecord.fail=8,13,22");
			TestUtil.logMsg("app.arraysize=30");
			jobParams.put("execution.number", "1");
			jobParams.put("writerecord.fail", "8,13,22");
			jobParams.put("app.arraysize", "30");


			TestUtil.logMsg("Locate job XML file: job_retry_listener_test.xml");



			TestUtil.logMsg("Invoking startJobAndWaitForResult for Execution #1");
			JobExecution jobExec = jobOp.startJobAndWaitForResult("job_retry_listener_test",jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()=" + jobExec.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()=" + jobExec.getExitStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.FAILED, jobExec.getBatchStatus());
			assertWithMessage("Testing execution #1", "Retry listener invoked", jobExec.getExitStatus());
		} catch(Exception e) {
			handleException(METHOD, e);
		}
	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}

}