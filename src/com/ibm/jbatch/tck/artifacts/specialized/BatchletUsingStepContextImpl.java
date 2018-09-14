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
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.reusable.MyPersistentUserData;

@javax.inject.Named("batchletUsingStepContextImpl")
public class BatchletUsingStepContextImpl extends AbstractBatchlet{

	private final static Logger logger = Logger.getLogger(BatchletUsingStepContextImpl.class.getName());
	
    @Inject 
    private StepContext stepCtx = null; 

    @Inject 
    private JobContext jobCtx = null; 
    
    private String BEGAN = "MadeItToBegin";
    private String CANCEL = "Cancelled";
    private String PROCESSED = "Processed";
    
    
    @Inject    
    @BatchProperty(name="force.failure")
    String forceFailureProp;
    private boolean forceFailure = false;
    
        
    
    private void begin() throws Exception {
        logger.fine("BatchletUsingStepContextImpl - @BeginStep");
        assert stepCtx.getExitStatus()==null;
        stepCtx.setExitStatus(BEGAN);
        
        if ("true".equalsIgnoreCase(forceFailureProp)) {
        	forceFailure = true;
        }
    }

    public static String GOOD_STEP_EXIT_STATUS = "VERY GOOD INVOCATION";
    public static String GOOD_JOB_EXIT_STATUS = "JOB: " + GOOD_STEP_EXIT_STATUS;

    @Override
    public String process() throws Exception {
    	this.begin();   	
    	
        logger.fine("BatchletUsingStepContextImpl - @Process");		
        assert stepCtx.getExitStatus().equals(BEGAN);
        
        MyPersistentUserData myData = null;
        if ((myData = (MyPersistentUserData)stepCtx.getPersistentUserData()) != null) {
        	if (forceFailure){
        		forceFailure = false;
        		stepCtx.setPersistentUserData(new MyPersistentUserData(myData.getData() + 1, forceFailure));
        	}
        } else {        
        	if (forceFailure){
        		stepCtx.setPersistentUserData(new MyPersistentUserData(4, forceFailure));
        	}
        }
        stepCtx.setTransientUserData(new MyTransient(3));
        stepCtx.setExitStatus(PROCESSED);
        end();
        
        if (forceFailure) {
        	throw new Exception("Fail on purpose in BatchletUsingStepContextImpl.process()");
        }
        
        return BatchStatus.COMPLETED.name();
    }

    @Override
    public void stop() throws Exception {
        logger.fine("BatchletUsingStepContextImpl - @Cancel");		
        stepCtx.setExitStatus(CANCEL);
    }

    private void end() throws Exception {
        logger.fine("BatchletUsingStepContextImpl - formerly @EndStep");
        MyPersistentUserData p = (MyPersistentUserData)stepCtx.getPersistentUserData();
        MyTransient t = (MyTransient)stepCtx.getTransientUserData();
        
        assert stepCtx.getExitStatus().equals(PROCESSED);
        stepCtx.setExitStatus(GOOD_STEP_EXIT_STATUS);
        jobCtx.setExitStatus(GOOD_JOB_EXIT_STATUS);
    }
    
    private class MyTransient {
        int data = 0;
        MyTransient(int x) {
            data = x;
        }   
    }
    


}

