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

import java.util.logging.Logger;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.listener.AbstractItemReadListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.inject.Inject;

import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;

@jakarta.inject.Named("myItemReadListenerImpl")
public class MyItemReadListenerImpl extends AbstractItemReadListener {

	private final static String sourceClass = MyItemReadListenerImpl.class.getName();
	private final static Logger logger = Logger.getLogger(sourceClass);
	
	int beforecounter = 1;
	int aftercounter = 1;
	
	public static final String GOOD_EXIT_STATUS = "MyItemReadListenerImpl: GOOD STATUS";
	public static final String BAD_EXIT_STATUS = "MyItemReadListenerImpl: BAD STATUS";
	
    @Inject 
    JobContext jobCtx; 
	
    @Inject    
    @BatchProperty(name="app.listenertest")
    String applistenerTest;

	@Override
	public void beforeRead(){
		logger.finer("In beforeRead()");
		beforecounter++;
	}
	
    @Override
	public void afterRead(Object item) throws Exception {
		
		if (item != null && ("READ").equals(applistenerTest)){
			logger.finer("In afterRead(), item = " + ((ReadRecord)item).getCount());
			aftercounter++;

			if (beforecounter == aftercounter) {
				jobCtx.setExitStatus(GOOD_EXIT_STATUS);
			} else
				jobCtx.setExitStatus(BAD_EXIT_STATUS);
		}
	}
	
	@Override
	public void onReadError(Exception e) throws Exception {
		logger.finer("In onReadRerror() " + e);
		jobCtx.setExitStatus("MyItemReadListenerImpl.onReadError");
	}
}
