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

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;

import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;

@javax.inject.Named("doSomethingArrayItemProcessorImpl")
public class DoSomethingArrayItemProcessorImpl implements ItemProcessor {
	
	private final static Logger logger = Logger.getLogger(DoSomethingArrayItemProcessorImpl.class.getName());

    @Inject    
    @BatchProperty(name="app.processFilterItem")
    String appProcessFilterItem;
	
	int filterNumber;
	boolean initSkipNumber = false;
	int count = 1;
	
	private int update = 100;
	
	@Override
	public ReadRecord processItem(Object record) throws Exception {
		
		if (appProcessFilterItem != null) {
			if (!initSkipNumber) {
				filterNumber = Integer.parseInt(appProcessFilterItem);
				initSkipNumber = true;
			}
		}
		
		if (initSkipNumber) {
			if (filterNumber == count) {
				logger.fine("AJM: filtering out #" + filterNumber);
				count++;
				return null; // filter
			}
		}
		
		count++;
		
		ReadRecord processedRecord = (ReadRecord)record;
		int currData = processedRecord.getCount();
		processedRecord.setRecord(currData + update);
		return processedRecord;
	}
}
