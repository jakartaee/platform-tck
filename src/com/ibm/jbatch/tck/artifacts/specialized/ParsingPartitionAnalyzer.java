/**
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
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import javax.batch.api.partition.AbstractPartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.ibm.jbatch.tck.artifacts.reusable.MyParallelSubJobsExitStatusBatchlet;

@Named
public class ParsingPartitionAnalyzer extends AbstractPartitionAnalyzer {
	
    @Inject
	JobContext jobCtx;
    
    @Inject
	StepContext stepCtx;
    
    private int counter = 0;
    
	@Override
	public void analyzeStatus(BatchStatus batchStatus, String exitStatus)
			throws Exception {
	    
	    counter++;
	    
		String goodPrefix = MyParallelSubJobsExitStatusBatchlet.GOOD_EXIT_STATUS;
		int idx = goodPrefix.length() + 1;
		if (!exitStatus.startsWith(goodPrefix)) {
			throw new IllegalStateException("Expected exit status to start with: " + goodPrefix + ", but found :" + exitStatus);
		}
	
		jobCtx.setExitStatus("JOB EXIT STATUS: " + counter);
		stepCtx.setExitStatus("STEP EXIT STATUS: " + counter);
	}
}
