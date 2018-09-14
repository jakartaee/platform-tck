/**
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
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.io.Serializable;

import javax.batch.api.partition.AbstractPartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.reusable.ExternalizableString;

@javax.inject.Named
public class MyPartitionAnalyzer extends AbstractPartitionAnalyzer {

	private volatile String analyzedData = "";
	
	private volatile String analyzedStatus = "";
	
    @Inject
	JobContext jobCtx;
		
    @Inject
	StepContext stepCtx;
	
	@Override 
	public void analyzeCollectorData(Serializable data) throws Exception {
		
		analyzedData = analyzedData  + ((ExternalizableString)data).getString() + "A";
		 
	}
	
	@Override 
	public void analyzeStatus(BatchStatus batchStatus, String exitStatus)throws Exception {
	    analyzedStatus = analyzedStatus  + "S";
	    
	    //If this method is called the partition is complete. So we should expect analyzedData to 
	    //have a 'CA' for each completed partition. 
	    
	    String expectedString = "";
	    
	    for (int i = 0; i < analyzedStatus.length(); i++){
	        expectedString = expectedString + "CA";
	    }
	    
	    if (!analyzedData.startsWith(expectedString)) {
	        throw new Exception("analyzeStatus was called at an unexpected time. Expected String to have at least=" +expectedString + " ActualData=" + analyzedData);
	    }
	    
	    
		stepCtx.setExitStatus(analyzedData);
		
	}

}
