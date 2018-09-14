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
package com.ibm.jbatch.tck.utils;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.Date;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;

public class TCKJobExecutionWrapper implements TCKJobExecution {

	private JobExecution jobExecution = null;
	private JobOperator jobOp = null;
	
	public TCKJobExecutionWrapper(JobExecution jobExecution, JobOperator jobOp) {
		this.jobExecution = jobExecution;
		this.jobOp = jobOp;
	}
	
	@Override
	public long getExecutionId() {
		return jobExecution.getExecutionId();		
	}

	@Override
	public String getJobName() {
		return jobExecution.getJobName();
	}

	@Override
	public BatchStatus getBatchStatus() {
		return jobExecution.getBatchStatus();
	}

	@Override
	public Date getStartTime() {		
		return jobExecution.getStartTime();
	}

	@Override
	public Date getEndTime() {
		return jobExecution.getEndTime();
	}

	@Override
	public String getExitStatus() {
		return jobExecution.getExitStatus();
	}

	@Override
	public Date getCreateTime() {
		return jobExecution.getCreateTime();
	}

	@Override
	public Date getLastUpdatedTime() {
		return jobExecution.getLastUpdatedTime();
	}

	@Override
	public Properties getJobParameters() {
		return jobExecution.getJobParameters();
	}

	@Override
	public long getInstanceId() throws NoSuchJobExecutionException, JobSecurityException {
		long jobExecutionId = jobExecution.getExecutionId();
		JobInstance jobInstance = jobOp.getJobInstance(jobExecutionId);
		return jobInstance.getInstanceId();		
	}

}
