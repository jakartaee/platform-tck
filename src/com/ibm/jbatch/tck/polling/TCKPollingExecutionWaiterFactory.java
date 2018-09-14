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
package com.ibm.jbatch.tck.polling;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import java.lang.IllegalStateException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.ibm.jbatch.tck.spi.JobExecutionWaiter;
import com.ibm.jbatch.tck.spi.JobExecutionWaiterFactory;
import com.ibm.jbatch.tck.spi.JobExecutionTimeoutException;

public class TCKPollingExecutionWaiterFactory implements JobExecutionWaiterFactory {

    private final static String sourceClass = TCKPollingExecutionWaiterFactory.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);
    
	private final int POLL_INTERVAL = 100; // .1 second

	/**
	 * This implementation does no pooling of any kind, it just creates a new instance with new thread each time.
	 * 
	 * @param executionId
	 * @param JobOperator 
	 * @param timeout In milliseconds
	 * @return JobExecutionWaiter
	 */
	@Override
	public JobExecutionWaiter createWaiter(long executionId, JobOperator jobOp, long timeout) {
		return new TCKPollingExecutionWaiter(executionId, jobOp, timeout);
	}

	private class TCKPollingExecutionWaiter implements JobExecutionWaiter {

		private long executionId;
		private JobOperator jobOp;
		private long timeout;

		private TCKPollingExecutionWaiter(long executionId, JobOperator jobOp, long timeout) {
			logger.fine("Creating waiter for executionId = " + executionId + ", jobOp = " + jobOp + ", timeout = " + timeout);
			this.executionId = executionId;
			this.jobOp = jobOp;
			this.timeout = timeout;			
		}

		@Override

		/**
		 * Wait for
		 *   1) BatchStatus to be one of: STOPPED ,FAILED , COMPLETED, ABANDONED
		 *     AND 
		 *   2) exitStatus to be non-null
		 * @return JobExceution
		 */
		public JobExecution awaitTermination() throws JobExecutionTimeoutException {
			logger.fine("Entering awaitTermination for executionId = " + executionId);
			JobExecution jobExecution = null;
			
			long startTime = System.currentTimeMillis();
			
			while (true) {				
				try {
					logger.finer("Sleeping for " + POLL_INTERVAL);
					long curTime = System.currentTimeMillis();
					timeOutIfExpired(startTime, curTime);
					Thread.sleep(POLL_INTERVAL);
					logger.finer("Wake up, check for termination.");
					 jobExecution = jobOp.getJobExecution(executionId);
					if (isTerminated(jobExecution)) {
						break;
					}
				} catch (InterruptedException e) {
					throw new IllegalStateException("Aborting on interrupt", e);
				} catch (JobSecurityException e) {
					throw new IllegalStateException("Aborting on security (authorization) exception", e);
				} catch (NoSuchJobExecutionException e) {
					throw new IllegalStateException("JobExecution disappeared for exec id =" + executionId);
				}
			}
			return jobExecution;
		}

		private boolean isTerminated(JobExecution jobExecution) {
			boolean retVal = false;
			BatchStatus bs = jobExecution.getBatchStatus();
			if (terminatedStatuses.contains(bs)) {
				logger.fine("Found terminating batch status of: " + jobExecution.getBatchStatus().name());
				if (jobExecution.getExitStatus() != null) {
					logger.fine("Found exit status of: " + jobExecution.getExitStatus());
					retVal = true;
				} else {
					logger.fine("Exit status is still 'null'.  Poll again.");
					retVal = false;
				}
			} else {
				logger.finer("Found non-terminating batch status of: " + jobExecution.getBatchStatus().name());
				retVal = false;
			}
			return retVal;
		}
		private void timeOutIfExpired(long startTime, long curTime) {
			long diff = curTime - startTime;
			if (diff > timeout) {
				logger.warning("Timed out waiting for TCK Job Execution to reach terminated status.  Time elapsed (long msec) = " + diff + ", and timeout = " + timeout);
				throw new IllegalStateException("Timed out waiting for TCK Job Execution to reach terminated status.  Time elapsed (long msec) = " + diff + ", and timeout = " + timeout);
			} else {
				logger.finer("Still waiting for TCK Job Execution to reach terminated status.  Time elapsed (long msec) = " + diff + ", and timeout = " + timeout);
			}
		}


	}
	// Full list:
	//public enum BatchStatus {STARTING, STARTED, STOPPING, STOPPED, FAILED, COMPLETED, ABANDONED }
	private static Set<BatchStatus> terminatedStatuses = new HashSet<BatchStatus>();
	static {
		terminatedStatuses.add(BatchStatus.STOPPED);
		terminatedStatuses.add(BatchStatus.FAILED);
		terminatedStatuses.add(BatchStatus.COMPLETED);
		terminatedStatuses.add(BatchStatus.ABANDONED);
	}
	
}
