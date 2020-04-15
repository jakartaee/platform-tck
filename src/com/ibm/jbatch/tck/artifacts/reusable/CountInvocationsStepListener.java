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
package com.ibm.jbatch.tck.artifacts.reusable;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.util.logging.Logger;

import jakarta.batch.api.listener.AbstractStepListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class CountInvocationsStepListener extends AbstractStepListener {
	
	private final static Logger logger = Logger.getLogger(CountInvocationsStepListener.class.getName());
	
    @Inject 
    private JobContext jobCtx = null; 
    
    public static int STATIC_BEFORE_COUNT = 0;
    public static int STATIC_AFTER_COUNT = 0;
    
	@Override
	public void beforeStep() throws Exception {
		if (jobCtx.getTransientUserData() == null) {
			MyCounter ctr = new MyCounter();
			ctr.incrementBeforeCount();
			jobCtx.setTransientUserData(ctr);
		} else if (jobCtx.getTransientUserData() instanceof MyCounter) {
			MyCounter ctr = (MyCounter)jobCtx.getTransientUserData();
			ctr.incrementBeforeCount();
		} else {
			logger.fine("In beforeStep(), not MyCounter");
		}
		STATIC_BEFORE_COUNT++;
	}
	
	@Override
	public void afterStep() throws Exception {
		if (jobCtx.getTransientUserData() instanceof MyCounter) {
			MyCounter ctr = (MyCounter)jobCtx.getTransientUserData();
			ctr.incrementAfterCount();
			jobCtx.setExitStatus(ctr.getBeforeCount() + "," + ctr.getAfterCount());
		} else {
			logger.fine("In afterStep(), not MyCounter");
		}
		STATIC_AFTER_COUNT++;
	}
	
}
