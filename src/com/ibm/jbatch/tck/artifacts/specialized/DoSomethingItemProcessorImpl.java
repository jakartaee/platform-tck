/*
 * Copyright 2012, 2020 International Business Machines Corp.
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



import jakarta.batch.api.chunk.ItemProcessor;

import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;

@javax.inject.Named("doSomethingItemProcessorImpl")
public class DoSomethingItemProcessorImpl implements ItemProcessor {
	private int update = 10;
	
	@Override
	public ReadRecord processItem(Object record) throws Exception {
		
		ReadRecord processedRecord = (ReadRecord)record;
		processedRecord.setRecord(update);
		update = update +1;
		return processedRecord;
	}
}
