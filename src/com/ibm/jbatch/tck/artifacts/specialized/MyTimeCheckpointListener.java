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

import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.listener.AbstractChunkListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;



@javax.inject.Named("myTimeCheckpointListener")
public class MyTimeCheckpointListener extends AbstractChunkListener {
    
    private final static String sourceClass = MyCustomCheckpointListener.class.getName();
    private final static Logger logger = Logger.getLogger(sourceClass);
    
    java.util.Date date;
    long ts;
    int timeinterval;
    boolean init=true;
    
    long startTime = 0;
    long endTime = 0;
    
    @Inject    
    @BatchProperty(name="timeinterval")
    String timeintervalString;
    
    @Inject 
    JobContext jobCtx;
    
    @Inject
    StepContext stepCtx;

    public MyTimeCheckpointListener(){

    }
    
    @Override
    public void beforeChunk() {
    	logger.fine("CHUNKLISTENER: beforeChunk");
    	
    	timeinterval = Integer.parseInt(timeintervalString);
    	
    		logger.fine("CHUNKLISTENER: got the timeinterval: " + timeinterval);
    		logger.fine("CHUNKLISTENER: startTime: " + startTime);
    		logger.fine("CHUNKLISTENER: endTime: " + endTime);
    		

        
        long curdiff = endTime - startTime;
        logger.fine("CHUNKLISTENER: curdiff: " + curdiff);
        int diff = 0;
        if (curdiff == 0){
        	diff = 0;
        }
        else {
        	diff = (int)((int)endTime - startTime)/1000;
        }
        
        logger.fine("AJM: time diff =" + diff);
        
        if ((diff >= timeinterval-1) && (diff <= timeinterval+1)) {
			logger.fine("CHUNKLISTENER: the checkpoint is occuring at the correct time -> " + diff + " which is: " + timeinterval + " +/- 1 second");
			jobCtx.setExitStatus("TRUE: " + diff);
		}
		else {
			logger.fine("CHUNKLISTENER: checkpoint outside the window surrounding the time interval of " + timeinterval);
			jobCtx.setExitStatus("FALSE: " + diff);
			//throw new Exception("WRITE: the chunk write did not occur at the correct time boundry -> "+ diff + " which is: " + timeinterval + "+/- 1 second");
		}
		        
        startTime = System.currentTimeMillis();    	
    }
    
   
    @Override
    public void afterChunk() {
    	logger.fine("CHUNKLISTENER: afterChunk");
    	
    	date = new java.util.Date();
        ts = date.getTime();
        
        endTime = System.currentTimeMillis();
    }
    
    @Override
    public void onError(Exception e) {
    	logger.fine("CHUNKLISTENER: onError");
    }

}