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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractCheckpointAlgorithm;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.reusable.MyPersistentRestartUserData;

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



@javax.inject.Named("myCustomCheckpointAlgorithm")
public class MyCustomCheckpointAlgorithm extends AbstractCheckpointAlgorithm {

	private static final String className = MyCustomCheckpointAlgorithm.class.getName();
	private static Logger logger  = Logger.getLogger(MyCustomCheckpointAlgorithm.class.getPackage().getName());
		
	boolean inCheckpoint = false;
	int checkpointIterations = 1;
	
   int threshold;
   long timeStarted = 0;
   int requests;
   
       @Inject 
   private StepContext stepCtx = null; 
   
       @Inject    
    @BatchProperty(name="writepoints")
   String writePointsString;
   
       @Inject    
    @BatchProperty(name="next.writepoints")
   String nextWritePointsString;
	
   int [] writePoints;
   
   boolean init = false;
   
   public void init(){
	   
	    MyPersistentRestartUserData myData = null;
        if ((myData = (MyPersistentRestartUserData)stepCtx.getPersistentUserData()) != null) {        	
        	stepCtx.setPersistentUserData(new MyPersistentRestartUserData(myData.getExecutionNumber() + 1, null));
        	logger.fine("AJM: iteration = " + ((MyPersistentRestartUserData)stepCtx.getPersistentUserData()).getExecutionNumber());
        	writePointsString = ((MyPersistentRestartUserData)stepCtx.getPersistentUserData()).getNextWritePoints();
        } else {        
        	stepCtx.setPersistentUserData(new MyPersistentRestartUserData(1, nextWritePointsString));
        }
        
	   String[] writePointsStrArr = writePointsString.split(",");
	   writePoints = new int[writePointsString.length()];
	   
		for (int i = 0; i<writePointsStrArr.length; i++){
			writePoints[i] = Integer.parseInt(writePointsStrArr[i]);
			logger.fine("CUSTOMCHKPT: writePoints[" + i + "] = " + writePoints[i]);
		}
		
		threshold = writePoints[checkpointIterations];
		requests = writePoints[0];
		
		init = true;
   }
   
	@Override
	public boolean isReadyToCheckpoint() throws Exception {
      	String method = "isReadyToCheckpoint";
      	if(logger.isLoggable(Level.FINER)) { logger.entering(className, method); }

      	if (!init){
      		
          	if (((MyPersistentRestartUserData)stepCtx.getPersistentUserData()).getExecutionNumber() == 2){
          		writePointsString = ((MyPersistentRestartUserData)stepCtx.getPersistentUserData()).getNextWritePoints();
          	}
      		
     	   String[] writePointsStrArr = writePointsString.split(",");
    	   writePoints = new int[writePointsString.length()];
    	   
    		for (int i = 0; i<writePointsStrArr.length; i++){
    			logger.fine("CUSTOMCHKPT: writePointsStrArr[" + i + "] = " + writePointsStrArr[i]);
    			writePoints[i] = Integer.parseInt(writePointsStrArr[i]);
    			logger.fine("CUSTOMCHKPT: writePoints[" + i + "] = " + writePoints[i]);
    		}
    		
    		threshold = writePoints[checkpointIterations];
    		requests = writePoints[0];
    		
    		init = true;
      	}
      	
      	requests++;
      	boolean ready = (requests >= threshold);
      	
       if ( ready) {
    	   checkpointIterations++;
    	   threshold = writePoints[checkpointIterations];
           long millis =  Long.valueOf( (new Date().getTime()) - timeStarted );
           if ( millis>0 ) { 
               String rate =  Integer.valueOf ( Long.valueOf( (requests*1000/millis) ).intValue()).toString();
               if(logger.isLoggable(Level.FINE)) { logger.fine(" - true [requests/second " + rate + "]"); }

           } else {
           	if(logger.isLoggable(Level.FINE)) { logger.fine(" - true [requests " + requests + "]"); }

           }
       }

       //if ( ready ) requests = 0;
       
       return ready;
	}

	   private class MyTransient {
	        int data = 0;
	        MyTransient(int x) {
	            data = x;
	        }   
	    }

}
