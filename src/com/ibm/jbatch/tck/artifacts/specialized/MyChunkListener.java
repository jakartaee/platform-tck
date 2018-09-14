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

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.listener.AbstractChunkListener;
import javax.batch.api.listener.StepListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.reusable.MyParentException;

/**
 * Originally this class was going to be used to check that we could view an earlier-thrown
 * exception via StepContext.getException() in each of afterChunk(), onError(Exception).
 * 
 * After a mailing list comment that we should relax the requirements on the implementation
 * to make the exception immediately visible to other artifacts, we agreed to only check
 * that we saw the expected exception in afterStep().
 * 
 * The quick change then was to recast this MyChunkListener as both a ChunkListener and
 * StepListener.  
 *
 */
@javax.inject.Named("myChunkListener")
public class MyChunkListener extends AbstractChunkListener implements StepListener {
	
	@Inject 
    StepContext stepCtx; 
	
	@Inject 
    JobContext jobCtx; 
	
    @Inject    
    @BatchProperty(name="fail.immediate")
    String failImmediateString;
 
    boolean failThrowEx = false;
	
    @Override
    public void beforeChunk() throws Exception {
    	
    	if (failImmediateString!=null){
			failThrowEx = Boolean.parseBoolean(failImmediateString);
		}
    	
    	if (failThrowEx){
			throw new MyParentException("Testing getException");
		}
    }

	@Override
	public void beforeStep() throws Exception {
		// No-op
	}

	@Override
	public void afterStep() throws Exception {
    	Exception ex = stepCtx.getException();
    	if (ex == null) {
    		jobCtx.setExitStatus("MyChunkListener: error, didn't find an exception.");
    	} else if (ex instanceof MyParentException){
    		jobCtx.setExitStatus("MyChunkListener: found instanceof MyParentException");
    	} else {
    		jobCtx.setExitStatus("MyChunkListener: did not find instanceof MyParentException, but found another exception");
    	}
	}
    
}
