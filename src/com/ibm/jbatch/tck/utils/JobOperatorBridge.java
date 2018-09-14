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
package com.ibm.jbatch.tck.utils;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.operations.JobExecutionAlreadyCompleteException;
import javax.batch.operations.JobExecutionIsRunningException;
import javax.batch.operations.JobExecutionNotMostRecentException;
import javax.batch.operations.JobExecutionNotRunningException;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobRestartException;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.operations.NoSuchJobInstanceException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.StepExecution;



import com.ibm.jbatch.tck.spi.JobExecutionWaiter;
import com.ibm.jbatch.tck.spi.JobExecutionWaiterFactory;
import com.ibm.jbatch.tck.spi.JobExecutionTimeoutException;

public class JobOperatorBridge {

	public static final String DEFAULT_JOB_OPERATOR_SLEEP_TIME = "60000";

	private final static Logger logger = Logger.getLogger(JobOperatorBridge.class.getName());
	
	private JobOperator jobOp = BatchRuntime.getJobOperator();
	private JobExecutionWaiterFactory waiterFactory = ServiceGateway.getJobExecutionWaiterFactoryService();

	private int sleepTime = Integer.parseInt(System.getProperty("tck.execution.waiter.timeout", DEFAULT_JOB_OPERATOR_SLEEP_TIME));
	private final String TIMEOUT_MSG = "Test failure due to timeout exception.  Either the timeout should be increased and there is nothing else wrong, " 
	                 + "or perhaps the runtime implementation is handing and/or unresponsive<p>";
	public JobOperatorBridge() {
		super();        
	}

	public List<String> getJobNames() throws JobSecurityException {
		return new ArrayList<String>(jobOp.getJobNames());
	}

	public int getJobInstanceCount(String jobName) throws NoSuchJobException, JobSecurityException {
		return jobOp.getJobInstanceCount(jobName);
	}

	public List<Long> getRunningExecutions(String jobName) throws NoSuchJobException, JobSecurityException {
		return jobOp.getRunningExecutions(jobName);
	}

	public List<JobExecution> getJobExecutions(JobInstance instance) throws NoSuchJobInstanceException, JobSecurityException {
		return jobOp.getJobExecutions(instance);
	}

	public TCKJobExecutionWrapper restartJobAndWaitForResult(long oldExecutionId, Properties restartJobParameters) throws NoSuchJobExecutionException, NoSuchJobException, JobRestartException, JobExecutionAlreadyCompleteException, JobExecutionNotMostRecentException, JobSecurityException, JobExecutionTimeoutException {    	

		JobExecution terminatedJobExecution = null;
		long newExecutionId = jobOp.restart(oldExecutionId, restartJobParameters);

		JobExecutionWaiter waiter = waiterFactory.createWaiter(newExecutionId, jobOp, sleepTime);

		try {
			terminatedJobExecution = waiter.awaitTermination();
		} catch (JobExecutionTimeoutException e) {
			logger.severe(TIMEOUT_MSG);
			TestUtil.logMsg(TIMEOUT_MSG);
			throw e;
		}									

		return new TCKJobExecutionWrapper(terminatedJobExecution, jobOp);
	}
	
	public TCKJobExecutionWrapper restartJobWithoutWaitingForResult(long oldExecutionId, Properties jobParameters) throws NoSuchJobExecutionException, NoSuchJobException, JobRestartException, JobExecutionAlreadyCompleteException, JobExecutionNotMostRecentException, JobSecurityException, JobExecutionTimeoutException {
		Long execID = (Long)jobOp.restart(oldExecutionId, jobParameters);
		JobExecution jobExecution = jobOp.getJobExecution(execID);
		return new TCKJobExecutionWrapper(jobExecution, jobOp);
	}

	public void abandonJobExecution(long executionId) throws NoSuchJobInstanceException, JobExecutionIsRunningException, JobSecurityException, NoSuchJobExecutionException {
		jobOp.abandon(executionId);        
	}

	public TCKJobExecutionWrapper startJobAndWaitForResult(String jobName) throws JobStartException, NoSuchJobExecutionException, JobSecurityException, JobExecutionTimeoutException {
		return startJobAndWaitForResult(jobName, null);
	}

	public TCKJobExecutionWrapper startJobWithoutWaitingForResult(String jobName, Properties jobParameters) throws JobStartException, NoSuchJobExecutionException, JobSecurityException {
		Long execID = (Long)jobOp.start(jobName, jobParameters);
		JobExecution jobExecution = jobOp.getJobExecution(execID);
		return new TCKJobExecutionWrapper(jobExecution, jobOp);
	}

	public void stopJobWithoutWaitingForResult(long jobInstanceId) throws NoSuchJobExecutionException, JobExecutionNotRunningException, JobSecurityException {
		jobOp.stop(jobInstanceId);
	}

	/*
	 * I haven't mentally proven it to myself but I'm assuming this can ONLY be used
	 * after startJobWithoutWaitingForResult(), not after startJobAndWaitForResult().
	 */
	public JobExecution stopJobAndWaitForResult(JobExecution jobExecution) throws NoSuchJobExecutionException, JobExecutionNotRunningException, JobSecurityException, JobExecutionTimeoutException {
		
		JobExecution terminatedJobExecution = null;
		jobOp.stop(jobExecution.getExecutionId());

		JobExecutionWaiter waiter = waiterFactory.createWaiter(jobExecution.getExecutionId(), jobOp, sleepTime);

		try {
			terminatedJobExecution = waiter.awaitTermination();
		} catch (JobExecutionTimeoutException e) {
			logger.severe(TIMEOUT_MSG);
			TestUtil.logMsg(TIMEOUT_MSG);
			throw e;
		}									

		return new TCKJobExecutionWrapper(terminatedJobExecution, jobOp);
	}


	public TCKJobExecutionWrapper startJobAndWaitForResult(String jobName, Properties jobParameters) throws JobStartException, NoSuchJobExecutionException, JobSecurityException, JobExecutionTimeoutException{
		JobExecution terminatedJobExecution = null;
		long executionId = jobOp.start(jobName, jobParameters);

		JobExecutionWaiter waiter = waiterFactory.createWaiter(executionId, jobOp, sleepTime);

		try {
			terminatedJobExecution = waiter.awaitTermination();
		} catch (JobExecutionTimeoutException e) {
			logger.severe(TIMEOUT_MSG);
			TestUtil.logMsg(TIMEOUT_MSG);
			throw e;
		}									

		return new TCKJobExecutionWrapper(terminatedJobExecution, jobOp);
	}



	public Properties getParameters(long executionId) throws NoSuchJobInstanceException, JobSecurityException, NoSuchJobExecutionException{
		return jobOp.getParameters(executionId);
	}

	public JobInstance getJobInstance(long executionId) throws NoSuchJobExecutionException, JobSecurityException{
		return jobOp.getJobInstance(executionId);
	}

	public JobExecution getJobExecution(long executionId) throws NoSuchJobExecutionException, JobSecurityException{
		return jobOp.getJobExecution(executionId);
	}

	public List<JobInstance> getJobInstances(String jobName, int start, int end) throws NoSuchJobException, JobSecurityException {
		return jobOp.getJobInstances(jobName, start, end);
	}

	public List<StepExecution> getStepExecutions(long executionId) throws NoSuchJobExecutionException, JobSecurityException {
		return jobOp.getStepExecutions(executionId);
	}

	public void startJobWithoutWaitingForResult(String jobName) throws JobStartException, NoSuchJobExecutionException, JobSecurityException {
		startJobWithoutWaitingForResult(jobName, null);
	}

}
