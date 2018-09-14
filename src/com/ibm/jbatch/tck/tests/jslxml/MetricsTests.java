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

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;






import com.ibm.jbatch.tck.artifacts.specialized.MetricsStepListener;
import com.ibm.jbatch.tck.utils.JobOperatorBridge;

public class MetricsTests extends ServiceEETest {

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
	public void cleanup() {

	}

	/*
	 * Obviously would be nicer to have more granular tests for some of this
	 * function, but here we're going a different route and saying, if it's
	 * going to require restart it will have some complexity, so let's test a
	 * few different functions in one longer restart scenario.
	 */

	/*
	 * @testName: testMetricsInApp
	 * 
	 * 
	 * @assertion: Section 7.1 Job Metrics - Ensure Metrics are available to Batch Artifacts during job execution
	 * @test_Strategy: Batch Artifact reads a known number of items - test that those reads are reflected 
	 *                 in the read count and accessible at job execution time to the Batch Artifact
	 * 
	 */
	
	
	public void testMetricsInApp() throws Fault {
		String METHOD = "testMetricsInApp";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=40");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.chunksize=7");
			TestUtil.logMsg("app.commitinterval=10");
			TestUtil.logMsg("numberOfSkips=0");
			TestUtil.logMsg("ReadProcessWrite=READ");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "40");
			jobParams.put("app.arraysize", "30");
			jobParams.put("app.chunksize", "7");
			jobParams.put("app.commitinterval", "10");
			jobParams.put("numberOfSkips", "0");
			jobParams.put("ReadProcessWrite", "READ");
			jobParams.put("app.writepoints", "0,7,14,21,28,30");
			jobParams.put("app.next.writepoints", "7,14,21,28,30");

			TestUtil.logMsg("Locate job XML file: testChunkMetrics.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testChunkMetrics",
					jobParams);
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="
					+ execution1.getExitStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());
			assertWithMessage("Testing metrics",
					MetricsStepListener.GOOD_EXIT_STATUS_READ,
					execution1.getExitStatus());
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testMetricsSkipRead
	 * 
	 * @assertion: Section 7.1 Job Metrics - Skip Read Count
	 * @test_Strategy: Force Batch Artifact to skip a known number of reads - test that those skips are reflected in the skip read count
	 * 
	 */
	
	
	public void testMetricsSkipRead() throws Fault {

		String METHOD = "testMetricsSkipRead";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=1,3");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("numberOfSkips=2");
			TestUtil.logMsg("ReadProcessWrite=READ_SKIP");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "1,3,4,12");
			jobParams.put("app.arraysize", "30");
			jobParams.put("numberOfSkips", "4");
			jobParams.put("ReadProcessWrite", "READ_SKIP");

			TestUtil.logMsg("Locate job XML file: testMetricsSkipCount.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricsSkipCount",
					jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="
					+ execution1.getExitStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());
			assertWithMessage("Testing execution #1",
					MetricsStepListener.GOOD_EXIT_STATUS_READ,
					execution1.getExitStatus());

			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution step = null;
			String stepNameTest = "step1";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			Metric[] metrics = step.getMetrics();

			TestUtil.logMsg("Testing the read count for execution #1");
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].getType().equals(Metric.MetricType.READ_SKIP_COUNT)) {
					TestUtil.logMsg("AJM: in test, found metric: " + metrics[i].getType() + "");
					assertWithMessage(
							"Testing the read skip count for execution #1", 4L,
							metrics[i].getValue());
				}
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testMetricsSkipWrite
	 * 
	 * @assertion: Section 7.1 Job Metrics - Skip Write Count
	 * @test_Strategy: Force Batch Artifact to skip a known number of writes - test that those skips are reflected in the skip write count
	 * 
	 */
	
	
	public void testMetricsSkipWrite() throws Fault {

		String METHOD = "testMetricsSkipWrite";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=1,3");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("numberOfSkips=2");
			TestUtil.logMsg("ReadProcessWrite=WRITE_SKIP");
			jobParams.put("execution.number", "1");
			jobParams.put("writerecord.fail", "1,3,4");
			jobParams.put("app.arraysize", "30");
			jobParams.put("numberOfSkips", "3");
			jobParams.put("ReadProcessWrite", "WRITE_SKIP");

			TestUtil.logMsg("Locate job XML file: testMetricsSkipWriteCount.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricsSkipWriteCount",
					jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="
					+ execution1.getExitStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution step = null;
			String stepNameTest = "step1";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			Metric[] metrics = step.getMetrics();

			TestUtil.logMsg("Testing the write skip count for execution #1");
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].getType().equals(Metric.MetricType.WRITE_SKIP_COUNT)) {
					TestUtil.logMsg("AJM: in test, found metric: " + metrics[i].getType() + "");
					assertWithMessage(
							"Testing the write skip count for execution #1", 3L,
							metrics[i].getValue());
				}
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testMetricsSkipProcess
	 * 
	 * @assertion: Section 7.1 Job Metrics - Skip Process Count
	 * @test_Strategy: Force Batch Artifact to skip a known number of processing - test that those skips are reflected in the skip process count
	 * 
	 */
	
	
	public void testMetricsSkipProcess() throws Fault {
		String METHOD = "testMetricsSkipProcess";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=7,13");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("numberOfSkips=2");
			TestUtil.logMsg("ReadProcessWrite=PROCESS");
			jobParams.put("execution.number", "1");
			jobParams.put("processrecord.fail", "7,13");
			jobParams.put("app.arraysize", "30");
			jobParams.put("numberOfSkips", "2");
			jobParams.put("ReadProcessWrite", "PROCESS");

			TestUtil.logMsg("Locate job XML file: testMetricsSkipCount.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricsSkipCount",
					jobParams);

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="
					+ execution1.getExitStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());
			assertWithMessage("Testing execution #1",
					MetricsStepListener.GOOD_EXIT_STATUS_PROCESS,
					execution1.getExitStatus());

			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution step = null;
			String stepNameTest = "step1";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			Metric[] metrics = step.getMetrics();

			TestUtil.logMsg("Testing the read count for execution #1");
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].getType().equals(Metric.MetricType.PROCESS_SKIP_COUNT)) {
					TestUtil.logMsg("AJM: in test, found metric: " + metrics[i].getType() + "");
					assertWithMessage(
							"Testing the read count for execution #1", 2L,
							metrics[i].getValue());
				}
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testReadMetric
	 * 
	 * @assertion: Section 7.1 Job Metrics - Read Count
	 * @test_Strategy: Batch Artifact reads a known number of items - test that those reads are reflected in the read count
	 * 
	 */
	
	
	public void testReadMetric() throws Fault {
		String METHOD = "testReadMetric";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			TestUtil.logMsg("execution.number=1");
			TestUtil.logMsg("readrecord.fail=40");
			TestUtil.logMsg("app.arraysize=30");
			TestUtil.logMsg("app.chunksize=7");
			TestUtil.logMsg("app.commitinterval=10");
			TestUtil.logMsg("numberOfSkips=0");
			TestUtil.logMsg("ReadProcessWrite=READ");
			jobParams.put("execution.number", "1");
			jobParams.put("readrecord.fail", "-1");
			jobParams.put("app.arraysize", "30");

			TestUtil.logMsg("Locate job XML file: testChunkMetrics.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricCount",
					jobParams);
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="
					+ execution1.getExitStatus() + "");

			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution step = null;
			String stepNameTest = "step1Metric";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			Metric[] metrics = step.getMetrics();

			TestUtil.logMsg("Testing the read count for execution #1");
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].getType().equals(Metric.MetricType.READ_COUNT)) {
					TestUtil.logMsg("AJM: in test, found metric: " + metrics[i].getType() + "");
					assertWithMessage(
							"Testing the read count for execution #1", 9L,
							metrics[i].getValue());
				}
			}

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testWriteMetric
	 * 
	 * @assertion: Section 7.1 Job Metrics - Write Count
	 * @test_Strategy: Batch Artifact writes a known number of items - test that those writes are reflected in the write count
	 * 
	 */
	
	
	public void testWriteMetric() throws Fault {
		String METHOD = "testWriteMetric";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();

			TestUtil.logMsg("Locate job XML file: testChunkMetrics.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricCount",
					jobParams);
			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			TestUtil.logMsg("execution #1 JobExecution getExitStatus()="
					+ execution1.getExitStatus() + "");

			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution step = null;
			String stepNameTest = "step1Metric";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			Metric[] metrics = step.getMetrics();

			TestUtil.logMsg("Testing the read count for execution #1");
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].getType().equals(Metric.MetricType.WRITE_COUNT)) {
					TestUtil.logMsg("AJM: in test, found metric: " + metrics[i].getType() + "");
					assertWithMessage(
							"Testing the write count for execution #1", 9L,
							metrics[i].getValue());
				}
			}

		} catch (Exception e) {
			handleException(METHOD, e);
		}

	}

	/*
	 * @testName: testMetricsFilterCount
	 * 
	 * @assertion: Section 7.1 Job Metrics - Filter Count
	 * @test_Strategy: Batch Artifact filters a known number of items while processing - test that those filter actions are reflected in the filter count
	 * 
	 */
	
	
	public void testMetricsFilterCount() throws Fault {

		String METHOD = "testMetricsFilterCount";
TestUtil.logTrace(METHOD);

		try {

			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			jobParams.put("app.processFilterItem", "3");
			TestUtil.logMsg("app.processFilterItem=3");

			TestUtil.logMsg("Locate job XML file: testMetricsFilterCount.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricsFilterCount",
					jobParams);

			TestUtil.logMsg("Obtaining StepExecutions for execution id: "
					+ execution1.getExecutionId() + "");
			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution tempstep = null;
			StepExecution step = null;
			String stepNameTest = "step1FM";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());
			Metric[] metrics = step.getMetrics();

			TestUtil.logMsg("Testing the filter count for execution #1");
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].getType().equals(Metric.MetricType.FILTER_COUNT)) {
					assertWithMessage(
							"Testing the filter count for execution #1", 1L,
							metrics[i].getValue());
				}
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testMetricsCommitCount
	 * 
	 * @assertion: Section 7.1 Job Metrics - Commit Count
	 * @test_Strategy: Batch Artifact read/process/writes a known number of items and all are committed - test that those commits are reflected in the commit count
	 * 
	 */
	
	
	public void testMetricsCommitCount() throws Fault {

		String METHOD = "testMetricsCommitCount";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			jobParams.put("app.processFilterItem", "3");
			TestUtil.logMsg("app.processFilterItem=3");

			TestUtil.logMsg("Locate job XML file: testMetricsCommitCount.xml");

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricsCommitCount",
					jobParams);

			TestUtil.logMsg("Obtaining StepExecutions for execution id: "
					+ execution1.getExecutionId() + "");
			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution tempstep = null;
			StepExecution step = null;
			String stepNameTest = "step1CCM";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());
			Metric[] metrics = step.getMetrics();

			TestUtil.logMsg("Testing the commit count for execution #1");
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].getType().equals(Metric.MetricType.COMMIT_COUNT)) {
					assertWithMessage(
							"Testing the commit count for execution #1", 4L,
							metrics[i].getValue());
				}
			}
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}


	/*
	 * @testName: testMetricsStepTimestamps
	 * 
	 * @assertion: Section 7.1 Job Metrics - Commit Count
	 * @test_Strategy: test case records a point in time and starts a job which contains a step. test that the step start time
	 *                 occurs after the test case point in time. test that the step end time occurs after the step start time. 
	 *                 test that the step end time occurs after the test case point in time.
	 * 
	 */
	
	
	public void testMetricsStepTimestamps() throws Fault {

		String METHOD = "testMetricsStepTimestamps";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			jobParams.put("app.processFilterItem", "3");
			TestUtil.logMsg("app.processFilterItem=3");

			TestUtil.logMsg("Locate job XML file: testMetricsCommitCount.xml");
			long time = System.currentTimeMillis();
			Date ts = new Date(time);

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricsCommitCount",
					jobParams);

			TestUtil.logMsg("Obtaining StepExecutions for execution id: "
					+ execution1.getExecutionId() + "");
			List<StepExecution> stepExecutions = jobOp
					.getStepExecutions(execution1.getExecutionId());

			StepExecution tempstep = null;
			StepExecution step = null;
			String stepNameTest = "step1CCM";

			for (StepExecution stepEx : stepExecutions) {
				if (stepNameTest.equals(stepEx.getStepName())) {
					step = stepEx;
				}
			}

			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			TestUtil.logMsg("AJM: testcase start time: " + ts + "");
			TestUtil.logMsg("AJM: step start time: " + step.getStartTime() + "");
			TestUtil.logMsg("AJM: step end time: " + step.getEndTime() + "");

			assertWithMessage("Start time of test occurs approximately before start time of step", roughlyOrdered(ts, step.getStartTime()));
			assertWithMessage("Start time of step occurs approximately before end time of step", roughlyOrdered(step.getStartTime(), step.getEndTime()));
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	/*
	 * @testName: testMetricsJobExecutionTimestamps
	 * 
	 * @assertion: Section 7.1 Job Metrics - Commit Count
	 * @test_Strategy: test starts a job. Test that the start time of the testcase occurs before the start time  of the job.
	 *                 test that the job create time occurs before the job start time. test that the job end time occurs
	 *                 after the job start time. test that the last status update time occurs after the job start time.
	 *                 test that the job end time occurs after the testcase start time.
	 * 
	 */
	
	
	public void testMetricsJobExecutionTimestamps() throws Fault {

		String METHOD = "testMetricsJobExecutionTimestamps";
TestUtil.logTrace(METHOD);

		try {
			TestUtil.logMsg("Create job parameters for execution #1:");
			Properties jobParams = new Properties();
			jobParams.put("app.processFilterItem", "3");
			TestUtil.logMsg("app.processFilterItem=3");

			TestUtil.logMsg("Locate job XML file: testMetricsCommitCount.xml");

			long time = System.currentTimeMillis();
			Date ts = new Date(time);

			TestUtil.logMsg("Invoke startJobAndWaitForResult for execution #1");
			JobExecution execution1 = jobOp.startJobAndWaitForResult("testMetricsCommitCount",
					jobParams);



			TestUtil.logMsg("execution #1 JobExecution getBatchStatus()="
					+ execution1.getBatchStatus() + "");
			assertWithMessage("Testing execution #1", BatchStatus.COMPLETED,
					execution1.getBatchStatus());

			TestUtil.logMsg("AJM: testcase start time: " + ts + "");
			TestUtil.logMsg("AJM: job create time: " + execution1.getCreateTime() + "");
			TestUtil.logMsg("AJM: job start time: " + execution1.getStartTime() + "");
			TestUtil.logMsg("AJM: job last updated time: " + execution1.getLastUpdatedTime() + "");
			TestUtil.logMsg("AJM: job end time: " + execution1.getEndTime() + "");

			assertWithMessage("Start time of test occurs approximately before create time of job", roughlyOrdered(ts, execution1.getCreateTime()));
			assertWithMessage("Create time of job occurs approximately before start time of job", roughlyOrdered(execution1.getCreateTime(), execution1.getStartTime()));
			assertWithMessage("Start time of job occurs approximately before end time of job", roughlyOrdered(execution1.getStartTime(), execution1.getEndTime()));
			assertWithMessage("Start time of job occurs approximately before Last Updated time of job", roughlyOrdered(execution1.getStartTime(), execution1.getLastUpdatedTime()));
		} catch (Exception e) {
			handleException(METHOD, e);
		}
	}

	private static void handleException(String methodName, Exception e) throws Fault {
TestUtil.logErr("Caught exception: "           + e.getMessage());
TestUtil.printStackTrace(e);
throw new Fault(methodName + " failed", e);}
	
	/*
	 * We want to confirm that 'd1' is roughly before 'd2', and also to
	 * allow for the fact that dates may be stored with a loss of precision.
	 * 
	 * Let's assume then that we only have whole seconds precision (without
	 * necessarily accounting for any fractional seconds).
	 * 
	 * So we can't simply perform d1 < d2, or even d1 <= d2 (the inclusion of 'equals' 
	 * corrects for a different problem, the problem of running so fast that
	 * the times for d1 and d2 are the same even though d1 may still have
	 * been executed first).
	 * 
	 * The "worst" case (in terms of highest rounding error), then, is that 'd1' gets
	 * rounded up while 'd2' gets rounded down, leaving the rounded 'd1' value a full 
	 * second higher than the rounded 'd2' value.
	 * 
	 * Therefore we check that d2 minus d1, which before rounding should be >= 0, is
	 * instead no less than -1000 (1 second).
	 */
	private static boolean roughlyOrdered(Date d1, Date d2) {
		long time1 = d1.getTime();
		long time2 = d2.getTime();
		
		long diff = time2 - time1;
		
		return diff >= -1000 ? true : false;
	}

}
