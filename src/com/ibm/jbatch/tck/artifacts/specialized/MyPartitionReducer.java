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

import javax.batch.api.partition.PartitionReducer;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@javax.inject.Named("myPartitionReducer")
public class MyPartitionReducer implements PartitionReducer {

    
    String history;
    
    @Inject
	StepContext stepCtx;
    
    @Inject
    JobContext jobCtx;
	
    @Override
	public void beginPartitionedStep() throws Exception {
		
        String exitStatus = stepCtx.getExitStatus();
        
        history = exitStatus + "Begin";
				
	}

    @Override
    public void beforePartitionedStepCompletion() throws Exception {
        String exitStatus = stepCtx.getExitStatus();
        
        stepCtx.setExitStatus(history + exitStatus + "Before");
        
    }

    @Override
    public void rollbackPartitionedStep() throws Exception {
        String exitStatus = stepCtx.getExitStatus();
        
        stepCtx.setExitStatus(history + exitStatus + "Rollback");
        
    }

    @Override
    public void afterPartitionedStepCompletion(PartitionStatus status) throws Exception {
        String exitStatus = stepCtx.getExitStatus();
        
        stepCtx.setExitStatus(exitStatus + "After");
        
        jobCtx.setExitStatus(stepCtx.getExitStatus());
        
    }


}
