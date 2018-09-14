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

import java.io.Serializable;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.chunktypes.ArrayIndexCheckpointData;
import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;
import com.ibm.jbatch.tck.artifacts.reusable.MyPersistentRestartUserData;

@javax.inject.Named("doSomethingArrayItemReaderImpl")
public class DoSomethingArrayItemReaderImpl  extends AbstractItemReader {
		
	private final static Logger logger = Logger.getLogger(DoSomethingArrayItemReaderImpl.class.getName());
	
	private int count = 0;
	private int[] readerDataArray;
	private int idx;
	ArrayIndexCheckpointData _cpd = new ArrayIndexCheckpointData();
	
    @Inject    
    @BatchProperty(name="readrecord.fail")
    String readrecordfailNumberString;
	
    @Inject    
    @BatchProperty(name="execution.number")
    String executionNumberString;
	
    @Inject    
    @BatchProperty(name="app.arraysize")
    String appArraySizeString;

    @Inject    
    @BatchProperty(name="app.checkpoint.position")
    String appCheckpointPositionString;

     @Inject 
	 private StepContext stepCtx = null; 
		
	int failnum;
	int execnum;
	int arraysize;
	int checkpointPosition;
	
	public DoSomethingArrayItemReaderImpl(){
			
	}
		
		@Override
		public void open(Serializable cpd) throws Exception {
						
		    ArrayIndexCheckpointData checkpointData = (ArrayIndexCheckpointData)cpd;
		    
			failnum = Integer.parseInt(readrecordfailNumberString);
			if (executionNumberString != null) {
				execnum = Integer.parseInt(executionNumberString);
			}
            
    		arraysize = Integer.parseInt(appArraySizeString);
    		readerDataArray =  new int[arraysize];
    		
    		if (appCheckpointPositionString != null) {
    			checkpointPosition = Integer.parseInt(appCheckpointPositionString);
    		}
    		
 	       //MyPersistentRestartUserData myData = null;
	        //if ((myData = stepCtx.getPersistentUserData()) != null) {        	
	        	//stepCtx.setPersistentUserData(new MyPersistentRestartUserData(myData.getExecutionNumber() + 1, null));
	        	//logger.fine("AJM: iteration = " + stepCtx.getPersistentUserData().getExecutionNumber());
	        //} else {        
	        	//stepCtx.setPersistentUserData(new MyPersistentRestartUserData(1, null));
	        //}
    		
    		for (int i = 0; i<arraysize; i++){
    			// init the data array
    			readerDataArray[i] = i;
    		}
    	
			if (cpd == null){
				//position at the beginning
				idx = 0;
			}
			else {
				// position at index held in the cpd
				idx = checkpointData.getCurrentIndex() + 1; 
			}
			logger.fine("READ: starting at index: " + idx);
			
			if (appCheckpointPositionString != null) {
				if (idx != checkpointPosition) {
					throw new Exception(
							"checkpointPosition incorect, test will now fail");
				} else {
					logger.fine("AJM: checkpoint position as expected");
				}
			}
		}
		
		@Override
		public ReadRecord readItem() throws Exception {
		
			int i = idx;
			
			execnum = ((MyPersistentRestartUserData)stepCtx.getPersistentUserData()).getExecutionNumber();
			logger.fine("AJM: iteration number = " + execnum);
			
			if (i == arraysize) {
				return null;
			}
			if (execnum == 2){
				failnum = -1;
			}
						
			if (idx == failnum-1){
				logger.fine("READ: got the fail num..." + failnum);
				throw new Exception("fail on purpose on idx = " + failnum);
			}
			count = count + 1;
			idx = idx+1;
			_cpd.setCurrentIndex(i);
		    return new ReadRecord(readerDataArray[i]);
		}
		
		@Override
		public ArrayIndexCheckpointData checkpointInfo() {
			
			logger.fine("READ: in getCPD cpd index from store: " + _cpd.getCurrentIndex());
			logger.fine("READ: in getCPD idx : " + idx);
			
		    return _cpd;   
		}
		
		   private class MyTransient {
		        int data = 0;
		        MyTransient(int x) {
		            data = x;
		        }   
		    }

}
