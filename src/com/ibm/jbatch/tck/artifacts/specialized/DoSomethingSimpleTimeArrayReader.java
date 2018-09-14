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

@javax.inject.Named("doSomethingSimpleTimeArrayReader")
public class DoSomethingSimpleTimeArrayReader extends AbstractItemReader {
		
	private final static Logger logger = Logger.getLogger(DoSomethingSimpleTimeArrayReader.class.getName());
	
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
    @BatchProperty(name="app.sleeptime")
    String sleeptimeString;
	
	     @Inject 
	 private StepContext stepCtx = null; 
		
	int execnum;
	int failnum;
	int arraysize;
	int sleeptime;
		
		@Override
		public void open(Serializable cpd) {
						
		    ArrayIndexCheckpointData checkpointData = (ArrayIndexCheckpointData)cpd;
		    
			failnum = Integer.parseInt(readrecordfailNumberString);
            execnum = Integer.parseInt(executionNumberString);   
    		arraysize = Integer.parseInt(appArraySizeString);
    		
    		sleeptime = Integer.parseInt(sleeptimeString);
    		
    		
    		readerDataArray =  new int[arraysize];
    		
    		for (int i = 0; i<arraysize; i++){
    			// init the data array
    			readerDataArray[i] = i;
    		}
    	
			if (checkpointData == null){
				//position at the beginning
				idx = 0;
			}
			else {
				// position at index held in the cpd
				idx = checkpointData.getCurrentIndex() + 1; 
			}
			logger.fine("READ: starting at index: " + idx);
		}
		
		@Override
		public ReadRecord readItem() throws Exception {
		
			int i = idx;
			
			if (i == arraysize) {
				return null;
			}
			
			execnum = ((MyPersistentRestartUserData)stepCtx.getPersistentUserData()).getExecutionNumber();
			
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
			Thread.sleep(sleeptime);
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

